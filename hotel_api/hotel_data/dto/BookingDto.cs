namespace hotel_data.dto;

public class BookingDto
{
 

    public enum enBookingStatus {Pending, Confirmed, Cancelled}
    public Guid? ID { get; set; }
    public Guid roomid { get; set; }
    public Guid userID { get; set; }
    public int days { get; set; }
    public enBookingStatus bookingStatus { get; set; }
    public decimal totalPrice { get; set; }
    public decimal firstPaymen { get; set; }
    public decimal servicePayemen { get; set; }
    public decimal maintainPayment { get; set; }
    public DateTime excpectedleavedAt { get; set; }
    public DateTime? leavedAt { get; set; }
    public DateTime? createdAt { get; set; }=DateTime.Now;
    
    public RoomDto? room { get; set; }
    public UserDto? user { get; set; }
    
    public BookingDto(
        Guid? id, 
        Guid roomid,
        Guid userId, 
        int days, 
        enBookingStatus bookingStatus, 
        decimal totalPrice,
        decimal firstPaymen, 
        decimal servicePayemen,
        decimal maintainPayment, 
        DateTime excpectedleavedAt,
        DateTime? leavedAt = null,
        DateTime? createdAt = null
        )
    {
        ID = id;
        this.roomid = roomid;
        userID = userId;
        this.days = days;
        this.bookingStatus = bookingStatus;
        this.totalPrice = totalPrice;
        this.firstPaymen = firstPaymen;
        this.servicePayemen = servicePayemen;
        this.maintainPayment = maintainPayment;
        this.excpectedleavedAt = excpectedleavedAt;
        this.leavedAt = leavedAt;
        this.createdAt = createdAt;
        this.room = RoomData.getRoom(roomid);
        this.user = UserData.getUser(userId);
    }


    public static string convertBookingStatus(enBookingStatus status)
    {
        switch (status)
        {
            case enBookingStatus.Pending:return "Pending";
            case enBookingStatus.Confirmed:return "Confirmed";
            default: return "Cancelled";
            
        }
    }

    public static enBookingStatus convertBookingStatus(string status)
    {
        switch (status)
        {
           case "Pending": return enBookingStatus.Pending;
           case "Confirmed": return enBookingStatus.Confirmed;
           default: return enBookingStatus.Cancelled;
        }
    }
}