using hotel_api_.RequestDto;
using hotel_api.Services;
using hotel_api.util;
using hotel_business;
using hotel_data.dto;
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


        [HttpPost("/sinUp")]
        [ProducesResponseType(StatusCodes.Status201Created)]
        [ProducesResponseType(StatusCodes.Status500InternalServerError)]
        [ProducesResponseType(StatusCodes.Status409Conflict)]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        public ActionResult adminSignUp(
            UserRequestDto userRequestData
        )
        {
            string? validateRequeset = clsValidation.validateInput(phone:userRequestData.phone,email:userRequestData.email,password:userRequestData.password);
            
            if(validateRequeset!=null)
                return BadRequest($"{validateRequeset}");
 

            var data = UserBuissnes.isExistByUserNameAndPassword(userRequestData.userName,
                clsUtil.hashingText(userRequestData.password));
            if (data != null)
                return StatusCode(409, "amdin already exist");

            var userId = Guid.NewGuid();

            var personDataHolder = new PersonDto(
                personID:null,
                email:userRequestData.name,
                name:userRequestData.email,
                phone:userRequestData.phone,
                address:userRequestData.address);
            
            var userData = new UserDto(
                userId:userId,
                personID:null,
                brithDay:userRequestData.brithDay,
                isVip:userRequestData.isVip,
                personData:personDataHolder,
                userName:userRequestData.userName,
                password:userRequestData.password
                );


            var userHolder = new UserBuissnes(userData);
            var result = userHolder.save();
            string accesstoken = "", refreshToken = "";
            if (result == false)
                return StatusCode(500, "some thing wrong");

            accesstoken = AuthinticationServices.generateToken(
               userID: userId, 
                email:userData.personData.email,
                config:_config,
                enTokenMode:AuthinticationServices.enTokenMode.AccessToken);
            refreshToken = AuthinticationServices.generateToken(
                userID:userId, 
                email:userData.personData.email, 
                config:_config,
                enTokenMode:AuthinticationServices.enTokenMode.RefreshToken);

            return StatusCode(201, new { accessToken = $"{accesstoken}", refreshToken = $"{refreshToken}" });
        }


        [HttpPost("/SinIn")]
        [ProducesResponseType(StatusCodes.Status201Created)]
        [ProducesResponseType(StatusCodes.Status500InternalServerError)]
        [ProducesResponseType(StatusCodes.Status409Conflict)]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        public ActionResult adminSignIn(string userName, string password)
        {
            if (userName == null || password == null)
                return BadRequest("data must not be empty");

            var data = UserBuissnes.getUserByUserNameAndPassword(userName, clsUtil.hashingText(password));
            
            if (data == null)
                return StatusCode(409, "user not exist");
            
            string accesstoken = "", refreshToken = "";
            if (data.userId == null)
            {
                accesstoken = AuthinticationServices.generateToken(data.userId, data.personData.email, _config,
                    AuthinticationServices.enTokenMode.AccessToken);
                refreshToken = AuthinticationServices.generateToken(data.userId, data.personData.email, _config,
                    AuthinticationServices.enTokenMode.RefreshToken);
            }


            return StatusCode(201, new { accessToken = $"{accesstoken}", refreshToken = $"{refreshToken}" });
        }
  
}