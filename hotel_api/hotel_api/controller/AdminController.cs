using System.ComponentModel.DataAnnotations;
using hotel_api_.RequestDto;
using hotel_api_.RequestDto.Booking;
using Microsoft.AspNetCore.Mvc;
using hotel_data.dto;
using hotel_business;
using hotel_api.util;
using hotel_api.Services;
using hotel_data;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.SignalR;

namespace hotel_api.controller
{
    [ApiController]
    [Route("api/admins")]
    public class AdminController : ControllerBase
    {
        private readonly IConfigurationServices _config;

        public AdminController(
            IConfigurationServices config
        )
        {
            this._config = config;
        }


        [HttpPost("signUp")]
        [ProducesResponseType(StatusCodes.Status201Created)]
        [ProducesResponseType(StatusCodes.Status500InternalServerError)]
        [ProducesResponseType(StatusCodes.Status409Conflict)]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        public ActionResult adminSignUp(
            AdminRequestDto adminRequestData
        )
        {
            string? validateRequeset = clsValidation.validateInput(phone: adminRequestData.phone,
                email: adminRequestData.email, password: adminRequestData.password);
            if (validateRequeset != null)
                return BadRequest($"{validateRequeset}");
            bool isExistEmail = false;
            isExistEmail = PersonBuisness.isPersonExistByEmailAndPhone(adminRequestData.email, adminRequestData.phone);

            if (isExistEmail)
                return BadRequest("email or phone  is already in use");
            isExistEmail = AdminBuissnes.isAdminExist(adminRequestData.userName);

            if (isExistEmail)
                return BadRequest("username is already in use");

            var data = AdminBuissnes.getAdmin(adminRequestData.userName,
                clsUtil.hashingText(adminRequestData.password));
            if (data != null)
                return StatusCode(409, "amdin already exist");
            var adminId = Guid.NewGuid();
            var adminData = new AdminDto(
                adminID: adminId,
                userName: adminRequestData.userName,
                password: clsUtil.hashingText(adminRequestData.password),
                personsId: null,
                personData: new PersonDto(
                    personID: null,
                    email: adminRequestData.email,
                    name: adminRequestData.name,
                    phone: adminRequestData.phone,
                    address: adminRequestData.address
                )
            );

            var adminHolder = new AdminBuissnes(adminData);
            var result = adminHolder.save();
            string accesstoken = "", refreshToken = "";
            if (result == false)
                return StatusCode(500, "some thing wrong");

            accesstoken = AuthinticationServices.generateToken(adminId, adminData?.personData?.email ?? "", _config,
                AuthinticationServices.enTokenMode.AccessToken);
            refreshToken = AuthinticationServices.generateToken(adminId, adminData?.personData?.email ?? "", _config,
                AuthinticationServices.enTokenMode.RefreshToken);

            return StatusCode(201, new { accessToken = $"{accesstoken}", refreshToken = $"{refreshToken}" });
        }


        [HttpPost("signIn")]
        [ProducesResponseType(StatusCodes.Status201Created)]
        [ProducesResponseType(StatusCodes.Status500InternalServerError)]
        [ProducesResponseType(StatusCodes.Status409Conflict)]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        public ActionResult adminSignIn(
            LoginRequestDto loginData
        )
        {
            var data = AdminBuissnes.getAdmin(loginData.userNameOrEmail, clsUtil.hashingText(loginData.password));
            if (data == null)
                return StatusCode(409, "amdin not exist");
            string accesstoken = "", refreshToken = "";

            accesstoken = AuthinticationServices.generateToken(data.adminID, data?.personData?.email ?? "", _config,
                AuthinticationServices.enTokenMode.AccessToken);
            refreshToken = AuthinticationServices.generateToken(data?.adminID, data?.personData?.email ?? "", _config,
                AuthinticationServices.enTokenMode.RefreshToken);


            return StatusCode(201, new { accessToken = $"{accesstoken}", refreshToken = $"{refreshToken}" });
        }

