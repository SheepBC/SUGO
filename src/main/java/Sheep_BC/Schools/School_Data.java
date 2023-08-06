package Sheep_BC.Schools;

public class School_Data {

    public String Local_Code;
    public String Local_Name;
    public String School_Code;
    public String School_Name;

    public School_Data(String Local_Code,String Local_Name,String School_Code,String School_Name){
        this.Local_Code = Local_Code;
        this.Local_Name = Local_Name;
        this.School_Code = School_Code;
        this.School_Name = School_Name;
    }

    public School_Data(String inform) {
        if(inform == null){
            Local_Code = null;
        }
        else {
            String[] list = inform.split(" ");
            Local_Code = list[0];
            Local_Name = list[1];
            School_Code = list[2];
            School_Name = list[3];
        }
    }

    public String toString(){
        return Local_Code+" "+Local_Name+" "+School_Code+" "+School_Name;
    }

}
