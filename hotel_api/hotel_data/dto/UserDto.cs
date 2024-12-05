using System;

namespace hotel_data.dto;

public class UserDto
{
    public UserDto(Guid userId, Guid personID, DateTime brithDay, bool isVip, PersonDto personData, string userName, string password)
    {
        this.userId = userId;
        this.personID = personID;
        this.brithDay = brithDay;
        this.isVip = isVip;
        this.personData = personData;
        this.userName = userName;
        this.password = password;
    }

    public Guid userId  { get; set; }
    public Guid personID  { get; set; }
    public DateTime brithDay   { get; set; }
    public bool isVip   { get; set; }
    public PersonDto personData { get; set; }
    public string userName { get; set; }
    public string password { get; set; }
    
}