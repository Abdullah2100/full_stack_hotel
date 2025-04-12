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
                  
                 // Option 1: Using positional parameters
                 string query = @"SELECT * FROM fn_bookin_insert(@roomid_ , @totalprice_, @userid_,@startbookindate_,@endbookingdate_)";

                 using (var cmd = new NpgsqlCommand(query, con))
                 {
                     cmd.Parameters.AddWithValue("@roomid_",bookingData.roomId);
                     cmd.Parameters.AddWithValue("@totalprice_",bookingData.totalPrice);
                     cmd.Parameters.AddWithValue("@userid_",bookingData.userId);
                     cmd.Parameters.AddWithValue("@startbookindate_",bookingData.bookingStart);
                     cmd.Parameters.AddWithValue("@endbookingdate_",bookingData.bookingEnd);
                
                     var result = cmd.ExecuteScalar();
                     isCreated = result != null && (bool)result;
                 }
             }
         }
         catch (Exception ex)
         {
             Console.WriteLine("Error creating booking: {0}", ex);
         }
         return isCreated;
     }
       
        public static bool isValideBookingDate(
            DateTime startBookingDate, 
            DateTime endBookingDate)
        {
            bool isVisibleBooking = false;
            try
            {
                using (var con = new NpgsqlConnection(connectionUr))
                {
                    con.Open();
                    string query = @"SELECT * FROM fn_isValid_booking(
                                   @startBooking,
                                  @endBooking 
                                    );";

                    using (var cmd = new NpgsqlCommand(query, con))
                    {
                        cmd.Parameters.AddWithValue("@startBooking", startBookingDate);
                        cmd.Parameters.AddWithValue("@endBooking", endBookingDate);
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

       public static  List<string>? getBookingDayesAtMonthAndYearData(
            int year, 
            int month
            )
       {
           List<string>? bookingsDayAtYearAndMonth = null;
            try
            {
                using (var con = new NpgsqlConnection(connectionUr))
                {
                    con.Open();
                    string query = @"SELECT * FROM fun_get_list_of_booking_at_specific_month_and_year(
                                   @year_,
                                  @month_ 
                                    );";

                    using (var cmd = new NpgsqlCommand(query, con))
                    {
                        cmd.Parameters.AddWithValue("@year_", year);
                        cmd.Parameters.AddWithValue("@month_", month);
                        var result = cmd.ExecuteScalar();

                        if (result != null && result.ToString().Length>0)
                        {
                            bookingsDayAtYearAndMonth = Convert.ToString(result)?.Split(',').ToList(); 
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine("this from check if the booking is visible error {0}", ex);
            }

            return bookingsDayAtYearAndMonth;
        }



}