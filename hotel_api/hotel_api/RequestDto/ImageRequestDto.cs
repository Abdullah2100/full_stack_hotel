using hotel_data.dto;

namespace hotel_api_.RequestDto;

public class ImageRequestDto
{
    public ImagesTbDto image;
    public IFormFile? data;
}