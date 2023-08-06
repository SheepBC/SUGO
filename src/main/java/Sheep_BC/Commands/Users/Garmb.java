package Sheep_BC.Commands.Users;

import Sheep_BC.Annotations.MessageEvent;
import Sheep_BC.Commands.Command_Type;
import Sheep_BC.Supporters.SugoData;
import Sheep_BC.Supporters.Tool;
import Sheep_BC.Supporters.UserDB;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Garmb {

    MessageReceivedEvent event;
    String[] arg;
    net.dv8tion.jda.api.entities.User user;
    String userid;
    MessageChannel channel;

    String notUser = "회원가입이 되어있지 않은 유저입니다.";

    public Garmb(MessageReceivedEvent event,String[] args){

        this.event = event;
        this.arg = args;
        user = event.getAuthor();
        userid = user.getId();
        channel = event.getChannel();

    }

    @MessageEvent(key = {"도박"},command = "숩도박 [걸돈 or 올인]", help = "도박을 합니다.", type = Command_Type.GARMB)
    public void Common(){
        String command = "숩도박 [걸돈 or 올인]";
        if(!UserDB.isUser(userid)){
            channel.sendMessage(notUser).queue();
            return;
        }
        if(arg.length < 2){
            channel.sendMessage(command).queue();
            return;
        }
        BigInteger price = BigInteger.ZERO;
        UserDB userDB = new UserDB(user);
        if(!arg[1].equalsIgnoreCase("올인") & !Tool.isBigI(arg[1])){
            channel.sendMessage(command).queue();
            return;
        }
        if(arg[1].equalsIgnoreCase("올인")){
            price = userDB.getMoney();
        }
        else {
            price = new BigInteger(arg[1]);
        }

        BigInteger max = SugoData.getMoney().divide(new BigInteger("3"));
        if(price.max(max).equals(price)){
            channel.sendMessage("최대 "+Tool.unit(max)+"까지 걸 수 있습니다.").queue();
            return;
        }

        if(price.max(userDB.getMoney().add(BigInteger.ONE)).equals(price)){
            channel.sendMessage("돈이 부족합니다.").queue();
            return;
        }
        if(price.min(new BigInteger("99")).equals(price)){
            channel.sendMessage("100베리 이상 거세요!!").queue();
            return;
        }
        double r = Math.random()*100;
        double b = 0;
        double H = 0;

        if(r < (H += 4.8125)) b = 0;
        else if( r < (H += 15.625)) b = 0.5;
        else if( r < (H += 18.75)) b = 0.7;
        else if( r < (H += 15.625)) b = 1;
        else if( r < (H += 18.75)) b = 1.25;
        else if( r < (H += 15.625)) b = 1.5;
        else if( r < (H += 4)) b = 1.7;
        else if( r < (H += 3.8125)) b = 2;
        BigDecimal bd_price = new BigDecimal(price);
        BigDecimal bd_b = new BigDecimal(String.valueOf(b));
        BigDecimal bd_add_price = bd_price.multiply(bd_b).subtract(bd_price);
        BigInteger add_price = bd_add_price.toBigInteger();
        userDB.addMoney(add_price);
        SugoData.addMoney(add_price.multiply(new BigInteger("-1")));
        EmbedBuilder embed = new EmbedBuilder();
        int addExp = (int) (Math.random()*100);
        embed.setFooter("+"+addExp+"경험치");
        embed.setTitle("도박 결과");
        embed.setColor(Tool.getRandomColor());
        embed.addField("건돈",Tool.unit(bd_price)+"베리",true);
        embed.addField("배율",b+"배",true);
        embed.addField("결과",Tool.unit(bd_price.multiply(bd_b).toBigInteger())+"베리",true);
        channel.sendMessageEmbeds(embed.build()).queue();

    }

    @MessageEvent(key = {"주사위도박"},command = "숩주사위도박 [걸돈 or 올인]", help = "주사위도박을 합니다.", type = Command_Type.GARMB)
    public void Dice(){
        String command = "숩주사위도박 [걸돈 or 올인]";
        if(!UserDB.isUser(userid)){
            channel.sendMessage(notUser).queue();
            return;
        }
        if(arg.length < 2){
            channel.sendMessage(command).queue();
            return;
        }
        BigInteger price = BigInteger.ZERO;
        UserDB userDB = new UserDB(user);
        if(!arg[1].equalsIgnoreCase("올인") & !Tool.isBigI(arg[1])){
            channel.sendMessage(command).queue();
            return;
        }
        if(arg[1].equalsIgnoreCase("올인")){
            price = userDB.getMoney();
        }
        else {
            price = new BigInteger(arg[1]);
        }

        if(price.max(userDB.getMoney().add(BigInteger.ONE)).equals(price)){
            channel.sendMessage("돈이 부족합니다.").queue();
            return;
        }
        if(price.min(new BigInteger("99")).equals(price)){
            channel.sendMessage("100베리 이상 거세요!!").queue();
            return;
        }
        String[] die = {"\uD83C\uDFB2**1**","\uD83C\uDFB2**2**","\uD83C\uDFB2**3**","\uD83C\uDFB2**4**","\uD83C\uDFB2**5**","\uD83C\uDFB2**6**"};
        String[] list = new String[2];
        list[0] = die[(int) (Math.random()*die.length)];
        list[1] = die[(int) (Math.random()*die.length)];
        double b =0;
        String g = "";
        if(list[0].equalsIgnoreCase(list[1])){
            g = "성공";
            b = 2.5;
        }else {
            g = "실패";
            b = 0.7;
        }
        BigDecimal bd_price = new BigDecimal(price);
        BigDecimal bd_b = new BigDecimal(String.valueOf(b));
        BigDecimal bd_add_price = bd_price.multiply(bd_b).subtract(bd_price);
        BigInteger add_price = bd_add_price.toBigInteger();
        userDB.addMoney(add_price);
        SugoData.addMoney(add_price.multiply(new BigInteger("-1")));
        EmbedBuilder embed = new EmbedBuilder();
        int addExp = (int) (Math.random()*50);
        embed.setFooter("+"+addExp+"경험치");
        embed.setColor(Tool.getRandomColor());
        embed.setTitle("주사위도박 결과");
        embed.addField("건돈",Tool.unit(bd_price)+"베리",true);
        embed.addField("**게임결과**",g+"/"+list[0]+"/"+list[1],true);
        embed.addField("**결과**", Tool.unit(bd_price.multiply(bd_b).toBigInteger())+"베리",true);
        channel.sendMessageEmbeds(embed.build()).queue();
    }

    @MessageEvent(key = {"슬롯머신"},command = "숩슬롯머신 [걸돈 or 올인]", help = "슬롯머신을 합니다.", type = Command_Type.GARMB)
    public void Slot_Machine(){
        JDA jda = event.getJDA();
        String command = "숩슬롯머신 [걸돈 or 올인]";
        if(!UserDB.isUser(userid)){
            channel.sendMessage(notUser).queue();
            return;
        }
        if(arg.length < 2){
            channel.sendMessage(command).queue();
            return;
        }
        BigInteger price = BigInteger.ZERO;
        UserDB userDB = new UserDB(user);
        if(!arg[1].equalsIgnoreCase("올인") & !Tool.isBigI(arg[1])){
            channel.sendMessage(command).queue();
            return;
        }
        if(arg[1].equalsIgnoreCase("올인")){
            price = userDB.getMoney();
        }
        else {
            price = new BigInteger(arg[1]);
        }

        if(price.max(userDB.getMoney().add(BigInteger.ONE)).equals(price)){
            channel.sendMessage("돈이 부족합니다.").queue();
            return;
        }
        if(price.min(new BigInteger("99")).equals(price)){
            channel.sendMessage("100베리 이상 거세요!!").queue();
            return;
        }

        String[] ss = {"1️⃣","2️⃣","3️⃣","4️⃣","5️⃣","6️⃣","7️⃣","8️⃣","9️⃣"};
        String[] gg = new String[3];
        double b = 1;
        String n = "";
        gg[0] = ss[(int) (Math.random()* ss.length)];
        gg[1] = ss[(int) (Math.random()* ss.length)];
        gg[2] = ss[(int) (Math.random()* ss.length)];
        if((gg[0]+gg[1]+gg[2]).equalsIgnoreCase("2️⃣1️⃣8️⃣")){
            b = 500;
            n = "1등";
            jda.retrieveUserById("336828850358976512").flatMap(User::openPrivateChannel)
                    .queue(Channel -> {Channel.sendMessage(user.getAsMention()+"슬롯머신 1등당첨").queue();});
        }else if((gg[0]+gg[1]+gg[2]).equalsIgnoreCase("4️⃣1️⃣5️⃣")){
            b = -415;
            n = "??등";
            jda.retrieveUserById("336828850358976512").flatMap(User::openPrivateChannel)
                    .queue(Channel -> {Channel.sendMessage(user.getAsMention()+"슬롯머신 -30당첨").queue();});
        }else if(gg[0].equals(gg[1]) && gg[1].equals(gg[2])){
            b = 30;
            n = "2등";
        }else if(gg[0].equals(gg[1]) || gg[1].equals(gg[2]) || gg[2].equals(gg[0])){
            b =1.2;
            n = "3등";
        }else {
            b = 0.5;
            n = "꼴등";
        }

        BigDecimal bd_price = new BigDecimal(price);
        BigDecimal bd_b = new BigDecimal(String.valueOf(b));
        BigDecimal bd_add_price = bd_price.multiply(bd_b).subtract(bd_price);
        BigInteger add_price = bd_add_price.toBigInteger();
        userDB.addMoney(add_price);
        SugoData.addMoney(add_price.multiply(new BigInteger("-1")));
        EmbedBuilder embed = new EmbedBuilder();
        int addExp = (int) (Math.random()*150);
        embed.setFooter("+"+addExp+"경험치");
        embed.setColor(Tool.getRandomColor());
        embed.setTitle("슬롯머신도박 결과");
        embed.addField("건돈",Tool.unit(bd_price)+"베리",true);
        embed.addField("**게임결과**",n+"/"+gg[0]+gg[1]+gg[2],true);
        embed.addField("**결과**", Tool.unit(bd_price.multiply(bd_b).toBigInteger())+"베리",true);
        channel.sendMessageEmbeds(embed.build()).queue();
    }

}
