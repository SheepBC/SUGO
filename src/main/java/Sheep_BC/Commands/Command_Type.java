package Sheep_BC.Commands;

import net.dv8tion.jda.api.entities.Emoji;

public enum Command_Type {
    BASIC("기본","기본적인 명령어",Emoji.fromUnicode("🍉")),
    MUSIC("음악","음악관련 명령어",Emoji.fromUnicode("🎶")),
    SCHOOL("학교","학교관련 명령어",Emoji.fromUnicode("🏫")),
    RECOMMAND("추천","추천관련 명령어",Emoji.fromUnicode("🎲")),
    USER("유저","유저관련 명령어",Emoji.fromUnicode("👤"));

    private final String command;
    private final String inform;
    private final Emoji emoji;

    Command_Type(String command, String inform,Emoji emoji){
        this.command = command;
        this.inform = inform;
        this.emoji = emoji;
    }

    public String getCommand(){
        return command;
    }
    public Emoji getEmoji(){
        return emoji;
    }

    public String getInform(){
        return inform;
    }

}


