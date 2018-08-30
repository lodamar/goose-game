package it.bitrock.model;

public enum FixedResponse {
    UNRECOGNIZED_COMMAND("Unrecognized command"),
    MISSING_NAME("Missing name"),
    ALREADY_EXISTING_PLAYER(": already existing player"),
    NOT_EXISTING_PLAYER(": not existing player"),
    INVALID_DIE_ROLL("Invalid dice roll");

    private final String message;

    FixedResponse(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
