package com.deneb.client.features.modules.misc;

import com.deneb.client.features.Category;
import com.deneb.client.features.Module;
import com.deneb.client.value.BValue;
import com.deneb.client.value.IValue;
import com.deneb.client.value.MValue;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.potion.PotionEffect;

import java.util.UUID;

@Module.Info(name = "FakePlayer",category = Category.MISC)
public class FakePlayer extends Module {

    public static String customName = "None";
    IValue health = setting("Health",10,0,36);
    BValue customMode = setting("CustomName",false);
    MValue mode = setting("Name",new MValue.Mode("B_312",true),
                new MValue.Mode("popbob"),
                new MValue.Mode("jared2013"),
                new MValue.Mode("bachi"),
                new MValue.Mode("dot5"),
                new MValue.Mode("FitMC"),
                new MValue.Mode("Cyri")).v(v -> !customMode.getValue());

    @Override
    public void onEnable() {
        if(mc.player == null || mc.world == null) return;
        EntityOtherPlayerMP fakePlayer = new EntityOtherPlayerMP(mc.world, new GameProfile(UUID.fromString("60569353-f22b-42da-b84b-d706a65c5ddf"), customMode.getValue() ? customName : mode.getToggledMode().getName()));
        fakePlayer.copyLocationAndAnglesFrom(mc.player);
        for (PotionEffect potionEffect : mc.player.getActivePotionEffects()) {
            fakePlayer.addPotionEffect(potionEffect);
        }
        fakePlayer.setHealth(health.getValue());
        fakePlayer.inventory.copyInventory(mc.player.inventory);
        fakePlayer.rotationYawHead = mc.player.rotationYawHead;
        mc.world.addEntityToWorld(-100, fakePlayer);
    }

    @Override
    public void onDisable() {
        mc.world.removeEntityFromWorld(-100);
    }

}
