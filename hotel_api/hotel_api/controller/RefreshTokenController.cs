using hotel_api.Services;
using hotel_business;
using Microsoft.AspNetCore.Mvc;

namespace hotel_api.controller;

[ApiController]
[Route("api/refreshToken")]
public class RefreshTokenController : Controller
{
    private readonly IConfigurationServices _config;

    public RefreshTokenController(IConfigurationServices config)
    {
        this._config = config;
    }


    [HttpPost("refresh")]
    [ProducesResponseType(StatusCodes.Status201Created)]
    [ProducesResponseType(StatusCodes.Status401Unauthorized)]
    [ProducesResponseType(StatusCodes.Status400BadRequest)]
    public ActionResult generateRefreshToken(string tokenHolder)
    {
        var issuer = (Guid)AuthinticationServices.decodeToken("iss", tokenHolder);
        var issAt = (DateTime)AuthinticationServices.decodeToken("iat", tokenHolder);
        var expireAt = (DateTime)AuthinticationServices.decodeToken("exp", tokenHolder);
        string? email = GeneralBuisness.isExistById(issuer);
        if (email!=null)
        {
            var dayBetween = issAt - expireAt;
            if (dayBetween.Days == 30)
            {
               string accesstoken = AuthinticationServices.generateToken(issuer, email??"", _config,
                    AuthinticationServices.enTokenMode.AccessToken),
                refreshToken = AuthinticationServices.generateToken(issuer, email ?? "", _config,
                    AuthinticationServices.enTokenMode.RefreshToken);
               return Ok(new { accessToken = $"{accesstoken}", refreshToken = $"{refreshToken}"});
            }
            else
            {
                return BadRequest("send valide token ");
            }
        }
        else
        {
            return StatusCode(401, "Invalid token");
        }
      
    }
}