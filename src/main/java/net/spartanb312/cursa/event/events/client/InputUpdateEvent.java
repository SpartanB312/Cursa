package net.spartanb312.cursa.event.events.client;

public final class InputUpdateEvent {

    private final int key;
    private final char character;

    public InputUpdateEvent(int key, char character) {
        this.key = key;
        this.character = character;
    }

    public final int getKey() {
        return this.key;
    }

    public final char getCharacter() {
        return this.character;
    }

}