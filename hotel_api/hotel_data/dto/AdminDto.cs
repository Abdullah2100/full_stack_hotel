using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace hotel_data.dto
{
    public class AdminDto
    {

        public int? adminID { get; set; }
        public string userName { get; set; }
        public string password { get; set; }
        public PersonDto? personData { get; set; }

        public AdminDto(int? adminID, string userName, string password, PersonDto? personData)
        {
            this.adminID = adminID;
            this.userName = userName;
            this.password = password;
            this.personData = personData;
        }

    }
}