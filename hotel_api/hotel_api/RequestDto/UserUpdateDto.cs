using System.ComponentModel.DataAnnotations;

namespace hotel_api_.RequestDto;

public class UserUpdateDto
{
    [Required]
    public Guid Id { get; set; }
    [MaxLength(50)]
    public string name { get; set; } = "";

    public string email { get; set; } = "";

    [MaxLength(10)]
    public string phone { get; set; } = "";

    public string address { get; set; } = "";

    public string userName { get; set; } = "";

    [MaxLength(16)]
    public string password { get; set; } = "";

    public DateTime? brithDay { get; set; } = null;
    
    public bool isVip   { get; set; } = false;
    
    
}