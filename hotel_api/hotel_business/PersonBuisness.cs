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
        public enum enMode {add, update};
        enMode mode = enMode.add;
        public int ID { get; set; }
        public string name { get; set; }
        public string phone { get; set; }
        public string  address { get; set; }

        public PersonBuisness(string name,string phone,string address,enMode enMode){
            this.name = name;
            this.phone = phone;
            this.address = address;
            this.mode = enMode;
        }

        private  bool createPerson(){
            return PersonData.createPerson(name,phone,address);
        }


        public bool save(){
            switch(mode){
                case enMode.add:{
                    if(createPerson()){
                        return true;
                    }return false;
                }
                case enMode.update:{
                    return false;
                }
                default:return false;
            }
        }
    }

}