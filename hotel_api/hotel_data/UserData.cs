using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using hotel_data.dto;
using Npgsql;

namespace hotel_data
{
    public class UserData
    {
        static string connectionUr = clsConnnectionUrl.url;

        public static UserDto? getUser
        (
            Guid id
        )
        {
            try
            {
                using (var con = new NpgsqlConnection(connectionUr))
                {
                    con.Open();
                    string query = @"SELECT * FROM usersview WHERE userid = @id";

                    using (var cmd = new NpgsqlCommand(query, con))
                    {
                        cmd.Parameters.AddWithValue("@id", id);

                        using (var reader = cmd.ExecuteReader())
                        {
                            if (reader.Read())
                            {
                                if (((bool)reader["ispersondeleted"] == true)) return null;

                                var personData = new PersonDto(
                                    personID: (Guid)reader["personid"],
                                    email: (string)reader["email"],
                                    name: (string)reader["name"],
                                    phone: (string)reader["phone"],
                                    address: reader["address"] == DBNull.Value ? "" : (string)reader["address"]
                                );

                                var userData = new UserDto(
                                    userId: id,
                                    personID: (Guid)reader["personid"],
                                    brithDay: (DateTime)reader["dateofbrith"],
                                    isVip: (bool)reader["isvip"],
                                    personData: personData,
                                    userName: (string)reader["username"],
                                    password: (string)(reader["password"])
                                );

                                return userData;
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine("this from getting user by id error {0}", ex);
            }

            return null;
        }

        public static UserDto? getUser
        (
            string userName,
            string password
        )
        {
            UserDto? userHolder = null;
            try
            {
                using (var con = new NpgsqlConnection(connectionUr))
                {
                    con.Open();
                    string query = @"SELECT * FROM usersview WHERE username = @username AND password = @password";

                    using (var cmd = new NpgsqlCommand(query, con))
                    {
                        cmd.Parameters.AddWithValue("@username", userName);
                        cmd.Parameters.AddWithValue("@password", password);

                        using (var reader = cmd.ExecuteReader())
                        {
                            if (reader.Read())
                            {
                                if ((bool)reader["ispersondeleted"] == true) return null;
                                
                                var personData = new PersonDto(
                                    (Guid)reader["personid"],
                                    (string)reader["email"],
                                    (string)reader["name"],
                                    (string)reader["phone"],
                                    reader["address"] == DBNull.Value ? "" : (string)reader["address"]
                                );

                                userHolder = new UserDto(
                                    userId: (Guid)reader["userid"],
                                    personID: (Guid)reader["personid"],
                                    brithDay: (DateTime)reader["dateofbirth"],
                                    isVip: (bool)reader["isvip"],
                                    personData: personData,
                                    userName: userName,
                                    password: password
                                );

                                return userHolder;
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine("this from getting user by id error {0}", ex);
            }

            return userHolder;
        }


        public static bool createUser ( UserDto userData )
        {
            bool isCreated = false;
            try
            {
                using (var con = new NpgsqlConnection(connectionUr))
                {
                    con.Open();
                    string query = @"SELECT fn_user_insert ( 
                                  @userId_u::UUID , 
                                  @name::VARCHAR(50) ,
                                  @phone::VARCHAR(13) ,
                                  @email::VARCHAR(100) ,
                                  @address::TEXT ,
                                  @username::VARCHAR(50) ,
                                  @password::TEXT ,
                                  @IsVIP , 
                                  @DateOfBirth::DATE  ) ";
                    using (var cmd = new NpgsqlCommand(query, con))
                    {
                        cmd.Parameters.AddWithValue("@userId_u", userData.userId);
                        cmd.Parameters.AddWithValue("@name", userData.personData.name);
                        cmd.Parameters.AddWithValue("@phone", userData.personData.phone);
                        cmd.Parameters.AddWithValue("@email", userData.personData.email);
                        cmd.Parameters.AddWithValue("@address", userData.personData.address);
                        cmd.Parameters.AddWithValue("@username", userData.userName);
                        cmd.Parameters.AddWithValue("@password", userData.password);
                        cmd.Parameters.AddWithValue("@IsVIP", userData.isVip);
                        cmd.Parameters.AddWithValue("@DateOfBirth", userData.brithDay);
                        cmd.ExecuteNonQuery();

                    isCreated = true;
                    }

                }

            }
            catch (Exception ex)
            {
                Console.WriteLine("this from create user error {0}", ex);
            }

            return isCreated;
        }
        
        public static bool updateUser ( UserDto userData )
        {
            bool isCreated = false;
            try
            {
                using (var con = new NpgsqlConnection(connectionUr))
                {
                    con.Open();
                    string query = @"SELECT fn_user_update ( 
                                  @userId_u , 
                                  @name ,
                                  @phone ,
                                  @email ,
                                  @address ,
                                  @username ,
                                  @password ,
                                  @IsVIP 
                                    ) ";
                    using (var cmd = new NpgsqlCommand(query, con))
                    {
                        cmd.Parameters.AddWithValue("@userId_u", userData.userId);
                        cmd.Parameters.AddWithValue("@name", userData.personData.name);
                        cmd.Parameters.AddWithValue("@phone", userData.personData.phone);
                        cmd.Parameters.AddWithValue("@email", userData.personData.email);
                        cmd.Parameters.AddWithValue("@address", userData.personData.address);
                        cmd.Parameters.AddWithValue("@username", userData.userName);
                        cmd.Parameters.AddWithValue("@password", userData.password);
                        cmd.Parameters.AddWithValue("@IsVIP", userData.isVip);

                    }

                    isCreated = true;
                }

            }
            catch (Exception ex)
            {
                Console.WriteLine("this from create user error {0}", ex);
            }

            return isCreated;
        }

            public static bool isExist(
            Guid id
            )
        {
            bool isExist = false;
            try
            {
                using (var connection = new NpgsqlConnection(connectionUr))
                {

                    connection.Open();

                    string query = @" SELECT * FROM  users WHERE userid =@id;";

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
               string username,
               string password
               )
        {
            bool isExist = false;
            try
            {
                using (var connection = new NpgsqlConnection(connectionUr))
                {

                    connection.Open();

                    string query = @" SELECT * FROM  users WHERE username =@name AND password =@password;";

                    using (var cmd = new NpgsqlCommand(query, connection))
                    {
                        cmd.Parameters.AddWithValue("@username", username);
                        cmd.Parameters.AddWithValue("@password", username);

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
 
        
        public static bool delete(
           Guid id 
        )
        {
            bool isDeleted = false;
            try
            {
                using (var connection = new NpgsqlConnection(connectionUr))
                {

                    connection.Open();

                    string query = @"DELETE FROM  users WHERE userid =@id;"; 
                    using (var cmd = new NpgsqlCommand(query, connection))
                    {
                        cmd.Parameters.AddWithValue("@id", id);

                        using (var result = cmd.ExecuteReader())
                        {
                            if (result.Read())
                                isDeleted = true;
                        }
                    }
                }
                return isDeleted;
            }
            catch (Exception ex)
            {
                Console.WriteLine("\nthis error from person deleted {0} \n", ex.Message);

            }
            return isDeleted;
        }

   
    }
}