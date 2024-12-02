using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using hotel_data;
using hotel_data.dto;
using hotel_business;
using hotel_api.util;
using hotel_api.Services;

namespace hotel_api.controller
{
    [ApiController]
    [Route("api/[controller]")]
    public class AdminController : ControllerBase
    {

        private readonly IConfigurationServices _config;



        [HttpPost(Name = "AddStudent")]
        [ProducesResponseType(StatusCodes.Status201Created)]
        [ProducesResponseType(StatusCodes.Status500InternalServerError)]
        [ProducesResponseType(StatusCodes.Status409Conflict)]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        public ActionResult adminSignUp(AdminDto adminData)
        {
            if (adminData.personData == null)
                return BadRequest("data must not be empty");

            var data = AdminBuissnes.getAdmin(adminData.userName, clsUtil.hashingText(adminData.password));
            if (data != null)
                return StatusCode(409, "amdin already exist");
            var adminId = Guid.NewGuid();
            adminData.adminID = adminId;
            var adminHolder = new AdminBuissnes(adminData);
            var result = adminHolder.save();
            string accesstoken = "", refreshToken = "";
            if (result == false)
                return StatusCode(500, "some thing wrong");

            accesstoken = AuthinticationServices.generateToken((Guid)data.adminID, adminData.personData.email, _config, AuthinticationServices.enTokenMode.AccessToken);
            refreshToken = AuthinticationServices.generateToken((Guid)data.adminID, adminData.personData.email, _config, AuthinticationServices.enTokenMode.RefreshToken);

            return StatusCode(201, new { accessToken = $"{accesstoken}", refreshToken = $"{refreshToken}" });

        }


        [HttpPost(Name = "AddStudent")]
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

                accesstoken = AuthinticationServices.generateToken((Guid)data.adminID, adminData.personData.email, _config, AuthinticationServices.enTokenMode.AccessToken);
                refreshToken = AuthinticationServices.generateToken((Guid)data.adminID, adminData.personData.email, _config, AuthinticationServices.enTokenMode.RefreshToken);
            }


            return StatusCode(201, new { accessToken = $"{accesstoken}", refreshToken = $"{refreshToken}" });

        }




    }
}