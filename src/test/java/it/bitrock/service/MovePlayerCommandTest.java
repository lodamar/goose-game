package it.bitrock.service;

import org.junit.Before;
import org.junit.Test;

import static it.bitrock.model.Command.ADD_PLAYER;
import static it.bitrock.model.Command.MOVE_PLAYER;
import static it.bitrock.model.FixedResponse.*;
import static org.assertj.core.api.Assertions.assertThat;

public class MovePlayerCommandTest {
    private static final String PIPPO = "Pippo";
    private static final String PLUTO = "Pluto";
    private System system;

    @Before
    public void setUp() {
        system = new System();
    }

    @Test
    public void shouldRefuseMissingName() {
        assertThat(system.processCommand(MOVE_PLAYER.command())).isEqualTo(MISSING_NAME.message());
    }

    @Test
    public void shouldRefuseEmptyName() {
        assertThat(system.processCommand(MOVE_PLAYER.command() + "  ")).isEqualTo(MISSING_NAME.message());
    }

    @Test
    public void shouldRefuseWrongDiceRolls() {
        assertThat(system.processCommand(MOVE_PLAYER.command() + PIPPO + " error")).isEqualTo(INVALID_DIE_ROLL.message());
        assertThat(system.processCommand(MOVE_PLAYER.command() + PIPPO + " error,")).isEqualTo(INVALID_DIE_ROLL.message());
        assertThat(system.processCommand(MOVE_PLAYER.command() + PIPPO + " error,error")).isEqualTo(INVALID_DIE_ROLL.message());
        assertThat(system.processCommand(MOVE_PLAYER.command() + PIPPO + " 1,error")).isEqualTo(INVALID_DIE_ROLL.message());
        assertThat(system.processCommand(MOVE_PLAYER.command() + PIPPO + " 1,,error")).isEqualTo(INVALID_DIE_ROLL.message());
    }

    @Test
    public void shouldRefuseMovingNotExistingPlayer() {
        assertThat(system.processCommand(MOVE_PLAYER.command() + PIPPO + " 1,3")).isEqualTo(PIPPO + NOT_EXISTING_PLAYER.message());
    }

    @Test
    public void shouldRefuseInvalidDiceRolls() {
        system.processCommand(ADD_PLAYER.command() + PIPPO);
        assertThat(system.processCommand(MOVE_PLAYER.command() + PIPPO + " 11,3")).isEqualTo(INVALID_DIE_ROLL.message());
        assertThat(system.processCommand(MOVE_PLAYER.command() + PIPPO + " 1,73")).isEqualTo(INVALID_DIE_ROLL.message());
        assertThat(system.processCommand(MOVE_PLAYER.command() + PIPPO + " -1,3")).isEqualTo(INVALID_DIE_ROLL.message());
        assertThat(system.processCommand(MOVE_PLAYER.command() + PIPPO + " 6,0")).isEqualTo(INVALID_DIE_ROLL.message());
    }

    @Test
    public void shouldMovePlayer() {
        system.processCommand(ADD_PLAYER.command() + PIPPO);
        assertThat(system.processCommand(MOVE_PLAYER.command() + PIPPO + " 1, 6")).isEqualTo(PIPPO + " rolls 1, 6. " + PIPPO + " moves from Start to 7");
        assertThat(system.processCommand(MOVE_PLAYER.command() + PIPPO + "  2,6")).isEqualTo(PIPPO + " rolls 2, 6. " + PIPPO + " moves from 7 to 15");
    }

    @Test
    public void shouldRollDiceAndMovePlayerIfDiceRollsAreMissing() {
        system.processCommand(ADD_PLAYER.command() + PIPPO);
        assertThat(system.processCommand(MOVE_PLAYER.command() + PIPPO)).isNotBlank().isNotEqualTo(INVALID_DIE_ROLL.message());
    }

