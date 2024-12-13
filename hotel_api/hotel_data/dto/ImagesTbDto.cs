namespace hotel_data.dto;

public class ImagesTbDto
{
    public ImagesTbDto(Guid? imagePathId, string imagePath, Guid belongTo, DateTime createdAt)
    {
        this.imagePathId = imagePathId;
        this.imagePath = imagePath;
        this.belongTo = belongTo;
        this.createdAt = createdAt;
    }

    public Guid? imagePathId  { get; set; }
    public string imagePath { get; set; }
    public Guid belongTo   { get; set; }    
    public DateTime createdAt { get; set; }
    
}