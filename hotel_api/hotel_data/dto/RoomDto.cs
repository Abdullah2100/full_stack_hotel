namespace hotel_data.dto;

public class RoomDto
{
    public RoomDto(
        Guid roomId, 
        enStatsu status, 
        decimal pricePerNight, 
        int capacity, 
        Guid roomtypeid, 
        int bedNumber, 
        Guid beglongTo,
        DateTime createdAt
        )
    {
        this.roomId = roomId;
        this.status = status;
        this.pricePerNight = pricePerNight;
        this.capacity = capacity;
        this.roomtypeid = roomtypeid;
        this.bedNumber = bedNumber;
        this.beglongTo = beglongTo;
        this.createdAt = createdAt;
    }

    public Guid roomId { get; set; }
    public enStatsu status {get; set;} = enStatsu.Available; 
    public decimal pricePerNight {get; set;}
    public int capacity {get; set;}
    public Guid roomtypeid {get; set;}
    public DateTime createdAt { get; set; } = DateTime.UtcNow;
    public int bedNumber { get; set; }
    public Guid beglongTo {get; set;}
    public List<string> images { get; set; } = new List<string>();
}