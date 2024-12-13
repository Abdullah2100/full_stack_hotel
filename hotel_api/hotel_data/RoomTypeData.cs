using hotel_data.dto;
using Npgsql;

namespace hotel_data;

public class RoomTypeData
{
    static string connectionUr = clsConnnectionUrl.url;

    public static bool createRoomType(RoomTypeDto roomData)
    {
        bool isCreated = false;
        try
        {
            using (var con = new NpgsqlConnection(connectionUr))
            {
                con.Open();
                string query = "SELECT fn_roomtype_insert( name_n , createdBy_n )";
                using (var cmd = new NpgsqlCommand(query, con))
                {
                    cmd.Parameters.AddWithValue("name_n", roomData.roomTypeName);
                    cmd.Parameters.AddWithValue("createdBy_n", roomData.createdBy);
                    var result = cmd.ExecuteScalar();
                    if (result != null && bool.TryParse(result.ToString(), out bool isComplate))
                    {
                        isCreated = isComplate;
                    }
                }
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine("this the error from create roomtype {0}", ex.Message);
        }

        return isCreated;
    }
    
   
    public static bool updateRoomType(RoomTypeDto roomData)
    {
        bool isCreated = false;
        try
        {
            using (var con = new NpgsqlConnection(connectionUr))
            {
                con.Open();
                string query = "SELECT fn_roomtype_update( name_n , createdBy_n )";
                using (var cmd = new NpgsqlCommand(query, con))
                {
                    cmd.Parameters.AddWithValue("name_n", roomData.roomTypeName);
                    cmd.Parameters.AddWithValue("createdBy_n", roomData.createdBy);
                    var result = cmd.ExecuteScalar();
                    if (result != null && bool.TryParse(result.ToString(), out bool isComplate))
                    {
                        isCreated = isComplate;
                    }
                }
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine("this the error from update roomtype {0}", ex.Message);
        }

        return isCreated;
    }

    public static bool isExist(Guid ID)
    {
        bool isExist = false;
        try
        {
            using (var con = new NpgsqlConnection(connectionUr))
            {
                con.Open();
                string query = "SELECT count(*)>0 FROM RoomTypes WHERE RoomTypeID = id";
                using (var cmd = new NpgsqlCommand(query, con))
                {
                    cmd.Parameters.AddWithValue("id",ID);
                    var result = cmd.ExecuteScalar();
                    if (result != null && bool.TryParse(result.ToString(), out bool isComplate))
                    {
                        isExist = isComplate;
                    }
                }
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine("this the error from create roomtype {0}", ex.Message);
        }

        return isExist;
    }

    public static bool isExist(string name )
    {
        bool isExist = false;
        try
        {
            using (var con = new NpgsqlConnection(connectionUr))
            {
                con.Open();
                string query = "SELECT count(*)>0 FROM RoomTypes WHERE name = name";
                using (var cmd = new NpgsqlCommand(query, con))
                {
                    cmd.Parameters.AddWithValue("name",name);
                    var result = cmd.ExecuteScalar();
                    if (result != null && bool.TryParse(result.ToString(), out bool isComplate))
                    {
                        isExist = isComplate;
                    }
                }
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine("this the error from create roomtype {0}", ex.Message);
        }

        return isExist;
    }
    
}