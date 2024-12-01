using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
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
 

            using (var sha = new System.Security.Cryptography.SHA256Managed())
            {
                byte[] textData = System.Text.Encoding.UTF8.GetBytes(text);
                byte[] hash = sha.ComputeHash(textData);
                return BitConverter.ToString(hash).Replace("-", String.Empty);
            }
           
        }

    }
}