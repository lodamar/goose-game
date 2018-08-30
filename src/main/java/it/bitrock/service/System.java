package it.bitrock.service;

import it.bitrock.model.Command;
import it.bitrock.model.Player;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static it.bitrock.model.FixedResponse.*;
import static it.bitrock.service.Game.*;

class System {
    private Game game;

    System() {
        this.game = new Game();
    }

    String processCommand(String command) {
        if (command.startsWith(Command.ADD_PLAYER.command())) {
            return processAddPlayerCommand(command);
        } else if (command.startsWith(Command.MOVE_PLAYER.command())) {
            return processMovePlayerCommand(command);
        }

        return UNRECOGNIZED_COMMAND.message();
    }
    
    boolean gameInProgress() {
        return game.getWinningPlayer() == null;
    } 
    
    String winningPlayer() {
        return game.getWinningPlayer();
    }

    private String processAddPlayerCommand(String command) {
        String name = command.substring(Command.ADD_PLAYER.command().length());
        if(name.trim().isEmpty()) {
            return MISSING_NAME.message();
        }

        return addPlayer(name);
    }

    private String processMovePlayerCommand(String command) {
        String commandArgs = command.substring(Command.MOVE_PLAYER.command().length());
        int nameEndIndex = commandArgs.indexOf(" ");

        boolean noDiceRolls = nameEndIndex == -1;

        String name = noDiceRolls ? commandArgs : commandArgs.substring(0, nameEndIndex);
        if(name.isEmpty()) {
            return MISSING_NAME.message();
        }

        if(noDiceRolls) {
            return rollDiceAndMovePlayer(name);
        }

        String diceRolls = commandArgs.substring(nameEndIndex);

        String[] dice = diceRolls.split(",");
        if (dice.length != 2) {
            return INVALID_DIE_ROLL.message();
        }

        try {
            int firstDie = Integer.parseInt(dice[0].trim());
            int secondDie = Integer.parseInt(dice[1].trim());

            return movePlayer(name, firstDie, secondDie);
        } catch (NumberFormatException e) {
            return INVALID_DIE_ROLL.message();
        }
    }

    private String addPlayer(String name) {
        boolean alreadyPresent = !game.addPlayer(new Player(name));
        if(alreadyPresent) {
            return name + ALREADY_EXISTING_PLAYER.message();
        }

        return "players: " + String.join(", ", game.playersNames());
    }

    private String rollDiceAndMovePlayer(String name) {
        return movePlayer(name, rollDie(), rollDie());
    }

    private String movePlayer(String name, int firstDie, int secondDie) {
        return game.playerFromName(name)
                .map(player -> moveResponseMessage(player, firstDie, secondDie))
                .orElse(name + NOT_EXISTING_PLAYER.message());
    }

    private String moveResponseMessage(Player player, int firstDie, int secondDie) {
        if(firstDie < 1 || secondDie < 1 || firstDie > 6 || secondDie > 6) {
            return INVALID_DIE_ROLL.message();
        }

        int startingPosition = player.getPosition();
        int finalPosition = startingPosition + firstDie + secondDie;
        player.setPosition(finalPosition);

        return successfulMove(player, firstDie, secondDie, startingPosition, finalPosition);
    }

    private String successfulMove(Player player, int firstDie, int secondDie, int startingPosition, int finalPosition) {
        String name = player.getName();

        String baseMessage = name + " rolls " + firstDie + ", " + secondDie + ". " + name +
                " moves from " + startingPosition(startingPosition) + " to " + finalPosition(finalPosition);

        return finalPositionAdditionalRules(player, firstDie + secondDie, startingPosition, baseMessage);
    }

    private String finalPositionAdditionalRules(Player player, int sumOfDice, int startingPosition, String baseMessage) {
        int finalPosition = player.getPosition();
        String name = player.getName();

        if(finalPosition == FINAL_SPACE) {
            game.setWinningPlayer(name);
            return baseMessage + ". " + name + " Wins!!";
        }

        if(finalPosition == THE_BRIDGE_START) {
            player.setPosition(THE_BRIDGE_END);
            String bridgeMessage = baseMessage + ". " + name + " jumps to " + THE_BRIDGE_END;
            return finalPositionAdditionalRules(player, sumOfDice, startingPosition, bridgeMessage);
        }

        if(finalPosition > FINAL_SPACE) {
            int bouncePosition = FINAL_SPACE - (finalPosition - FINAL_SPACE);
            player.setPosition(bouncePosition);
            String bounceMessage = baseMessage + ". " + name + " bounces! " + name + " returns to " + bouncePosition;
            return finalPositionAdditionalRules(player, sumOfDice, startingPosition, bounceMessage);
        }

        if(game.isGooseSpace(finalPosition)) {
            int newFinalPosition = finalPosition + sumOfDice;
            player.setPosition(newFinalPosition);

            String gooseMessage = baseMessage + ". " + name + " moves again and goes to " + finalPosition(newFinalPosition);
            return finalPositionAdditionalRules(player, sumOfDice, startingPosition, gooseMessage);
        }

        Optional<Player> playerInPosition = game.searchAnotherPlayerInPosition(finalPosition, player);
        if(playerInPosition.isPresent()) {
            Player prankedPlayer = playerInPosition.get();
            prankedPlayer.setPosition(startingPosition);
            return baseMessage + ". On " + finalPosition + " there is " + prankedPlayer.getName() + ", who returns to " + finalPosition(startingPosition);
        }

        return baseMessage;
    }

    private String finalPosition(int finalPosition) {
        if (finalPosition > FINAL_SPACE) return String.valueOf(FINAL_SPACE);
        else if (finalPosition == THE_BRIDGE_START) return "The Bridge";
        else if (finalPosition == 0) return "Start";
        else if (game.isGooseSpace(finalPosition)) return finalPosition+", The Goose";
        else return String.valueOf(finalPosition);
    }

    private String startingPosition(int startingPosition) {
        return startingPosition == 0 ? "Start" : String.valueOf(startingPosition);
    }

    private int rollDie() {
        return ThreadLocalRandom.current().nextInt(1, 7);
    }
}
