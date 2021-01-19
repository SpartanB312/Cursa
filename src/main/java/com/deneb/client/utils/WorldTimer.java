package com.deneb.client.utils;

public class WorldTimer {
    private float OverrideSpeed = 1.0f;

    private void useTimer() {
        if (OverrideSpeed != 1.0f && OverrideSpeed > 0.1f) {
            Wrapper.mc.timer.tickLength = 50.0f / OverrideSpeed;
        }
    }

    public void SetOverrideSpeed(float f) {
        OverrideSpeed = f;
        useTimer();
    }

    public void resetTime() {
        Wrapper.mc.timer.tickLength = 50;
    }
}
