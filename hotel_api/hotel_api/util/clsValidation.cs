using System.Text.RegularExpressions;
using hotel_api_.RequestDto;

namespace hotel_api.util;

sealed class clsValidation
{
    public static string? validateInput(
        AdminRequestDto admin
           )
    {
        if(!isValidPhone(admin.phone))
            return "write valide phone";
        if (!isValidEmail(admin.email))
            return "write valide email";
       if(!isValidPassword(admin.password)) 
           return "write valide password";
         return null;
    }


    public static bool isValidPhone(string? phone)
    {
        return   Regex.Match(phone,@"^\+?\d{9,15}$").Success;
    }

    public static bool isValidEmail(string? email)
    {
        return  Regex.Match(email, @"^[a-zA-Z0-9._%±]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,}$").Success;
    }

    public static bool isValidPassword(string password)
    {
        return Regex.IsMatch(password, @"^(?=(.*[A-Z]){2})(?=(.*\d){2})(?=(.*[a-z]){2})(?=(.*[!@#$%^&*()_+|\\/?<>:;'""-]){2})[A-Za-z\d!@#$%^&*()_+|\\/?<>:;'""-]*$");
    }
}