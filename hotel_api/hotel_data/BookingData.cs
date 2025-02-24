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
                      
                            cmd.Parameters.AddWithValue("@RoomID_",bookingData.roomid);
                            cmd.Parameters.AddWithValue("@Dayes_",bookingData.days);
                            cmd.Parameters.AddWithValue("@Dayes_",bookingData.days);
                            cmd.Parameters.AddWithValue("@BookingStatus_",BookingDto.convertBookingStatus(bookingData.bookingStatus));
                            cmd.Parameters.AddWithValue("@TotalPrice_",bookingData.totalPrice);
                            cmd.Parameters.AddWithValue("@FristPayment_",bookingData.firstPaymen);
                            cmd.Parameters.AddWithValue("@ServicePayment_",bookingData.servicePayemen>0?bookingData.servicePayemen:0);
                            cmd.Parameters.AddWithValue("@MaintincePayment_",bookingData.maintainPayment>0?bookingData.maintainPayment:0);
                            cmd.Parameters.AddWithValue("@MaintincePayment_",bookingData.excpectedleavedAt);
                            cmd.Parameters.AddWithValue("@userid_",bookingData.userID);
                            


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
        
/*
        public static bool updateUser(UserDto userData)
        {
            bool isCreated = false;
            try
            {
                using (var con = new NpgsqlConnection(connectionUr))
                {
                    con.Open();
                    string query = @"SELECT * FROM fn_user_update ( 
                                  @userId_u , 
                                  @name::VARCHAR, 
                                  @phone::VARCHAR ,
                                  @address::TEXT, 
                                  @username::VARCHAR, 
                                  @password::TEXT, 
                                  @IsVIP::BOOLEAN, 
                                   @personid_u,
                                   @brithday_u::DATE
                                 
                                    ) ";
                    using (var cmd = new NpgsqlCommand(query, con))
                    {
                        cmd.Parameters.AddWithValue("@userId_u", userData.userId);
                        cmd.Parameters.AddWithValue("@name", userData.personData.name);
                        cmd.Parameters.AddWithValue("@phone", userData.personData.phone);
                        cmd.Parameters.AddWithValue("@address", userData.personData.address);
                        cmd.Parameters.AddWithValue("@username", userData.userName);
                        cmd.Parameters.AddWithValue("@password", userData.password);
                        cmd.Parameters.AddWithValue("@IsVIP", userData.isVip);
                        cmd.Parameters.AddWithValue("@personid_u", userData.personData.personID);
                        cmd.Parameters.AddWithValue("@brithday_u", userData.brithDay);
                      
                        var result = cmd.ExecuteScalar();
                        if (result != null && int.TryParse(result?.ToString(), out int userId))
                        {
                            isCreated = userId > 0;
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
 */
}