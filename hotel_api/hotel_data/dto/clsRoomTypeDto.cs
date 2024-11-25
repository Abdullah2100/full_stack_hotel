using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace hotel_data.dto
{
    public class clsRoomTypeDto
    {
        public  int? id { get; set; }=null;
        public string name { get; set; }="";
        public string description { get; set; }="";
    }
}