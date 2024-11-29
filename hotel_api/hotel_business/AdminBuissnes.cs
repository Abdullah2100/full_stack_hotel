using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using hotel_data;
using hotel_data.dto;

namespace hotel_business
{
    public class AdminBuissnes
    {

        public enum enMode { add, update }
        enMode mode = enMode.add;
        public int ID { get; set; }
        public string userName { get; set; }
        public string password { get; set; }
        public long? personID { get; set; }
        public bool isDeleted { get; set; } = false;
        public PersonDto? personData { get; set; }

        public AdminDto amdinData
        {
            get { return new AdminDto(ID, userName, password, personData); }
        }

        public AdminBuissnes(AdminDto adminData, enMode mode = enMode.add)
        {
            bool isDeleted = false;
            this.ID = ID;
            this.userName = adminData.userName;
            this.password = adminData.password;
            this.personID = adminData.personData.personID;
            this.mode = mode;
            this.personData = adminData.personData;
            this.isDeleted = isDeleted;
        }

        private bool _createAdmin()
        {

            bool result = AdminData.createAdmin(adminData: amdinData);
            return result == true;
        }

        private bool _updateAdmin()
        {

            bool result = AdminData.updateAdmin(adminData: amdinData);
            return result == true;
        }

        public bool save()
        {
            switch (mode)
            {
                case enMode.add:
                    {
                        if (_createAdmin()) return true;
                        return false;
                    }

                case enMode.update:
                    {
                        if (_updateAdmin()) return true;
                        return false;
                    }
                default: return false;
            }
        }

        public static bool deleteAdmin(int id)
        {
            return AdminData.deleteAdmin(id);
        }

        public static AdminDto? getAdmin(int id)
        {
            return AdminData.getAdmin(id);
        }

        public static AdminDto? getAdmin(string userName, string password)
        {
            return AdminData.getAdmin(userName, password);
        }

        public static bool isAdminExist(int id)
        {
            return AdminData.isExist(id);
        }

        public static List<PersonDto> getDeletedPersons()
        {
            return PersonData.getPersonsDeleted();
        }

        public static List<PersonDto> getNotDeletedPersons()
        {
            return PersonData.getPersonsNotDeleted();
        }

    }
}