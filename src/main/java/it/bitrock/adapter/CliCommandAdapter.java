package it.bitrock.adapter;

import it.bitrock.port.CommandPort;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CliCommandAdapter implements CommandPort, AutoCloseable {
    private final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public String nextCommand() {
        try {
            return bufferedReader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void printResult(String result) {
        System.out.println(result);
    }


    @Override
    public void close() throws Exception {
        if(bufferedReader != null) {
            bufferedReader.close();
        }
    }
}
