package it.bitrock.service;

import it.bitrock.model.Player;

import java.util.*;
import java.util.stream.Collectors;

class Game {
    static final int FINAL_SPACE = 63;
    static final int THE_BRIDGE_START = 6;
    static final int THE_BRIDGE_END = 12;
    private static final int[] GOOSE_SPACES = { 5, 9, 14, 18, 23, 27 };

    private final Set<Player> players = new HashSet<>();
    private String winningPlayer;

    boolean addPlayer(Player player) {
        return players.add(player);
    }

    Set<Player> getPlayers() {
        return Collections.unmodifiableSet(players);
    }

    Optional<Player> playerFromName(String name) {
        return players
                .stream()
                .filter(player -> player.getName().equals(name))
                .findFirst();
    }

    Set<String> playersNames() {
        return players
                .stream()
                .map(Player::getName)
                .collect(Collectors.toSet());
    }

    boolean isGooseSpace(int position) {
        return Arrays.stream(GOOSE_SPACES)
                .anyMatch(gooseSpace -> gooseSpace == position);
    }

    Optional<Player> searchAnotherPlayerInPosition(int position, Player currentPlayer) {
        return players.stream()
                .filter(player -> !player.equals(currentPlayer))
                .filter(player -> player.getPosition() == position)
                .findFirst();
    }

    String getWinningPlayer() {
        return winningPlayer;
    }

    void setWinningPlayer(String winningPlayer) {
        this.winningPlayer = winningPlayer;
    }
}
