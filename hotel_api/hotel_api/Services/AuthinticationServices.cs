using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Linq;
using System.Security.Claims;
using System.Threading.Tasks;
using hotel_api.util;
using Microsoft.IdentityModel.Tokens;

namespace hotel_api.Services
{
    public class AuthinticationServices
    {
        
        public enum enTokenMode{AccessToken,RefreshToken}
        public static string generateToken(
            Guid userID,
            string email,
            bool isRefresh,
        IConfigurationServices config,
            enTokenMode enTokenMode=enTokenMode.AccessToken
        )
        {
            var tokenHandler = new JwtSecurityTokenHandler();
            var key = config.getKey("credentials:key");
            var issuer = config.getKey("credentials:Issuer");
            var audience = config.getKey("credentials:Audience");

            var claims = new List<Claim>(){
                new (JwtRegisteredClaimNames.Jti,clsUtil.generateGuid()),
                new (JwtRegisteredClaimNames.Sub,userID.ToString()),
                new (JwtRegisteredClaimNames.Email,email)
            };

            var tokenDescip = new SecurityTokenDescriptor{
                Subject = new ClaimsIdentity(claims),
                Expires = clsUtil.generateDateTime(enTokenMode),
                Issuer = issuer,
                Audience = audience,
                SigningCredentials = new SigningCredentials(
                    new SymmetricSecurityKey(
                        System.Text.Encoding.UTF8.GetBytes(key))
                        ,SecurityAlgorithms.HmacSha256Signature) 
            };

            var token = tokenHandler.CreateToken(tokenDescip);
            return tokenHandler.WriteToken(token);

        }
    }
}