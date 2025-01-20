using hotel_data.dto;
using Npgsql;

namespace hotel_data;

public class ImagesData
{
    static string connectionUr = clsConnnectionUrl.url;

    public static bool createImages(ImagesTbDto image)
    {
        bool isCreated = false;
        try
        {
            using (var con = new NpgsqlConnection(connectionUr))
            {
                con.Open();
                string query = "INSERT INTO images(name,belongto) VALUES (@name,@belongto)";
                using (var cmd = new NpgsqlCommand(query, con))
                {
                    cmd.Parameters.AddWithValue("@name", image.belongTo);
                    cmd.Parameters.AddWithValue("@belongto", image.belongTo);
                    var result = cmd.ExecuteScalar();
                    if (result != null && bool.TryParse(result.ToString(), out bool isComplate))
                    {
                        isCreated = isComplate;
                    }
                }
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine("this the error from create images {0}", ex.Message);
        }

        return isCreated;
    }


    public static bool updateImages(ImagesTbDto image)
    {
        bool isCreated = false;
        try
        {
            using (var con = new NpgsqlConnection(connectionUr))
            {
                con.Open();
                string query = "UPDATE images SET name =@name WHERE imageid = @ID";
                using (var cmd = new NpgsqlCommand(query, con))
                {
                    cmd.Parameters.AddWithValue("@image_path", image.path);
                    cmd.Parameters.AddWithValue("@ID", image.id);
                    var result = cmd.ExecuteScalar();
                    if (result != null && bool.TryParse(result.ToString(), out bool isComplate))
                    {
                        isCreated = isComplate;
                    }
                }
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine("this the error from update roomtype {0}", ex.Message);
        }

        return isCreated;
    }

    public static string? image(Guid? belongto)
    {
        if (belongto == null) return null;
        string? image = null;
        try
        {
            using (var con = new NpgsqlConnection(connectionUr))
            {
                con.Open();
                string query = "SELECT * FROM images where belongto = @belongto";
                using (var cmd = new NpgsqlCommand(query, con))
                {
                    cmd.Parameters.AddWithValue("belongto", belongto);
                    using (var reader = cmd.ExecuteReader())
                    {
                        if (reader.HasRows)
                        {
                            if (reader.Read())
                            {
                                image = (string)reader["name"];
                            }
                        }
                    }
                }
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine("this the error from get image by path {0}", ex.Message);
        }

        return image;
    }

    public static List<string> images(Guid? belongto)
    {
        if (belongto == null) return null;
        List<string>? images = new List<string>();
        try
        {
            using (var con = new NpgsqlConnection(connectionUr))
            {
                con.Open();
                string query = "SELECT * FROM images where belongto = @belongto";
                using (var cmd = new NpgsqlCommand(query, con))
                {
                    cmd.Parameters.AddWithValue("belongto", belongto);
                    using (var reader = cmd.ExecuteReader())
                    {
                        if (reader.HasRows)
                        {
                            while (reader.Read())
                            {
                                images.Append((string)reader["name"]);
                            }
                        }
                    }
                }
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine("this the error from get image by path {0}", ex.Message);
        }

        return images;
    }


    public static bool isExist(string path)
    {
        bool isExist = false;
        try
        {
            using (var con = new NpgsqlConnection(connectionUr))
            {
                con.Open();
                string query = "SELECT count(*)>0 FROM Images_tb  WHERE  path= path";
                using (var cmd = new NpgsqlCommand(query, con))
                {
                    cmd.Parameters.AddWithValue("path", path);
                    var result = cmd.ExecuteScalar();
                    if (result != null && bool.TryParse(result.ToString(), out bool isComplate))
                    {
                        isExist = isComplate;
                    }
                }
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine("this the error from get image by path {0}", ex.Message);
        }

        return isExist;
    }
}