using hotel_data.dto;
using Npgsql;

namespace hotel_data
{
    public sealed class clsRoomTypeData
    {


        public static Boolean createRoomType(string name, string description, int EmployeeID)
        {
            bool isCreated = false;
            try
            {
                using (var connection = new NpgsqlConnection(clsConnnectionUrl.url))
                {
                    string query = @"INSERT INTO roomtypes (typename,description,employeeid) 
                           VALUES(@typeName,@description)
                           RETURNING RoomTypeID;";

                    using (var cmd = new NpgsqlCommand(query, connection))
                    {
                        cmd.Parameters.AddWithValue("@typeName", name);
                        cmd.Parameters.AddWithValue("@description", description);
                        cmd.Parameters.AddWithValue("@employeeid", EmployeeID);

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

        public static Boolean isExist(string name)
        {
            bool isExist = false;
            try
            {
                using (var connection = new NpgsqlConnection(clsConnnectionUrl.url))
                {
                    string query = @"SELECT * FROM roomtypes WHERE typename = @typename";

                    using (var cmd = new NpgsqlCommand(query, connection))
                    {
                        cmd.Parameters.AddWithValue("@typename", name);

                        using (var result = cmd.ExecuteReader())
                        {
                            isExist = result.HasRows;
                        }
                    }
                }

                return isExist;
            }
            catch (Exception ex)
            {

                return isExist;
            }
        }

    }
}