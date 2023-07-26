package Sheep_BC.Main;


import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;

public class Main extends ListenerAdapter {

    public static JDA jda;

    public static void main(String[] args) throws LoginException {
        jda = JDABuilder.createDefault(Setting.getBasic("BotToken")).build();
        jda.getPresence().setActivity(Activity.playing("\"숩명령어\"를 쳐보라고 말하는중 "));
        jda.getPresence().setStatus(OnlineStatus.ONLINE);
        jda.addEventListener(new Main());
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if(event.getAuthor().isBot()){
            String s = event.getAuthor().getId();
            String u = event.getJDA().getSelfUser().getId();
            if(s.equalsIgnoreCase(u)) return;
        }

        Runnable runnable = new MessageReceived(event);
        Thread thread = new Thread(runnable);
        thread.start();
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        Runnable runnable = new ButtonInteraction(event);
        Thread thread = new Thread(runnable);
        thread.start();
    }

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        Runnable runnable = new ModalInteraction(event);
        Thread thread = new Thread(runnable);
        thread.start();
    }

    @Override
    public void onSelectMenuInteraction(@NotNull SelectMenuInteractionEvent event) {
        Runnable runnable = new SelectMenuInteraction(event);
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
