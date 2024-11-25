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


        public IActionResult Index()
        {
            return View();
        }

        [ResponseCache(Duration = 0, Location = ResponseCacheLocation.None, NoStore = true)]
        public IActionResult Error()
        {
            return View("Error!");
        }

        [ProducesResponseType(StatusCodes.Status201Created)]
        public ActionResult createRoomType(clsRoomTypeDto roomtypeData)
        {

            var roomTypeHolder = new clsRoomTypeBuisness(roomtypeData.name, roomtypeData.description);

            bool result = roomTypeHolder.Save();

        }

    }
}