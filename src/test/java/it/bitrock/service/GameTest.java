package it.bitrock.service;

import it.bitrock.model.Player;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GameTest {
    private Game game;

    @Before
    public void setUp() {
        game = new Game();
    }

    @Test
    public void shouldAddSinglePlayer() {
        Player player = new Player("player1");
        assertThat(game.addPlayer(player)).isTrue();

        assertThat(game.getPlayers())
                .hasSize(1)
                .containsExactly(player);
    }

    @Test
    public void shouldAddMultiplePlayers() {
        Player player1 = new Player("player1");
        assertThat(game.addPlayer(player1)).isTrue();

        Player player2 = new Player("player2");
        assertThat(game.addPlayer(player2)).isTrue();

        Player player3 = new Player("player3");
        assertThat(game.addPlayer(player3)).isTrue();

        assertThat(game.getPlayers())
                .hasSize(3)
                .containsExactlyInAnyOrder(player1, player2, player3);
    }

    @Test
    public void ifAlreadyPresentShouldReturnFalse() {
        Player player1 = new Player("player1");
        assertThat(game.addPlayer(player1)).isTrue();

        assertThat(game.addPlayer(new Player("player1"))).isFalse();

        assertThat(game.getPlayers())
                .hasSize(1)
                .containsExactly(player1);
    }

}