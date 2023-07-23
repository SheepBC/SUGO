package Sheep_BC.Commands;

public enum Command_Type {
    BASIC("기본"),
    MUSIC("음악"),
    SCHOOL("학교");

    private final String command;

    Command_Type(String command){
        this.command = command;
    }

    public String getCommand(){
        return command;
    }

}


