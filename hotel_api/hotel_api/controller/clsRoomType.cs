using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Threading.Tasks;
using hotel_business;
using hotel_data.dto;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;

namespace hotel_api.controller
{
    [Route("[controller]")]
    public class clsRoomType : Controller
    {
        private readonly ILogger<clsRoomType> _logger;

        public clsRoomType(ILogger<clsRoomType> logger)
        {
            _logger = logger;
        }




        [ProducesResponseType(StatusCodes.Status201Created)]
        [ProducesResponseType(StatusCodes.Status409Conflict)]
        [ProducesResponseType(StatusCodes.Status500InternalServerError)]
        public ActionResult createRoomType(clsRoomTypeDto roomtypeData)
        {
            if (clsRoomTypeBuisness.isExistByName(roomtypeData.name))
                return StatusCode(409, "typeName already is exist");

            var roomTypeHolder = new clsRoomTypeBuisness(roomtypeData.name, roomtypeData.description, roomtypeData.employeeid);

            bool result = roomTypeHolder.Save();

            if (result == false) return StatusCode(500, "typeName already is exist");

            return StatusCode(201, "roomType insert seccessfuly");
        }

    }
}