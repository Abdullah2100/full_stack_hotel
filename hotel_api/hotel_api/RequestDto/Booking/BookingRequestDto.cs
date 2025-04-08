using System.ComponentModel.DataAnnotations;

namespace hotel_api_.RequestDto.Booking;

public class BookingRequestDto
{
    [Required] public Guid roomId { get; set; }
    [Required]  public Guid userId { get; set; }
    [Required] public DateTime bookingStart { get; set; }
    [Required] public DateTime bookingEnd { get; set; }
}