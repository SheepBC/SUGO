package Sheep_BC.Commands;

import Sheep_BC.Annotations.MessageEvent;
import Sheep_BC.Sing.GuildMusicManager;
import Sheep_BC.Sing.PlayerManager;
import Sheep_BC.Supporters.Tool;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

public class Sing {

    MessageReceivedEvent event;
    String userID;
    MessageChannel channel;
    Member member;
    AudioManager manager;
    TextChannel textChannel;
    String[] args;

    public Sing(MessageReceivedEvent event,String[] args) {
        this.event = event;
        userID = event.getAuthor().getId();
        channel = event.getChannel();
        member = event.getMember();
        manager = event.getGuild().getAudioManager();
        textChannel = event.getTextChannel();
       this.args = args;
    }
    @MessageEvent(key = {"노래","play"},command = "숩노래/play [링크]", help = "노래를 틉니다.", type = Command_Type.MUSIC)
    public void play(){

        if(args.length == 1){
            channel.sendMessage("숩노래 [링크]").queue();
            return;
        }
        String url = event.getMessage().getContentRaw().substring(4);

        if(!isUrl(url)){
            url = "ytsearch:"+url;
        }

        GuildVoiceState voiceState = member.getVoiceState();
        if(voiceState.getChannel() == null){
            channel.sendMessage("음성채널에 먼저 들어가세요.").queue();
            return;
        }
        manager.openAudioConnection(voiceState.getChannel());

        PlayerManager.getINSTANCE()
                .loadAndPlayer(event.getTextChannel(),url);
    }

    @MessageEvent(key = {"끄기","stop"},command = "숩끄기/stop", help = "노래를 끕니다.", type = Command_Type.MUSIC)
    public void stop(){
        GuildVoiceState voiceState = member.getVoiceState();
        if(voiceState.getChannel() == null){
            channel.sendMessage("음성채널에 먼저 들어가세요.").queue();
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getINSTANCE().getMusicManager(event.getGuild());

        musicManager.scheduler.player.stopTrack();
        musicManager.scheduler.queue.clear();
        manager.closeAudioConnection();
        channel.sendMessage("노래가 멈췄습니다.").queue();

    }

    @MessageEvent(key = {"넘기기","다음","스킵","skip"},command = "숩넘기기/다음/스킵/skip", help = "노래를 넘깁니다.", type = Command_Type.MUSIC)
    public void skip(){
        GuildVoiceState voiceState = member.getVoiceState();
        if(voiceState.getChannel() == null){
            channel.sendMessage("음성채널에 먼저 들어가세요.").queue();
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getINSTANCE().getMusicManager(event.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;
        if(audioPlayer.getPlayingTrack() == null){
            channel.sendMessage("노래가 없는데?").queue();
            return;
        }

        musicManager.scheduler.nextTrack();
    }

    @MessageEvent(key = {"지금","now"},command = "숩지금/now", help = "현재 무슨노래인지 알려줍니다.", type = Command_Type.MUSIC)
    public void now(){
        GuildVoiceState voiceState = member.getVoiceState();
        if(voiceState.getChannel() == null){
            channel.sendMessage("음성채널에 먼저 들어가세요.").queue();
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getINSTANCE().getMusicManager(event.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;
        AudioTrack audioTrack = audioPlayer.getPlayingTrack();
        String title = audioTrack.getInfo().title;
        String maker = audioTrack.getInfo().author;
        String time = Tool.time_Unit(audioTrack.getInfo().length/1000);

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(title);
        embed.setColor(Tool.getRandomColor());
        embed.setFooter(maker+"("+time+")");

        event.getMessage().replyEmbeds(embed.build()).queue();
    }

    @MessageEvent(key = {"목록","리스트","lisr"},command = "숩목록/리스트/list", help = "노래 목록을 알려줍니다.", type = Command_Type.MUSIC)
    public void list(){
        final GuildMusicManager musicManager = PlayerManager.getINSTANCE().getMusicManager(event.getGuild());
        final BlockingQueue<AudioTrack> queue = musicManager.scheduler.queue;
        final AudioPlayer audioPlayer = musicManager.audioPlayer;

        if(audioPlayer.getPlayingTrack() == null){
            channel.sendMessage("노래가 없는데?").queue();
            return;
        }

        final ArrayList<AudioTrack> list = new ArrayList<>(queue);

        int num1 = 1;
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("노래 목록");
        embed.setColor(Tool.getRandomColor());

        AudioTrackInfo now = audioPlayer.getPlayingTrack().getInfo();
        String maker = now.author;
        String time = Tool.time_Unit(now.length/1000);

        embed.addField("```[#1]"+now.title+"```",maker+"("+time+")",true);
        for(AudioTrack track:list){
            num1++;

            AudioTrackInfo info = track.getInfo();
            String makerInfo = info.author;
            String timeInfo = Tool.time_Unit(info.length/1000);
            embed.addField("```[#"+num1+"]"+info.title+"```",makerInfo+"("+timeInfo+")",true);

            if(num1 > 10) break;
        }
        channel.sendMessageEmbeds(embed.build()).queue();
    }

    public static void menu(SelectMenuInteractionEvent event, String url){

        MessageChannel channel = event.getChannel();
        Member member = event.getMember();
        AudioManager manager = event.getGuild().getAudioManager();

        if(!isUrl(url)){
            url = "ytsearch:"+url;
        }

        GuildVoiceState voiceState = member.getVoiceState();
        if(voiceState.getChannel() == null){
            channel.sendMessage("음성채널에 먼저 들어가세요.").queue();
            return;
        }
        manager.openAudioConnection(voiceState.getChannel());

        PlayerManager.getINSTANCE()
                .loadAndPlayer(event.getTextChannel(),url);
    }

    private static boolean isUrl(String url) {
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

}

