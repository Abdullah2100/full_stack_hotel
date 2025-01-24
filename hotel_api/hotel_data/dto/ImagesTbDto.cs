namespace hotel_data.dto;

public class ImagesTbDto
{
    public ImagesTbDto(
        Guid? imagePathId,
        string imagePath,
        Guid belongTo
      
        )
    {
        this.id = imagePathId;
        this.path = imagePath;
        this.belongTo = belongTo;
    }

    public Guid? id  { get; set; }
    public string path { get; set; }
    public Guid belongTo   { get; set; }    
    
}