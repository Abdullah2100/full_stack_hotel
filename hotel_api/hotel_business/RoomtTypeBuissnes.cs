using hotel_data;
using hotel_data.dto;

namespace hotel_business;

public class RoomtTypeBuissnes
{
   public enum enMode {add,update}
   public enMode mode { get; set; }  
    public Guid? ID { get; set; }
    public string name { get; set; }
    public string image { get; set; }
    public Guid createdBy { get; set; }
    public DateTime createdAt { get; set; }

    public RoomTypeDto roomType
    {
        get
        {
            return new RoomTypeDto(roomTypeId: ID, roomTypeName: name, createdAt: createdAt, createdBy: createdBy,imagePath:image);
        }
    }

    public RoomtTypeBuissnes(RoomTypeDto roomType,enMode mode =enMode.add)
    {
        this.ID = roomType.roomTypeID;
        this.name = roomType.roomTypeName;
        this.createdBy = roomType.createdBy;
        this.createdAt = roomType.createdAt;
        this.image = roomType.imagePath;
        this.mode = mode;
    }

    private bool _createRoomType()
    {
        return RoomTypeData.createRoomType(roomType);
    }

    private bool _updateRoomType()
    {
        return RoomTypeData.updateRoomType(roomType);
    }
    
    public bool save()
    {
        switch (mode)
        {
            case enMode.add:
            {
                if (_createRoomType())
                {
                    return true;
                }
                return false;
            }
            case enMode.update:
            {
                if (_updateRoomType())
                {
                    return true;
                }
                return false;
            }
            default: return false;
        }
    }

    public static bool isExist(Guid id)
    {
        return RoomTypeData.isExist(id);
    }
    public static bool isExist(string  name)
    {
        return RoomTypeData.isExist(name);
    }

    public static List<RoomTypeDto> getRoomTypes()
    {
        return RoomTypeData.getAll();
    }
}