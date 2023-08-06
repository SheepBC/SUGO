package Sheep_BC.Supporters;

import java.math.BigInteger;

public class SugoData {

    public static String DataPath = "Sub/Data.txt";
    public static String ChartPath = "Sub/Charts/";
    public static String AttendPath = "Sub/Attend.txt";
    public static String sp_m = "SP_Price";
    public static String sg_m = "SG_Money";
    public static String sg_sp = "SG_SP";
    public static String am_h = "AM_Health";
    public static String version = "version";
    public static String money = "money";
    public static String tax = "tax";
    public static String last_SP = "last_SP";
    public static String change_SP = "change_SP";
    public static String now_SP = "now_SP";

    public static BigInteger getSP(){
        try{
            return new  BigInteger(SaveNLoad.loadP(DataPath,now_SP));
        }catch (Exception e){
            return new BigInteger("33000");
        }
    }

    public static BigInteger getMoney(){
        return new BigInteger("100000");//수정할것
    }

    public static void addMoney(BigInteger bInt){
        BigInteger bi = getMoney().add(bInt);
        //수정할것 setMoney(bi);
    }

}
