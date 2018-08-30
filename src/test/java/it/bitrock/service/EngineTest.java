package it.bitrock.service;

import it.bitrock.doubles.CommandAdapterDouble;
import org.junit.Before;
import org.junit.Test;

import static java.util.Arrays.asList;

public class EngineTest {
    private Engine engine;
    private CommandAdapterDouble commandAdapterDouble;

    @Before
    public void setUp() {
        commandAdapterDouble = new CommandAdapterDouble();
        engine = new Engine(commandAdapterDouble);
    }

    @Test(timeout = 2_000L)
    public void shouldCompleteAGame() {
        commandAdapterDouble.addPlayers(asList("Pippo", "Pluto", "Paperoga", "Gastone"));
        engine.play();
    }
}
