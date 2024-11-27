using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Npgsql;

namespace hotel_data
{
    public class PersonData
    {
        public static string connectionUrl = clsConnnectionUrl.url;

        public static bool createPerson(
            string name,
            string phone,
            string address
            )
        {
            bool isCreated = false;
            try
            {
                using (var connection = new NpgsqlConnection(connectionUrl))
                {

                    connection.Open();

                    string query = @"
                                INSERT INTO persons (name, phone, address)
                                VALUES (@name,@phone,@address);
                                RETURNING PersonID;";
                            
                    using (var cmd = new NpgsqlCommand(query,connection))
                    {

                        cmd.Parameters.AddWithValue("@name",name);
                        cmd.Parameters.AddWithValue("@phone",phone);
                        cmd.Parameters.AddWithValue("@address",address);
                        
                        var resultID = cmd.ExecuteScalar();
                        isCreated = ((long)resultID)>0?true:false;
                    }
                }
                return isCreated;
            }
            catch (Exception ex)
            {
                Console.WriteLine("\nthis error from person create {0} \n",ex.Message);

            }
            return isCreated;
        }
    }
}