package Sheep_BC.Supporters;

import SheepBC.BaseN;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class Tool {

    public static Color getRandomColor(){
        int R = (int) (Math.random()*256);
        int G = (int) (Math.random()*256);
        int B = (int) (Math.random()*256);
        Color color = new Color(R,G,B);
        return color;
    }

    public static boolean isLong(String s){
        try {
            Long l = Long.parseLong(s);
            return true;
        }catch (Exception e){
            return false;
        }
    }


    //랭크 관련
    public static String toSerialize(Object object){

        byte[] serializeMember;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(object);
            serializeMember = bos.toByteArray();
            String b64 = Base64.getEncoder().encodeToString(serializeMember);

            BaseN base64 = new BaseN(getList64());
            BaseN baseM = new BaseN(getListM());

            String num = base64.BaseNToDecimal(b64);
            return baseM.DecimalToBaseN(num);


        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static Object toObject(String Serialize){

        BaseN baseM = new BaseN(getListM());
        BaseN base64 = new BaseN(getList64());
        String sm = baseM.BaseNToDecimal(Serialize);
        String bm = base64.DecimalToBaseN(sm);

        byte[] serializeMember = Base64.getDecoder().decode(bm);
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(serializeMember);
            ObjectInputStream ois = new ObjectInputStream(bis);
            return  ois.readObject();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public static String[] getList64(){
        String b6 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
        return b6.split("");
    }

    public static String[] getListM(){

        ArrayList<String> list = new ArrayList<>();
        String b6 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        list.addAll(java.util.List.of(b6.split("")));

        String[] CHO = {"ㄱ","ㄲ","ㄴ","ㄷ","ㄸ","ㄹ","ㅁ","ㅂ","ㅃ",
                "ㅅ","ㅆ","ㅇ","ㅈ","ㅉ","ㅊ","ㅋ","ㅌ","ㅍ","ㅎ"};

        String[] JOONG = {"ㅏ","ㅐ","ㅑ","ㅒ","ㅓ","ㅔ","ㅕ","ㅖ","ㅗ","ㅘ",
                "ㅙ","ㅚ","ㅛ","ㅜ","ㅝ","ㅞ","ㅟ","ㅠ","ㅡ","ㅢ","ㅣ"};

        String[] JONG = {"","ㄱ","ㄲ","ㄳ","ㄴ","ㄵ","ㄶ","ㄷ","ㄹ","ㄺ","ㄻ","ㄼ",
                "ㄽ","ㄾ","ㄿ","ㅀ","ㅁ","ㅂ","ㅄ","ㅅ","ㅆ","ㅇ","ㅈ","ㅊ","ㅋ","ㅌ","ㅍ","ㅎ"};
        list.addAll(java.util.List.of(CHO));
        list.addAll(List.of(JOONG));

        for(int i = 0;i< CHO.length;i++){
            for(int i1 = 0;i1< JOONG.length;i1++){
                for(int i2 = 0;i2< JONG.length;i2++){
                    char c = (char) ((i*21+i1)*28+i2+0xAC00);
                    list.add(String.valueOf(c));
                }
            }
        }

        return list.toArray(new String[0]);
    }

    public static String[] division(String s,int n){
        if(n == 1) return new String[]{s};
        int num = s.length()/(n-1);
        String[] list = new String[n];
        int index = 0;
        int beginIndex = s.length() - s.length() % (n-1);
        for (int i = 0; i< beginIndex;){
            list[index] = s.substring(i,i+num);
            i+= num;
            index++;
        }
        list[index] = s.substring(beginIndex);
        return list;
    }

}
