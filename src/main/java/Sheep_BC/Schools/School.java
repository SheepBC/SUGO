package Sheep_BC.Schools;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.util.ArrayList;

public class School {

    String key;
    public School(String key){
        this.key = key;
    }

    public ArrayList<School_Data> getSchool(String School_Name){
        String url ="https://open.neis.go.kr/hub/schoolInfo";
        Document document;
        ArrayList<School_Data> list = new ArrayList<>();
        try {
            document = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(url+"?KEY="+key+"&SCHUL_NM="+ URLEncoder.encode(School_Name));

        }catch (Exception e){
            return list;
        }
        NodeList rows = document.getDocumentElement().getElementsByTagName("row");
        for (int i = 0;i<rows.getLength();i++){
            String lc = document.getElementsByTagName("ATPT_OFCDC_SC_CODE").item(i).getTextContent();
            String ln = document.getElementsByTagName("LCTN_SC_NM").item(i).getTextContent();
            String Sc = document.getElementsByTagName("SD_SCHUL_CODE").item(i).getTextContent();
            String Sn = document.getElementsByTagName("SCHUL_NM").item(i).getTextContent();
            list.add(new School_Data(lc,ln,Sc,Sn));
        }

        return list;
    }

    public ArrayList<String> getMeals(School_Data school,String day){
        String url ="https://open.neis.go.kr/hub/mealServiceDietInfo";
        Document document;
        ArrayList<String> list = new ArrayList<>();
        try {
            document = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(url+"?KEY="+key+"&ATPT_OFCDC_SC_CODE="+school.Local_Code+"&SD_SCHUL_CODE="+school.School_Code+"&MLSV_YMD="+day);
        }catch (Exception e){
            return list;
        }
        NodeList meals = document.getDocumentElement().getElementsByTagName("DDISH_NM");
        for (int i = 0;i<meals.getLength();i++){
            String meal = meals.item(i).getTextContent()
                    .replace("<br/>",",")
                    .replace("  ","");

            list.add(meal);
        }

        return list;
    }

    private void out(Document document){
        TransformerFactory tf =TransformerFactory.newInstance();
        try {
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

            StringWriter writer = new StringWriter();

            transformer.transform(new DOMSource(document), new StreamResult(writer));

            String output = writer.getBuffer().toString();

            System.out.println(output);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

}
