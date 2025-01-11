using System.ComponentModel.DataAnnotations;

namespace hotel_api_.RequestDto;

public class RoomTypeRequest
{
    public Guid? id { get; set; } = null;
    [Required] 
    [StringLength(50)]
    public string name { get; set; }
    public IFormFile? image { get; set; } = null;
}