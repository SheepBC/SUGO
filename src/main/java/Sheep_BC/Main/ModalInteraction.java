package Sheep_BC.Main;

import Sheep_BC.Commands.Basic;
import Sheep_BC.Commands.Maneger;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.interactions.components.Component;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;

public class ModalInteraction implements Runnable{

    ModalInteractionEvent event;
    String Modal_Id;

    public ModalInteraction(ModalInteractionEvent event){
        this.event = event;
        Modal_Id = event.getModalId();
    }

    @Override
    public void run() {
        if(Modal_Id.equalsIgnoreCase("Inquiry")){
            Basic.sendInquiry(event);
        }
        if(Modal_Id.startsWith("Inquiry_Reply")){
            Maneger.Modal_Inquiry_Reply(event);
        }
    }

    /*
    ACTION_ROW
    BUTTON
    SELECT_MENU
    TEXT_INPUT
     */
}
