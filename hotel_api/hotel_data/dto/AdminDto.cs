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
        public string imagePath { get; set; }

        public AdminDto(
            Guid adminID,
            string userName,
            string password,
            Guid? personsId, 
            PersonDto? personData,
            string? imagePath=""
            )
        {
            this.adminID = adminID;
            this.userName = userName;
            this.password = password;
            this.personData = personData;
            this.personID = personsId;
            this.imagePath = imagePath;
        }

        public UserDto toUserDto()
        {
            var personData = new PersonDto(
                personID: this.personID,
                email: this.personData.email,
                phone: this.personData.phone,
                name: this.personData.name,
                address: this.personData.address
            );

            var userData = new UserDto(
                userId: this.adminID,
                userName: this.userName,
                password: "",
                personData: personData,
                imagePath: this.imagePath,
                isDeleted:false
            );
            userData.isUser = false;
            return userData;
        }

    }
}