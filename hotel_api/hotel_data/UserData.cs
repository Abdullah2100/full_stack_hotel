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
        private static string minioUrl = clsConnnectionUrl.minIoConnectionUrl + "user/";


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
                            if (reader.HasRows)
                            {
                                if (reader.Read())
                                {
                                    // if (((bool)reader["ispersondeleted"] == true)) return null;

                                    var personData = PersonData.getPerson((Guid)reader["personid"]);


                                    var imageHolder = ImagesData.image(id,minioUrl);
                                    var userData = new UserDto(
                                        userId: id,
                                        brithDay: (DateTime)reader["dateofbirth"],
                                        isVip: (bool)reader["isvip"],
                                        personData: personData,
                                        userName: (string)reader["username"],
                                        password: (string)(reader["password"]),
                                        imagePath:imageHolder==null?"":imageHolder.path

                                    );

                                    return userData;
                                }
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
            string username
        )
        {
            try
            {
                using (var con = new NpgsqlConnection(connectionUr))
                {
                    con.Open();
                    string query = @"SELECT * FROM usersview WHERE username = @username";

                    using (var cmd = new NpgsqlCommand(query, con))
                    {
                        cmd.Parameters.AddWithValue("@username", username);

                        using (var reader = cmd.ExecuteReader())
                        {
                            if (reader.HasRows)
                            {
                                if (reader.Read())
                                {
                                    if (((bool)reader["ispersondeleted"] == true)) return null;

                                    var personData = PersonData.getPerson((Guid)reader["personid"]);


                                    var imageHolder = ImagesData.image( (Guid)reader["userid"]);
                                 
                                    var userData = new UserDto(
                                        userId: (Guid)reader["userid"],
                                        brithDay: (DateTime)reader["dateofbrith"],
                                        isVip: (bool)reader["isvip"],
                                        personData: personData,
                                        userName: (string)reader["username"],
                                        password: (string)(reader["password"]),
                                        imagePath:imageHolder==null?"":imageHolder.path
                                            
                                    );

                                    return userData;
                                }
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
                    string query =
                        @"SELECT * FROM usersview WHERE username = @username OR email = @username AND password = @password";

                    using (var cmd = new NpgsqlCommand(query, con))
                    {
                        cmd.Parameters.AddWithValue("@username", userName);
                        cmd.Parameters.AddWithValue("@password", password);

                        using (var reader = cmd.ExecuteReader())
                        {
                            if (reader.Read())
                            {
                                if ((bool)reader["ispersondeleted"] == true) return null;

                                var personData = PersonData.getPerson((Guid)reader["personid"]);

                                var imageHolder = ImagesData.image( (Guid)reader["userid"]);
                                 
                                
                                userHolder = new UserDto(
                                    userId: (Guid)reader["userid"],
                                    brithDay: (DateTime)reader["dateofbirth"],
                                    isVip: (bool)reader["isvip"],
                                    personData: personData,
                                    userName: userName,
                                    password: password,
                                    imagePath:imageHolder==null?"":imageHolder.path

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

        public static List<UserDto> getUsersByPage(int pageNumber = 1, int numberOfUser = 20)
        {
            List<UserDto> users = new List<UserDto>();
            try
            {
                using (var con = new NpgsqlConnection(connectionUr))
                {
                    con.Open();
                    string query = @"select * from  getUserPagination(@pagenumber,@limitnumber)";

                    using (var cmd = new NpgsqlCommand(query, con))
                    {
                        cmd.Parameters.AddWithValue("@pagenumber", pageNumber <= 1 ? 1 : pageNumber - 1);
                        cmd.Parameters.AddWithValue("@limitnumber", numberOfUser);

                        using (var reader = cmd.ExecuteReader())
                        {
                            if (reader.HasRows)
                            {
                                while (reader.Read())
                                {
                                    //if ((bool)reader["isdeleted"] == true) continue;

                                    var personData = PersonData.getPerson((Guid)reader["personid"]);
                                    var imageHolder = ImagesData.image( (Guid)reader["userid"]);
                                
                                    var userHolder = new UserDto(
                                        userId: (Guid)reader["userid"],
                                        brithDay: (DateTime)reader["dateofbirth"],
                                        isVip: (bool)reader["isvip"],
                                        personData: personData,
                                        userName: (string)reader["UserName"],
                                        password: "",
                                        isDeleted: (bool)reader["isdeleted"],
                                        imagePath:imageHolder==null?"":imageHolder.path
                                    );

                                    users.Add(userHolder);
                                }
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine("this from getting user by id error {0}", ex.Message);
                return null;
            }

            return users;
        }

        
        
        public static bool createUser(UserDto userData)
        {
            bool isCreated = false;
            try
            {
                using (var con = new NpgsqlConnection(connectionUr))
                {
                    con.Open();
                    string query = @"SELECT * FROM fn_user_insert_in(
                                      @userId_u, 
                                      @name::VARCHAR, 
                                      @phone::VARCHAR, 
                                      @email::VARCHAR,
                                      @address::TEXT, 
                                      @username::VARCHAR, 
                                      @password::TEXT, 
                                      @IsVIP::BOOLEAN, 
                                      @DateOfBirth::DATE, 
                                      @addBy_u)";

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
                        if (userData.addBy == null)
                            cmd.Parameters.AddWithValue(@"addBy_u", DBNull.Value);
                        else
                            cmd.Parameters.AddWithValue("@addBy_u", userData.addBy);


                        var result = cmd.ExecuteScalar();

                        if (result != null && int.TryParse(result?.ToString(), out int userId))
                        {
                            isCreated = userId > 0;
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine("this from create user error {0}", ex);
            }

            return isCreated;
        }

        public static bool updateUser(UserDto userData)
        {
            bool isCreated = false;
            try
            {
                using (var con = new NpgsqlConnection(connectionUr))
                {
                    con.Open();
                    string query = @"SELECT * FROM fn_user_update ( 
                                  @userId_u , 
                                  @name::VARCHAR, 
                                  @phone::VARCHAR ,
                                  @address::TEXT, 
                                  @username::VARCHAR, 
                                  @password::TEXT, 
                                  @IsVIP::BOOLEAN, 
                                   @personid_u,
                                   @brithday_u::DATE
                                 
                                    ) ";
                    using (var cmd = new NpgsqlCommand(query, con))
                    {
                        cmd.Parameters.AddWithValue("@userId_u", userData.userId);
                        cmd.Parameters.AddWithValue("@name", userData.personData.name);
                        cmd.Parameters.AddWithValue("@phone", userData.personData.phone);
                        cmd.Parameters.AddWithValue("@address", userData.personData.address);
                        cmd.Parameters.AddWithValue("@username", userData.userName);
                        cmd.Parameters.AddWithValue("@password", userData.password);
                        cmd.Parameters.AddWithValue("@IsVIP", userData.isVip);
                        cmd.Parameters.AddWithValue("@personid_u", userData.personData.personID);
                        cmd.Parameters.AddWithValue("@brithday_u", userData.brithDay);
                      
                        var result = cmd.ExecuteScalar();
                        if (result != null && int.TryParse(result?.ToString(), out int userId))
                        {
                            isCreated = userId > 0;
                        }
                    }
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
                        cmd.ExecuteNonQuery();
                        isDeleted = true;
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


        public static bool unDelete(
            Guid id
        )
        {
            bool isDeleted = false;
            try
            {
                using (var connection = new NpgsqlConnection(connectionUr))
                {
                    connection.Open();
                    string query = @"UPDATE  users  SET IsDeleted = FALSE  WHERE userid =@id;";

                    using (var cmd = new NpgsqlCommand(query, connection))
                    {
                        cmd.Parameters.AddWithValue("@id", id);
                        cmd.ExecuteNonQuery();
                        isDeleted = true;
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

        public static bool vipUser(
            Guid id
        )
        {
            bool isDeleted = false;
            try
            {
                using (var connection = new NpgsqlConnection(connectionUr))
                {
                    connection.Open();
                    string query =
                        @"UPDATE  users  SET isvip  =  CASE WHEN  isvip = TRUE THEN FALSE ELSE TRUE END  WHERE userid =@id;";

                    using (var cmd = new NpgsqlCommand(query, connection))
                    {
                        cmd.Parameters.AddWithValue("@id", id);
                        cmd.ExecuteNonQuery();
                        isDeleted = true;
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