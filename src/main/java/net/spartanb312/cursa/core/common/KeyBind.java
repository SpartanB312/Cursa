package net.spartanb312.cursa.core.common;

import net.spartanb312.cursa.core.concurrent.task.VoidTask;

public class KeyBind {

    private int keyCode;
    private final VoidTask action;

    public KeyBind(int keyCode, VoidTask action) {
        this.keyCode = keyCode;
        this.action = action;
    }

    public void test(int keyCode) {
        if (this.keyCode == keyCode) action.invoke();
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }

    public int getKeyCode() {
        return keyCode;
    }

}
