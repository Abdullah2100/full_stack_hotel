using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Npgsql;

namespace hotel_data
{
    public class DepartmentData
    {
           public static string connectionUrl = clsConnnectionUrl.url;

        public static bool createDeparmtment(
            string name 
            )
        {
            bool isCreated = false;
            try
            {
                using (var connection = new NpgsqlConnection(connectionUrl))
                {

                    connection.Open();

                    string query = @"
                                INSERT INTO departments (name)
                                VALUES (@name);
                                RETURNING departmentid;";

                    using (var cmd = new NpgsqlCommand(query, connection))
                    {
                        cmd.Parameters.AddWithValue("@permissionNum", name);

                        var resultID = cmd.ExecuteScalar();
                        isCreated = ((long)resultID) > 0 ? true : false;
                    }
                }
                return isCreated;
            }
            catch (Exception ex)
            {
                Console.WriteLine("\nthis error from department create {0} \n", ex.Message);

            }
            return isCreated;
        }

    }
}