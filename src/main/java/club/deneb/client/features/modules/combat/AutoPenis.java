package club.deneb.client.features.modules.combat;

import club.deneb.client.event.events.client.PacketEvent;
import club.deneb.client.features.Category;
import club.deneb.client.features.Module;
import club.deneb.client.utils.Wrapper;
import club.deneb.client.value.Value;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;
import java.util.List;

/**
 * Created by B_312 on 01/03/21
 */
@Module.Info(name = "AutoPenis",category = Category.COMBAT)
public class AutoPenis extends Module {

    private static double yaw;
    private static double pitch;
    private static boolean isSpoofingAngles;

    Value<Integer> tickDelay = setting("TickDelay",15,0,100);
    Value<Boolean> looking = setting("LookingMode",true);

    public static Minecraft mc = Minecraft.getMinecraft();
    public List<Block> emptyBlocks = Arrays.asList(Blocks.AIR, Blocks.FLOWING_LAVA, Blocks.LAVA, Blocks.FLOWING_WATER, Blocks.WATER, Blocks.VINE, Blocks.SNOW_LAYER, Blocks.TALLGRASS, Blocks.FIRE);
    int obiIndex;
    int placeTick = 1;
    BlockPos origin;
    BlockPos hopperPos;
    EnumFacing horizontalFace;
    Vec3d vec;

    @Override
    public void onEnable() {
        if (mc == null) {
            return;
        }
        resetRotation();
        this.obiIndex = -1;
        this.placeTick = 1;
        if (mc.player != null && mc.objectMouseOver != null) {
            if (!looking.getValue()) {
                for (int cap2 = -2; cap2 <= 2; ++cap2) {
                    for (int y = -1; y <= 2; ++y) {
                        for (int x = -2; x <= 2; ++x) {
                            if ((cap2 != 0 || y != 0 || x != 0) && (cap2 != 0 || y != 1 || x != 0) && this.emptyBlocks.contains(mc.world.getBlockState(mc.player.getPosition().add(cap2, y, x)).getBlock()) && this.emptyBlocks.contains(mc.world.getBlockState(mc.player.getPosition().add(cap2, y + 1, x)).getBlock())) {
                                this.origin = new BlockPos(mc.player.getPosition().add(cap2, y - 3, x));
                            }
                        }
                    }
                }
            } else {
                this.origin = new BlockPos( mc.objectMouseOver.getBlockPos().getX(),  mc.objectMouseOver.getBlockPos().getY(),  mc.objectMouseOver.getBlockPos().getZ());
            }
            this.horizontalFace = mc.player.getHorizontalFacing();
            this.hopperPos = this.origin.offset(this.horizontalFace.getOpposite()).up();
        } else {
            toggle();
        }
    }

    @Override
    public void onTick() {
        if (mc == null) {
            return;
        }
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = Minecraft.getMinecraft().player.inventory.mainInventory.get(i);
            if (itemStack.getItem().equals(Item.getItemFromBlock(Blocks.OBSIDIAN))) {
                this.obiIndex = i;
            }
        }


        if(checkNulls()){
            vec = new Vec3d(this.origin.getX(), this.origin.getY(), this.origin.getZ());
            if(placeTick == tickDelay.getValue()){
                this.changeItem(this.obiIndex);
                this.placeBlock(this.origin, EnumFacing.UP, vec);
            }
            if(placeTick == tickDelay.getValue() * 2){
                this.changeItem(this.obiIndex);
                this.placeBlock(this.origin.add(0,0,1), EnumFacing.UP, vec);
            }
            if(placeTick == tickDelay.getValue() * 3){
                this.changeItem(this.obiIndex);
                this.placeBlock(this.origin.add(0,0,-1), EnumFacing.UP, vec);
            }
            if(placeTick == tickDelay.getValue() * 4){
                this.changeItem(this.obiIndex);
                this.placeBlock(this.origin.add(0,1,0), EnumFacing.UP, vec);
            }
            if(placeTick == tickDelay.getValue() * 5){
                this.changeItem(this.obiIndex);
                this.placeBlock(this.origin.add(0,2,0), EnumFacing.UP, vec);
            }
            if(placeTick == tickDelay.getValue() * 6){
                this.changeItem(this.obiIndex);
                this.placeBlock(this.origin.add(0,3,0), EnumFacing.UP, vec);
                toggle();
            }
            placeTick ++;
        } else {
            toggle();
        }
    }

    public boolean checkNulls() {
        return this.obiIndex != -1 && this.origin != null && this.horizontalFace != null && this.hopperPos != null;
    }

    public void changeItem(int slot) {
        mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
        mc.player.inventory.currentItem = slot;
    }

    public void placeBlock(BlockPos pos, EnumFacing facing, Vec3d vec) {
        lookAtPos(pos,facing);
        mc.playerController.processRightClickBlock(Minecraft.getMinecraft().player, Minecraft.getMinecraft().world, pos, facing, vec, EnumHand.MAIN_HAND);
        mc.player.swingArm(EnumHand.MAIN_HAND);
    }

    private void lookAtPos(BlockPos block, EnumFacing face) {
        float[] v = getRotationsBlock(block, face, false);
        setYawAndPitch(v[0], v[1]);
    }

    public static float[] getRotationsBlock(BlockPos block, EnumFacing face, boolean Legit) {
        double x = block.getX() + 0.5 - Minecraft.getMinecraft().player.posX +  (double) face.getXOffset()/2;
        double z = block.getZ() + 0.5 - Minecraft.getMinecraft().player.posZ +  (double) face.getZOffset()/2;
        double y = (block.getY() + 0.5);

        if (Legit)
            y += 0.5;

        double d1 = Minecraft.getMinecraft().player.posY +Minecraft.getMinecraft().player.getEyeHeight() - y;
        double d3 = MathHelper.sqrt(x * x + z * z);
        float yaw = (float) (Math.atan2(z, x) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) (Math.atan2(d1, d3) * 180.0D / Math.PI);

        if (yaw < 0.0F) {
            yaw += 360f;
        }
        return new float[]{yaw, pitch};
    }

    private void setYawAndPitch(float yaw1, float pitch1) {
        yaw = yaw1;
        pitch = pitch1;
        isSpoofingAngles = true;
    }


    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event){
        if (isDisabled()){
            return;
        }
        Packet<?> packet = event.getPacket();
        if (packet instanceof CPacketPlayer) {
            if (isSpoofingAngles) {
                ((CPacketPlayer) packet).yaw = (float) yaw;
                ((CPacketPlayer) packet).pitch = (float) pitch;
                isSpoofingAngles = false;
            }
        }
    }

    private static void resetRotation() {
        if (isSpoofingAngles) {
            yaw = mc.player.rotationYaw;
            pitch = mc.player.rotationPitch;
            isSpoofingAngles = false;
        }
    }


}