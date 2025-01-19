using hotel_data.dto;
using Npgsql;

namespace hotel_data;

public class RoomData
{
    static string connectionUr = clsConnnectionUrl.url;

    public static RoomDto getRoom(Guid roomID)
    {
        RoomDto? room = null;
        try
        {
            using (var con = new NpgsqlConnection(connectionUr))
            {
                con.Open();
                string query = @"select * from rooms where roomid = @roomid";

                using (var cmd = new NpgsqlCommand(query, con))
                {
                    cmd.Parameters.AddWithValue("@roomid", roomID);
                    using (var reader = cmd.ExecuteReader())
                    {
                        if (reader.HasRows)
                        {
                            if (reader.Read())
                            {
                                string statusHolder = (string)reader["status"];
                                room = new RoomDto(
                                    roomId: (Guid)reader["roomid"],
                                    status: convertStatusToEnum(statusHolder),
                                    pricePerNight: (decimal)reader["pricepernight"],
                                    createdAt: (DateTime)reader["created_at"],
                                    roomtypeid: (Guid)reader["roomtypeid"],
                                    capacity: (int)reader["capacity"],
                                    bedNumber: (int)reader["bednumber"],
                                    beglongTo: (Guid)reader["belongto"]
                                );
                            }
                        }
                    }
                }
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine("this from getting user by id error {0}", ex.Message);
            return null;
        }

        return room;
    }

    public static bool isExist(Guid roomID)
    {
        bool isExist = false;
        try
        {
            using (var con = new NpgsqlConnection(connectionUr))
            {
                con.Open();
                string query = @"select * from rooms where roomid = @roomid";

                using (var cmd = new NpgsqlCommand(query, con))
                {
                    cmd.Parameters.AddWithValue("@roomid", roomID);
                    using (var reader = cmd.ExecuteReader())
                    {
                        if (reader.HasRows)
                        {
                            if (reader.Read())
                            {
                                isExist = true;
                            }
                        }
                    }
                }
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine("this from getting user by id error {0}", ex.Message);
            isExist = false;
        }

        return isExist;
    }


    public static bool createRoom
    (
        RoomDto roomData
    )
    {
        bool isCration = false;
        try
        {
            using (var con = new NpgsqlConnection(connectionUr))
            {
                con.Open();
                string query = @"select * from fn_room_insert_new
                                (
                               @status::VARCHAR,
                               @pricePerNight_::NUMERIC,
                               @CreatedAt_::TIMESTAMP , 
                               @roomtypeid_ ,
                               @capacity_::INT ,
                               @bedNumber_::INT ,
                               @belongTo_
                                )";

                using (var cmd = new NpgsqlCommand(query, con))
                {
                    cmd.Parameters.AddWithValue("@status",reversedStatusEnumToString(roomData.status));
                    cmd.Parameters.AddWithValue("@pricePerNight_", roomData.pricePerNight);
                    cmd.Parameters.AddWithValue("@CreatedAt_", roomData.createdAt);
                    cmd.Parameters.AddWithValue("@roomtypeid_", roomData.roomtypeid);
                    cmd.Parameters.AddWithValue("@capacity_", roomData.capacity);
                    cmd.Parameters.AddWithValue("@bedNumber_", roomData.bedNumber);
                    cmd.Parameters.AddWithValue("@belongTo_", roomData.beglongTo);
                    var reader = cmd.ExecuteScalar();
                    if (reader != null && bool.TryParse(reader.ToString(),out bool result))
                    {
                        isCration = result;
                    }
               
                }
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine("this from create room {0}", ex.Message);
           
        }

        return isCration;
    }
    
    
    public static bool updateRoom
    (
        RoomDto roomData
    )
    {
        bool isUpdate = false;
        try
        {
            using (var con = new NpgsqlConnection(connectionUr))
            {
                
                con.Open();
                string query = @"select * from fn_room_update_new
                                (
                               @roomid_,
                               @status::VARCHAR,
                               @pricePerNight_::NUMERIC,
                               @roomtypeid_ ,
                               @capacity_::INT ,
                               @bedNumber_::INT
                                )";
                
                using (var cmd = new NpgsqlCommand(query, con))
                {
                    cmd.Parameters.AddWithValue("@roomid_", roomData.roomtypeid);
                    cmd.Parameters.AddWithValue("@status",reversedStatusEnumToString(roomData.status));
                    cmd.Parameters.AddWithValue("@pricePerNight_", roomData.pricePerNight);
                    cmd.Parameters.AddWithValue("@roomtypeid_", roomData.roomtypeid);
                    cmd.Parameters.AddWithValue("@capacity_", roomData.capacity);
                    cmd.Parameters.AddWithValue("@bedNumber_", roomData.bedNumber);
                    var reader = cmd.ExecuteScalar();
                    if (reader != null && bool.TryParse(reader.ToString(),out bool result))
                    {
                        isUpdate = result;
                    }
                }
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine("this from create room {0}", ex.Message);
           
        }

        return isUpdate;
    }

 
    
    private static enStatsu convertStatusToEnum(string status)
    {
        switch (status)
        {
            case "Available":
            {
                return enStatsu.Available;
            }
            case "Booked":
            {
                return enStatsu.Booked;
            }
            default: return enStatsu.UnderMaintenance;
        }
    }
    
    
    private static string reversedStatusEnumToString(enStatsu status)
    {
        switch (status)
        {
            case enStatsu.Available:
            {
                return "Available"  ;
            }
            case enStatsu.Booked :
            {
                return "Booked";
            }
            default: return "Under Maintenance";
        }
    }

    
}