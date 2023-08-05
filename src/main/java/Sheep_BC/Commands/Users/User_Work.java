package Sheep_BC.Commands.Users;

import Sheep_BC.Annotations.MessageEvent;
import Sheep_BC.Commands.Command_Type;
import Sheep_BC.Supporters.SugoData;
import Sheep_BC.Supporters.Tool;
import Sheep_BC.Supporters.UserDB;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class User_Work {

    MessageReceivedEvent event;
    String[] arg;
    net.dv8tion.jda.api.entities.User user;
    String userid;
    MessageChannel channel;

    String notUser = "회원가입이 되어있지 않은 유저입니다.";

    public User_Work(MessageReceivedEvent event,String[] args){

        this.event = event;
        this.arg = args;
        user = event.getAuthor();
        userid = user.getId();
        channel = event.getChannel();

    }

    @MessageEvent(key = {"도둑질"},command = "숩도둑질", help = "도둑질 합니다.", type = Command_Type.WORK)
    public void stealing(){
        if(!UserDB.isUser(userid)){
            channel.sendMessage(notUser).queue();
            return;
        }
        UserDB userDB = new UserDB(user);
        if(userDB.getExp().min(new BigInteger("50000")).equals(userDB.getExp())){
            channel.sendMessage("최소 5만경험치를 가지고있었야합니다.").queue();
            return;
        }

        if(Math.random()*4<1){
            userDB.addExp(-1500);
            channel.sendMessage("도둑질에 실패하였습니다.(-1500경험치)").queue();
            return;
        }

        BigDecimal x = new BigDecimal(userDB.getExp().divide(new BigInteger("10000")));
        BigDecimal p = x.divide(x.add(new BigDecimal("700")),5, RoundingMode.HALF_EVEN);
        double dp = Math.random()*p.doubleValue();

        UserDB stolen = getStolen(userDB);
        BigDecimal price = new BigDecimal(stolen.getMoney());
        BigDecimal bp = new BigDecimal(String.valueOf(dp));
        price = price.multiply(bp);

        BigDecimal x2 = new BigDecimal(userDB.getExp().divide(new BigInteger("4500")));
        BigDecimal p2 = x2.divide(x2.add(new BigDecimal("250")),5, RoundingMode.HALF_EVEN);
        double dp2 = Math.random()*p2.doubleValue();
        BigDecimal bp2 = new BigDecimal(String.valueOf(dp2));
        bp2 = BigDecimal.ONE.subtract(bp2);

        price = price.multiply(bp2);
        if(price.max(new BigDecimal("100000000")).equals(price)){
            double dp3 = Math.random()*0.5+0.5;
            BigDecimal bp3 = new BigDecimal(String.valueOf(dp3));
            bp3 = bp3.pow(3);
            //System.out.println("-------------------------------");
            //System.out.println("1 "+Support.unit(price.toBigInteger()));
            price = price.multiply(bp3);
            //System.out.println("2 "+Support.unit(price.toBigInteger()));
        }

        if(price.max(new BigDecimal("5000000000")).equals(price)){
            double dp3 = Math.random()*0.9+0.1;
            BigDecimal bp3 = new BigDecimal(String.valueOf(dp3));
            bp3 = bp3.pow(7);
            price = price.multiply(bp3);
            //System.out.println("3 "+Support.unit(price.toBigInteger()));
        }


        int addExp = addExp(price.toBigInteger())*-1;
        userDB.addExp(addExp);
        stolen.addMoney(price.multiply(new BigDecimal("-1")).toBigInteger());
        userDB.addMoney(price.toBigInteger());
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(user.getName()+"님의 도둑질");
        embed.setColor(Tool.getRandomColor());
        embed.addField("훔친돈","```"+Tool.unit(price.toBigInteger())+"```",true);
        embed.setFooter(addExp+"Exp "+stolen.getName());
        channel.sendMessageEmbeds(embed.build()).queue();

    }

    private UserDB getStolen(UserDB userDB){
        String[] ids = UserDB.getUserList();
        int num = (int) (Math.random()*(ids.length));
        System.out.println(num+" "+ids.length);
        UserDB stolen = new UserDB(ids[num]);
        if(userDB.userid.equals(stolen.userid)){
            return getStolen(userDB);
        }
        return stolen;
    }

    private int addExp(BigInteger price){
        BigInteger sp = price.divide(SugoData.getSP());
        if (sp.equals(BigInteger.ZERO)){
            return (int) (Math.random()*150);
        }
        else if(sp.min(BigInteger.valueOf(2500)).equals(sp)){
            return (int) (Math.random()*550+200);
        }
        else if(sp.min(BigInteger.valueOf(15000)).equals(sp)){
            return (int) (Math.random()*4750+250);
        }
        else if(sp.min(BigInteger.valueOf(160000)).equals(sp)){
            return (int) (Math.random()*10000+5000);
        }
        else if(sp.min(BigInteger.valueOf(330000)).equals(sp)){
            return (int) (Math.random()*10000+10000);
        }
        else if(sp.min(BigInteger.valueOf(3330000)).equals(sp)){
            return (int) (Math.random()*20000+20000);
        }
        else {
            return (int) (Math.random()*10000+40000);
        }
    }

}
