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
        public Guid id { get; set; }
        public string username { get; set; }
        public string password { get; set; }
        public Guid? personid { get; set; }
        public bool isdeleted { get; set; } = false;
        public PersonDto? persondata { get; set; }

        public AdminDto amdindata
        {
            get { return new AdminDto(id, username, password, persondata); }
        }

        public AdminBuissnes(AdminDto AdminData, enMode mode = enMode.add)
        {
            bool isdeleted = false;
            this.id = id;
            this.username = AdminData.userName;
            this.password = AdminData.password;
            this.personid = AdminData.personData.personID;
            this.mode = mode;
            this.persondata = AdminData.personData;
            this.isdeleted = isdeleted;
        }

        private bool _createadmin()
        {

            bool result = AdminData.createAdmin(amdindata);
            return result == true;
        }

        private bool _updateadmin()
        {

            bool result = AdminData.updateAdmin(amdindata);
            return result == true;
        }

        public bool save()
        {
            switch (mode)
            {
                case enMode.add:
                    {
                        if (_createadmin()) return true;
                        return false;
                    }

                case enMode.update:
                    {
                        if (_updateadmin()) return true;
                        return false;
                    }
                default: return false;
            }
        }

        public static bool deleteAdmin(int id)
        {
            return AdminData.deleteAdmin(id);
        }

        public static AdminDto? getAdmin(Guid id)
        {
            return AdminData.getAdmin(id);
        }

        public static AdminDto? getAdmin(string username, string password)
        {
            return AdminData.getAdmin(username, password);
        }

        public static bool isAdminExist(int id)
        {
            return AdminData.isExist(id);
        }

        public static List<PersonDto> getDeletedPersons()
        {
            return PersonData.getPersonsDeleted();
        }

        public static  List<PersonDto> getnotDeletedPersons()
        {
            return PersonData.getPersonsNotDeleted();
        }

    }
}