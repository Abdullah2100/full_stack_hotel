using Minio;
using Minio.DataModel.Args;

namespace hotel_api.Services;

public class MinIoServices{

    public enum enBucketName{USER,RoomType}


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

    private static async Task<bool> _isExistBouket(IConfigurationServices _config, IMinioClient client,string bucketName)


    {
        var beArgs = new BucketExistsArgs()
            .WithBucket(bucketName);
        return await client.BucketExistsAsync(beArgs).ConfigureAwait(false);

    }

    private static async void _createNewBuket(IMinioClient client, enBucketName bukentName)
    {
        try
        {
            await client.MakeBucketAsync(
                new MakeBucketArgs()
                    .WithBucket(bukentName.ToString())
            ).ConfigureAwait(false);

        }
        catch (Exception ex)
        {
            Console.WriteLine("this error from create new Bukent name {0}",ex.Message);
        }
    }
  
    public static async Task<bool> uploadFile(IConfigurationServices _config,IFormFile file , enBucketName bukentName)


    {
        try
        {
            var butketName = bukentName.ToString();
            using (var minioClient = _client(_config))
            {
                var isExistBuket = await _isExistBouket(_config, minioClient,butketName);
                if (!isExistBuket)
                {
                    _createNewBuket(minioClient,bukentName);
                };
                

                using (var fileStream = file.OpenReadStream())
                {
                    var putObject= new PutObjectArgs()
                        .WithBucket(butketName)
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
    
    private static async Task<bool> deleteFile(IConfigurationServices _config,string fileName,string bucketName)
    {
        try
        {
            using (var minioClient = _client(_config))
            {
                var isExistBuket = await _isExistBouket(_config, minioClient, bucketName);

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