        //user
        [Authorize]
        [HttpPost("User")]
        [ProducesResponseType(StatusCodes.Status201Created)]
        [ProducesResponseType(StatusCodes.Status500InternalServerError)]
        [ProducesResponseType(StatusCodes.Status401Unauthorized)]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        public async Task<IActionResult> createNewUser(
            [FromForm] UserRequestDto userRequestData
        )
        {
            var authorizationHeader = HttpContext.Request.Headers["Authorization"];
            var id = AuthinticationServices.GetPayloadFromToken("id",
                authorizationHeader.ToString().Replace("Bearer ", ""));
            if (id == null)
            {
                return StatusCode(401, "you not have Permission");
            }

            Guid? adminid = null;
            if (Guid.TryParse(id.Value.ToString(), out Guid outid))
            {
                adminid = outid;
            }

            var isHasPermissionToCreateUser = AdminBuissnes.isAdminExist(adminid ?? Guid.Empty);

            if (!isHasPermissionToCreateUser)
            {
                return StatusCode(401, "you not have Permission");
            }

            string? validateRequeset = clsValidation.validateInput(phone: userRequestData.phone,
                email: userRequestData.email, password: userRequestData.password);

            if (validateRequeset != null)
                return StatusCode(400, validateRequeset);


            bool isExistEmail = PersonBuisness.isPersonExistByEmailAndPhone(
                userRequestData.email,
                userRequestData.phone
            );

            if (isExistEmail)
                return StatusCode(400, "email or phone is already in use");


            var data = UserBuissnes.getUserByUserNameAndPassword(userRequestData.userName,
                clsUtil.hashingText(userRequestData.password));
            if (data != null)
                return StatusCode(409, "user already exist");


            var userId = Guid.NewGuid();


            string? imagePath = null;
            if (userRequestData.imagePath != null)
            {
                imagePath = await MinIoServices.uploadFile(_config, userRequestData.imagePath,
                    MinIoServices.enBucketName.USER);
            }

            saveImage(imagePath, userId);

            var personDataHolder = new PersonDto(
                personID: null,
                email: userRequestData.email,
                name: userRequestData.name,
                phone: userRequestData.phone,
                address: userRequestData.address ?? ""
            );


            data = new UserBuissnes(new UserDto(
                userId: userId,
                brithDay: userRequestData.brithDay,
                isVip: userRequestData.isVip,
                personData: personDataHolder,
                userName: userRequestData.userName,
                password: clsUtil.hashingText(userRequestData.password),
                addBy: adminid
            ));

            var result = data.save();

            if (result == false)
                return StatusCode(500, "some thing wrong");

            return StatusCode(201, new { message = "created seccessfully" });
        }

        [Authorize]
        [HttpPut("User/{userid:guid}")]
        [ProducesResponseType(StatusCodes.Status200OK)]
        [ProducesResponseType(StatusCodes.Status500InternalServerError)]
        [ProducesResponseType(StatusCodes.Status401Unauthorized)]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        public async Task<IActionResult> updateUser(
            [FromForm] UserUpdateDto userRequestData, Guid userid
        )
        {
            var authorizationHeader = HttpContext.Request.Headers["Authorization"];
            var id = AuthinticationServices.GetPayloadFromToken("id",
                authorizationHeader.ToString().Replace("Bearer ", ""));
            if (id == null)
            {
                return StatusCode(401, "you not have Permission");
            }

            Guid? adminid = null;
            if (Guid.TryParse(id.Value.ToString(), out Guid outID))
            {
                adminid = outID;
            }


            var isHasPermissionToCreateUser = AdminBuissnes.isAdminExist(adminid ?? Guid.Empty);


            if (!isHasPermissionToCreateUser)
            {
                return StatusCode(401, "you not have Permission");
            }

            var data = UserBuissnes.getUserByID(userid);
            if (data == null)
                return StatusCode(409, "user notFound exist");


            string? validateRequeset = clsValidation.validateInput(phone: userRequestData.phone,
                email: userRequestData.email, password: userRequestData.password);

            if (validateRequeset != null)
                return StatusCode(400, validateRequeset);


            bool isExistPhone = false;

            if (userRequestData.phone != null && userRequestData.phone != data.personData.phone)
                isExistPhone = PersonBuisness.isPersonExistByPhone(userRequestData.phone);


            if (isExistPhone)
                return StatusCode(400, "phone is already in use");


            var imageHolder = ImageBuissness.getImageByBelongTo(data.ID);

            string? imagePath = null;
            if (userRequestData.imagePath != null)
            {
                imagePath = await MinIoServices.uploadFile(_config, userRequestData.imagePath,
                    MinIoServices.enBucketName.USER, imageHolder?.path ?? "");
            }


            saveImage(imagePath, data.ID, imageHolder);

            data.imagePath = imagePath;

            updateUserData(ref data, userRequestData);


            var result = data.save();
            if (result == false)
                return StatusCode(500, "some thing wrong");

            return StatusCode(201, new { message = "created seccessfully" });
        }


