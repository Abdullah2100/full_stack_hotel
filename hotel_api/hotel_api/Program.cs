using System.Text;
using hotel_api.Services;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.IdentityModel.Tokens;
using Microsoft.OpenApi.Models;

var builder = WebApplication.CreateBuilder(args);

// Register services
builder.Services.AddEndpointsApiExplorer();

// Register Swagger/OpenAPI
builder.Services.AddSwaggerGen(c =>
{
    c.SwaggerDoc("v1", new OpenApiInfo
    {
        Title = "Hotel API",
        Version = "v1"
    });
});

// Register custom services
builder.Services.AddSingleton<IConfigurationServices, ConfigurationServicesImp>();

var configuration = builder.Configuration;

// Add controllers to the application
builder.Services.AddControllers();

// Configure JWT Authentication
builder.Services.AddAuthorization();
builder.Services.AddAuthentication(JwtBearerDefaults.AuthenticationScheme)
    .AddJwtBearer(options =>
    {
        options.TokenValidationParameters = new TokenValidationParameters
        {
            ValidateIssuer = true,
            ValidateAudience = true,
            ValidateLifetime = true,
            ValidateIssuerSigningKey = true,
            IssuerSigningKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(configuration["credentials:key"] ?? "")),
            ValidIssuer = configuration["credentials:Issuer"],
            ValidAudience = configuration["credentials:Audience"]
        };
    });
builder.Services.AddCors(options =>
{
    options.AddPolicy("AllowAllOrigins", policy =>
    {
        policy.AllowAnyOrigin()    // Allows all origins
            .AllowAnyMethod()    // Allows any HTTP methods (GET, POST, etc.)
            .AllowAnyHeader();   // Allows any headers
    });
});
var app = builder.Build();

// Configure the HTTP request pipeline
if (app.Environment.IsDevelopment())
{
    // Enable Swagger
    app.UseSwagger();

    // Enable Swagger UI
    app.UseSwaggerUI(c =>
    {
        c.SwaggerEndpoint("/swagger/v1/swagger.json", "Hotel API v1");
        c.RoutePrefix = string.Empty;  // Swagger UI will be available at the root URL
    });
}
app.UseCors("AllowAllOrigins");
// Authentication and Authorization middleware
app.UseAuthentication();
app.UseAuthorization();

// Enable HTTPS and map controllers
app.UseHttpsRedirection();
app.MapControllers();

app.Run();