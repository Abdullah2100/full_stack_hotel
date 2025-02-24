using System.ComponentModel.DataAnnotations;

namespace hotel_api_.RequestDto.Booking;

public class BookingRequestDto
{
    [Required]
    public string enBookingStatus {get; set;} 
    [Required]
    public Guid roomid { get; set; }
    [Required]
    public Guid userID { get; set; }
    [Required]
    public int days { get; set; }
    
    [Required]
    public decimal totalPrice { get; set; }
    [Required]
    public decimal firstPaymen { get; set; }
    [Required]
    public decimal servicePayemen { get; set; }
    [Required]
    public decimal maintainPayment { get; set; }
    [Required]
    public DateTime excpectedleavedAt { get; set; }
    
    public DateTime? leavedAt { get; set; }
    public DateTime? createdAt { get; set; }=DateTime.Now;
    
}