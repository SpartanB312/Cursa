package net.spartanb312.cursa.event.events.client;

import net.spartanb312.cursa.event.CursaEvent;

public final class KeyEvent extends CursaEvent {

    private final int key;
    private final char character;

    public KeyEvent(int key, char character) {
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