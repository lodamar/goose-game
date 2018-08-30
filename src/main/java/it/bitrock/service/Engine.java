package it.bitrock.service;

import it.bitrock.port.CommandPort;

public class Engine {
    private final CommandPort commandPort;
    private final System system;

    public Engine(CommandPort commandPort) {
        this.commandPort = commandPort;
        this.system = new System();
    }

    public void play() {
        commandPort.printResult("Welcome to the Goose Game, have fun!");
        while(system.gameInProgress()) {
            issueCommand();
        }
        commandPort.printResult("Player " + system.winningPlayer() + " wins the game!");
    }

    private void issueCommand() {
        String command = commandPort.nextCommand();
        commandPort.printResult(system.processCommand(command));
    }
}
