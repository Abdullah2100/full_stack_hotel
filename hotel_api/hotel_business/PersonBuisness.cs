using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Cryptography.X509Certificates;
using System.Threading.Tasks;
using hotel_data;
using hotel_data.dto;

namespace hotel_business
{
    public class PersonBuisness
    {
        public enum enMode { add, update };
        enMode mode = enMode.add;
        public long ID { get; set; }
        public string name { get; set; }
        public string phone { get; set; }
        public string address { get; set; }

        public bool isDeleted { get; set; } = false;

        public PersonBuisness(long id, string name, string phone, string address, enMode enMode)
        {
            this.ID = id;
            this.name = name;
            this.phone = phone;
            this.address = address;
            this.mode = enMode;
        }

        public PersonBuisness(PersonDto personData, bool isDeleted, enMode enMode)
        {
            this.name = personData.name;
            this.phone = personData.phone;
            this.address = personData.address;
            this.mode = enMode;
            this.isDeleted = isDeleted;
        }

        private bool createPerson()
        {
            return PersonData.createPerson(name, phone, address);
        }

        private bool updatePerson()
        {
            return PersonData.updatePerson(ID, name, phone, address);
        }


        public bool save()
        {
            switch (mode)
            {
                case enMode.add:
                    {
                        if (createPerson())
                        {
                            return true;
                        }
                        return false;
                    }
                case enMode.update:
                    {
                        if (updatePerson())
                        {
                            return true;
                        }
                        return false;
                    }
                default: return false;
            }
        }

        public static PersonBuisness? getPersonByID(long peronsID)
        {
            bool isDeleted = false;
            var personData = PersonData.getPerson(peronsID, ref isDeleted);
            if (personData != null)
            {
                return new PersonBuisness(personData, isDeleted, enMode.update);
            }
            return null;
        }


        public static bool isPersonExistByID(long peronsID)
        {
            return PersonData.isExist(peronsID);
        }

        public static bool isPersonExistByName(string name)
        {
            return PersonData.isExist(name);
        }
    }

}
