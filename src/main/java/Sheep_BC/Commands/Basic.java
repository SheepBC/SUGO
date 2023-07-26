package Sheep_BC.Commands;

import Sheep_BC.Annotations.ButtonEvent;
import Sheep_BC.Annotations.MessageEvent;
import Sheep_BC.Main.ButtonInteraction;
import Sheep_BC.Main.MessageReceived;
import Sheep_BC.Main.SelectMenuInteraction;
import Sheep_BC.Main.Setting;
import Sheep_BC.Supporters.Tool;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;

import java.awt.*;
import java.lang.reflect.Method;

public class Basic {

    MessageReceivedEvent event;
    String userID;
    User user;
    MessageChannel channel;

    String[] args;

    public Basic(MessageReceivedEvent event) {
        this.event = event;
        userID = event.getAuthor().getId();
        channel = event.getChannel();
        args = event.getMessage().getContentRaw().split(" ");
        user = event.getAuthor();
    }
    @MessageEvent(key = {"봇","dk"},command = "숩봇", help = "숩이의 도움말을 봅니다.", type = Command_Type.BASIC)
    public void Bot(){
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Tool.getRandomColor());
        embed.setTitle("SUBACKGOGI");
        embed.addField("**버전**",Setting.getBasic("Version"),true);
        embed.addField("**접속한 서버**",event.getJDA().getGuilds().size()+"개",true);
        embed.setThumbnail("https://lh3.googleusercontent.com/a-/AOh14GgDzMM93Ku5VERrDuCFdGqgiTrL04yUOnbLdJMK=s600-k-no-rp-mo");
        embed.setFooter("제작자: 양불리");

        channel.sendMessageEmbeds(embed.build()).queue();
    }

    @MessageEvent(key = {"핑","ping"},command = "숩핑/ping", help = "핑을 봅니다.", type = Command_Type.BASIC)
    public void Ping(){
        long time = System.currentTimeMillis();
        channel.sendMessage("핑!").queue(response -> {
            response.editMessageFormat("퐁: %d ms", System.currentTimeMillis() - time).queue();
        });
    }

    @MessageEvent(key = {"프로필"},command = "숩프로필", help = "프로필을 보내줍니다.", type = Command_Type.BASIC)
    public void Profile(){
        String profile = user.getAvatarUrl();
        channel.sendMessage(profile == null ? ":thinking: ":profile).queue();
    }


    @MessageEvent(key = {"도움말","help"},command = "숩도움말/help", help = "도움말을 봅니다.", type = Command_Type.BASIC)
    public void help(){

        SelectMenu.Builder menu = SelectMenu.create("help");

        for(Command_Type commandType:Command_Type.values()){
            menu.addOption(commandType.getCommand(), commandType.name(),commandType.getInform(),commandType.getEmoji());
        }

        if(userID.equalsIgnoreCase("336828850358976512")){
            //menu.addOption(Command_Type.;
        }

        channel.sendMessage("숩봇의 명령어를 알려줍니다.밑에서 선택해주세요!").setActionRow(menu.build()).queue();
    }

    public static void help_Menu(SelectMenuInteractionEvent event){
        Command_Type commandType = Command_Type.valueOf(event.getValues().get(0));
        event.replyEmbeds(getHelp_Embed(commandType).build()).setEphemeral(true).queue();
    }

    private static EmbedBuilder getHelp_Embed(Command_Type type){
        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle(type.getCommand() + " 명령어");
        embed.setColor(Tool.getRandomColor());

        for(Class clas: MessageReceived.event_class){
            for(Method method: clas.getMethods()){
                if(method.isAnnotationPresent(Sheep_BC.Annotations.MessageEvent.class)){
                    MessageEvent messageEvent = method.getAnnotation(Sheep_BC.Annotations.MessageEvent.class);
                    if(messageEvent.type() == type){
                        embed.addField(messageEvent.command(),messageEvent.help(),true);
                    }
                }
            }
        }
        String footer = "\uD83C\uDF49TIP 숩대신 ㅅ,시써도 작동됩니다.[]는 필수,()는 선택";;
        embed.setFooter(footer);

        return embed;
    }

    @MessageEvent(key = {"문의"},command = "숩문의", help = "문의를 할수있게하는 버튼을 보냅니다.", type = Command_Type.BASIC)
    public void Inquiry(){
        Button button = Button.primary("Inquiry", "문의하러가기");
        channel.sendMessage("문의를 하실려면 아래버튼을 눌러주세요!!!").setActionRow(button).queue();
    }

    @ButtonEvent()
    public static void Inquiry_Button(ButtonInteractionEvent event){
        TextInput textInput = TextInput.create("text","문의 내용", TextInputStyle.PARAGRAPH)
                .setMaxLength(555)
                .setMinLength(3)
                .setPlaceholder("제돈이 사라졌었요!!!!")
                .build();


        Modal.Builder modal = Modal.create("Inquiry","문의");
        modal.addActionRow(textInput);
        event.replyModal(modal.build()).queue();
    }

    public static void sendInquiry(ModalInteractionEvent event){
        JDA jda = event.getJDA();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Tool.getRandomColor());
        embed.setFooter(event.getUser().getId());
        embed.setTitle(event.getUser().getName()+"님의 문의");
        embed.addField("문의내용","```"+event.getValue("text").getAsString()+"```",true);
        event.reply("문의가 완료되었습니다.").queue();
        Button button = Button.primary("Inquiry_Reply", "답장하러가기");
        jda.getTextChannelById(Setting.getInquiry()).sendMessageEmbeds(embed.build()).setActionRow(button).queue();
        jda.retrieveUserById(event.getUser().getId()).flatMap(User::openPrivateChannel).queue(privateChannel -> privateChannel.sendMessageEmbeds(embed.build()).queue());
    }

}
