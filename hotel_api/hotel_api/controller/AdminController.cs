using System.ComponentModel.DataAnnotations;
using hotel_api_.RequestDto;
using Microsoft.AspNetCore.Mvc;
using hotel_data.dto;
using hotel_business;
using hotel_api.util;
using hotel_api.Services;
using hotel_data;
using Microsoft.AspNetCore.Authorization;

namespace hotel_api.controller
{
    [ApiController]
    [Route("api/admins")]
    public class AdminController : ControllerBase
    {
        private readonly IConfigurationServices _config;

        public AdminController(IConfigurationServices config)
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

        [Authorize]
        [HttpPost("createUser")]
        [ProducesResponseType(StatusCodes.Status201Created)]
        [ProducesResponseType(StatusCodes.Status500InternalServerError)]
        [ProducesResponseType(StatusCodes.Status401Unauthorized)]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        public IActionResult createNewUser(
            [FromForm]  UserRequestDto userRequestData
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

            string? validateRequeset = clsValidation.validateInput(phone: userRequestData.phone,
                email: userRequestData.email, password: userRequestData.password);

            if (validateRequeset != null)
                return StatusCode(400, validateRequeset);


            bool isExistEmail =
                PersonBuisness.isPersonExistByEmailAndPhone(userRequestData.email, userRequestData.phone);

            if (isExistEmail)
                return StatusCode(400, "email or phone is already in use");

            if (userRequestData.imagePath != null)
            {
                MinIoServices.uploadFile(_config,userRequestData.imagePath,MinIoServices.enBucketName.USER);
            }

            var data = UserBuissnes.
                getUserByUserNameAndPassword(userRequestData.userName,
                clsUtil.hashingText(userRequestData.password));
            if (data != null)
                return StatusCode(409, "user already exist");


            var userId = Guid.NewGuid();

            var personDataHolder = new PersonDto(
                personID: null,
                email: userRequestData.email,
                name: userRequestData.name,
                phone: userRequestData.phone,
                address: userRequestData.address
            );

            
            data = new UserBuissnes(new UserDto(
                userId: userId,
                personID: null,
                brithDay: userRequestData.brithDay,
                isVip: userRequestData.isVip,
                personData: personDataHolder,
                userName: userRequestData.userName,
                password: clsUtil.hashingText(userRequestData.password),
                addBy: adminid
            ));

            var result = data.save();
            string accesstoken = "", refreshToken = "";
            if (result == false)
                return StatusCode(500, "some thing wrong");

            return StatusCode(201, new { message="created seccessfully" });
        }
        
        [Authorize]
        [HttpPost("updateUser")]
        [ProducesResponseType(StatusCodes.Status200OK)]
        [ProducesResponseType(StatusCodes.Status500InternalServerError)]
        [ProducesResponseType(StatusCodes.Status401Unauthorized)]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        public async Task<IActionResult> updateUser(
            [FromForm]    UserUpdateDto userRequestData
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

            string? validateRequeset = clsValidation.validateInput(phone: userRequestData.phone,
                email: userRequestData.email, password: userRequestData.password);

            if (validateRequeset != null)
                return StatusCode(400, validateRequeset);


            bool isExistPhone =   PersonBuisness.
                isPersonExistByPhone(userRequestData.phone);

         var  data = UserBuissnes.getUserByID(userRequestData.Id);
            
   
            if(isExistPhone&&userRequestData.phone!= data.personData.phone)
                return StatusCode(400, "phone is already in use");
                

            if (data == null)
                return StatusCode(409, "user notFound exist");
            
            updateUserData(ref data, userRequestData);
            if (userRequestData.imagePath != null)
            {
             await   MinIoServices.uploadFile(_config,userRequestData.imagePath,MinIoServices.enBucketName.USER);
            }


            var result = data.save();
            if (result == false)
                return StatusCode(500, "some thing wrong");

            return StatusCode(201, new { message="created seccessfully" });
        }

     
        private void updateUserData(
           
            ref UserBuissnes user,
            UserUpdateDto userRequestData
            )
             {
                 if (userRequestData?.name?.Length > 0 && user.personData.name!= userRequestData.name)
                 {
                     user.personData.name = userRequestData.name;
                 }

                 if (userRequestData?.address?.Length > 0&&user.personData.address!= userRequestData.address)
                 {
                     user.personData.address = userRequestData.address;
                 }
                 if (userRequestData?.brithDay != null &&user.brithDay!= userRequestData?.brithDay)
                 {
                     user.brithDay = (DateTime)userRequestData.brithDay;
                 }
                 if (userRequestData?.isVip == true)
                     user.isVip = true;
                 if (userRequestData?.userName?.Length > 0 &&user.userName!= userRequestData.userName)
                     user.userName = userRequestData.userName;
                 if (userRequestData?.password?.Length > 0)
                     user.password = clsUtil.hashingText(userRequestData.password);
                 if(userRequestData?.brithDay != null)
                     user.brithDay =(DateTime) userRequestData!.brithDay;
             }
        
        [Authorize]
        [HttpDelete("deleteUser/{userId:guid}")]
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


            var  data = UserBuissnes.getUserByID(userId);
            
            if (data == null)
                return StatusCode(409, "user notFound exist");
            

            var result = UserBuissnes.deleteUser(userId);
            if (result == false)
                return StatusCode(500, "some thing wrong");

            return StatusCode(201, new { message="created seccessfully" });
        } 
              
        [Authorize]
        [HttpPost("makeUserVip/{userId:guid}")]
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


            var  data = UserBuissnes.getUserByID(userId);
            
            if (data == null)
                return StatusCode(409, "user notFound exist");
            

            var result = UserBuissnes.makeVipUser(userId);
            if (result == false)
                return StatusCode(500, "some thing wrong");

            return StatusCode(201, new { message="user now is vip" });
        }  
    }
    
    
}