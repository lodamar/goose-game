package it.bitrock;

import it.bitrock.adapter.CliCommandAdapter;
import it.bitrock.service.Engine;

public class GooseGame {
    public static void main(String[] args) throws Exception {
        try(CliCommandAdapter cliCommandAdapter = new CliCommandAdapter()) {
            Engine engine = new Engine(cliCommandAdapter);
            engine.play();
        }
    }
}
