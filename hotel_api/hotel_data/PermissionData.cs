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

        /// <summary>
        /// Creates a new permission entry in the database.
        /// </summary>
        /// <param name="permissionNum">The permission number.</param>
        /// <param name="description">The description of the permission.</param>
        /// <returns>True if the permission was created successfully; otherwise, false.</returns>
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
                        isCreated = ((long)resultID) > 0 ? true : false;
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