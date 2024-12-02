using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace hotel_api.Services
{
    public class ConfigurationServicesImp : IConfigurationServices
    {

        private readonly IConfiguration _configurationService;

        public ConfigurationServicesImp(IConfiguration configurationService){
            _configurationService = configurationService;
        }

        public string getKey(string key)
        {
            return _configurationService[key];
        }

        
    }

  
}