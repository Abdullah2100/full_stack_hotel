using hotel_data;
using hotel_data.dto;

namespace hotel_business;

public class BookingBuiseness
{
   
    enMode mode = enMode.add;
 
    public Guid? ID { get; set; }
    public Guid roomid { get; set; }
    public Guid userID { get; set; }
    public int days { get; set; }
    public BookingDto.enBookingStatus bookingStatus { get; set; }
    public decimal totalPrice { get; set; }
    public decimal firstPaymen { get; set; }
    public decimal servicePayemen { get; set; }
    public decimal maintainPayment { get; set; }
    public DateTime excpectedleavedAt { get; set; }
    public DateTime? leavedAt { get; set; }
    public DateTime? createdAt { get; set; }=DateTime.Now;
    
    public BookingDto booking { get
    {
        return new BookingDto(
            id: this.ID,
            roomid: this.roomid,
            userId: this.userID,
            days: this.days,
            bookingStatus: this.bookingStatus,
            totalPrice: this.totalPrice,
            firstPaymen: this.firstPaymen,
            servicePayemen: this.servicePayemen,
            maintainPayment: this.maintainPayment,
            excpectedleavedAt: this.excpectedleavedAt,
            leavedAt: this.leavedAt,
            createdAt: this.createdAt
        );
    }}
    
    
    public BookingBuiseness(
        BookingDto booking
    )
    {
        ID = booking.ID;
        this.roomid = booking.roomid;
        userID = booking.userID;
        this.days = booking.days;
        this.bookingStatus = booking.bookingStatus;
        this.totalPrice = booking.totalPrice;
        this.firstPaymen = booking.firstPaymen;
        this.servicePayemen = booking.servicePayemen;
        this.maintainPayment = booking.maintainPayment;
        this.excpectedleavedAt = booking.excpectedleavedAt;
        this.leavedAt = booking.leavedAt;
        this.createdAt = booking.createdAt;
    }

    private bool _createBooking()
    {
        return BookingData.createBooking(this.booking);
    }

    public bool save()
    {
        switch (mode)
        {
            case enMode.add: return _createBooking();
            default: return false;
        } 
    }
 
}