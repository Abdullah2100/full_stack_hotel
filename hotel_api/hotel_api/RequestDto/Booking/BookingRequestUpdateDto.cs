namespace hotel_api_.RequestDto.Booking;

public class BookingRequestUpdateDto
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
    
}