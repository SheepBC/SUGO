package Sheep_BC.Commands;

import Sheep_BC.Annotations.ButtonEvent;
import Sheep_BC.Supporters.Tool;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.ModalInteraction;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;

public class Maneger {
    MessageReceivedEvent event;
    String userID;
    User user;
    MessageChannel channel;

    String[] args;

    public Maneger(MessageReceivedEvent event) {
        this.event = event;
        userID = event.getAuthor().getId();
        channel = event.getChannel();
        args = event.getMessage().getContentRaw().split(" ");
        user = event.getAuthor();
    }
    @ButtonEvent(only_Maneger = true)
    public static void Button_Inquiry_Reply(ButtonInteractionEvent event){
        MessageEmbed embed = event.getMessage().getEmbeds().get(0);
        String text = embed.getFields().get(0).getValue().replaceAll("```","");
        Modal.Builder modal = Modal.create("Inquiry_Reply"+embed.getFooter().getText(),"문의답장");

        String[] list = di(text,4);
        int i = 0;
        for (String s:list){
            if(s == null) break;
            String[] list2 =di2(s);
            TextInput textInput = TextInput.create("I"+i,list2[0], TextInputStyle.PARAGRAPH)
                    .setPlaceholder(list2[1] == null ? "...":list2[1])
                    .setMaxLength(1000)
                    .build();
            i++;
            modal.addActionRow(textInput);
        }

        TextInput textInput2 = TextInput.create("text","답장 내용(답장 후 첫 칸에 문의 내용 요약)", TextInputStyle.PARAGRAPH)
                .setPlaceholder("몰라!!")
                .setMaxLength(1000)
                .build();

        modal.addActionRow(textInput2);
        event.replyModal(modal.build()).queue();
    }

    private static String[] di2(String s){
        String[] list = new String[2];
        if(s.length() <= 45){
            list[0] = s;
        }
        else {
            list[0] = s.substring(0,45);
            list[1] = s.substring(46);
        }
        return list;
    }

    private static String[] di(String s,int n){
        if(n == 1) return new String[]{s};
        int num = 145;
        String[] list = new String[n];
        int index = 0;
        int beginIndex = s.length() - s.length() % (n-1);
        for (int i = 0; i< beginIndex;){
            if(i+num > s.length()){
                list[index] = s.substring(i,s.length());
            }
            else {
                list[index] = s.substring(i,i+num);
            }
            i+= num;
            index++;

        }
        return list;
    }

    public static void Modal_Inquiry_Reply(ModalInteractionEvent event){
        String userid = event.getModalId().replace("Inquiry_Reply","");
        JDA jda = event.getJDA();
        EmbedBuilder embed = new  EmbedBuilder();
        embed.setTitle("문의 답장");
        embed.setColor(Tool.getRandomColor());
        embed.addField("문의내용",event.getValue("I0").getAsString(),true);
        embed.addField("답장",event.getValue("text").getAsString(),true);
        jda.retrieveUserById(userid).flatMap(User::openPrivateChannel).queue(privateChannel -> privateChannel.sendMessageEmbeds(embed.build()).queue());
        event.replyEmbeds(embed.build()).queue();
    }

}
