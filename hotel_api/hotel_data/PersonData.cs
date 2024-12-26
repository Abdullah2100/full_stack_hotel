using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using hotel_data.dto;
using Npgsql;

namespace hotel_data
{
    public class PersonData
    {
        public static string connectionUrl = clsConnnectionUrl.url;

        public static bool createPerson(
           PersonDto personData
            )
        {
            bool isCreated = false;
            try
            {
                using (var connection = new NpgsqlConnection(connectionUrl))
                {

                    connection.Open();

                    string query = @"
                                INSERT INTO persons ( name , email, phone, address)
                                VALUES (@name,@email,@phone,@address);
                                RETURNING personid;";

                    using (var cmd = new NpgsqlCommand(query, connection))
                    {

                        cmd.Parameters.AddWithValue("@name", personData.name);
                        cmd.Parameters.AddWithValue("@phone", personData.phone);
                        cmd.Parameters.AddWithValue("@email", personData.email);
                        cmd.Parameters.AddWithValue("@address", personData.address);

                      cmd.ExecuteScalar();
                        isCreated =  true ;
                    }
                }
                return isCreated;
            }
            catch (Exception ex)
            {
                Console.WriteLine("\nthis error from person create {0} \n", ex.Message);

            }
            return isCreated;
        }

        public static bool updatePerson(
                    PersonDto personData
           )
        {
            bool isUpdate = false;
            try
            {
                using (var connection = new NpgsqlConnection(connectionUrl))
                {

                    connection.Open();

                    string query = @"
                                UPDATE INTO persons 
                                SET name = @name, phone = @phone,address=@address)
                                WHERE personid =@id;";

                    using (var cmd = new NpgsqlCommand(query, connection))
                    {
                        if (personData != null)
                        {
                            cmd.Parameters.AddWithValue("@id", personData.personID!);
                            
                        cmd.Parameters.AddWithValue("@name", personData.name);
                        cmd.Parameters.AddWithValue("@phone", personData.phone);
                        cmd.Parameters.AddWithValue("@address", personData.address);
                        }
                        
                        cmd.ExecuteNonQuery();
                        isUpdate = true;
                    }
                }
                return isUpdate;
            }
            catch (Exception ex)
            {
                Console.WriteLine("\nthis error from person create {0} \n", ex.Message);

            }
            return isUpdate;
        }




        public static bool deletePerson(
          Guid id
       )
        {
            bool isDelelted = false;
            try
            {
                using (var connection = new NpgsqlConnection(connectionUrl))
                {

                    connection.Open();

                    string query = @" DELETE FROM  persons WHERE personid =@id;";

                    using (var cmd = new NpgsqlCommand(query, connection))
                    {
                        cmd.Parameters.AddWithValue("@id", id);

                        cmd.ExecuteNonQuery();
                         isDelelted = true;
                    }
                }
                return isDelelted;
            }
            catch (Exception ex)
            {
                Console.WriteLine("\nthis error from person deleted {0} \n", ex.Message);

            }
            return isDelelted;
        }


        public static PersonDto? getPerson(
            Guid id, ref bool isDelelted
            )
        {
            try
            {
                using (var connection = new NpgsqlConnection(connectionUrl))
                {

                    connection.Open();

                    string query = @" SELECT * FROM  persons WHERE personid =@id;";

                    using (var cmd = new NpgsqlCommand(query, connection))
                    {
                        cmd.Parameters.AddWithValue("@id", id);

                        using (var result = cmd.ExecuteReader())
                        {
                            if (result.HasRows)
                            {
                                isDelelted = (bool)result["isdeleted"];
                                var person = new PersonDto
                                (
                                   personID: id,
                                    email:(string)result["name"],
                                    name:(string)result["email"],
                                    phone:result["address"] == DBNull.Value ? "" : (string)result["address"],
                                    address:(string)result["phone"],
                                    createdAt: (DateTime)result["CreatedAt"]
                                );
                            }
                        }
                    }
                }
                return null;
            }
            catch (Exception ex)
            {
                Console.WriteLine("\nthis error from person getPerson by id {0} \n", ex.Message);

            }
            return null;
        }

      

        public static bool isExist(
            Guid id
            )
        {
            bool isExist = false;
            try
            {
                using (var connection = new NpgsqlConnection(connectionUrl))
                {

                    connection.Open();

                    string query = @" SELECT * FROM  persons WHERE personid =@id;";

                    using (var cmd = new NpgsqlCommand(query, connection))
                    {
                        cmd.Parameters.AddWithValue("@id", id);

                        using (var result = cmd.ExecuteReader())
                        {
                            if (result.Read())
                                isExist = true;
                        }
                    }
                }
                return isExist;
            }
            catch (Exception ex)
            {
                Console.WriteLine("\nthis error from person deleted {0} \n", ex.Message);

            }
            return isExist;
        }


        public static bool isExist(
               string email,
               string phone
               )
        {
            bool isExist = false;
            try
            {
                using (var connection = new NpgsqlConnection(connectionUrl))
                {

                    connection.Open();

                    string query = @" SELECT * FROM  persons WHERE email =@email OR phone = @phone;";

                    using (var cmd = new NpgsqlCommand(query, connection))
                    {
                        cmd.Parameters.AddWithValue("@email", email);
                        cmd.Parameters.AddWithValue("@phone", phone);

                        using (var result = cmd.ExecuteReader())
                        {
                            if (result.Read())
                                isExist = true;
                        }
                    }
                }
                return isExist;
            }
            catch (Exception ex)
            {
                Console.WriteLine("\nthis error from person deleted {0} \n", ex.Message);

            }
            return isExist;
        }


    }
}