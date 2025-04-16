using hotel_api_.RequestDto;
using hotel_api_.RequestDto.Booking;
using hotel_api.Services;
using hotel_api.util;
using hotel_business;
using hotel_data.dto;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace hotel_api.controller;

[ApiController]
[Route("api/user")]
public class UserController : Controller
{
    private readonly IConfigurationServices _config;

    public UserController(IConfigurationServices config)
    {
        this._config = config;
    }


    [HttpPost("signUp")]
    [ProducesResponseType(StatusCodes.Status201Created)]
    [ProducesResponseType(StatusCodes.Status500InternalServerError)]
    [ProducesResponseType(StatusCodes.Status409Conflict)]
    [ProducesResponseType(StatusCodes.Status400BadRequest)]
    public IActionResult userSignUp(
        UserRequestDto userRequestData
    )
    {
        string? validateRequeset = clsValidation.validateInput(phone: userRequestData.phone,
            email: userRequestData.email, password: userRequestData.password);

        if (validateRequeset != null)
            return StatusCode(400, validateRequeset);


        bool isExistEmail = PersonBuisness.isPersonExistByEmailAndPhone(userRequestData.email, userRequestData.phone);

        if (isExistEmail)
            return StatusCode(400, "email or phone already in use");


        var data = UserBuissnes.getUserByUserName(userRequestData.userName);

        if (data != null)
            return StatusCode(409, "userName is already exist");


        var userId = Guid.NewGuid();

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
            password: clsUtil.hashingText(userRequestData.password)
        ));
        var result = data.save();
        string accesstoken = "", refreshToken = "";
        if (result == false)
            return StatusCode(500, "some thing wrong");

        accesstoken = AuthinticationServices.generateToken(
            userID: userId,
            email: data.personData.email,
            config: _config,
            enTokenMode: AuthinticationServices.enTokenMode.AccessToken);
        refreshToken = AuthinticationServices.generateToken(
            userID: userId,
            email: data.personData.email,
            config: _config,
            enTokenMode: AuthinticationServices.enTokenMode.RefreshToken);

        return StatusCode(201, new { accessToken = $"{accesstoken}", refreshToken = $"{refreshToken}" });
    }


    [HttpPost("signIn")]
    [ProducesResponseType(StatusCodes.Status201Created)]
    [ProducesResponseType(StatusCodes.Status500InternalServerError)]
    [ProducesResponseType(StatusCodes.Status409Conflict)]
    [ProducesResponseType(StatusCodes.Status400BadRequest)]
    public ActionResult userSignIn(LoginRequestDto loginData)
    {
        var data = UserBuissnes.getUserByUserNameAndPassword(loginData.userNameOrEmail,
            clsUtil.hashingText(loginData.password));

        if (data == null)
            return StatusCode(409, "user not exist");

        string accesstoken = "", refreshToken = "";

        accesstoken = AuthinticationServices.generateToken(data.ID, data.personData.email, _config,
            AuthinticationServices.enTokenMode.AccessToken);
        refreshToken = AuthinticationServices.generateToken(data.ID, data.personData.email, _config,
            AuthinticationServices.enTokenMode.RefreshToken);


        return StatusCode(200, new { accessToken = $"{accesstoken}", refreshToken = $"{refreshToken}" });
    }

    /// <summary>
    /// this to get room date by page for user
    /// </summary>
    /// <param name="pageNumber"></param>
    /// <returns></returns>

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
          //  string minioEndPoint = "http://" + _config.getKey("minio_end_point") + "/room/";
            var rooms = RoomBuisness.getAllRooms(
                pageNumber, 
                25//,
               // minioEndPoint
                );
            return Ok(rooms);
        }
        catch (Exception ex)
        {
            return StatusCode(500, "Something went wrong");
        }
    }


    /// <summary>
    /// this creating new booking 
    /// </summary>
    /// <param name="booking"></param>
    /// <returns></returns>
    [Authorize]
    [HttpPost("booking")]
    [ProducesResponseType(StatusCodes.Status201Created)]
    [ProducesResponseType(StatusCodes.Status500InternalServerError)]
    [ProducesResponseType(StatusCodes.Status401Unauthorized)]
    [ProducesResponseType(StatusCodes.Status400BadRequest)]
    public IActionResult createBooking
    (BookingRequestDto bookingData)
    {
        
        var authorizationHeader = HttpContext.Request.Headers["Authorization"];
        var id = AuthinticationServices.GetPayloadFromToken("id",
            authorizationHeader.ToString().Replace("Bearer ", ""));
        Guid? userID = null;
        if (Guid.TryParse(id.Value.ToString(), out Guid outID))
        {
            userID = outID;
        }

        if (userID == null)
        {
            return StatusCode(401, "you not have Permission");
        }
        

        var isVisibleBooking =
            BookingBuiseness.isValidBooking(bookingData.bookingStartDateTime, bookingData.bookingEndDateTime);
        if (!isVisibleBooking)
            return BadRequest("هناك حجز ضمن الفترة المختارة");

        var  bookingFullDate = (bookingData.bookingEndDateTime-bookingData.bookingStartDateTime);

        if (bookingFullDate.Days==0)
        {
            return BadRequest("booking at least one day is required");
        }

        var bookingDayes = Convert.ToDecimal(bookingFullDate.Days);

        var room = RoomBuisness.getRoom(bookingData.roomId);

        var totalPriceHolder = (bookingDayes * room.pricePerNight);
        var bookingDataHolder = new BookingDto(
            bookingId: null,
            roomId: bookingData.roomId,
            userId: (Guid)userID,
            bookingStart: bookingData.bookingStartDateTime,
            bookingEnd: bookingData.bookingEndDateTime,
            bookingStatus: null,
            totalPrice: totalPriceHolder,
            servicePayment: null,
            maintenancePayment: null,
            paymentStatus: null,
            createdAt: DateTime.Now,
            cancelledAt: null,
            cancellationReason: null,
            actualCheckOut: null
        );
        var bookingHolder = new BookingBuiseness(bookingDataHolder);

        var result = bookingHolder.save();

        if (result == false)
            return StatusCode(500, "some thing wrong");
        
        return StatusCode(201, new { message = "booking created seccessfully" });

    }
    
    
    [Authorize]
    [HttpPost("booking/between{year:int}&{month:int}")]
    [ProducesResponseType(StatusCodes.Status500InternalServerError)]
    [ProducesResponseType(StatusCodes.Status200OK)]
    public IActionResult getBookingDayAtYearAndMont
    (int  year, int month)
    {
        List<string> bookingDay = BookingBuiseness.getBookingDayesAtMonthAndYearBuissness(year, month);
        return StatusCode(200, bookingDay??[]);
    }

  
    [Authorize]
    [HttpGet("booking/{pageNumber:int}")]
    [ProducesResponseType(StatusCodes.Status201Created)]
    [ProducesResponseType(StatusCodes.Status500InternalServerError)]
    [ProducesResponseType(StatusCodes.Status401Unauthorized)]
    [ProducesResponseType(StatusCodes.Status400BadRequest)]
    public IActionResult bookings
    (int pageNumber)
    {
        
        var authorizationHeader = HttpContext.Request.Headers["Authorization"];
        var id = AuthinticationServices.GetPayloadFromToken("id",
            authorizationHeader.ToString().Replace("Bearer ", ""));
        Guid? userID = null;
        if (Guid.TryParse(id.Value.ToString(), out Guid outID))
        {
            userID = outID;
        }

        if (userID == null)
        {
            return StatusCode(401, "you not have Permission");
        }

        var myBookingListData = BookingBuiseness.getUserBookingList(userID.Value, pageNumber,24);

        
        return Ok(myBookingListData);

    } 
   
}