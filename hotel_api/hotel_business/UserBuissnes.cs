using hotel_data;
using hotel_data.dto;

namespace hotel_business;

public class UserBuissnes
{
    public enum enMode {add,update}
    enMode mode = enMode.add; 
    public Guid ID { get; set; }
    public Guid? personID { get; set; }
    public Guid? addBy { get; set; }
    public DateTime brithDay { get; set; }
    public bool isVip { get; set; }
    public PersonDto personData { get; set; }
    public string userName { get; set; }
    public string password { get; set; }

    public UserDto userDataHolder
    {
        get
        {
            return new UserDto(
                userId: ID,
                personID: personData.personID ?? Guid.Empty,
                brithDay: brithDay,
                isVip: isVip,
                userName: userName,
                password: password,
                personData: personData
                ,addBy:addBy
            );
        }
    }

    public UserBuissnes(UserDto userData,enMode mode=enMode.add)
    {
        this.ID = userData.userId;
        this.personID = userData.personData.personID ?? Guid.Empty;
        this.brithDay = userData.brithDay;
        this.isVip = userData.isVip;
        this.personData = userData.personData;
        this.userName = userData.userName;
        this.password = userData.password;
        this.addBy = userData.addBy;
        this.mode = mode;
    }

    private bool _createUser()
    {
       return  UserData.createUser(userDataHolder); 
    }

    private bool _updateUser()
    {
        return UserData.updateUser(userDataHolder);
    }

    public bool save()
    {
        switch (mode)
        {
            case enMode.add:
            {
                return _createUser() ? true : false; 
            }
            case enMode.update: return _updateUser()? true : false;
            
            default: return false;
        }
    }

    public static  bool isExistByID(Guid id)
    {
        return UserData.isExist(id);
    }

    public static bool isExistByUserNameAndPassword(string username,string password )
    {
        return UserData.isExist(username,password);
    }

    public static UserBuissnes? getUserByID(Guid id)
    {
        var user = UserData.getUser(id);
        
        return user == null ? null : new UserBuissnes(user,enMode.update);
    }
    public static UserBuissnes? getUserByUserNameAndPassword(string userNme,string password)
    {
        var user = UserData.getUser(userNme, password);
        return user == null ? null : new UserBuissnes(user,enMode.update);
    }

    public static UserBuissnes? getUserByUserName(string userNme)
    {
        var user = UserData.getUser(userNme);
        return user == null ? null : new UserBuissnes(user,enMode.update);
    }


    public static bool deleteUser(Guid id)
    {
        return UserData.delete(id);
    }

    public static List<UserDto> getAllUsers(int pageNumber)
    {
        return UserData.getUsersByPage(pageNumber:pageNumber);
    }
    
}