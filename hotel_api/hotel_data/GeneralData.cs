using hotel_data.dto;
using Npgsql;

namespace hotel_data;

public class GeneralData
{
    static string connectionUr = clsConnnectionUrl.url;

    public static string isValideId(Guid id)
    {
        string  email = "";
        
           try
            {
                using (var con = new NpgsqlConnection(connectionUr))
                {
                    con.Open();
                    string query = @"SELECT isExistById(@id::UUID)";

                    using (var cmd = new NpgsqlCommand(query, con))
                    {
                        cmd.Parameters.AddWithValue("@id", id);
                        

                        using (var reader = cmd.ExecuteReader())
                        {
                            if (reader.Read())
                            {

                                email = (string)reader["isexistbyid"];
                                ;
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine("this from getting user by id error {0}", ex);
            }

            return email;

    }

    public static bool isExistByEmailAndID(string email, Guid id)
    {
           bool  isExist = false;
        
            try
            {
                using (var con = new NpgsqlConnection(connectionUr))
                {
                    con.Open();
                    string query = @"SELECT isExistByIdAndEmail(@email_hold,@id)";

                    using (var cmd = new NpgsqlCommand(query, con))
                    {
                        cmd.Parameters.AddWithValue("@id", id);
                        cmd.Parameters.AddWithValue("@email_hold", email);


                        var result = cmd.ExecuteScalar();
                        if (result != null && bool.Parse(result.ToString()!)==true)
                        {
                            isExist = true;
                        }

                    }
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine("this from getting user by id error {0}", ex);
            }

            return isExist;



    } 
    
}