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


        [HttpPost("adminSinUp")]
        [ProducesResponseType(StatusCodes.Status201Created)]
        [ProducesResponseType(StatusCodes.Status500InternalServerError)]
        [ProducesResponseType(StatusCodes.Status409Conflict)]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        public ActionResult adminSignUp(
            AdminRequestDto adminRequestData
        )
        {
            string? validateRequeset = clsValidation.validateInput(adminRequestData);
            
            if(validateRequeset!=null)
                return BadRequest($"{validateRequeset}");
 

            var data = AdminBuissnes.getAdmin(adminRequestData.userName,
                clsUtil.hashingText(adminRequestData.password));
            if (data != null)
                return StatusCode(409, "amdin already exist");
            var adminId = Guid.NewGuid();
            var adminData = new AdminDto(
                adminId,
                adminRequestData.userName,
                adminRequestData.password,
                new PersonDto(
                    null,
                    adminRequestData.name,
                    adminRequestData.email,
                    adminRequestData.phone,
                    adminRequestData.address));
             
            var adminHolder = new AdminBuissnes(adminData);
            var result = adminHolder.save();
            string accesstoken = "", refreshToken = "";
            if (result == false)
                return StatusCode(500, "some thing wrong");

            accesstoken = AuthinticationServices.generateToken(adminId, adminData.personData.email, _config,
                AuthinticationServices.enTokenMode.AccessToken);
            refreshToken = AuthinticationServices.generateToken(adminId, adminData.personData.email, _config,
                AuthinticationServices.enTokenMode.RefreshToken);

            return StatusCode(201, new { accessToken = $"{accesstoken}", refreshToken = $"{refreshToken}" });
        }


        [HttpPost("addminSinIn")]
        [ProducesResponseType(StatusCodes.Status201Created)]
        [ProducesResponseType(StatusCodes.Status500InternalServerError)]
        [ProducesResponseType(StatusCodes.Status409Conflict)]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        public ActionResult adminSignIn(AdminDto adminData)
        {
            if (adminData.personData == null)
                return BadRequest("data must not be empty");

            var data = AdminBuissnes.getAdmin(adminData.userName, clsUtil.hashingText(adminData.password));
            if (data == null)
                return StatusCode(409, "amdin not exist");
            string accesstoken = "", refreshToken = "";
            if (data.adminID == null)
            {
                accesstoken = AuthinticationServices.generateToken(data.adminID, adminData.personData.email, _config,
                    AuthinticationServices.enTokenMode.AccessToken);
                refreshToken = AuthinticationServices.generateToken(data.adminID, adminData.personData.email, _config,
                    AuthinticationServices.enTokenMode.RefreshToken);
            }


            return StatusCode(201, new { accessToken = $"{accesstoken}", refreshToken = $"{refreshToken}" });
        }
    }
}