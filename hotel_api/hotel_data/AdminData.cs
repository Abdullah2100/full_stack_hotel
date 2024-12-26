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

        public static bool createAdmin(AdminDto adminData)
        {
            bool isCreated = false;
            try
            {
                using (var connection = new NpgsqlConnection(connectionUrl))
                {
                    connection.Open();

                    string query =
                        @" SELECT fn_admin_insert(@adminid_n, @name, @phone, @email, @address, @username, @password)";

                    using (var cmd = new NpgsqlCommand(query, connection))
                    {
                        cmd.Parameters.AddWithValue("@adminid_n", adminData.adminID!);
                        if (adminData.personData != null)
                        {
                            cmd.Parameters.AddWithValue("@name", adminData.personData.name);
                            cmd.Parameters.AddWithValue("@phone", adminData.personData.phone);
                            cmd.Parameters.AddWithValue("@address", adminData.personData.address);
                            cmd.Parameters.AddWithValue("@email", adminData.personData.email);
                        }

                        cmd.Parameters.AddWithValue("@username", adminData.userName);
                        cmd.Parameters.AddWithValue("@password", adminData.password);

                        var resultID = cmd.ExecuteScalar();
                        isCreated = true;
                    }
                }

                return isCreated;
            }
            catch (Exception ex)
            {
                Console.WriteLine("\nthis error from admin create {0} \n", ex.Message);
            }

            return isCreated;
        }

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

                    string query =
                        @"SELECT fn_admin_update(@adminid, @name, @phone, @address, @personid, @username, @password)";

                    using (var cmd = new NpgsqlCommand(query, connection))
                    {
                        cmd.Parameters.AddWithValue("@adminid", adminData.adminID!);

                        if (adminData.personData != null)
                        {
                            cmd.Parameters.AddWithValue("@name", adminData.personData.name);
                            cmd.Parameters.AddWithValue("@phone", adminData.personData.phone);
                            cmd.Parameters.AddWithValue("@address", adminData.personData.address);
                            cmd.Parameters.AddWithValue("@personid", adminData.personData.personID!);
                        }

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

        public static bool deleteAdmin(
            Guid ID
        )
        {
            bool isDelelted = false;
            try
            {
                using (var connection = new NpgsqlConnection(connectionUrl))
                {
                    connection.Open();

                    string query = @"DELETE FROM  admins WHERE adminid =@id;";

                    using (var cmd = new NpgsqlCommand(query, connection))
                    {
                        cmd.Parameters.AddWithValue("@id", ID);

                        cmd.ExecuteNonQuery();
                        isDelelted = true;
                    }
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine("\nthis error from person deleted {0} \n", ex.Message);
            }

            return isDelelted;
        }

        public static AdminDto? getAdmin(
            Guid ID
        )
        {
            try
            {
                using (var connection = new NpgsqlConnection(connectionUrl))
                {
                    connection.Open();

                    string query = @"SELECT * FROM fn_admin_get(@ID)";

                    using (var cmd = new NpgsqlCommand(query, connection))
                    {
                        cmd.Parameters.AddWithValue("@ID", ID);

                        using (var result = cmd.ExecuteReader())
                        {
                            if (result.HasRows)
                            {
                                var person = new PersonDto
                                (
                                    (Guid)result["personid"],
                                    (string)result["name"],
                                    (string)result["email"],
                                    result["address"] == DBNull.Value ? "" : (string)result["address"],
                                    (string)result["phone"],
                                    createdAt: (DateTime)result["CreatedAt"]
                                );

                                var admin = new AdminDto
                                (
                                    adminID: ID,
                                    userName: (string)result["username"],
                                    password: "",
                                    personsId: (Guid)result["personid"],
                                    personData: person
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


        public static AdminDto? getAdmin(
            string username,
            string password
        )
        {
            AdminDto? adminData = null;
            try
            {
                using (var connection = new NpgsqlConnection(connectionUrl))
                {
                    connection.Open();
                    string query =
                        " SELECT * from fn_admin_get_username_password(@username_a::VARCHAR(50), @password_a::TEXT) ";

                    using (var cmd = new NpgsqlCommand(query, connection))
                    {
                        cmd.Parameters.AddWithValue("@username_a", username);
                        cmd.Parameters.AddWithValue("@password_a", password);

                        using (NpgsqlDataReader? result = cmd.ExecuteReader())
                        {
                            if (result.HasRows)
                            {
                                if (result.Read())
                                {
                                    adminData = new AdminDto
                                    (
                                        adminID: (Guid)result["adminid"],
                                        userName: username,
                                        password: password,
                                        personsId: (Guid)result["personid"],
                                        personData: new PersonDto
                                        (
                                            (Guid)result["personid"],
                                            (string)result["name"],
                                            (string)result["email"],
                                            result["address"] == DBNull.Value ? "" : (string)result["address"],
                                            (string)result["phone"],
                                            createdAt: null
                                        )
                                    );
                                }
                            }
                        }
                    }
                }

                return adminData;
            }
            catch (Exception ex)
            {
                Console.WriteLine("\nthis error from person deleted {0} \n", ex.Message);
                return adminData;
            }
        }


        public static bool isExist(
            Guid ID
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

        public static bool isExist(
            string username
        )
        {
            bool isExist = false;
            try
            {
                using (var connection = new NpgsqlConnection(connectionUrl))
                {
                    connection.Open();

                    string query = @" SELECT * FROM  admins WHERE username =@username;";

                    using (var cmd = new NpgsqlCommand(query, connection))
                    {
                        cmd.Parameters.AddWithValue("@username", username);

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