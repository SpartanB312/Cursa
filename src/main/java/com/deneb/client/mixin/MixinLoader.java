package com.deneb.client.mixin;

import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Map;

public class MixinLoader implements IFMLLoadingPlugin {

    public static final Logger log = LogManager.getLogger("MIXIN");

    public static File LOCATION = null;

    public MixinLoader() {
        log.info("Deneb mixins initialized");
        MixinBootstrap.init();
        Mixins.addConfigurations("mixins.deneb.json");
        MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
        log.info(MixinEnvironment.getDefaultEnvironment().getObfuscationContext());
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[] {"com.mrcrayfish.controllable.asm.ControllableTransformer"};
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        boolean isObfuscatedEnvironment = (boolean) data.get("runtimeDeobfuscationEnabled");
        if((Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment"))
        {
            try
            {
                LOCATION = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
            }
            catch(URISyntaxException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            LOCATION = (File) data.get("coremodLocation");
        }
    }


    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
