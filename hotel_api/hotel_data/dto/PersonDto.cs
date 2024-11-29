using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace hotel_data.dto
{
    public class PersonDto
    {

        public long? personID { get; set; }
        public string name { get; set; }
        public string phone { get; set; }
        public string address { get; set; }

        public PersonDto(long? personID, string name, string phone, string address)
        {
            this.personID = personID;
            this.name = name;
            this.phone = phone;
            this.address = address;
        }

    }
}