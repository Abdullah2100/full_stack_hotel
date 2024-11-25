using hotel_data.dto;
using Npgsql;

namespace hotel_data
{
   public sealed class clsRoomTypeData
    {


        public static Boolean createRoomType(string name,string description)
        {
            bool isCreated = false;
            try
            {
                using (var connection = new NpgsqlConnection(clsConnnectionUrl.url))
                {
                    string query = @"INSERT INTO roomtypes (typename,description) 
                           VALUES(@typeName,@description)
                           RETURNING RoomTypeID;";

                    using (var cmd = new NpgsqlCommand(query, connection))
                    {
                        cmd.Parameters.AddWithValue("@typeName", name);
                        cmd.Parameters.AddWithValue("@description", description);

                        var id = cmd.ExecuteScalar();

                        isCreated = id != null;
                    }
                }

                return isCreated;
            }
            catch (Exception ex)
            {

                return isCreated;
            }
        }

    }
}