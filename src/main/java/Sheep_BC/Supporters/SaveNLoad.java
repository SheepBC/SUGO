package Sheep_BC.Supporters;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Properties;

public class SaveNLoad {

    public static String loadP(String file, String name){
        Properties prop = new Properties();
        FileInputStream fis;
        InputStreamReader isr;
        String result = "";
        try {
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            prop.load(isr);
            result = prop.getProperty(name);
            fis.close(); isr.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void addP(String file,String name,String txt){
        Properties prop = new Properties();
        FileInputStream fis;
        InputStreamReader isr;

        Folder(file);

        try {
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis,StandardCharsets.UTF_8);
            Reader reader = new InputStreamReader(fis,StandardCharsets.UTF_8);
            prop.load(reader);
            fis.close(); isr.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        prop.setProperty(name,txt);
        OutputStreamWriter osw;
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file);
            osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            prop.store(osw, file);
            fos.close(); osw.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void Folder(String path){
        String[] list = path.split("/");
        String path2 = list[0];
        for(String s:list){
            if(!s.equalsIgnoreCase(list[0]) & !s.equalsIgnoreCase(list[list.length-1])){
                path2 += "/"+s;
                if(!checkFolder(path2)){
                    makeFolder(path2);
                }
            }
        }
    }

    public static void makeFolder(String foldername){
        File makeFolder = new File(foldername);
        try {
            makeFolder.mkdirs();
            makeFolder.exists();
        }catch (Exception e){
            System.out.println(foldername+"은 이미있는폴더입니다.");
            makeFolder.exists();
        }
    }

    public static boolean checkFolder(String foldername){
        File file = new File(foldername);
        boolean b = file.canRead();
        file.exists();
        return b;
    }

    public static double getRandom(String path){
        Properties prop = new Properties();
        FileInputStream fis;
        InputStreamReader isr;
        String[] list;
        try {
            fis = new FileInputStream(path);
            isr = new InputStreamReader(fis,StandardCharsets.UTF_8);
            prop.load(isr);
            list = prop.stringPropertyNames().toArray(new String[0]);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        double max = 0;
        ArrayList<Double> listD = new ArrayList<>();
        for (String s:list){
            max += Double.parseDouble(s);
            listD.add(Double.parseDouble(s));
        }

        double r = Math.random()*max;
        double nr = 0;
        double last_nr = 0;

        for (double d:listD){
            last_nr = nr;
            nr += d;
            if(last_nr<= r&r < nr){
                String[] l = loadP(path,String.valueOf(d)).split("~");
                double[] l2 = {Double.parseDouble(l[0]),Double.parseDouble(l[1])};
                return  Math.random()*(l2[1]-l2[0])+l2[0];
            }
        }

        return 1;
    }


}
