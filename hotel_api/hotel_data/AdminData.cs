using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using hotel_data.dto;
using Npgsql;

namespace hotel_data
{
    public class AdminData
    {
        public static string connectionUrl = clsConnnectionUrl.url;

        /// <summary>
        /// Creates a new admin user in the database.
        /// </summary>
        /// <param name="adminData">The AdminDto object containing admin details.</param>
        /// <returns>True if the admin was created successfully; otherwise, false.</returns>
        public static bool createAdmin(AdminDto adminData)
        {
            bool isCreated = false;
            try
            {
                using (var connection = new NpgsqlConnection(connectionUrl))
                {

                    connection.Open();

                    string query = @"pr_admin_insert";

                    using (var cmd = new NpgsqlCommand(query, connection))
                    {

                        cmd.Parameters.AddWithValue("@name", adminData.personData.name);
                        cmd.Parameters.AddWithValue("@phone", adminData.personData.phone);
                        cmd.Parameters.AddWithValue("@email", adminData.personData.email);
                        cmd.Parameters.AddWithValue("@address", adminData.personData.address);
                        cmd.Parameters.AddWithValue("@personid", adminData.personData.personID!);
                        cmd.Parameters.AddWithValue("@username", adminData.userName);
                        cmd.Parameters.AddWithValue("@password", adminData.password);

                        var resultID = cmd.ExecuteScalar();
                        isCreated = ((int)resultID) > 0 ? true : false;
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

        /// <summary>
        /// Updates an existing admin user in the database.
        /// </summary>
        /// <param name="adminData">The AdminDto object containing updated admin details.</param>
        /// <returns>True if the admin was updated successfully; otherwise, false.</returns>
        public static bool updateAdmin(
                AdminDto adminData
           )
        {
            bool isUpdate = false;
            try
            {
                using (var connection = new NpgsqlConnection(connectionUrl))
                {

                    connection.Open();

                    string query = @"fn_admin_update";

                    using (var cmd = new NpgsqlCommand(query, connection))
                    {
                        cmd.Parameters.AddWithValue("@adminid", adminData.adminID!);
                        cmd.Parameters.AddWithValue("@name", adminData.personData.name);
                        cmd.Parameters.AddWithValue("@phone", adminData.personData.phone);
                        cmd.Parameters.AddWithValue("@email", adminData.personData.email);
                        cmd.Parameters.AddWithValue("@address", adminData.personData.address);
                        cmd.Parameters.AddWithValue("@personid", adminData.personData.personID);
                        cmd.Parameters.AddWithValue("@username", adminData.userName);
                        cmd.Parameters.AddWithValue("@password", adminData.password);

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

        /// <summary>
        /// Deletes an admin user from the database.
        /// </summary>
        /// <param name="ID">The ID of the admin to delete.</param>
        /// <returns>True if the admin was deleted successfully; otherwise, false.</returns>
        public static bool deleteAdmin(
       int ID
       )
        {
            bool isDelelted = false;
            try
            {
                using (var connection = new NpgsqlConnection(connectionUrl))
                {

                    connection.Open();

                    string query = @" DELETE FROM  admins WHERE adminid =@id;";

                    using (var cmd = new NpgsqlCommand(query, connection))
                    {
                        cmd.Parameters.AddWithValue("@id", ID);

                        cmd.ExecuteNonQuery();
                        return isDelelted;
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

        /// <summary>
        /// Retrieves an admin user from the database by ID.
        /// </summary>
        /// <param name="ID">The ID of the admin to retrieve.</param>
        /// <returns>An AdminDto object if the admin exists; otherwise, null.</returns>
        public static AdminDto? getAdmin(
            Guid ID
            )
        {
            try
            {
                using (var connection = new NpgsqlConnection(connectionUrl))
                {

                    connection.Open();

                    string query = @" SELECT * FROM  admins WHERE adminid =@ID;";

                    using (var cmd = new NpgsqlCommand(query, connection))
                    {
                        cmd.Parameters.AddWithValue("@ID", ID);

                        using (var result = cmd.ExecuteReader())
                        {
                            if (result.HasRows)
                            {
                                var person = new PersonDto
                              (
                                  ID,
                                  (string)result["name"],
                                  result["address"] == DBNull.Value ? "" : (string)result["address"],
                                  (string)result["email"],
                                  (string)result["phone"]
                              );

                                var admin = new AdminDto
                                (
                                    (Guid)result["amdinid"],
                                    (string)result["username"],
                                    (string)result["password"],
                                    person
                                );

                                return admin;
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



        /// <summary>
        /// Retrieves an admin user from the database by username and password.
        /// </summary>
        /// <param name="username">The username of the admin to retrieve.</param>
        /// <param name="password">The password of the admin to retrieve.</param>
        /// <returns>An AdminDto object if the admin exists; otherwise, null.</returns>
        public static AdminDto getAdmin(
               string username,
               string password
               )
        {
            bool isExist = false;
            try
            {
                using (var connection = new NpgsqlConnection(connectionUrl))
                {

                    connection.Open();

                    string query = @" SELECT * FROM  admins WHERE username =@username AND password = @password;";

                    using (var cmd = new NpgsqlCommand(query, connection))
                    {
                        cmd.Parameters.AddWithValue("@username", username);
                        cmd.Parameters.AddWithValue("@password", password);

                        using (var result = cmd.ExecuteReader())
                        {
                            if (result.HasRows)
                            {
                                var person = new PersonDto
                              (
                                  (Guid)result["personid"],
                                  (string)result["name"],
                                   result["address"] == DBNull.Value ? "" : (string)result["address"],
                                  (string)result["email"],
                                  (string)result["phone"]
                              );

                                var admin = new AdminDto
                                (
                                    (Guid)result["amdinid"],
                                    username,
                                    password,
                                    person
                                );

                                return admin;
                            }
                        }
                    }
                }
                return null;
            }
            catch (Exception ex)
            {
                Console.WriteLine("\nthis error from person deleted {0} \n", ex.Message);

            }
            return null;
        }



        /// <summary>
        /// Checks if an admin exists in the database by ID.
        /// </summary>
        /// <param name="ID">The ID of the admin to check for existence.</param>
        /// <returns>True if the admin exists; otherwise, false.</returns>
        public static bool isExist(
            int ID
            )
        {
            bool isExist = false;
            try
            {
                using (var connection = new NpgsqlConnection(connectionUrl))
                {

                    connection.Open();

                    string query = @" SELECT * FROM  admins WHERE adminid =@ID;";

                    using (var cmd = new NpgsqlCommand(query, connection))
                    {
                        cmd.Parameters.AddWithValue("@ID", ID);

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