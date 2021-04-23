package club.deneb.client.features.modules.misc;

import club.deneb.client.features.Category;
import club.deneb.client.features.Module;
import club.deneb.client.utils.ChatUtil;
import club.deneb.client.value.Value;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

/**
 * Created by snowmii on 2019
 */
@Module.Info(name = "StashFinder",category = Category.MISC)
public class StashFinder extends Module {

    Value<Boolean> chest = setting("Chests", true);
    Value<Boolean> shulker = setting("Shulker boxes", true);
    Value<Boolean> donkey = setting("Donkey", true);
    Value<Boolean> enderChest = setting("Ender Chest", true);
    Value<Boolean> autowalk = setting("AutoWalk", false);
    Value<Boolean> chat = setting("Send Message", false);

    private final ArrayList<BlockPos> chests = new ArrayList<>();
    private final ArrayList<BlockPos> shulkers = new ArrayList<>();
    private final ArrayList<BlockPos> enderchests = new ArrayList<>();
    private final ArrayList<BlockPos> donkeys = new ArrayList<>();

    @Override
    public void onTick() {
        for (TileEntity tileEntity : mc.world.loadedTileEntityList) {
            BlockPos pos = tileEntity.getPos();
            if (tileEntity instanceof TileEntityChest && !chests.contains(pos) && chest.getValue()) {
                chests.add(pos);
                if (chat.getValue()) {
                    ChatUtil.printChatMessage("Found a chest at " + pos);
                }
            }
            if (tileEntity instanceof TileEntityEnderChest && !enderchests.contains(pos) && enderChest.getValue()) {
                enderchests.add(pos);
                if (chat.getValue()) {
                    ChatUtil.printChatMessage("Found a ender chest at " + pos);
                }
            }
            if (tileEntity instanceof TileEntityShulkerBox && !shulkers.contains(pos) && shulker.getValue()) {
                shulkers.add(pos);
                if (chat.getValue()) {
                    ChatUtil.printChatMessage("Found a shulker box at " + pos);
                }
            }
        }

        if (donkey.getValue())
        for (Entity entity : mc.world.loadedEntityList) {
            BlockPos pos2 = entity.getPosition();
            if(entity instanceof EntityDonkey && !this.donkeys.contains(pos2)) {
                donkeys.add(pos2);
                if (chat.getValue()) {
                    ChatUtil.printChatMessage("Found a donkey at " + pos2);
                }
            }
        }
    }

    @SubscribeEvent
    public void onInputUpdateEvent(InputUpdateEvent event){
        if (autowalk.getValue() && mc.player != null) {
            event.getMovementInput().moveForward = 1;
        }
    }


    private String getWorld(){
        int world = mc.player.dimension;
        if (world == -1){
            return  " [Nether] ";
        }
        if (world == 0){
            return  " [Overworld] ";
        }
        if (world == 1){
            return  " [End] ";
        }
        return " Null ";
    }


    @Override
    public void onDisable() {
        try {
            DateFormat df = new SimpleDateFormat("yy-MM-dd");
            DateFormat df2 = new SimpleDateFormat("HH-mm");
            Date date = new Date();
            Date date2 = new Date();
            File file = new File("Deneb/stashfinder/" + df.format(date) + " " + df2.format(date2) + ".txt");
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            FileOutputStream fop = new FileOutputStream(file);
            OutputStreamWriter writer = new OutputStreamWriter(fop);
            if (chest.getValue()) {
                chests.forEach(blockPos -> {
                    String pos = "X" + blockPos.getX() + ", Y" + blockPos.getY() + ", Z" + blockPos.getZ();
                    try {
                        writer.append("In the ").append(mc.isSingleplayer() ? "Single Player" : "Server" + Objects.requireNonNull(mc.getCurrentServerData()).serverIP).append(getWorld()).append(" chest at ").append(pos).append("\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
            if (enderChest.getValue()) {
                enderchests.forEach(blockPos -> {
                    String pos = "X" + blockPos.getX() + ", Y" + blockPos.getY() + ", Z" + blockPos.getZ();
                    try {
                        writer.append("In the ").append(mc.isSingleplayer() ? "Single Player" : "Server" + Objects.requireNonNull(mc.getCurrentServerData()).serverIP).append(getWorld()).append(" ender chest at ").append(pos).append("\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
            if (shulker.getValue()) {
                shulkers.forEach(blockPos -> {
                    String pos = "X" + blockPos.getX() + ", Y" + blockPos.getY() + ", Z" + blockPos.getZ();
                    try {
                        writer.append("In the ").append(mc.isSingleplayer() ? "Single Player" : "Server" + Objects.requireNonNull(mc.getCurrentServerData()).serverIP).append(getWorld()).append(" shulker box at ").append(pos).append("\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
            if(donkey.getValue()) {
                donkeys.forEach(blockPos -> {
                    String pos = "X" + blockPos.getX() + ", Y" + blockPos.getY() + ", Z" + blockPos.getZ();
                    try {
                        writer.append("In the ").append(mc.isSingleplayer() ? "Single Player" : "Server" + Objects.requireNonNull(mc.getCurrentServerData()).serverIP).append(getWorld()).append(" donkey at ").append(pos).append("\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
            writer.close();
            fop.close();
            ChatUtil.sendNoSpamMessage("File output succeed");
        } catch (IOException e) {
            ChatUtil.sendNoSpamMessage("File output exception:" + e);
        } finally {
            chests.clear();
            donkeys.clear();
            shulkers.clear();
            enderchests.clear();
        }
    }

}