        private void updateUserData(
            ref UserBuissnes user,
            UserUpdateDto? userRequestData
        )
        {
            if (userRequestData == null) return;

            if (userRequestData?.name?.Length > 0 && user.personData.name != userRequestData.name)
            {
                user.personData.name = userRequestData.name;
            }

            if (userRequestData?.address?.Length > 0 && user.personData.address != userRequestData.address)
            {
                user.personData.address = userRequestData.address;
            }

            if (userRequestData?.brithDay != null && user.brithDay != userRequestData?.brithDay)
            {
                user.brithDay = (DateTime)userRequestData.brithDay;
            }

            if (userRequestData?.isVip == true)
                user.isVip = true;
            if (userRequestData?.userName?.Length > 0 && user.userName != userRequestData.userName)
                user.userName = userRequestData.userName;
            if (userRequestData?.password?.Length > 0)
                user.password = clsUtil.hashingText(userRequestData.password);
            if (userRequestData?.brithDay != null)
                user.brithDay = (DateTime)userRequestData!.brithDay;
            if (userRequestData?.phone != null)
            {
                user.userDataHolder.personData.phone = userRequestData.phone;
            }
        }


        [Authorize]
        [HttpDelete("User/{userId:guid}")]
        [ProducesResponseType(StatusCodes.Status200OK)]
        [ProducesResponseType(StatusCodes.Status500InternalServerError)]
        [ProducesResponseType(StatusCodes.Status401Unauthorized)]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        public IActionResult deleteUser(
            Guid userId
        )
        {
            var authorizationHeader = HttpContext.Request.Headers["Authorization"];
            var id = AuthinticationServices.GetPayloadFromToken("id",
                authorizationHeader.ToString().Replace("Bearer ", ""));
            if (id == null)
            {
                return StatusCode(401, "you not have Permission");
            }


            Guid? adminid = null;
            if (Guid.TryParse(id.Value.ToString(), out Guid outID))
            {
                adminid = outID;
            }


            var isHasPermissionToCreateUser = AdminBuissnes.isAdminExist(adminid ?? Guid.Empty);


            if (!isHasPermissionToCreateUser)
            {
                return StatusCode(401, "you not have Permission");
            }


            var data = UserBuissnes.getUserByID(userId);

            if (data == null)
                return StatusCode(409, "user notFound exist");


            var result = UserBuissnes.deleteUser(userId);
            if (result == false)
                return StatusCode(500, "some thing wrong");

            return StatusCode(201, new { message = "created seccessfully" });
        }

        [Authorize]
        [HttpPost("User/{userId:guid}")]
        [ProducesResponseType(StatusCodes.Status200OK)]
        [ProducesResponseType(StatusCodes.Status500InternalServerError)]
        [ProducesResponseType(StatusCodes.Status401Unauthorized)]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        public IActionResult makeUserVip(
            Guid userId
        )
        {
            var authorizationHeader = HttpContext.Request.Headers["Authorization"];
            var id = AuthinticationServices.GetPayloadFromToken("id",
                authorizationHeader.ToString().Replace("Bearer ", ""));
            Guid? adminid = null;
            if (Guid.TryParse(id?.Value.ToString(), out Guid outID))
            {
                adminid = outID;
            }

            if (adminid == null)
            {
                return StatusCode(401, "you not have Permission");
            }

            var isHasPermissionToCreateUser = AdminBuissnes.isAdminExist(adminid ?? Guid.Empty);


            if (!isHasPermissionToCreateUser)
            {
                return StatusCode(401, "you not have Permission");
            }


            var data = UserBuissnes.getUserByID(userId);

            if (data == null)
                return StatusCode(409, "user notFound exist");


            var result = UserBuissnes.makeVipUser(userId);
            if (result == false)
                return StatusCode(500, "some thing wrong");

            return StatusCode(201, new { message = "user now is vip" });
        }


