using hotel_data.dto;
using Microsoft.AspNetCore.Mvc;

namespace hotel_api_.RequestDto;

public class ImageRequestDto
{
    // public ImagesTbDto image;
    public Guid? id { get; set; }
    public string? path { get; set; }
    public Guid? belongTo { get; set; }
    public bool? isDeleted { get; set; } = false;
    public bool? isThumnail { get; set; } = false;
    [FromForm] 
    public IFormFile? data { get; set;}
}