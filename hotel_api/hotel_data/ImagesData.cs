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
                string query = "SELECT fn_images_tb_insert( belongTo_i , path_i )";
                using (var cmd = new NpgsqlCommand(query, con))
                {
                    cmd.Parameters.AddWithValue("belongTo_i", image.belongTo);
                    cmd.Parameters.AddWithValue("path_i", image.imagePath);
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
                string query = "UPDATE Images_tb SET path =@image_path WHERE imageId = @ID";
                using (var cmd = new NpgsqlCommand(query, con))
                {
                    cmd.Parameters.AddWithValue("@image_path", image.imagePath);
                    cmd.Parameters.AddWithValue("@ID", image.imagePathId!);
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

  
    public static bool isExist(string path )
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
                    cmd.Parameters.AddWithValue("path",path);
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