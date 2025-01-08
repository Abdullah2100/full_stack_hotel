namespace hotel_api_.RequestDto;

public class RoomTypeRequest
{
 
        public RoomTypeRequest(
            string name,
            IFormFile image,
            Guid id
        )
        {
            this.name = name;
            this.image = image;
            this.id = id;
        }

        public Guid? id { get; set; } = null;
        public string name { get; set; }
        public IFormFile? image { get; set; } = null;
}