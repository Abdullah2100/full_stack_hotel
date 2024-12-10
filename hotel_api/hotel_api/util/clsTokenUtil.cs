using System.Security.Claims;
using hotel_api.Services;

namespace hotel_api.util;

public class clsTokenUtil
{
    public   enum enTokenClaimType
    {
        Iss, Aud,Email,Sub,Exp,Lat,None
    }
    private static enTokenClaimType _convertKeyToClaimType(string key)
    {
        switch (key)
        {
            case "iss":return enTokenClaimType.Iss;
            case "aud":return enTokenClaimType.Aud;
            case "email":return enTokenClaimType.Email;
            case "id": return enTokenClaimType.Sub;
            case "lat":return enTokenClaimType.Lat;
            case "exp":return enTokenClaimType.Exp;
            default:return enTokenClaimType.None;
        }
    }

    public static Claim? getClaimType(IEnumerable<Claim> claim, string key)
    {
        enTokenClaimType claimType = _convertKeyToClaimType(key);
        switch (claimType)
        {
            case enTokenClaimType.Aud:
            {
                return claim.First(x=>x.Type=="aud");
            }
            case enTokenClaimType.Iss:
            {
                return claim.First(x => x.Type == "iss");
            }
            case enTokenClaimType.Email:
            {
                return claim.First(x => x.Type == "email");
            }
            case enTokenClaimType.Sub:
            {
                return claim.First(x => x.Type == "sub");
            }
            case enTokenClaimType.Lat:
            {
                return claim.First(x => x.Type == "iat");
            }
            case enTokenClaimType.Exp:
            {
                return claim.First(x => x.Type == "exp");
            }
            default:
            {
                return null;
            }
                
        }

    }

    public static bool isValidIssuerAndAudience(string issuer, string audience, IConfigurationServices _config)
    {
        var currentIssuer = _config.getKey("credentials:Issuer");
        var currentIudience = _config.getKey("credentials:Audience");
        
        return  currentIssuer.Equals(issuer)&& currentIudience.Equals(audience);
        

    }


    public static bool isRefreshToken(string issuAt, string expireAt)
    {
        long lIssuDate = long.Parse(issuAt);
        long lExpireDate = long.Parse(expireAt);
        
        var issuDateTime = DateTimeOffset.FromUnixTimeSeconds(lIssuDate).DateTime;
        var expireTime =DateTimeOffset.FromUnixTimeSeconds(lExpireDate).DateTime;
        
        var rsult = issuDateTime-expireTime;
        return rsult.Days>=29;
    }
}