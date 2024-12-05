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
                                var personData = new PersonDto(
                                    (Guid)reader["personid"],
                                    (string)reader["email"],
                                    (string)reader["name"],
                                    (string)reader["phone"],
                                    reader["address"] == DBNull.Value ? "" : (string)reader["address"]
                                );

                                var userData = new UserDto(
                                    userId: (Guid)reader["userid"],
                                    personID: (Guid)reader["personid"],
                                    brithDay: (DateTime)reader["dateofbrith"],
                                    isVip: (bool)reader["isvip"],
                                    personData: personData,
                                    userName: userName,
                                    password: password
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


        public static bool createUser
        (
            UserDto userData
        )
        {
            bool isCreated = false;
            try
            {
                using (var con = new  NpgsqlConnection(connectionUr))
                {
                   con.Open();
                   string query = "SELECT ";
                }

                isCreated = true;
            }
            catch (Exception ex)
            {
                Console.WriteLine("this from create user error {0}", ex);
            }

            return isCreated;
        }
    }
}