        //roomType
        [Authorize]
        [HttpPost("roomtype")]
        [ProducesResponseType(StatusCodes.Status201Created)]
        [ProducesResponseType(StatusCodes.Status500InternalServerError)]
        [ProducesResponseType(StatusCodes.Status401Unauthorized)]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        public async Task<IActionResult> createNewRoomType(
            [FromForm] RoomTypeRequestUpdateDto roomTypeData
        )
        {
            var authorizationHeader = HttpContext.Request.Headers["Authorization"];

            var id = AuthinticationServices.GetPayloadFromToken("id",
                authorizationHeader.ToString().Replace("Bearer ", ""));
            Guid? adminid = null;
            if (Guid.TryParse(id.Value.ToString(), out Guid outID))
            {
                adminid = outID;
            }

            if (adminid == null)
            {
                return StatusCode(401, "you not have Permission");
            }

            var isHasPermissionToCreateRoomType = AdminBuissnes.isAdminExist(adminid ?? Guid.Empty);


            if (!isHasPermissionToCreateRoomType)
            {
                return StatusCode(401, "you not have Permission");
            }


            if (roomTypeData.name.Length > 50)
                return StatusCode(400, "roomtype name must be under 50 characters");


            bool isExistName = RoomtTypeBuissnes.isExist(roomTypeData.name);

            if (isExistName)
                return StatusCode(400, "roomtype is already exist");


            var roomtypeid = Guid.NewGuid();


            string? imageHolderPath = null;
            if (roomTypeData.image != null)
            {
                imageHolderPath = await MinIoServices.uploadFile(_config, roomTypeData.image,
                    MinIoServices.enBucketName.RoomType);
            }

            saveImage(imageHolderPath, roomtypeid);

            var roomTypeHolder = new RoomtTypeBuissnes(
                new RoomTypeDto(
                    roomTypeId: roomtypeid,
                    roomTypeName: roomTypeData.name,
                    createdBy: (Guid)adminid,
                    createdAt: DateTime.Now
                )
            );

            var result = roomTypeHolder.save();

            if (result == false)
                return StatusCode(500, "some thing wrong");

            return StatusCode(201, new { message = "created seccessfully" });
        }


        [Authorize]
        [HttpGet("roomtype{isNotDeletion:bool}")]
        [ProducesResponseType(StatusCodes.Status201Created)]
        [ProducesResponseType(StatusCodes.Status500InternalServerError)]
        [ProducesResponseType(StatusCodes.Status401Unauthorized)]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        public IActionResult getRoomTypes(bool? isNotDeletion)
        {
            try
            {
                var authorizationHeader = HttpContext.Request.Headers["Authorization"];
                var id = AuthinticationServices.GetPayloadFromToken("id",
                    authorizationHeader.ToString().Replace("Bearer ", ""));
                Guid? adminid = null;
                if (Guid.TryParse(id.Value.ToString(), out Guid outID))
                {
                    adminid = outID;
                }

                if (adminid == null)
                {
                    return StatusCode(401, "you not have Permission");
                }

                var isHasPermission = AdminBuissnes.isAdminExist(adminid ?? Guid.Empty);


                if (!isHasPermission)
                {
                    return StatusCode(401, "you not have Permission");
                }

                var roomtypes = RoomtTypeBuissnes.getRoomTypes(isNotDeletion ?? false);

                return Ok(roomtypes);
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
                return StatusCode(500, "some thing wrong");
            }
        }


