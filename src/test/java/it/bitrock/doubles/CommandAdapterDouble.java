package it.bitrock.doubles;

import it.bitrock.port.CommandPort;

import java.util.ArrayList;
import java.util.List;

public class CommandAdapterDouble implements CommandPort {
    private final List<String> players = new ArrayList<>();
    private int addedPlayers;
    private int currentPlayerIndex;

    @Override
    public String nextCommand() {
        if (players.size() > addedPlayers) {
            return "add player " + players.get(addedPlayers++);
        } else {
            updateIndex();
            return "move " + players.get(currentPlayerIndex);
        }
    }

    @Override
    public void printResult(String result) {
        // a logger should be better but this is just pure exercise
        System.out.println(result);
    }

    public void addPlayers(List<String> players) {
        this.players.addAll(players);
    }

    private void updateIndex() {
        currentPlayerIndex++;
        if(currentPlayerIndex >= players.size()) currentPlayerIndex = 0;
    }
}
