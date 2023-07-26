package Sheep_BC.Sing;

import Sheep_BC.Supporters.Tool;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class PlayerManager {

    private static PlayerManager INSTANCE;

    private final Map<Long,GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    public PlayerManager(){
        this.musicManagers =new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);

    }

    public GuildMusicManager getMusicManager(Guild guild){
        return this.musicManagers.computeIfAbsent(guild.getIdLong(),(guildId) ->{

            final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);
            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());
            return guildMusicManager;

        });
    }

    public void loadAndPlayer(TextChannel channel,String trackURL){
        final GuildMusicManager musicManager = this.getMusicManager(channel.getGuild());

        this.audioPlayerManager.loadItemOrdered(musicManager, trackURL, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                musicManager.scheduler.queue(audioTrack);
                String title = audioTrack.getInfo().title;
                String maker = audioTrack.getInfo().author;
                String time = Tool.time_Unit(audioTrack.getInfo().length/1000);

                final GuildMusicManager musicManager = PlayerManager.getINSTANCE().getMusicManager(channel.getGuild());
                final BlockingQueue<AudioTrack> queue = musicManager.scheduler.queue;
                int num = queue.size();

                String nums = "바로재생";
                if(num > 0){
                    nums = "대기열 "+num+"번";
                }

                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle(title);
                embed.setColor(Tool.getRandomColor());
                embed.setFooter(maker+" ("+time+" , "+nums+")");

                channel.sendMessageEmbeds(embed.build()).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                final List<AudioTrack> tracks = audioPlaylist.getTracks();

                SelectMenu.Builder menu = SelectMenu.create("Sing");


                for(final AudioTrack track:tracks){
                    menu.addOption(track.getInfo().title,track.getInfo().uri,Tool.time_Unit(track.getInfo().length/1000)+" "+track.getInfo().author);
                }
                channel.sendMessage("검색내용: "+audioPlaylist.getName()).setActionRow(menu.build()).queue();
            }

            @Override
            public void noMatches() {

            }

            @Override
            public void loadFailed(FriendlyException e) {

            }
        });
    }

    public static PlayerManager getINSTANCE(){
        if(INSTANCE == null){
            INSTANCE = new PlayerManager();
        }
        return INSTANCE;
    }

}
