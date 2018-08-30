package it.bitrock.model;

public enum Command {
    ADD_PLAYER("add player "),
    MOVE_PLAYER("move ");

    private final String command;

    Command(String command) {
        this.command = command;
    }

    public String command() {
        return command;
    }
}
