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


        public clsRoomTypeBuisness(string name, string des, enMode enMode = enMode.add)
        {
            RoomTypeID = 0;
            mode = enMode;
            TypeName = name;
            Description = des;
        }

        private bool createNewRoomType(string name,string description)
        {
            return clsRoomTypeData.createRoomType(name,description);
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
                        if (createNewRoomType(this.TypeName,this.Description)) return true;
                        return false;
                    }
            }
        }

    }
}