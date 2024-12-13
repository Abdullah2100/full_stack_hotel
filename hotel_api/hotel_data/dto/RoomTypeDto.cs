namespace hotel_data.dto;

public class RoomTypeDto
{
    public RoomTypeDto(Guid? roomTypeId, string roomTypeName, Guid createdBy, DateTime createdAt)
    {
        roomTypeID = roomTypeId;
        this.roomTypeName = roomTypeName;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }

    public Guid? roomTypeID { get; set; }
    public string roomTypeName { get; set; }
    public Guid createdBy { get; set; }
    public DateTime createdAt { get; set; }
    
}