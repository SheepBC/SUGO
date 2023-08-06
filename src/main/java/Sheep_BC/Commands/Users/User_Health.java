package Sheep_BC.Commands.Users;

import Sheep_BC.Annotations.MessageEvent;
import Sheep_BC.Commands.Command_Type;
import Sheep_BC.Supporters.Tool;
import Sheep_BC.Supporters.UserDB;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Timer;
import java.util.TimerTask;

public class User_Health {

    MessageReceivedEvent event;
    String[] arg;
    net.dv8tion.jda.api.entities.User user;
    String userid;
    MessageChannel channel;

    String notUser = "회원가입이 되어있지 않은 유저입니다.";


    public User_Health(MessageReceivedEvent event,String[] args){

        this.event = event;
        this.arg = args;
        user = event.getAuthor();
        userid = user.getId();
        channel = event.getChannel();

    }

    @MessageEvent(key = {"회복"},command = "숩회복", help = "회복합니다.", type = Command_Type.Health)
    public void health(){

        if(!UserDB.isUser(userid)){
            channel.sendMessage(notUser).queue();
            return;
        }

        UserDB userDB = new UserDB(user);
        if(userDB.isHealth()){
            channel.sendMessage("이미 회복중 입니다.").queue();
            return;
        }

        if(userDB.getHealth() >= 0){
            channel.sendMessage("건강이 0이상 입니다. 숩잠 을 사용 하여 건강을 올려 주세요").queue();
            return;
        }

        userDB.setIsHealth(true);
        long health = userDB.getHealth();
        String time = Tool.time_Unit(health*-60);

        Start_Health(event.getJDA(), userid);

        channel.sendMessage("예상 소요 시간: "+time).queue();

    }

    @MessageEvent(key = {"그만회복"},command = "숩그만회복", help = "회복을 멈춥니다.", type = Command_Type.Health)
    public void stop_health(){
        if(!UserDB.isUser(userid)){
            channel.sendMessage(notUser).queue();
            return;
        }

        UserDB userDB = new UserDB(user);
        if(!userDB.isHealth()){
            channel.sendMessage("회복 중이 아닙니다.").queue();
        }
        else {
            userDB.setIsStopHealth(true);
            channel.sendMessage("회복이 곧 완료됩니다.").queue();
        }
    }

    public static void Start_Health(JDA jda, String userid){
        if(UserDB.isUser(userid)){

            Timer timer = new Timer();
            int second = 1000;

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    UserDB userDB = new UserDB(userid);
                    userDB.addHealth(1);
                    addHealth_to_Stat(userDB);

                    if(userDB.getIsStopHealth()){
                        userDB.setIsHealth(false);
                        userDB.setIsStopHealth(false);
                        timer.cancel();

                        jda.retrieveUserById(userid).flatMap(net.dv8tion.jda.api.entities.User::openPrivateChannel)
                                .queue(Channel ->{Channel.sendMessage("회복이완료되었습니다.").queue();});

                    }

                    if(userDB.getHealth() >=0){
                        userDB.setIsHealth(false);
                        timer.cancel();

                        jda.retrieveUserById(userid).flatMap(net.dv8tion.jda.api.entities.User::openPrivateChannel)
                                .queue(Channel ->{Channel.sendMessage("회복이완료되었습니다.").queue();});

                    }

                }
            },60*second,60*second);

        }
    }
    
    @MessageEvent(key = {"잠"},command = "숩잠 [잘 시간(예: 1주 1일 1시간 1분,최소 단위 **분**)]", help = "잠을 잡니다.", type = Command_Type.Health)
    public void sleep(){

        if(!UserDB.isUser(userid)){
            channel.sendMessage(notUser).queue();
            return;
        }

        UserDB userDB = new UserDB(user);

        if(userDB.isSleeping()){
            channel.sendMessage("이미 잠자는 중 입니다.").queue();
            return;
        }

        if(userDB.getSleepTime() > 0 ){
            channel.sendMessage("잠에서 깨어 나고 나서 다시 시도 해 주세요.").queue();
            return;
        }

        if(userDB.getHealth() < 0){
            channel.sendMessage("건강이 0미만 입니다. 숩회복 을 사용 하여 회복 하여 주세요").queue();
            return;
        }

        if(arg.length < 2){
            channel.sendMessage("숩잠 [잘 시간(예: 1주 1일 1시간 1분,최소 단위 **분**)]").queue();
            return;
        }
        long m = Tool.Time_Unit_To_Second(arg[1]).longValue();
        if(m <= 0){
            if(Tool.isLong(arg[1])){
                m = Long.parseLong(arg[1])*60;
            }else {
                channel.sendMessage("숩잠 [잘 시간(예: 1주 1일 1시간 1분,최소 단위 **분**)]").queue();
                return;
            }
        }
        userDB.setAll_SleepTime(m);
        Start_Sleep(event.getJDA(), userid);
        channel.sendMessage(Tool.time_Unit(m)+" 만큼 잠을 자기 시작 합니다.").queue();
    }

    @MessageEvent(key = {"그만잠"},command = "숩그만잠", help = "잠에서 깨어납니다.", type = Command_Type.Health)
    public void stop_Sleep(){
        if(!UserDB.isUser(userid)){
            channel.sendMessage(notUser).queue();
            return;
        }

        UserDB userDB = new UserDB(user);
        if(!userDB.isSleeping()){
            channel.sendMessage("잠자는 중이 아닙니다.").queue();
        }
        else {
            userDB.setAll_SleepTime(0);
            channel.sendMessage("잠에서 곧 깨어납니다.").queue();
        }
    }

    public static void Start_Sleep(JDA jda, String userid){
        if(UserDB.isUser(userid)){

            Timer timer = new Timer();
            int second = 1000;

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    UserDB userDB = new UserDB(userid);
                    addHealth_to_Sleep(userDB);
                    addHealth_to_Stat(userDB);
                    userDB.addSleepTime(60);

                    if(userDB.getSleepTime() >=userDB.getAll_SleepTime()){
                        userDB.setAll_SleepTime(0);
                        userDB.setSleepTime(0);
                        timer.cancel();

                        jda.retrieveUserById(userid).flatMap(User::openPrivateChannel)
                                .queue(Channel ->{Channel.sendMessage("잠에서 깨어 났습니다.").queue();});

                    }

                }
            },60*second,60*second);

        }
    }

    private static void addHealth_to_Sleep(UserDB userDB){
        double time = userDB.getSleepTime()/60+0.1;

        if(time <= 580){
            double x = Math.abs(time-360);
            double f = Math.ceil(Math.sqrt(x)/5);
            long p = (long) (6.5-f);

            userDB.addHealth(p);
        }
        else {
            BigDecimal r750 = new BigDecimal("750");
            BigDecimal rT = new BigDecimal(String.valueOf(time));
            BigDecimal q = r750.divide(rT,7, RoundingMode.HALF_EVEN).sqrt(MathContext.DECIMAL32);
            long st = (long) (Math.ceil(q.doubleValue()+0.00001));
            userDB.addHealth(st+1);
        }
    }

    private static void addHealth_to_Stat(UserDB userDB){
        int L = userDB.getResilience();
        if(L != 0){

            double d = Math.random()*100;
            double p = (double) L*100 /(L+22);
            if(d<p){
                userDB.addHealth(1);
            }
        }
    }
}
