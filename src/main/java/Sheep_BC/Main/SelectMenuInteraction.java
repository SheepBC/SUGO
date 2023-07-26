package Sheep_BC.Main;

import Sheep_BC.Commands.Basic;
import Sheep_BC.Commands.Maneger;
import Sheep_BC.Commands.Sing;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;

public class SelectMenuInteraction implements Runnable{

    SelectMenuInteractionEvent event;
    String option;
    String id;
    MessageChannel channel;

    public SelectMenuInteraction(SelectMenuInteractionEvent event){
        this.event = event;
        option = event.getValues().get(0);
        channel = event.getChannel();
        id = event.getComponentId();
    }

    @Override
    public void run() {
        if(id.equalsIgnoreCase("Sing")){
            Sing.menu(event,option);
        }if(id.equalsIgnoreCase("help")){
            Basic.help_Menu(event);
        }

    }

}
