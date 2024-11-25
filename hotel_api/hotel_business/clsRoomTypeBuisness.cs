using System.Data.Common;
using hotel_data;
using hotel_data.dto;
using Npgsql.Replication;

namespace hotel_business
{
    public class clsRoomTypeBuisness
    {
        public enum enMode { update, add };
        public enMode mode;
        public int? RoomTypeID { get; set; } = null;
        public string TypeName { get; set; } = "";
        public string Description { get; set; } = "";
        public int EmployeeID { get; set; }


        public clsRoomTypeBuisness(string name, string des, int employeeid, enMode enMode = enMode.add)
        {
            RoomTypeID = 0;
            mode = enMode;
            TypeName = name;
            Description = des;
            EmployeeID = employeeid;
        }

        private bool createNewRoomType()
        {
            return clsRoomTypeData.createRoomType(TypeName, Description, EmployeeID);
        }

        public bool Save()
        {
            switch (mode)
            {
                case enMode.update:
                    {
                        return false;
                    }
                default:
                    {
                        if (createNewRoomType()) return true;
                        return false;
                    }
            }
        }

        public static Boolean isExistByName(string typeName)
        {
            return clsRoomTypeData.isExist(typeName);
        }

    }
}