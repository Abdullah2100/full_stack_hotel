using hotel_data.dto;

namespace hotel_api_.RequestDto;

public class RoomRequestDto
{
    public enStatsu status {get; set;} 
    
    public decimal pricePerNight {get; set;}
    
    public int capacity {get; set;}
    
    public Guid roomtypeid {get; set;}
    
    public int bedNumber { get; set; }

    public List<ImageRequestDto>? images { get; set; }
    public string? location { get; set; }
    public double?  latitude { get; set; }
    public double? longitude { get; set; }
}