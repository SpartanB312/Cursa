package net.spartanb312.cursa.event.events.client;

import net.spartanb312.cursa.event.CursaEvent;

/**
 * We use this to launch our client
 */
public class InitializationEvent extends CursaEvent {
    public static class PreInitialize extends InitializationEvent{
    }

    public static class Initialize extends InitializationEvent{
    }

    public static class PostInitialize extends InitializationEvent{
    }
}
