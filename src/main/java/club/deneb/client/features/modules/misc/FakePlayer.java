package club.deneb.client.features.modules.misc;

import club.deneb.client.features.Category;
import club.deneb.client.features.Module;
import club.deneb.client.value.BooleanValue;
import club.deneb.client.value.IntValue;
import club.deneb.client.value.ModeValue;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.potion.PotionEffect;

import java.util.UUID;

@Module.Info(name = "FakePlayer",category = Category.MISC)
public class FakePlayer extends Module {

    public static String customName = "None";
    IntValue health = setting("Health",10,0,36);
    BooleanValue customMode = setting("CustomName",false);
    ModeValue mode = setting("Name",new ModeValue.Mode("B_312",true),
                new ModeValue.Mode("popbob"),
                new ModeValue.Mode("jared2013"),
                new ModeValue.Mode("bachi"),
                new ModeValue.Mode("dot5"),
                new ModeValue.Mode("FitMC"),
                new ModeValue.Mode("Cyri")).r(customMode);

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
