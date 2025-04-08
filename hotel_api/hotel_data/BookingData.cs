using hotel_data.dto;
using Npgsql;

namespace hotel_data;

public class BookingData
{
    static string connectionUr = clsConnnectionUrl.url;
 
        
        public static bool createBooking(BookingDto bookingData)
        {
            bool isCreated = false;
            try
            {
                using (var con = new NpgsqlConnection(connectionUr))
                {
                    con.Open();
                    string query = @"SELECT * FROM fn_bookin_insert(
                                     RoomID_::UUID  ,
                                     Dayes_::INT,
                                     BookingStatus_::VARCHAR(50) ,
                                     TotalPrice_::NUMERIC(10, 2),
                                     FristPayment_::NUMERIC(10, 2) ,
                                     ServicePayment_::NUMERIC(10, 2) ,
                                     MaintincePayment_::NUMERIC(10, 2),
                                      MaintincePayment_::TIMESTAMP  ,
                                     userid_::UUID
                                )";

                    using (var cmd = new NpgsqlCommand(query, con))
                    {
                        var result = cmd.ExecuteScalar();

                        if (result != null && bool.TryParse(result?.ToString(), out bool isCreatedOutpu))
                        {
                            isCreated = isCreatedOutpu;
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine("this from create user error {0}", ex);
            }

            return isCreated;
        }
       
       
        public static bool createBooking(DateTime startBookingDate, DateTime endBookingDate)
        {
            bool isVisibleBooking = false;
            try
            {
                using (var con = new NpgsqlConnection(connectionUr))
                {
                    con.Open();
                    string query = @"SELECT * FROM fn_isValid_booking(
                                   startBookingDate::TIMESTAMP,
                                  endBookingDate::TIMESTAMP 
                                    ); ";

                    using (var cmd = new NpgsqlCommand(query, con))
                    {

                        var result = cmd.ExecuteScalar();

                        if (result != null && bool.TryParse(result?.ToString(), out bool isVisibleBookingResult))
                        {
                            isVisibleBooking = isVisibleBookingResult;
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine("this from check if the booking is visible error {0}", ex);
            }

            return isVisibleBooking;
        }

    

}