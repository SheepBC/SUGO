package Sheep_BC.Commands.Users;

import Sheep_BC.Annotations.MessageEvent;
import Sheep_BC.Commands.Command_Type;
import Sheep_BC.Supporters.UserDB;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class User {

    MessageReceivedEvent event;
    String[] arg;
    net.dv8tion.jda.api.entities.User user;
    String userid;
    MessageChannel channel;

    String notUser = "회원가입이 되어있지 않은 유저입니다.";

    public User(MessageReceivedEvent event,String[] args){

        this.event = event;
        this.arg = args;
        user = event.getAuthor();
        userid = user.getId();
        channel = event.getChannel();

    }

    public User(MessageChannel channel){
        this.channel = channel;
    }

    @MessageEvent(key = {"회원가입","가입"},command = "숩회원가입", help = "회원가입을 합니다.", type = Command_Type.USER)
    public void join(){
        if(UserDB.isUser(userid)){
            channel.sendMessage("이미 회원가입한 사람입니다.").queue();
            return;
        }
        UserDB userDB = new UserDB(user);
        try {
            userDB.makeUser();
        }catch (Exception e){
            System.out.println("회원가입함");
        }
        channel.sendMessage("회원가입이 완료되었습니다.").queue();
    }

}
