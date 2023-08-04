package Sheep_BC.Commands.Users;

import Sheep_BC.Annotations.MessageEvent;
import Sheep_BC.Commands.Command_Type;
import Sheep_BC.Supporters.SugoData;
import Sheep_BC.Supporters.Tool;
import Sheep_BC.Supporters.UserDB;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.math.BigInteger;

public class User {

    MessageReceivedEvent event;
    String[] arg;
    net.dv8tion.jda.api.entities.User user;
    String userid;
    MessageChannel channel;

    String notUser = "회원가입이 되어있지 않은 유저입니다.";

    public User(MessageReceivedEvent event,String[] args){

        this.event = event;
        this.arg = args;
        user = event.getAuthor();
        userid = user.getId();
        channel = event.getChannel();

    }

    public User(MessageChannel channel){
        this.channel = channel;
    }

    @MessageEvent(key = {"회원가입","가입"},command = "숩회원가입", help = "회원가입을 합니다.", type = Command_Type.USER)
    public void join(){
        if(UserDB.isUser(userid)){
            channel.sendMessage("이미 회원가입한 사람입니다.").queue();
            return;
        }
        UserDB userDB = new UserDB(user);
        try {
            userDB.makeUser();
        }catch (Exception e){
            System.out.println("회원가입함");
        }
        channel.sendMessage("회원가입이 완료되었습니다.").queue();
    }

    @MessageEvent(key = {"내정보"},command = "숩내정보", help = "자신의 정보를 확인합니다.", type = Command_Type.USER)
    public void inform(){
        if(!UserDB.isUser(userid)){
            channel.sendMessage(notUser).queue();
            return;
        }
        EmbedBuilder embed = new EmbedBuilder();
        UserDB userDB = new UserDB(user);

        embed.setColor(Tool.getRandomColor());
        embed.setTitle(userDB.getName()+"님의 정보");
        embed.addField("**이름**",userDB.getName(),true);
        //수정할것 BigInteger sp = userDB.getAll_Property().divide(SugoData.getSP());
        embed.addField("**재산**",
                //수정할것 "총자산: "+Tool.unit(userDB.getAll_Property())+" ("+userDB.get_AP_Rank()+"위)"+"\n"+
                        //수정할것 "SP: "+Tool.unit(sp)+"SP"+"\n"+
                        "경험치: "+Tool.unit(userDB.getExp()),false);

        String sleep = "\n잠: "+Tool.time_Unit(userDB.getSleepTime())+"/"+Tool.time_Unit(userDB.getAll_SleepTime());

        embed.addField("**건강**",
                "건강: "+Tool.unit(userDB.getHealth())
                        +"\n피로도: "+userDB.getTired()
                        +(userDB.isHealth() ? "\n회복중":"")
                        +(userDB.isSleeping() ? sleep:"")
                ,false);


        channel.sendMessageEmbeds(embed.build()).queue();
    }

    @MessageEvent(key = {"자산"},command = "숩자산", help = "자신의 자산 정보를 확인합니다.", type = Command_Type.USER)
    public void inform_Money(){
        if(!UserDB.isUser(userid)){
            channel.sendMessage(notUser).queue();
            return;
        }

        UserDB userDB = new UserDB(user);
        //수정할것 BigInteger sp = userDB.getAll_Property().divide(SugoData.getSP());
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Tool.getRandomColor());
        embed.setTitle(user.getName()+"님의 재산");
        embed.addField("**재산**",
                "돈: "+Tool.unit(userDB.getMoney())
                        //수정할것 + "\n주식: "+Tool.unit(userDB.getStock_All_Money())
                        + "\n빚: "+Tool.unit(userDB.getDebt())
                        + "\n경험치: "+Tool.unit(userDB.getExp())
                        + "\n-----------------",false);
                //수정할것+ "\n총자산: "+Tool.unit(userDB.getAll_Property())+" ("+userDB.get_AP_Rank()+"위)",false);
        embed.addField("**기타**",
                //수정할것 "SP: "+Tool.unit(sp)+"SP"
                "\n최고 자산: "+Tool.unit(userDB.getTopProperty())
                        +"\n최고 SP: "+Tool.unit(userDB.getTopSP())+"SP",true);

        channel.sendMessageEmbeds(embed.build()).queue();
    }
    
    @MessageEvent(key = {"건강"},command = "숩건강", help = "자신의 건강 정보를 확인합니다.", type = Command_Type.USER)
    public void inform_Health(){
        if(!UserDB.isUser(userid)){
            channel.sendMessage(notUser).queue();
            return;
        }

        UserDB userDB = new UserDB(user);
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Tool.getRandomColor());
        embed.setTitle(user.getName()+"님의 건강");
        String sleep = "\n잠: "+Tool.time_Unit(userDB.getSleepTime())+"/"+Tool.time_Unit(userDB.getAll_SleepTime());
        embed.addField("**건강**",
                "건강: "+Tool.unit(userDB.getHealth())
                        +"\n피로도: "+userDB.getTired()
                        +(userDB.isHealth() ? "\n회복중":"")
                        +(userDB.isSleeping() ? sleep:"")
                ,false);

        channel.sendMessageEmbeds(embed.build()).queue();
    }
}
