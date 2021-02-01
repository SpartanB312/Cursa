package club.deneb.client.features.modules.render;

import club.deneb.client.event.events.render.RenderEvent;
import club.deneb.client.features.Category;
import club.deneb.client.features.Module;
import club.deneb.client.utils.DenebTessellator;
import club.deneb.client.utils.GeometryMasks;
import club.deneb.client.utils.MathUtil;
import club.deneb.client.value.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

import static org.lwjgl.opengl.GL11.GL_QUADS;

@Module.Info(name = "HoleESP", category = Category.RENDER)
public class HoleESP extends Module {

    IntValue count = setting("Hole Count", 10, 0, 50);
    DoubleValue range = setting("Range", 6D, 0D, 15D);
    BooleanValue hideOwn = setting("HideOwn", false);
    BooleanValue low = setting("LowHole", false);
    ModeValue mode = setting("HoleMode", new ModeValue.Mode("Solid", true), new ModeValue.Mode("SolidFlat", false), new ModeValue.Mode("Full", false), new ModeValue.Mode("Outline", false));
    IntValue red = setting("BedrockRed", 130, 0, 255);
    IntValue green = setting("BedrockGreen", 104, 0, 255);
    IntValue blue = setting("BedrockBlue", 0, 0, 255);
    IntValue obbyred = setting("ObbyRed", 255, 0, 255);
    IntValue obbygreen = setting("ObbyGreen", 0, 0, 255);
    IntValue obbyblue = setting("ObbyBlue", 0, 0, 255);
    IntValue alpha = setting("Alpha", 39, 0, 255);
    FloatValue iWidth = setting("Width", 1.0f, 0.1f, 10.0f);

    private ArrayList<BlockPos> safeHoles;
    private ArrayList<BlockPos> unsafeHoles;

