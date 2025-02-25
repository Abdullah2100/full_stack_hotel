using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using hotel_data;

namespace hotel_business
{
    public class PermissionBuisness
    {
        enMode mode = enMode.add;
          public long ID { get; set; }
        public int permissionNum { get; set; }
        public string description { get; set; }

        public PermissionBuisness(int permissionNum,string description,enMode enMode){
            this.permissionNum = permissionNum;
            this.description = description;
            this.mode = enMode;
        }

        private  bool createPerson(){
            return PermissionData.createPermission(permissionNum,description);
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