        [Authorize]
        [HttpPut("roomtype/{roomtypeid:guid}")]
        [ProducesResponseType(StatusCodes.Status201Created)]
        [ProducesResponseType(StatusCodes.Status500InternalServerError)]
        [ProducesResponseType(StatusCodes.Status401Unauthorized)]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        public async Task<IActionResult> updateRoomTypes([FromForm] RoomTypeRequestUpdateDto roomTypeData,
            Guid roomtypeid)
        {
            var authorizationHeader = HttpContext.Request.Headers["Authorization"];
            var id = AuthinticationServices.GetPayloadFromToken("id",
                authorizationHeader.ToString().Replace("Bearer ", ""));
            Guid? adminid = null;
            if (Guid.TryParse(id.Value.ToString(), out Guid outID))
            {
                adminid = outID;
            }

            if (adminid == null)
            {
                return StatusCode(401, "you not have Permission");
            }

            var isHasPermissionToCreateUser = AdminBuissnes.isAdminExist(adminid ?? Guid.Empty);


            if (!isHasPermissionToCreateUser)
            {
                return StatusCode(401, "you not have Permission");
            }


            if (roomTypeData.name.Length > 50 || roomtypeid == null)
                return StatusCode(400, "roomtype name must be under 50 characters");


            var roomtypeHolder = RoomtTypeBuissnes.getRoomType(roomtypeid);

            if (roomtypeHolder == null)
                return StatusCode(400, "roomtype is already exist");

            var imageHolder = ImageBuissness.getImageByBelongTo((Guid)roomtypeid);

            string? imageHolderPath = null;
            if (roomTypeData.image != null)
            {
                imageHolderPath = await MinIoServices.uploadFile(_config, roomTypeData.image,
                    MinIoServices.enBucketName.RoomType, imageHolder.path);
            }


            saveImage(imageHolderPath, roomtypeid, imageHolder);

            updateRoomTypeData(ref roomtypeHolder, roomTypeData, (Guid)adminid);
            var result = roomtypeHolder.save();

            if (result == false)
                return StatusCode(500, "some thing wrong");

            return StatusCode(201, new { message = "created seccessfully" });
        }


        private void updateRoomTypeData(ref RoomtTypeBuissnes data, RoomTypeRequestUpdateDto holder, Guid createdBy)
        {
            if (data.name != holder.name)
            {
                data.name = holder.name;
            }

            if (createdBy != null && data.createdBy != createdBy)
            {
                data.createdBy = createdBy;
            }
        }


        [Authorize]
        [HttpDelete("roomtype/{roomtypeid:guid}")]
        [ProducesResponseType(StatusCodes.Status200OK)]
        [ProducesResponseType(StatusCodes.Status500InternalServerError)]
        [ProducesResponseType(StatusCodes.Status401Unauthorized)]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        public IActionResult deleteOrUnDeleteRoomtype(
            Guid roomtypeid
        )
        {
            var authorizationHeader = HttpContext.Request.Headers["Authorization"];
            var id = AuthinticationServices.GetPayloadFromToken("id",
                authorizationHeader.ToString().Replace("Bearer ", ""));
            Guid? adminid = null;
            if (Guid.TryParse(id.Value.ToString(), out Guid outID))
            {
                adminid = outID;
            }

            if (adminid == null)
            {
                return StatusCode(401, "you not have Permission");
            }

            var isHasPermissionToCreateUser = AdminBuissnes.isAdminExist(adminid ?? Guid.Empty);


            if (!isHasPermissionToCreateUser)
            {
                return StatusCode(401, "you not have Permission");
            }


            var data = RoomtTypeBuissnes.getRoomType(roomtypeid);

            if (data == null)
                return StatusCode(409, "user notFound exist");


            var result = RoomtTypeBuissnes.deleteOrUnDeleteRoomType(roomtypeid);
            if (result == false)
                return StatusCode(500, "some thing wrong");

            return StatusCode(201, new { message = "created seccessfully" });
        }


        //room
        [Authorize]
        [HttpPost("room")]
        [ProducesResponseType(StatusCodes.Status201Created)]
        [ProducesResponseType(StatusCodes.Status500InternalServerError)]
        [ProducesResponseType(StatusCodes.Status401Unauthorized)]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        public async Task<IActionResult> createRoom
            ([FromForm] RoomRequestDto roomData)
        {
            var authorizationHeader = HttpContext.Request.Headers["Authorization"];
            var id = AuthinticationServices.GetPayloadFromToken("id",
                authorizationHeader.ToString().Replace("Bearer ", ""));
            Guid? adminid = null;
            if (Guid.TryParse(id.Value.ToString(), out Guid outID))
            {
                adminid = outID;
            }

            if (adminid == null)
            {
                return StatusCode(401, "you not have Permission");
            }

            var isHasPermissionToCreateUser = AdminBuissnes.isAdminExist(adminid ?? Guid.Empty);


            if (!isHasPermissionToCreateUser)
            {
                return StatusCode(401, "you not have Permission");
            }


            var roomId = Guid.NewGuid();

            List<ImageRequestDto>? imageHolderPath = null;
            if (roomData.images != null)
            {
                imageHolderPath = await MinIoServices.uploadFile(
                    _config,
                    roomData.images,
                    MinIoServices.enBucketName.ROOM,
                    roomId.ToString()
                );
            }

            saveImage(imageHolderPath, roomId);

            var roomHolder = new RoomBuisness(
                new RoomDto(
                    roomId: roomId,
                    status: roomData.status,
                    pricePerNight: roomData.pricePerNight,
                    roomtypeid: roomData.roomtypeid,
                    capacity: roomData.capacity,
                    bedNumber: roomData.bedNumber,
                    beglongTo: (Guid)adminid,
                    createdAt: DateTime.Now
                )
            );

            var result = roomHolder.save();

            if (result == false)
                return StatusCode(500, "some thing wrong");

            return StatusCode(201, new { message = "created seccessfully" });
        }


