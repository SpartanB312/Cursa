package club.deneb.client.features.modules.combat;

import club.deneb.client.features.modules.misc.AutoTool;
import club.deneb.client.client.FriendManager;
import club.deneb.client.features.Category;
import club.deneb.client.features.Module;
import club.deneb.client.features.ModuleManager;
import club.deneb.client.utils.EntityUtil;
import club.deneb.client.utils.LagCompensator;
import club.deneb.client.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;

/**
 * Credit : KAMI ,thank you.
 * Created by 086 on 12/12/2017.
 * Last Updated 5 August 2019 by hub
 */
@Module.Info(name = "Aura", category = Category.COMBAT, description = "Hits entities around you")
public class Aura extends Module {

    Value<Boolean> players = setting("Players", true);
    Value<Boolean> animals = setting("Animals", false);
    Value<Boolean> mobs = setting("Mobs", false);
    Value<Double> range = setting("Range", 5.5d,0.0d,10.0d);
    Value<Boolean> wait = setting("Wait", true);
    Value<Boolean> walls = setting("Walls", false);
    Value<Boolean> sharpness = setting("32k Switch", false);

    @Override
    public void onTick() {
        if (mc.player.isDead) {
            return;
        }
        boolean shield = mc.player.getHeldItemOffhand().getItem().equals(Items.SHIELD) && mc.player.getActiveHand() == EnumHand.OFF_HAND;
        if (mc.player.isHandActive() && !shield) {
            return;
        }

        if (wait.getValue()) {
            if (mc.player.getCooledAttackStrength(getLagComp()) < 1) {
                return;
            } else if (mc.player.ticksExisted % 2 != 0) {
                return;
            }
        }

        for (Entity target : Minecraft.getMinecraft().world.loadedEntityList) {
            if (!EntityUtil.isLiving(target)) {
                continue;
            }
            if (target == mc.player) {
                continue;
            }
            if (mc.player.getDistance(target) > range.getValue()) {
                continue;
            }
            if (((EntityLivingBase) target).getHealth() <= 0) {
                continue;
            }
            if (((EntityLivingBase) target).hurtTime != 0 && wait.getValue()) {
                continue;
            }
            if (!walls.getValue() && (!mc.player.canEntityBeSeen(target) && !canEntityFeetBeSeen(target))) {
                continue; // If walls is on & you can't see the feet or head of the target, skip. 2 raytraces needed
            }
            if (players.getValue() && target instanceof EntityPlayer && !FriendManager.isFriend(target.getName())) {
                attack(target);
                return;
            } else {
                if (EntityUtil.isPassive(target) ? animals.getValue() : (EntityUtil.isMobAggressive(target) && mobs.getValue())) {
                    if (ModuleManager.getModuleByName("AutoTool").isEnabled()) {
                        AutoTool.equipBestWeapon();
                    }
                    attack(target);
                    return;
                }
            }
        }
    }

    private boolean checkSharpness(ItemStack stack) {

        if (stack.getTagCompound() == null) {
            return false;
        }

        NBTTagList enchants = (NBTTagList) stack.getTagCompound().getTag("ench");

		if (enchants == null) {
			return false;
		}


        for (int i = 0; i < enchants.tagCount(); i++) {
            NBTTagCompound enchant = ((NBTTagList) enchants).getCompoundTagAt(i);
            if (enchant.getInteger("id") == 16) {
                int lvl = enchant.getInteger("lvl");
                if (lvl >= 16) {
                    return true;
                }
                break;
            }
        }

        return false;

    }

    private void attack(Entity e) {

        if (sharpness.getValue()) {

            if (!checkSharpness(mc.player.getHeldItemMainhand())) {

                int newSlot = -1;

                for (int i = 0; i < 9; i++) {
                    ItemStack stack = mc.player.inventory.getStackInSlot(i);
                    if (stack == ItemStack.EMPTY) {
                        continue;
                    }
                    if (checkSharpness(stack)) {
                        newSlot = i;
                        break;
                    }
                }

                if (newSlot != -1) {
                    mc.player.inventory.currentItem = newSlot;
                }

            }

        }

        mc.playerController.attackEntity(mc.player, e);
        mc.player.swingArm(EnumHand.MAIN_HAND);

    }

    private float getLagComp() {
        if (wait.getValue()) {
            return -(20 - LagCompensator.INSTANCE.getTickRate());
        }
        return 0.0F;
    }

    private boolean canEntityFeetBeSeen(Entity entityIn) {
        return mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posX + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(entityIn.posX, entityIn.posY, entityIn.posZ), false, true, false) == null;
    }
}