using hotel_data;
using hotel_data.dto;

namespace hotel_business;

public class ImageBuissness
{
    public enum enMode {add,update}
    public enMode mode { get; set; }  
    public Guid? ID  { get; set; }
    public string path { get; set; }
    public Guid belongTo   { get; set; }    
    public DateTime createdAt { get; set; }
    public ImagesTbDto imageHolder{get{return new ImagesTbDto(imagePathId:ID,imagePath:path,belongTo:belongTo,createdAt:createdAt);}}
    public ImageBuissness(ImagesTbDto image,enMode mode)
    {
        this.ID = image.imagePathId;
        this.path = image.imagePath;
        this.belongTo = image.belongTo;
        this.createdAt = image.createdAt;
        this.mode = mode;   
    }

    private bool _createImage()
    {
        return ImagesData.createImages(imageHolder);
    }

    private bool _updateImage()
    {
        return ImagesData.updateImages(imageHolder);
    }
    public bool save()
    {
        switch (mode)
        {
            case enMode.add:
            {
                return _createImage() ? true : false; 
            }
            case enMode.update: return _updateImage()? true : false;
            
            default: return false;
        }
    } 

}