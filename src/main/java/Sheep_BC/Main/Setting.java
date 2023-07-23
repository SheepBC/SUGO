package Sheep_BC.Main;


import Sheep_BC.Supporters.SaveNLoad;

public class Setting {

    public static String getBasic(String key){
        return SaveNLoad.loadP("Setting/Basic.txt",key);
    }

    public static String getPythonPath(String path){
        String dir = System.getProperty("user.dir");
        String pp = SaveNLoad.loadP("Setting/PythonPath.txt",path);
        return dir+"/Python/"+pp;
    }

    public static String getCommandHelp(String command){
        return SaveNLoad.loadP("Setting/Commands/Help.txt",command);
    }

    public static String getCommandInform(String command){
        String s = SaveNLoad.loadP("Setting/Commands/Inform.txt",command);
        if(s == null) return "";
        return SaveNLoad.loadP("Setting/Commands/Inform.txt",command);
    }

    public static String getGoExe(){
        return SaveNLoad.loadP("Setting/Basic.txt","GOEXE");
    }
    public static String getlog(){
        return SaveNLoad.loadP("Setting/Basic.txt","log");
    }
    public static String getInquiry(){
        return SaveNLoad.loadP("Setting/Basic.txt","Inquiry");
    }

}
