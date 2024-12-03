using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Npgsql;

namespace hotel_data
{
    public class PermissionData
    {
        public static string connectionUrl = clsConnnectionUrl.url;

        public static bool createPermission(
            int permissionNum,
            string description
            )
        {
            bool isCreated = false;
            try
            {
                using (var connection = new NpgsqlConnection(connectionUrl))
                {

                    connection.Open();

                    string query = @"
                                INSERT INTO permissions (permissionnum, description)
                                VALUES (@permissionNum,@desicription);
                                RETURNING PersonID;";
                    using (var cmd = new NpgsqlCommand(query, connection))
                    {
                        cmd.Parameters.AddWithValue("@permissionNum", permissionNum);
                        cmd.Parameters.AddWithValue("@desicription", description);
                        var resultID = cmd.ExecuteScalar();
                        isCreated =  true ;
                    }
                }
                return isCreated;
            }
            catch (Exception ex)
            {
                Console.WriteLine("\nthis error from  create {0} \n", ex.Message);

            }
            return isCreated;
        }

    }
}