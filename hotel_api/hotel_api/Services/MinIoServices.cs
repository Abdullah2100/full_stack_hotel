using Minio;
using Minio.DataModel.Args;

namespace hotel_api.Services
{
    public class MinIoServices
    {
        public enum enBucketName { USER, RoomType }

        private static IMinioClient? _client(IConfigurationServices _config)
        {
            try
            {
                return new MinioClient()
                   .WithEndpoint(_config.getKey("minio_end_point"))
                    .WithCredentials(_config.getKey("accessKy"), _config.getKey("secretKey"))
                    .WithSSL(false)
                    .Build();
            }
            catch (Exception error)
            {
                Console.WriteLine("Error creating MinIO client: {0}", error.Message);
                return null;
            }
        }

        private static async Task<bool> _isExistBucket(IMinioClient client, string bucketName)
        {
            try
            {
                var buckets = await client.ListBucketsAsync().ConfigureAwait(false);
                var result = buckets.Buckets.Any(b => b.Name == bucketName);
                return result ;
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error checking bucket existence: {0}", ex.Message);
                return false;
            }
        }

        private static async Task _createNewBucket(IMinioClient client, string bucketName)
        {
            try
            {
                var mbArgs = new MakeBucketArgs().WithBucket(bucketName);
                await client.MakeBucketAsync(mbArgs).ConfigureAwait(false);
                Console.WriteLine("Bucket '{0}' created successfully.", bucketName);
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error creating bucket: {0}", ex.Message);
            }
        }

        public static async Task<bool> uploadFile(IConfigurationServices _config, IFormFile file, enBucketName bucketName)
        {
            try
            {
                var bucketNameStr = bucketName.ToString().ToLower();
                var minioClient = _client(_config);

                if (minioClient == null)
                {
                    Console.WriteLine("Failed to initialize MinIO client.");
                    return false;
                }

                // Check if bucket exists and create if necessary
                var isExistBucket = await _isExistBucket(minioClient, bucketNameStr);
                if (!isExistBucket)
                {
                    await _createNewBucket(minioClient, bucketNameStr);
                }

                // Upload the file
                using (var fileStream = file.OpenReadStream())
                {
                    var putObject = new PutObjectArgs()
                        .WithBucket(bucketNameStr)
                        .WithObject(file.FileName)
                        .WithStreamData(fileStream)
                        .WithObjectSize(file.Length)
                        .WithContentType(file.ContentType);

                    await minioClient.PutObjectAsync(putObject).ConfigureAwait(false);
                    Console.WriteLine("File '{0}' uploaded successfully to bucket '{1}'.", file.FileName, bucketNameStr);
                    return true;
                }
            }
            catch (Exception error)
            {
                Console.WriteLine("Error uploading file: {0}", error.Message);
                return false;
            }
        }

        public static async Task<bool> deleteFile(IConfigurationServices _config, string fileName, string bucketName)
        {
            try
            {
                var minioClient = _client(_config);

                if (minioClient == null)
                {
                    Console.WriteLine("Failed to initialize MinIO client.");
                    return false;
                }

                var isExistBucket = await _isExistBucket(minioClient, bucketName);
                if (!isExistBucket)
                {
                    Console.WriteLine("Bucket '{0}' does not exist.", bucketName);
                    return false;
                }

                await minioClient.RemoveObjectAsync(
                    new RemoveObjectArgs()
                        .WithBucket(bucketName)
                        .WithObject(fileName)
                ).ConfigureAwait(false);

                Console.WriteLine("File '{0}' deleted from bucket '{1}'.", fileName, bucketName);
                return true;
            }
            catch (Exception error)
            {
                Console.WriteLine("Error deleting file: {0}", error.Message);
                return false;
            }
        }
    }
}
