using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using hotel_data;

namespace hotel_business
{
    public class DepartmentBuisness
    {
              public enum enMode {add, update};
        enMode mode = enMode.add;
          public long ID { get; set; }
        public string name { get; set; }

        public DepartmentBuisness(string name,enMode enMode){
            this.name = name;
            this.mode = enMode;
        }

        private  bool createPerson(){
            return DepartmentData.createDeparmtment(name);
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