        [Authorize]
        [HttpGet("room/{pageNumber:int}")]
        [ProducesResponseType(StatusCodes.Status201Created)]
        [ProducesResponseType(StatusCodes.Status500InternalServerError)]
        [ProducesResponseType(StatusCodes.Status401Unauthorized)]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        public async Task<IActionResult> getRooms
            (int pageNumber)
        {
            try
            {
                var rooms = RoomBuisness.getAllRooms(pageNumber, 25);

                return Ok(rooms);
            }
            catch (Exception ex)
            {
                return StatusCode(500, "Something went wrong");
            }
        }

        [Authorize]
        [HttpPut("room/{roomId:guid}")]
        [ProducesResponseType(StatusCodes.Status201Created)]
        [ProducesResponseType(StatusCodes.Status500InternalServerError)]
        [ProducesResponseType(StatusCodes.Status401Unauthorized)]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        public async Task<IActionResult> updateRoom
        ([FromForm] RoomRequestUpdateDto roomData,
            Guid roomId
        )
        {
            var authorizationHeader = HttpContext.Request.Headers["Authorization"];
            var id = AuthinticationServices.GetPayloadFromToken("id",
                authorizationHeader.ToString().Replace("Bearer ", ""));
            Guid? adminid = null;
            if (Guid.TryParse(id.Value.ToString(), out Guid outID))
            {
                adminid = outID;
            }

            if (adminid == null)
            {
                return StatusCode(401, "you not have Permission");
            }

            var isHasPermissionToCurd = AdminBuissnes.isAdminExist(adminid ?? Guid.Empty);


            if (!isHasPermissionToCurd)
            {
                return StatusCode(401, "you not have Permission");
            }

            var room = RoomBuisness.getRoom(roomId);

            if (room == null)
                return StatusCode(400, "room not found");


            List<ImageRequestDto>? imageHolderPath = null;
            if (roomData.images != null)
            {
                imageHolderPath = await MinIoServices.uploadFile(
                    _config,
                    roomData.images,
                    MinIoServices.enBucketName.ROOM,
                    roomId.ToString()
                );
            }

            if (imageHolderPath != null)
                saveImage(imageHolderPath, roomId);
            _updateRoomData(ref room, roomData);

            var result = room.save();

            if (result == false)
                return StatusCode(500, "some thing wrong");

            return StatusCode(200, new { message = "update seccessfully" });
        }

        private void _updateRoomData(ref RoomBuisness roomData, RoomRequestUpdateDto newRoomData)
        {
            if (newRoomData.status != null && roomData.status != newRoomData.status)
            {
                roomData.status = (enStatsu)newRoomData.status;
            }

            if (newRoomData.pricePerNight != null && newRoomData.pricePerNight != roomData.pricePerNight)
            {
                roomData.pricePerNight = (int)newRoomData.pricePerNight;
            }

            if (newRoomData.bedNumber != null && newRoomData.bedNumber != roomData.bedNumber)
            {
                roomData.bedNumber = (int)newRoomData.bedNumber;
            }

            if (newRoomData.roomtypeid != null && newRoomData.roomtypeid != roomData.roomtypeid)
            {
                roomData.roomtypeid = (Guid)newRoomData.roomtypeid;
            }

            if (newRoomData.capacity != null && newRoomData.capacity != roomData.capacity)
            {
                roomData.capacity = (int)newRoomData.capacity;
            }
        }


