package Sheep_BC.Main;

import Sheep_BC.Annotations.ButtonEvent;
import Sheep_BC.Annotations.MessageEvent;
import Sheep_BC.Commands.Basic;
import Sheep_BC.Commands.Maneger;
import Sheep_BC.Commands.Recommand;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Locale;

public class ButtonInteraction implements Runnable {

    ButtonInteractionEvent event;
    MessageChannel channel;

    User user;

    String Button_id;
    String Button_Label;
    public static Class[] event_class = new Class[]{Maneger.class,Basic.class};
    public ButtonInteraction(ButtonInteractionEvent event){
        this.event = event;
        channel = event.getChannel();
        user = event.getUser();
        Button_id = event.getButton().getId();
        Button_Label = event.getButton().getLabel();
    }
    @Override
    public void run(){
        //channel.sendMessage(Button_id+" "+Button_Label).queue();

        if(Button_id.equalsIgnoreCase("Inquiry")){
            Basic.Inquiry_Button(event);
        }
        else if(Button_id.equalsIgnoreCase("Inquiry_Reply")){
            Maneger.Button_Inquiry_Reply(event);
        }
        else if(Button_id.startsWith("Rank")){
            Recommand.rank_Button(event);
        }


    }





}
