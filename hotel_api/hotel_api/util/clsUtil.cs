using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using static hotel_api.Services.AuthinticationServices;

namespace hotel_api.util
{
     sealed class clsUtil
    {
        public static string generateGuid(){
            return Guid.NewGuid().ToString();
        }

        public static DateTime generateDateTime(enTokenMode mode){
            switch(mode){
                case enTokenMode.AccessToken:{
                    return DateTime.Now.AddSeconds(40);
                }
                default:{
                    return DateTime.Now.AddDays(30);
                }
            }
        }
    }
}