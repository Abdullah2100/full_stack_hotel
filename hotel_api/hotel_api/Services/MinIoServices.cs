using Minio;
using Minio.DataModel.Args;

namespace hotel_api.Services;

public class MinIoServices
{

    private static IMinioClient? _client(IConfigurationServices _config)
    {
        try
        {
           return  new MinioClient()
                .WithEndpoint(_config.getKey("minio_end_point")) 
                .WithCredentials(_config.getKey("accessKy"), _config.getKey("secretKey"))
                .Build();
        
        }
        catch (Exception error)
        {
            Console.WriteLine("this error from create minion client {0}",error.Message);
            return null;
        }
    }

    private static async Task<bool> isExistBouket(IConfigurationServices _config, IMinioClient client)
    {
        var beArgs = new BucketExistsArgs()
            .WithBucket(_config.getKey("bucket_name"));
        return await client.BucketExistsAsync(beArgs).ConfigureAwait(false);

    }
    private static async Task<bool> uploadFile(IConfigurationServices _config,IFormFile file)
    {
        try
        {
            using (var minioClient = _client(_config))
            {
                var isExistBuket = await isExistBouket(_config, minioClient);
                if (!isExistBuket) return false;
                using (var fileStream = file.OpenReadStream())
                {
                    var putObject= new PutObjectArgs()
                        .WithBucket(_config.getKey("bucket_name"))
                        .WithFileName(file.FileName)
                        .WithStreamData(fileStream)
                        .WithContentType(file.ContentType);
                     await minioClient.PutObjectAsync(putObject).ConfigureAwait(false);
                    return true;
                }
            }
   
        }
        catch (Exception error)
        {
            return false;
        }
    }
    
    private static async Task<bool> deleteFile(IConfigurationServices _config,string fileName)
    {
        try
        {
            using (var minioClient = _client(_config))
            {
                var isExistBuket = await isExistBouket(_config, minioClient);
                if (!isExistBuket) return false;
                 
                await minioClient.RemoveObjectAsync(
                    new RemoveObjectArgs()
                        .WithBucket(_config.getKey("bucket_name"))
                        .WithObject(fileName)
                );;
                return true;

            }
   
        }
        catch (Exception error)
        {
            return false;
        }
    }

}