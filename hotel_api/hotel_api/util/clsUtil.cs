using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Cryptography;
using System.Text;
using static hotel_api.Services.AuthinticationServices;

namespace hotel_api.util
{
    sealed class clsUtil
    {
        public static string generateGuid()
        {
            return Guid.NewGuid().ToString();
        }

        public static DateTime generateDateTime(enTokenMode mode)
        {
            switch (mode)
            {
                case enTokenMode.AccessToken:
                {
                    return DateTime.Now.AddSeconds(40);
                }
                default:
                {
                    return DateTime.Now.AddDays(30);
                }
            }
        }


        public static string hashingText(string text)
        {
            using (SHA256 sha256 = SHA256.Create())
            {
                // Compute the hash of the given string
                byte[] hashValue = sha256.ComputeHash(Encoding.UTF8.GetBytes(text));
 
                // Convert the byte array to string format
                return BitConverter.ToString(hashValue).Replace("-", "");
            } 
        }
    }
}