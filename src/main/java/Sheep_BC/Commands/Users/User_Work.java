package Sheep_BC.Commands.Users;

import Sheep_BC.Annotations.MessageEvent;
import Sheep_BC.Commands.Command_Type;
import Sheep_BC.Supporters.SaveNLoad;
import Sheep_BC.Supporters.SugoData;
import Sheep_BC.Supporters.Tool;
import Sheep_BC.Supporters.UserDB;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.HashMap;

public class User_Work {

    MessageReceivedEvent event;
    String[] arg;
    net.dv8tion.jda.api.entities.User user;
    String userid;
    MessageChannel channel;

    String notUser = "회원가입이 되어있지 않은 유저입니다.";

    String path = "Setting/Work&Gamb/";

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

    @MessageEvent(key = {"광질"},command = "숩광질 (사용할 건강)", help = "광질을 합니다.", type = Command_Type.WORK)
    public void mining(){
        if(!UserDB.isUser(userid)){
            channel.sendMessage(notUser).queue();
            return;
        }
        UserDB userDB = new UserDB(user);
        if(userDB.getHealth() < -1000){
            channel.sendMessage("건강이 부족합니다.").queue();
            return;
        }
        if(userDB.isHealth() || userDB.isSleeping()){
            channel.sendMessage("잠자거나 회복중입니다.").queue();
            return;
        }
        HashMap<String,Integer> list = new HashMap<>();
        BigInteger all_price = BigInteger.ZERO;
        long health = 20;
        long addExp = 0;
        if(arg.length >= 2){
            if(Tool.isLong(arg[1])){
                health = Long.parseLong(arg[1]);
            }
        }

        while (health >= 20 & userDB.getHealth() > -1000){
            String[] mine = mine().split(" ");
            String type = mine[0];
            int num = Integer.parseInt(mine[1]);
            BigInteger price = new BigInteger(mine[2]);
            long addHealth = Long.parseLong(mine[3]);
            long exp = Long.parseLong(mine[4]);

            list.putIfAbsent(type, 0);
            list.put(type,list.get(type)+num);
            health += addHealth;
            all_price = all_price.add(price);
            addExp += exp;
        }

        String s = "";

        for(String type:list.keySet()){
            s += type+": "+list.get(type)+"개\n";
        }
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Tool.getRandomColor());
        embed.setTitle(user.getName()+"님의 광질");
        embed.addField(Tool.unit(all_price)+"베리","```"+s+"```",true);
        embed.setFooter("+"+addExp+" 경험치");
        channel.sendMessageEmbeds(embed.build()).queue();

    }

    public String mine(){
        UserDB userDB = new UserDB(userid);
        int addHealth = (int) (Math.random()*40+1)*-1;
        int addExp = (int) (Math.random()*20)*+5;
        double r = SaveNLoad.getRandom(path+"mine.txt");

        String type = "";
        int num = 0;
        double price = 1;

        switch ((int) r){
            case 1:
                type = "다이아";
                num = (int) (Math.random()*5);
                price = 0.5;
                break;
            case 2:
                type = "큰_사고";
                num = (int) (Math.random()*2);
                price = -0.2;
                break;
            case 3:
                type = "금";
                num = (int) (Math.random()*5);
                price = 0.1;
                break;
            case 5:
                type = "사고";
                num = (int) (Math.random()*2);
                price = -0.023;
                break;
            case 6:
                type = "철";
                num = (int) (Math.random()*5);
                price = 0.02;
                break;
            case 7:
                type = "석탄";
                num = (int) (Math.random()*5);
                price = 0.005;
                break;
            case 9:
                type = "돌";
                num = (int) (Math.random()*5);
                price = 0.003;
                break;
        }

        num++;
        BigDecimal bd_sp = new BigDecimal(SugoData.getSP());
        BigDecimal bd_p = new BigDecimal(String.valueOf(price));
        BigDecimal bd_num = new BigDecimal(String.valueOf(num));
        BigInteger addPrice = bd_sp.multiply(bd_p).multiply(bd_num).toBigInteger();
        userDB.addHealth(addHealth);
        userDB.addMoney(addPrice);
        userDB.addExp(addExp);
        //수정할것SugoData.addMoney(addPrice.multiply(new BigInteger("-1")),SugoAddMoney.Work);

        return type+" "+num+" "+addPrice+" "+addHealth+" "+addExp;

    }

    @MessageEvent(key = {"고강도일","고강도"},command = "숩고강도일/고강도 (사용할 건강)", help = "일을 합니다.", type = Command_Type.WORK)
    public void Hard_Working(){
        if(!UserDB.isUser(userid)){
            channel.sendMessage(notUser).queue();
            return;
        }
        UserDB userDB = new UserDB(user);
        if(userDB.getHealth() < -1500){
            channel.sendMessage("건강이 부족합니다.").queue();
            return;
        }
        if(userDB.isHealth() || userDB.isSleeping()){
            channel.sendMessage("잠자거나 회복중입니다.").queue();
            return;
        }
        BigInteger all_price = BigInteger.ZERO;
        HashMap<String,Integer> lists = new HashMap<>();
        long health = 25;
        long addExp = 0;
        if(arg.length >= 2){
            if(Tool.isLong(arg[1])){
                health = Long.parseLong(arg[1]);
            }
        }
        while (health>=25 & userDB.getHealth() > -1500){
            String[] list = Hard_Work().split(" ");
            long addHealth = Long.parseLong(list[0]);
            long exp = Long.parseLong(list[1]);
            BigInteger addPrice = new BigInteger(list[2]);
            String type = list[3];

            lists.putIfAbsent(type, 0);
            lists.put(type,lists.get(type)+1);
            health += addHealth;
            all_price = all_price.add(addPrice);
            addExp += exp;

        }
        String s = "";

        for(String type:lists.keySet()){
            s += type+": "+lists.get(type)+"개\n";
        }
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Tool.getRandomColor());
        embed.setTitle(user.getName()+"님의 광질");
        embed.addField(Tool.unit(all_price)+"베리","```"+s+"```",true);
        embed.setFooter("+"+addExp+" 경험치");
        channel.sendMessageEmbeds(embed.build()).queue();
    }

    public String Hard_Work(){
        UserDB userDB = new UserDB(userid);
        int addHealth = (int) ((Math.random()*165)+150)*-1;
        int addExp = (int) (Math.random()*10)*+5;
        String type = "고강도일";
        double p = Math.random()*1.125+1;
        int r = (int) (Math.random() * 10);
        if(r == 0){
            p *= -0.85;
            type = "병원비";
        }
        BigDecimal bd_sp = new BigDecimal(SugoData.getSP());
        BigDecimal bd_p = new BigDecimal(String.valueOf(p));
        BigInteger addPrice = bd_sp.multiply(bd_p).toBigInteger();

        userDB.addExp(addExp);
        userDB.addHealth(addHealth);
        userDB.addMoney(addPrice);
        //수정할것SugoData.addMoney(addPrice.multiply(new BigInteger("-1")),SugoAddMoney.Work);

        return addHealth+" "+addExp+" "+addPrice+" "+type;
    }

}
