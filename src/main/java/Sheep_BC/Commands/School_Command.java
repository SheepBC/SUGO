package Sheep_BC.Commands;

import Sheep_BC.Annotations.MessageEvent;
import Sheep_BC.Schools.School;
import Sheep_BC.Schools.School_Data;
import Sheep_BC.Supporters.Tool;
import Sheep_BC.Supporters.UserDB;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;

import java.time.LocalDate;
import java.util.ArrayList;

public class School_Command {


    MessageReceivedEvent event;
    String[] arg;
    net.dv8tion.jda.api.entities.User user;
    String userid;
    MessageChannel channel;

    String notUser = "회원가입이 되어있지 않은 유저입니다.";


    public School_Command(MessageReceivedEvent event, String[] args){

        this.event = event;
        this.arg = args;
        user = event.getAuthor();
        userid = user.getId();
        channel = event.getChannel();

    }

    @MessageEvent(key = {"학교"},command = "숩학교 [학교이름]", help = "학교를 지정합니다.", type = Command_Type.SCHOOL)
    public void setSchool(){
        if(!UserDB.isUser(userid)){
            channel.sendMessage(notUser).queue();
            return;
        }
        if(arg.length == 1){
            channel.sendMessage("숩학교 [학교이름]").queue();
            return;
        }
        School school = new School("0e97dec410cc4ad18b70643ebabeac1a");
        ArrayList<School_Data> list = school.getSchool(arg[1]);
        if(list.size() == 0){
            channel.sendMessage("학교를 찾지못하였습니다.").queue();
        }
        else if(list.size() == 1){
            UserDB userDB = new UserDB(user);
            userDB.setSchool(list.get(0));
            event.getMessage().reply(list.get(0).School_Name+" 등록하였습니다.").queue();
        }
        else {
            SelectMenu.Builder menu = SelectMenu.create("School");
            for(School_Data sd:list){
                menu.addOption(sd.School_Name,sd.toString(),sd.Local_Name);
            }
            channel.sendMessage("학교를 고르세요").setActionRow(menu.build()).queue();
        }
    }

    @MessageEvent(key = {"급식"},command = "숩급식 (학교or날짜(MMDD or YYMMDD or YYYYMMDD))", help = "급식을 확인합니다.", type = Command_Type.SCHOOL)
    public void meal(){
        if(arg.length == 1){
            if(!UserDB.isUser(userid)){
                channel.sendMessage(notUser).queue();
                return;
            }
            UserDB userDB = new UserDB(user);
            if(userDB.getSchool() == null){
                channel.sendMessage("학교등록하세요. 또는 숩급식 (학교or날짜(MMDD or YYMMDD or YYYYMMDD))").queue();
            }
            else {
                String day = LocalDate.now().toString().replace("-","");
                sendMeal(channel, userDB.getSchool(), day);
            }

        }
        else {
            School school = new School("0e97dec410cc4ad18b70643ebabeac1a");
            if(Tool.isLong(arg[1])){
                if(arg[1].length() != 4 &arg[1].length() != 6 &arg[1].length() != 8){
                    channel.sendMessage("숩급식 (학교or날짜(MMDD or YYMMDD or YYYYMMDD))").queue();
                    return;
                }
                if(!UserDB.isUser(userid)){
                    channel.sendMessage(notUser).queue();
                    return;
                }
                UserDB userDB = new UserDB(user);
                if(userDB.getSchool() == null){
                    channel.sendMessage("학교등록하세요").queue();
                }
                else {
                    String day = arg[1];
                    if(day.length() == 4) day = LocalDate.now().getYear()+day;
                    if(day.length() == 6) day = "20"+day;
                    sendMeal(channel, userDB.getSchool(), day);
                }

            }
            else {
                ArrayList<School_Data> list = school.getSchool(arg[1]);
                SelectMenu.Builder menu = SelectMenu.create("meal");
                String day = LocalDate.now().toString().replace("-","");
                if(list.size() == 0){
                    channel.sendMessage("학교를 찾지 못하였습니다.").queue();
                }
                else if(list.size() == 1){
                    sendMeal(channel,list.get(0),day);
                }
                else {
                    for(School_Data sd:list){
                        menu.addOption(sd.School_Name,sd+"/"+day,sd.Local_Name);
                    }
                    channel.sendMessage("학교를 고르세요").setActionRow(menu.build()).queue();
                }
            }
        }
    }

    public static void sendMeal(MessageChannel channel, School_Data school_data, String day){
        School school = new School("0e97dec410cc4ad18b70643ebabeac1a");
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Tool.getRandomColor());
        embed.setTitle(school_data.School_Name+"("+day+")");
        for(String meal: school.getMeals(school_data,day)){
            embed.addField("메뉴","```"+meal.replace(",","\n")+"```",true);
        }
        if(embed.getFields().size() == 0){
            embed.addField("메뉴","```급식정보가 없습니다.```",true);
        }
        channel.sendMessageEmbeds(embed.build()).queue();
    }
}
