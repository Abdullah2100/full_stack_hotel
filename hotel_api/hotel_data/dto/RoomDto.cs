namespace hotel_data.dto;

public class RoomDto
{
    public Guid roomId { get; set; }
    public Guid userId { get; set; }
    public enStatsu status {get; set;} = enStatsu.Available; 
    public decimal pricePerNight {get; set;}
    public int capacity {get; set;}
    public Guid roomtypeid {get; set;}
    public DateTime createdAt { get; set; } = DateTime.UtcNow;
    public int bedNumber { get; set; }
    public Guid beglongTo {get; set;}
    //public List<string> images { get; set; } = new List<string>();
}