    @Test
    public void shouldWinGameIfLandOnFinalSpace() {
        system.processCommand(ADD_PLAYER.command() + PIPPO);
        system.processCommand(MOVE_PLAYER.command() + PIPPO + " 6,6");
        system.processCommand(MOVE_PLAYER.command() + PIPPO + " 6,6");
        system.processCommand(MOVE_PLAYER.command() + PIPPO + " 6,6");
        system.processCommand(MOVE_PLAYER.command() + PIPPO + " 6,6");
        system.processCommand(MOVE_PLAYER.command() + PIPPO + " 6,6");
        assertThat(system.processCommand(MOVE_PLAYER.command() + PIPPO + " 1,2"))
                .isEqualTo(PIPPO + " rolls 1, 2. " + PIPPO + " moves from 60 to 63. "+ PIPPO + " Wins!!");
    }

    @Test
    public void shouldBounceBackIfGraterThanFinalSpace() {
        system.processCommand(ADD_PLAYER.command() + PIPPO);
        system.processCommand(MOVE_PLAYER.command() + PIPPO + " 6,6");
        system.processCommand(MOVE_PLAYER.command() + PIPPO + " 6,6");
        system.processCommand(MOVE_PLAYER.command() + PIPPO + " 6,6");
        system.processCommand(MOVE_PLAYER.command() + PIPPO + " 6,6");
        system.processCommand(MOVE_PLAYER.command() + PIPPO + " 6,6");
        assertThat(system.processCommand(MOVE_PLAYER.command() + PIPPO + " 4,5"))
                .isEqualTo(PIPPO + " rolls 4, 5. " + PIPPO + " moves from 60 to 63. "+ PIPPO + " bounces! " + PIPPO + " returns to 57");
    }

    @Test
    public void shouldJumpToBridgeEnd() {
        system.processCommand(ADD_PLAYER.command() + PIPPO);
        assertThat(system.processCommand(MOVE_PLAYER.command() + PIPPO + " 3,3"))
                .isEqualTo(PIPPO + " rolls 3, 3. " + PIPPO + " moves from Start to The Bridge. "+ PIPPO + " jumps to 12");
        assertThat(system.processCommand(MOVE_PLAYER.command() + PIPPO + "  2,5")).isEqualTo(PIPPO + " rolls 2, 5. " + PIPPO + " moves from 12 to 19");
    }

    @Test
    public void shouldMoveAgainIfGoose() {
        system.processCommand(ADD_PLAYER.command() + PIPPO);
        assertThat(system.processCommand(MOVE_PLAYER.command() + PIPPO + " 2,3"))
                .isEqualTo(PIPPO + " rolls 2, 3. " + PIPPO + " moves from Start to 5, The Goose. "+ PIPPO + " moves again and goes to 10");
        assertThat(system.processCommand(MOVE_PLAYER.command() + PIPPO + "  2,5")).isEqualTo(PIPPO + " rolls 2, 5. " + PIPPO + " moves from 10 to 17");
    }

    @Test
    public void shouldMoveMultipleTimeIfMultipleGoose() {
        system.processCommand(ADD_PLAYER.command() + PIPPO);
        assertThat(system.processCommand(MOVE_PLAYER.command() + PIPPO + " 6,3"))
                .isEqualTo(PIPPO + " rolls 6, 3. " + PIPPO + " moves from Start to 9, The Goose. "
                        + PIPPO + " moves again and goes to 18, The Goose. "
                        + PIPPO + " moves again and goes to 27, The Goose. "
                        + PIPPO + " moves again and goes to 36");
        assertThat(system.processCommand(MOVE_PLAYER.command() + PIPPO + "  2,5")).isEqualTo(PIPPO + " rolls 2, 5. " + PIPPO + " moves from 36 to 43");
    }

    @Test
    public void shouldSwitchPlayers() {
        system.processCommand(ADD_PLAYER.command() + PIPPO);
        system.processCommand(ADD_PLAYER.command() + PLUTO);
        system.processCommand(MOVE_PLAYER.command() + PLUTO + " 3,3");
        assertThat(system.processCommand(MOVE_PLAYER.command() + PIPPO + " 3,3")).isEqualTo(PIPPO + " rolls 3, 3. " + PIPPO + " moves from Start to The Bridge. "+ PIPPO + " jumps to 12." +
                " On 12 there is " + PLUTO + ", who returns to Start");
    }
}