    @Override
    public void onTick() {
        int holecount = 0;
        if (safeHoles == null) {
            safeHoles = new ArrayList<>();
        } else {
            safeHoles.clear();
        }

        if (unsafeHoles == null) {
            unsafeHoles = new ArrayList<>();
        } else {
            unsafeHoles.clear();
        }

        final Iterable<BlockPos> blocks = BlockPos.getAllInBox(mc.player.getPosition().add(-(range.getValue()), -6, -(range.getValue())), mc.player.getPosition().add(range.getValue(), 2, range.getValue()));
        for (final BlockPos pos : blocks) {
            if (!mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR)
                    || !mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals(Blocks.AIR)
                    || !mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals(Blocks.AIR)
                    || this.hideOwn.getValue() && pos.equals(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ))) {
                continue;
            }
            if (!mc.world.getBlockState(pos).getMaterial().blocksMovement() && !mc.world.getBlockState(pos.add(0, 1, 0)).getMaterial().blocksMovement()) {


                //final boolean doubleHole = (mc.world.getBlockState(pos.add(0, -1, 0)).getBlock() == Blocks.BEDROCK | mc.world.getBlockState(pos.add(0, -1, 0)).getBlock() == Blocks.OBSIDIAN) &&
                final boolean solidNeighbours = (mc.world.getBlockState(pos.add(0, -1, 0)).getBlock() == Blocks.BEDROCK | mc.world.getBlockState(pos.add(0, -1, 0)).getBlock() == Blocks.OBSIDIAN) && (mc.world.getBlockState(pos.add(1, 0, 0)).getBlock() == Blocks.BEDROCK | mc.world.getBlockState(pos.add(1, 0, 0)).getBlock() == Blocks.OBSIDIAN) && (mc.world.getBlockState(pos.add(0, 0, 1)).getBlock() == Blocks.BEDROCK | mc.world.getBlockState(pos.add(0, 0, 1)).getBlock() == Blocks.OBSIDIAN) && (mc.world.getBlockState(pos.add(-1, 0, 0)).getBlock() == Blocks.BEDROCK | mc.world.getBlockState(pos.add(-1, 0, 0)).getBlock() == Blocks.OBSIDIAN) && (mc.world.getBlockState(pos.add(0, 0, -1)).getBlock() == Blocks.BEDROCK | mc.world.getBlockState(pos.add(0, 0, -1)).getBlock() == Blocks.OBSIDIAN) && mc.world.getBlockState(pos.add(0, 0, 0)).getMaterial() == Material.AIR && mc.world.getBlockState(pos.add(0, 1, 0)).getMaterial() == Material.AIR && mc.world.getBlockState(pos.add(0, 2, 0)).getMaterial() == Material.AIR;
                final boolean obiNeighbours = (mc.world.getBlockState(pos.add(0, -1, 0)).getBlock() == Blocks.OBSIDIAN | mc.world.getBlockState(pos.add(1, 0, 0)).getBlock() == Blocks.OBSIDIAN | mc.world.getBlockState(pos.add(0, 0, 1)).getBlock() == Blocks.OBSIDIAN | mc.world.getBlockState(pos.add(-1, 0, 0)).getBlock() == Blocks.OBSIDIAN | mc.world.getBlockState(pos.add(0, 0, -1)).getBlock() == Blocks.OBSIDIAN) && mc.world.getBlockState(pos.add(0, 0, 0)).getMaterial() == Material.AIR && mc.world.getBlockState(pos.add(0, 1, 0)).getMaterial() == Material.AIR && mc.world.getBlockState(pos.add(0, 2, 0)).getMaterial() == Material.AIR;
                final boolean bedNeighbours = mc.world.getBlockState(pos.add(0, -1, 0)).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.add(1, 0, 0)).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.add(0, 0, 1)).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.add(-1, 0, 0)).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.add(0, 0, -1)).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.add(0, 0, 0)).getMaterial() == Material.AIR && mc.world.getBlockState(pos.add(0, 1, 0)).getMaterial() == Material.AIR && mc.world.getBlockState(pos.add(0, 2, 0)).getMaterial() == Material.AIR;
                if (!solidNeighbours || holecount >= count.getValue()) {
                    continue;
                }
                if (obiNeighbours){
                    this.unsafeHoles.add(pos);
                }
                if (bedNeighbours) {
                    this.safeHoles.add(pos);
                }
                holecount++;
            }
        }
    }

    @Override
    public void onWorldRender(RenderEvent event) {
        if (!(safeHoles == null) && !safeHoles.isEmpty()) {
            safeHoles.sort(Comparator.comparingDouble(pos -> mc.player.getDistance(pos.x, pos.y, pos.z)));
            int color = new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()).getRGB();
            for (BlockPos hole : safeHoles) {
                BlockPos GreenHolePos = hole;
                if (low.getValue()){
                    GreenHolePos = new BlockPos(hole.getX(), hole.getY() - 1, hole.getZ());
                }
                drawBlock(GreenHolePos, iWidth.getValue(), color);
            }
        }
        if (!(unsafeHoles == null) && !unsafeHoles.isEmpty()) {
            int color = new Color(this.obbyred.getValue(), this.obbygreen.getValue(), this.obbyblue.getValue(), this.alpha.getValue()).getRGB();
            for (BlockPos hole : unsafeHoles) {
                BlockPos RedHolePos = hole;
                if (low.getValue()) {
                    RedHolePos = new BlockPos(hole.getX(), hole.getY() - 1, hole.getZ());
                }
                drawBlock(RedHolePos, iWidth.getValue(), color);
            }
        }
    }


    private void drawBlock(BlockPos blockPos, float iWidth, int color) {
        if (mode.getMode("Solid").isToggled() || mode.getMode("SolidFlat").isToggled()) {
            DenebTessellator.prepare(GL_QUADS);
            if (mode.getMode("SolidFlat").isToggled()) {
                DenebTessellator.drawBox(blockPos, color, GeometryMasks.Quad.UP);
            } else if (mode.getMode("Solid").isToggled()) {
                DenebTessellator.drawBox(blockPos, color, GeometryMasks.Quad.ALL);
            }
            DenebTessellator.release();
        } else {
            IBlockState iBlockState = mc.world.getBlockState(blockPos);
            Vec3d interp = MathUtil.interpolateEntity(mc.player, mc.getRenderPartialTicks());
            if (mode.getMode("Full").isToggled()) {
                DenebTessellator.drawFullBox(iBlockState.getSelectedBoundingBox(mc.world, blockPos).grow(0.0020000000949949026).offset(-interp.x, -interp.y, -interp.z), iWidth, color);
            } else if (mode.getMode("Outline").isToggled()) {
                DenebTessellator.drawBoundingBox(iBlockState.getSelectedBoundingBox(mc.world, blockPos).grow(0.0020000000949949026).offset(-interp.x, -interp.y, -interp.z), iWidth, color);
            }
        }
    }

    @Override
    public void onDisable() {
        safeHoles.clear();
        unsafeHoles.clear();
    }


}