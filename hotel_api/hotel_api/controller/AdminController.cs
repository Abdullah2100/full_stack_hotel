using System.ComponentModel.DataAnnotations;
using hotel_api_.RequestDto;
using Microsoft.AspNetCore.Mvc;
using hotel_data.dto;
using hotel_business;
using hotel_api.util;
using hotel_api.Services;
using hotel_data;

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
            bool isExistEmail = PersonBuisness.isPersonExistByEmail(adminRequestData.email);

            if (isExistEmail)
                return BadRequest("email is already in use");


            var data = AdminBuissnes.getAdmin(adminRequestData.userName,
                clsUtil.hashingText(adminRequestData.password));
            if (data != null)
                return StatusCode(409, "amdin already exist");
            var adminId = Guid.NewGuid();
            var adminData = new AdminDto(
                adminId,
                adminRequestData.userName,
                password: clsUtil.hashingText(adminRequestData.password)
                ,
                new PersonDto(
                    personID: null,
                    email:adminRequestData.email,
                    name:adminRequestData.name,
                    adminRequestData.phone,
                    adminRequestData.address));

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
    }
}