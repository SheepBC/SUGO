package Sheep_BC.Main;

import Sheep_BC.Commands.Basic;
import Sheep_BC.Commands.Maneger;
import Sheep_BC.Commands.School_Command;
import Sheep_BC.Commands.Sing;
import Sheep_BC.Commands.Users.User;
import Sheep_BC.Commands.Users.User_Rank;
import Sheep_BC.Schools.School_Data;
import Sheep_BC.Supporters.UserDB;
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
        if(id.equalsIgnoreCase("UserRank")){
            User_Rank user = new User_Rank(channel);
            user.User_Rank_System(option);
        }
        if(id.equalsIgnoreCase("otherUser-Inform")){
            User user = new User(channel);
            user.otherUser_inform_System(option);
        }
        else if(id.startsWith("meal")){
            String[] list = option.split("/");
            School_Data sd = new School_Data(list[0]);
            School_Command.sendMeal(channel,sd,list[1]);
        }
        else if(id.equalsIgnoreCase("School")){
            String notUser = "회원가입이 되어있지 않은 유저입니다.";
            String userid = event.getUser().getId();
            if(!UserDB.isUser(userid)){
                event.getChannel().sendMessage(notUser).queue();
                return;
            }
            School_Data school_data = new School_Data(option);
            UserDB userDB = new UserDB(userid);
            userDB.setSchool(school_data);
            event.reply(school_data.School_Name+" 등록하였습니다.").queue();
        }

    }

}
