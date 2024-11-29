using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using hotel_data;
using hotel_data.dto;
using hotel_business;

namespace hotel_api.controller
{
    [ApiController]
    [Route("api/[controller]")]
    public class AdminController : ControllerBase
    {
        [HttpPost(Name = "AddStudent")]
        [ProducesResponseType(StatusCodes.Status201Created)]
        [ProducesResponseType(StatusCodes.Status500InternalServerError)]
        [ProducesResponseType(StatusCodes.Status409Conflict)]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        public ActionResult createAdmin(AdminDto adminData)
        {
            if (adminData.personData == null)
                return BadRequest("data must not be empty");

            var data = AdminBuissnes.getAdmin(adminData.userName, adminData.password);
            if (data != null)
                return StatusCode(409, "amdin already exist");

            var adminHolder = new AdminBuissnes(adminData);
            var result = adminHolder.save();

            if (result == false)
                return StatusCode(500, "some thing wrong");

            return StatusCode(201, "admin created");

        }


    }
}