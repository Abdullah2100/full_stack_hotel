using hotel_data;

namespace hotel_business;

public class GeneralBuisness
{
    public static string isExistById(Guid id)
    {
        return GeneralData.isValideId(id);
    }
}