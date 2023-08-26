package net.spartanb312.cursa.client;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.spartanb312.cursa.Cursa;

/**
 * This is a mixin injection mod, but if you want to skid other forge mod's module, you can put those stuff here.
 */
@Mod(modid = Cursa.MOD_ID, name = Cursa.MOD_NAME, version = Cursa.MOD_VERSION)
public class CursaForgeMod {

    @Mod.EventHandler
    public void initialize(FMLInitializationEvent event) {
        Cursa.log.info("Cursa Forge Mod initializing...");
    }

}
