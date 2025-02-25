using System.ComponentModel.DataAnnotations;
using hotel_data.dto;

namespace hotel_api_.RequestDto.Booking;

public class BookingRequestUpdateDto
{
    public string enBookingStatus { get; set; }
    [Required] public Guid? ID { get; set; }

    [Required] public Guid roomid { get; set; }
    //[Required] public Guid userID { get; set; }

    public int? days { get; set; }

    public BookingDto.enBookingStatus? bookingStatus { get; set; }

    public decimal? servicePayemen { get; set; }

    public decimal? maintainPayment { get; set; }

    public DateTime? leavedAt { get; set; }
}