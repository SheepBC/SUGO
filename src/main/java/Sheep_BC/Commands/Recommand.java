package Sheep_BC.Commands;

import SheepBC.BaseN;
import Sheep_BC.Annotations.ButtonEvent;
import Sheep_BC.Annotations.MessageEvent;
import Sheep_BC.Supporters.Rank;
import Sheep_BC.Supporters.Tool;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Recommand {

    MessageReceivedEvent event;
    String userID;
    User user;
    MessageChannel channel;

    String[] args;

    public Recommand(MessageReceivedEvent event,String[] args) {
        this.event = event;
        userID = event.getAuthor().getId();
        channel = event.getChannel();
        this.args = args;
        user = event.getAuthor();
    }

    @MessageEvent(key = {"게임추천","겜추"},command = "숩게임추천/겜추", help = "게임을 추천해줍니다.", type = Command_Type.RECOMMAND)
    public void Game(){
        String[] list = {"마인크래프트","끄투","래프트","갱비스트","얼치홀","그타","캐치마인드",
                "옵치","폴가이즈","로블록스","MUCK","갈틱폰","포탈","유료트럭 시물레이션","배그",
                "발로란트","롤","히오스","원신","마인크래프트 던전스","직접 만들어서 하기","휴폴플",
                "워킹 시물레이션 2020","애니팡","술래잡기","얼음땡","BattleBlock Theater","지뢰 찾기"};
        String title = "게임추천";
        recommend(list,title);
    }

    @MessageEvent(key = {"산추천","산추"},command = "숩산추천/산추", help = "산을 추천해줍니다.", type = Command_Type.RECOMMAND)
    public void Mount(){
        String[] list = {"무등산","백두산","||금강산||","지리산","한라산","에베레스트 산"};
        String title = "산추천";
        recommend(list,title);
    }

    @MessageEvent(key = {"주사위"},command = "숩주사위", help = "주사위를 던집니다.", type = Command_Type.RECOMMAND)
    public void Dice(){
        String[] list = {"\uD83C\uDFB2**1**","\uD83C\uDFB2**2**","\uD83C\uDFB2**3**",
                "\uD83C\uDFB2**4**","\uD83C\uDFB2**5**","\uD83C\uDFB2**6**"};
        String title = "주사위";
        recommend(list,title);
    }


    public void recommend(String[] list,String title){
        EmbedBuilder embed = new EmbedBuilder();
        String commend = "숩"+title+" [원하는 횟수]";

        if(args.length == 1){
            embed.setTitle(list[(int)(Math.random()*list.length)]).setColor(Tool.getRandomColor());
            channel.sendMessageEmbeds(embed.build()).queue();
        }

        if(args.length > 1){
            if(!Tool.isLong(args[1])){
                channel.sendMessage(commend).queue();
                return;
            }
            Long num = Long.parseLong(args[1]);
            if(num < 1){
                channel.sendMessage("최소 횟수는 1회 입니다.").queue();
                return;
            }

            embed.setColor(Tool.getRandomColor());
            embed.setTitle(title);

            ArrayList<Rank> rankList = Rank.getRankList(list,num);

            for(int i = 1;i<=5;i++){
                Rank rank = rankList.get(i-1);
                if(!rank.getNumber().equals(BigDecimal.ZERO)){
                    embed.addField(i+"위 ",rank.getname()+":"+ rank.getNumber(),true);
                }
            }

            if(Rank.getRankSize(rankList) > 6){
                String s = Tool.toSerialize(toRank(rankList));
                List<Button> buttons = new ArrayList<>();
                String[] sList = Tool.division(s,5);
                String[] seeList = {"전체","순위","보기","\uD83E\uDD14"};
                int i = 0;
                for(String s1:sList){
                    if(!s1.equalsIgnoreCase(sList[sList.length-1])){
                        Button button = Button.primary("Rank"+s1,seeList[i]);
                        i++;
                        buttons.add(button);
                    }
                }
                embed.setFooter(sList[sList.length-1]);

                channel.sendMessageEmbeds(embed.build()).setActionRows(ActionRow.of(buttons)).queue();
            }else {
                channel.sendMessageEmbeds(embed.build()).queue();
            }

        }

    }
    @MessageEvent(key = {"추천"},command = "숩추천 [원하는 목록1/목록2...] ([원하는 횟수])", help = "원하는것중에서 추천해줍니다.", type = Command_Type.RECOMMAND)
    public void customRecommend(){
        String command = "숩추천 [원하는 목록1/목록2...] ([원하는 횟수])";
        String title ="추천";
        if(args.length < 2){
            channel.sendMessage(command).queue();
            return;
        }
        String listString ="";
        int num = 1;
        for (int i =1;i<args.length;i++){
            if(i == (args.length-1) & args[i].startsWith("[") & args[i].endsWith("]")){
                String numS = args[i];
                numS = numS.replace("[","")
                        .replace("]","");
                try {
                    num = Integer.parseInt(numS);
                }catch (Exception e){
                    listString += args[i]+" ";
                }
            }
            else {
                listString += args[i]+" ";
            }
        }

        String[] list = listString.split("/");
        EmbedBuilder embed = new EmbedBuilder();

        ArrayList<Rank> rankList = Rank.getRankList(list,num);

        embed.setColor(Tool.getRandomColor());
        if(num == 1){
            embed.setTitle(rankList.get(0).getname());
            channel.sendMessageEmbeds(embed.build()).queue();
        }
        else {
            embed.setTitle(title);

            int max = Math.min(5, rankList.size());
            for(int i = 1;i<=max;i++){
                Rank rank = rankList.get(i-1);
                if(!rank.getNumber().equals(BigDecimal.ZERO)){
                    embed.addField(i+"위 ",rank.getname()+":"+ rank.getNumber(),true);
                }
            }

            if(Rank.getRankSize(rankList) > 6){
                String s = Tool.toSerialize(toRank(rankList));
                List<Button> buttons = new ArrayList<>();
                String[] sList = Tool.division(s,5);
                String[] seeList = {"전체","순위","보기","\uD83E\uDD14"};
                int i = 0;
                for(String s1:sList){
                    if(!s1.equalsIgnoreCase(sList[sList.length-1])){
                        Button button = Button.primary("Rank"+s1,seeList[i]);
                        i++;
                        buttons.add(button);
                    }
                }
                embed.setFooter(sList[sList.length-1]);

                channel.sendMessageEmbeds(embed.build()).setActionRows(ActionRow.of(buttons)).queue();
            }else {
                channel.sendMessageEmbeds(embed.build()).queue();
            }
        }

    }

    public static String[] toRank(ArrayList<Rank> list){
        String[] ranks = new String[list.size()];
        BaseN baseM = new BaseN(Tool.getListM());

        for(int l = 0;l<list.size();l++){
            Rank rank = list.get(l);
            if(!rank.getNumber().equals(BigDecimal.ZERO)){
                String num =    baseM.DecimalToBaseN(rank.getNumber().toString().replace(".0","")) ;
                String name = rank.getname();
                //System.out.println(rank.getname()+" "+name);
                ranks[l] = name+"/."+num;
            }
        }

        return ranks;
    }
    @ButtonEvent
    public static void rank_Button(ButtonInteractionEvent event){
        String s = "";

        User user = event.getUser();
        List<Button> buttons = event.getMessage().getButtons();
        String footer = event.getMessage().getEmbeds().get(0).getFooter().getText();

        for (Button b:buttons){
            s += b.getId().replace("Rank","");
        }
        s += footer;
        String[] list = (String[]) Tool.toObject(s);
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("추천");
        embed.setColor(Tool.getRandomColor());

        int i = 1;
        for(String sr:list){
            String[] srs = sr.split("/.");
            BaseN baseM = new BaseN(Tool.getListM());
            Rank rank = new Rank(srs[0],Double.parseDouble(baseM.BaseNToDecimal(srs[1])));
            if(!rank.getNumber().equals(BigDecimal.ZERO)){
                embed.addField(i+"위 ",rank.getname()+":"+ rank.getNumber(),true);
                i++;
            }
        }

        event.replyEmbeds(embed.build()).setEphemeral(true).queue();

    }

}
