package net.spartanb312.cursa.module.modules.player;

import net.spartanb312.cursa.common.annotations.ModuleInfo;
import net.spartanb312.cursa.common.annotations.Parallel;
import net.spartanb312.cursa.core.setting.Setting;
import net.spartanb312.cursa.module.Category;
import net.spartanb312.cursa.module.Module;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.potion.PotionEffect;

import java.util.UUID;

@Parallel
@ModuleInfo(name = "FakePlayer", category = Category.PLAYER, description = "Spawn a fake player entity in client side")
public class FakePlayer extends Module {

    public static String customName = "None";
    Setting<Integer> health = setting("Health", 10, 0, 36);
    Setting<Boolean> customMode = setting("CustomName", false);
    Setting<String> playerName = setting("Name", "B_312").whenFalse(customMode);

    @Override
    public void onEnable() {
        if (mc.player == null || mc.world == null) return;
        EntityOtherPlayerMP fakePlayer = new EntityOtherPlayerMP(mc.world, new GameProfile(UUID.fromString("60569353-f22b-42da-b84b-d706a65c5ddf"), customMode.getValue() ? customName : playerName.getValue()));
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

    @Override
    public String getModuleInfo() {
        return customMode.getValue() ? customName : playerName.getValue();
    }

}