        [Authorize]
        [HttpDelete("room/{roomId:guid}")]
        [ProducesResponseType(StatusCodes.Status201Created)]
        [ProducesResponseType(StatusCodes.Status500InternalServerError)]
        [ProducesResponseType(StatusCodes.Status401Unauthorized)]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        public async Task<IActionResult> deleteOrUnDeleteRoom
        ([FromForm] RoomRequestUpdateDto roomData,
            Guid roomId
        )
        {
            var authorizationHeader = HttpContext.Request.Headers["Authorization"];
            var id = AuthinticationServices.GetPayloadFromToken("id",
                authorizationHeader.ToString().Replace("Bearer ", ""));
            Guid? adminid = null;
            if (Guid.TryParse(id.Value.ToString(), out Guid outID))
            {
                adminid = outID;
            }

            if (adminid == null)
            {
                return StatusCode(401, "you not have Permission");
            }

            var isHasPermissionToCurd = AdminBuissnes.isAdminExist(adminid ?? Guid.Empty);


            if (!isHasPermissionToCurd)
            {
                return StatusCode(401, "you not have Permission");
            }

            var room = RoomBuisness.getRoom(roomId);

            if (room == null)
                return StatusCode(400, "room not found");


            var result = RoomBuisness.deleteRoom(room.ID, (Guid)adminid);

            if (result == false)
                return StatusCode(500, "some thing wrong");
            return StatusCode(200, new { message = "deleted seccessfully" });
        }


        [Authorize]
        [HttpDelete("room")]
        [ProducesResponseType(StatusCodes.Status201Created)]
        [ProducesResponseType(StatusCodes.Status500InternalServerError)]
        [ProducesResponseType(StatusCodes.Status401Unauthorized)]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        public async Task<IActionResult> createBooking
        ([FromBody] BookingRequestDto bookingData
        )
        {
            var authorizationHeader = HttpContext.Request.Headers["Authorization"];
            var id = AuthinticationServices.GetPayloadFromToken("id",
                authorizationHeader.ToString().Replace("Bearer ", ""));
            Guid? adminid = null;
            if (Guid.TryParse(id.Value.ToString(), out Guid outID))
            {
                adminid = outID;
            }

            if (adminid == null)
            {
                return StatusCode(401, "you not have Permission");
            }

            var isHasPermissionToCurd = AdminBuissnes.isAdminExist(adminid ?? Guid.Empty);


            if (!isHasPermissionToCurd)
            {
                return StatusCode(401, "you not have Permission");
            }


            var isThereAnyConfirmBookingByRoomID = BookingBuiseness.getBookingConfirmByRoomID(bookingData.roomid);

            if (isThereAnyConfirmBookingByRoomID == true)
                return StatusCode(400, "room already is confirming booking");

            var bookingDataHolder = new BookingDto(
                id: null,
                roomid: bookingData.roomid,
                userId: adminid.Value,
                days: bookingData.days,
                bookingStatus: BookingDto.convertBookingStatus(bookingData.enBookingStatus),
                totalPrice: bookingData.totalPrice,
                firstPaymen: bookingData.firstPaymen,
                servicePayemen: 0,
                maintainPayment: 0,
                excpectedleavedAt: bookingData.excpectedleavedAt,
                leavedAt: null,
                createdAt: DateTime.Now
            );
            var bookingHolder = new BookingBuiseness(bookingDataHolder);

            var result = bookingHolder.save();

            if (result == false)
                return StatusCode(500, "some thing wrong");
            return StatusCode(200, new { message = "deleted seccessfully" });
        }

        private void saveImage(
            string? imagePath,
            Guid? id
            , ImageBuissness? imageHolder = null
        )
        {
            if (imageHolder != null)
            {
                imageHolder.path = imagePath;
                imageHolder.save();
            }
            else if (imagePath != null && id != null)
            {
                imageHolder =
                    new ImageBuissness(
                        new ImagesTbDto(
                            imagePath: imagePath,
                            belongTo: (Guid)id,
                            imagePathId: null,
                            isThumnail: imageHolder.isThumnail));
                imageHolder.save();
            }
        }

        private void saveImage(
            List<ImageRequestDto>? imagePath,
            Guid id
        )
        {
            if (imagePath != null)
            {
                foreach (var path in imagePath)
                {
                    var imageHolder = new ImageBuissness(
                        new ImagesTbDto(
                            imagePath: path.fileName,
                            belongTo: id,
                            imagePathId: null,
                            isThumnail: path.isThumnail)
                    );
                    imageHolder.save();
                }
            }
        }
    }
}