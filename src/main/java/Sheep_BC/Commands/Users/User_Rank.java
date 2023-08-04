package Sheep_BC.Commands.Users;

import Sheep_BC.Annotations.MessageEvent;
import Sheep_BC.Commands.Command_Type;
import Sheep_BC.Supporters.Rank;
import Sheep_BC.Supporters.SaveNLoad;
import Sheep_BC.Supporters.Tool;
import Sheep_BC.Supporters.UserDB;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class User_Rank {

    MessageReceivedEvent event;
    String[] arg;
    net.dv8tion.jda.api.entities.User user;
    String userid;
    MessageChannel channel;

    String notUser = "회원가입이 되어있지 않은 유저입니다.";

    public User_Rank(MessageReceivedEvent event,String[] args){

        this.event = event;
        this.arg = args;
        user = event.getAuthor();
        userid = user.getId();
        channel = event.getChannel();

    }

    public User_Rank(MessageChannel channel){
        this.channel = channel;
    }

    @MessageEvent(key = {"순위","랭크"},command = "숩순위/랭크 (돈/자산(재산)/건강/경험치/빚)", help = "순위를 확인합니다.", type = Command_Type.USER)
    public void Rank() {

        SelectMenu.Builder menu = SelectMenu.create("UserRank")
                .addOption("돈", "돈")
                .addOption("자산(재산)", "재산")
                .addOption("건강", "건강")
                .addOption("경험치", "경험치")
                .addOption("빚", "빚");

        if (arg.length == 1) {

            channel.sendMessage("**숩순위 (돈/자산(재산)/건강/경험치/빚)**").setActionRow(menu.build()).queue();
            return;
        }
        String[] list = {"돈","자산","재산","건강","경험치","빚"};
        if(!List.of(list).contains(arg[1])){
            channel.sendMessage("**숩순위 (돈/자산(재산)/건강/경험치/빚)**").setActionRow(menu.build()).queue();
            return;
        }

        User_Rank_System(arg[1]);
    }

    public void User_Rank_System(String s) {
        //System.out.println(s);
        String path = "";
        String key = "";
        boolean isOne = true;

        switch (s) {
            case "돈":
                path = "Properties.txt";
                key = "Money";
                break;
            case "재산":
            case "자산":
                isOne = false;
                break;
            case "건강":
                path = "HealthNOthers.txt";
                key = "Health";
                break;
            case "경험치":
                path = "Properties.txt";
                key = "Exp";
                break;
            case "빚":
                path = "Properties.txt";
                key = "Debt";
                break;
            default:
                return;
        }
        if (isOne) {
            ArrayList<Rank> list = getRank(path, key);
            EmbedBuilder embed = new EmbedBuilder();

            String title = s;
            embed.setTitle(title);
            embed.setColor(Tool.getRandomColor());
            int i = 1;
            for (Rank rank : list) {
                UserDB userDB = new UserDB(rank.getname());
                embed.addField(i + "위 "+userDB.getName() ,Tool.unit(rank.getNumber()), true);
                i++;
                if (i > 5) {
                    Button button = Button.primary("UserRank" + s, "전체순위보기");
                    ArrayList<Button> buttons = new ArrayList<>();
                    buttons.add(button);
                    channel.sendMessageEmbeds(embed.build()).setActionRows(ActionRow.of(buttons)).queue();
                    return;
                }
            }
            Button button = Button.primary("UserRank" + s, "전체순위보기");
            ArrayList<Button> buttons = new ArrayList<>();
            buttons.add(button);
            channel.sendMessageEmbeds(embed.build()).setActionRows(ActionRow.of(buttons)).queue();
        } else {
            ArrayList<Rank> list = new ArrayList<>();

            switch (s) {
                case "재산":
                case "자산":
                    for (String userid : UserDB.getUserList()) {
                        UserDB userDB = new UserDB(userid);
                        BigInteger pro = userDB.getAll_Property();
                        list.add(new Rank(userid, pro));
                    }
                    break;
                case "스탯":
                    for (String userid : UserDB.getUserList()) {
                        UserDB userDB = new UserDB(userid);
                        int sta = userDB.getLuck() + userDB.getResilience() + userDB.getStealing() + userDB.getDefense();
                        list.add(new Rank(userid, sta));
                    }
                    break;
                case "죽거나파산한횟수":
                    for (String userid : UserDB.getUserList()) {
                        UserDB userDB = new UserDB(userid);
                        int die = userDB.getDead_Num() + userDB.getFailure_Num();
                        list.add(new Rank(userid, die));
                    }
                    break;
            }
            Collections.sort(list, new Comparator<Rank>() {
                @Override
                public int compare(Rank o1, Rank o2) {
                    if (o1.getNumber().max(o2.getNumber()).equals(o1.getNumber())) {
                        return -1;
                    } else if (o1.getNumber().min(o2.getNumber()).equals(o1.getNumber())) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });

            EmbedBuilder embed = new EmbedBuilder();
            String title = s;
            //String title = arg[1] == null ? "":arg[1];
            embed.setTitle(title);
            embed.setColor(Tool.getRandomColor());
            int i = 1;
            for (Rank rank : list) {
                UserDB userDB = new UserDB(rank.getname());
                embed.addField(i + "위 "+userDB.getName(), Tool.unit(rank.getNumber()), true);
                i++;
                if (i > 5) {
                    Button button = Button.primary("UserRank" + s, "전체순위보기");
                    ArrayList<Button> buttons = new ArrayList<>();
                    buttons.add(button);
                    channel.sendMessageEmbeds(embed.build()).setActionRows(ActionRow.of(buttons)).queue();
                    return;
                }
            }
            Button button = Button.primary("UserRank" + s, "전체순위보기");
            ArrayList<Button> buttons = new ArrayList<>();
            buttons.add(button);
            channel.sendMessageEmbeds(embed.build()).setActionRows(ActionRow.of(buttons)).queue();
        }
    }

    public static ArrayList<Rank> getRank(String path,String key){
        ArrayList<Rank> RankList = new ArrayList<>();
        for(String userid:UserDB.getUserList()){
            RankList.add(new Rank(userid, new BigDecimal(SaveNLoad.loadP("UserDB/"+userid+"/"+path,key))));
        }

        Collections.sort(RankList, new Comparator<Rank>() {
            @Override
            public int compare(Rank o1, Rank o2) {
                if(o1.getNumber().max(o2.getNumber()).equals(o1.getNumber())){
                    return -1;
                }else if(o1.getNumber().min(o2.getNumber()).equals(o1.getNumber())){
                    return 1;
                }else {
                    return 0;
                }
            }
        });

        return RankList;
    }

    public static void FullRank(ButtonInteractionEvent event, String arg1){

        String path = "";
        String key = "";
        boolean isOne = true;

        switch (arg1){
            case "돈":
                path = "Properties.txt";
                key = "Money";
                break;
            case "재산":
            case "자산":
            case "스탯":
            case "죽거나파산한횟수":
                isOne = false;
                break;
            case "건강":
                path = "HealthNOthers.txt";
                key =  "Health";
                break;
            case "경험치":
                path = "Properties.txt";
                key = "Exp";
                break;
            case "빚":
                path = "Properties.txt";
                key = "Debt";
                break;
            case "미납세":
                path = "Properties.txt";
                key = "Tax";
                break;
            default:
                return;
        }
        if(isOne){
            ArrayList<Rank> list = getRank(path,key);
            EmbedBuilder embed = new EmbedBuilder();

            String title = arg1;
            embed.setTitle(title);
            embed.setColor(Tool.getRandomColor());
            int i = 1;
            for(Rank rank:list){
                UserDB userDB = new UserDB(rank.getname());
                embed.addField(i+"위 "+userDB.getName(),Tool.unit(rank.getNumber()),true);
                i++;
            }
            event.replyEmbeds(embed.build()).setEphemeral(true).queue();
        }
        else {
            ArrayList<Rank> list = new ArrayList<>();

            switch (arg1){
                case "재산":
                case "자산":
                    for(String userid:UserDB.getUserList()){
                        UserDB userDB = new UserDB(userid);
                        BigInteger pro = userDB.getAll_Property();
                        list.add(new Rank(userid,pro));
                    }
                    break;
                case "스탯":
                    for(String userid:UserDB.getUserList()){
                        UserDB userDB = new UserDB(userid);
                        int sta = userDB.getLuck()+userDB.getResilience()+userDB.getStealing()+userDB.getDefense();
                        list.add(new Rank(userid,sta));
                    }
                    break;
                case "죽거나파산한횟수":
                    for(String userid:UserDB.getUserList()){
                        UserDB userDB = new UserDB(userid);
                        int die = userDB.getDead_Num()+userDB.getFailure_Num();
                        list.add(new Rank(userid,die));
                    }
                    break;
            }
            Collections.sort(list, new Comparator<Rank>() {
                @Override
                public int compare(Rank o1, Rank o2) {
                    if(o1.getNumber().max(o2.getNumber()).equals(o1.getNumber())){
                        return -1;
                    }else if(o1.getNumber().min(o2.getNumber()).equals(o1.getNumber())){
                        return 1;
                    }else {
                        return 0;
                    }
                }
            });

            EmbedBuilder embed = new EmbedBuilder();

            String title = arg1;
            embed.setTitle(title);
            embed.setColor(Tool.getRandomColor());
            int i = 1;
            for(Rank rank:list){
                UserDB userDB = new UserDB(rank.getname());
                embed.addField(i+"위",userDB.getName()+": "+Tool.unit(rank.getNumber()),true);
                i++;
            }
            event.replyEmbeds(embed.build()).setEphemeral(true).queue();

        }

    }

}
