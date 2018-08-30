package it.bitrock.service;

import org.junit.Before;
import org.junit.Test;

import static it.bitrock.model.Command.ADD_PLAYER;
import static it.bitrock.model.FixedResponse.*;
import static org.assertj.core.api.Assertions.assertThat;

public class AddPlayerCommandTest {
    private static final String PIPPO = "Pippo";
    private static final String PLUTO = "Pluto";
    private System system;

    @Before
    public void setUp() {
        system = new System();
    }

    @Test
    public void shouldRefuseUnrecognizedCommands() {
        assertThat(system.processCommand("invalid command")).isEqualTo(UNRECOGNIZED_COMMAND.message());
    }

    @Test
    public void shouldRefuseMissingName() {
        assertThat(system.processCommand(ADD_PLAYER.command())).isEqualTo(MISSING_NAME.message());
    }

    @Test
    public void shouldRefuseEmptyName() {
        assertThat(system.processCommand(ADD_PLAYER.command() + "     ")).isEqualTo(MISSING_NAME.message());
    }

    @Test
    public void shouldRefuseDuplicatePlayers() {
        system.processCommand(ADD_PLAYER.command() + PIPPO);
        assertThat(system.processCommand(ADD_PLAYER.command() + PIPPO)).isEqualTo(PIPPO + ALREADY_EXISTING_PLAYER.message());
    }

    @Test
    public void shouldAddPlayer() {
        assertThat(system.processCommand(ADD_PLAYER.command() + PIPPO)).isEqualTo("players: "+PIPPO);
    }

    @Test
    public void shouldAddMultiplePlayers() {
        system.processCommand(ADD_PLAYER.command() + PIPPO);
        assertThat(system.processCommand(ADD_PLAYER.command() + PLUTO)).isEqualTo("players: "+PIPPO+", "+PLUTO);
    }

}