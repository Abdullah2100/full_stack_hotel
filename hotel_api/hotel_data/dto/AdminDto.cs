using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace hotel_data.dto
{
    public class AdminDto
    {

        public Guid adminID { get; set; }
        public Guid? personID { get; set; }
        public string userName { get; set; }
        public string password { get; set; }
        public PersonDto? personData { get; set; }

        public AdminDto(Guid adminID, string userName, string password,Guid? personsId, PersonDto? personData)
        {
            this.adminID = adminID;
            this.userName = userName;
            this.password = password;
            this.personData = personData;
            this.personID = personsId;
        }

    }
}