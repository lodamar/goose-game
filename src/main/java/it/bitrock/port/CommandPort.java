package it.bitrock.port;

public interface CommandPort {
    String nextCommand();
    void printResult(String result);
}
