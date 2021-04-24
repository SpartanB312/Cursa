package club.deneb.client.features.modules.misc

import net.minecraft.util.math.BlockPos
import club.deneb.client.features.Category
import club.deneb.client.features.Module
import net.minecraft.tileentity.TileEntityChest
import club.deneb.client.utils.ChatUtil
import net.minecraft.tileentity.TileEntityEnderChest
import net.minecraft.tileentity.TileEntityShulkerBox
import net.minecraft.entity.passive.EntityDonkey
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.client.event.InputUpdateEvent
import java.io.File
import java.io.IOException
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.function.Consumer

@Module.Info(name = "StashFinder",
    description = "Automatically log the storage around you",
    category = Category.MISC
)
class StashFinder : Module() {

    private val chest = setting("Chests", true)
    private val shulker = setting("Shulker boxes", true)
    private val donkey = setting("Donkey", true)
    private val enderChest = setting("Ender Chest", true)
    private val autoWalk = setting("AutoWalk", false)
    private val chat = setting("Send Message", false)

    private val chests = ArrayList<BlockPos>()
    private val shulkers = ArrayList<BlockPos>()
    private val enderChests = ArrayList<BlockPos>()
    private val donkeys = ArrayList<BlockPos>()
    override fun onTick() {
        for (tileEntity in mc.world.loadedTileEntityList) {
            val pos = tileEntity.pos
            if (tileEntity is TileEntityChest && !chests.contains(pos) && chest.value) {
                chests.add(pos)
                if (chat.value) {
                    ChatUtil.printChatMessage("Found a chest at $pos")
                }
            }
            if (tileEntity is TileEntityEnderChest && !enderChests.contains(pos) && enderChest.value) {
                enderChests.add(pos)
                if (chat.value) {
                    ChatUtil.printChatMessage("Found a ender chest at $pos")
                }
            }
            if (tileEntity is TileEntityShulkerBox && !shulkers.contains(pos) && shulker.value) {
                shulkers.add(pos)
                if (chat.value) {
                    ChatUtil.printChatMessage("Found a shulker box at $pos")
                }
            }
        }
        if (donkey.value) for (entity in mc.world.loadedEntityList) {
            val pos2 = entity.position
            if (entity is EntityDonkey && !donkeys.contains(pos2)) {
                donkeys.add(pos2)
                if (chat.value) {
                    ChatUtil.printChatMessage("Found a donkey at $pos2")
                }
            }
        }
    }

    @SubscribeEvent
    fun onInputUpdateEvent(event: InputUpdateEvent) {
        if (autoWalk.value && mc.player != null) {
            event.movementInput.moveForward = 1f
        }
    }

    private val world: String
        get() {
            val world = mc.player.dimension
            if (world == -1) {
                return " [Nether] "
            }
            if (world == 0) {
                return " [OverWorld] "
            }
            return if (world == 1) {
                " [End] "
            } else " Null "
        }

    override fun onDisable() {
        try {
            val df: DateFormat = SimpleDateFormat("yy-MM-dd")
            val df2: DateFormat = SimpleDateFormat("HH-mm")
            val date = Date()
            val date2 = Date()
            val file = File("Deneb/stashfinder/" + df.format(date) + " " + df2.format(date2) + ".txt")
            if (!file.exists()) {
                file.parentFile.mkdirs()
                try {
                    file.createNewFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            val fop = FileOutputStream(file)
            val writer = OutputStreamWriter(fop)
            if (chest.value) {
                chests.forEach(Consumer { blockPos: BlockPos ->
                    val pos = "X" + blockPos.getX() + ", Y" + blockPos.getY() + ", Z" + blockPos.getZ()
                    try {
                        writer.append("In the ").append(
                            if (mc.isSingleplayer) "Single Player" else "Server" + Objects.requireNonNull(
                                mc.getCurrentServerData()
                            )!!.serverIP
                        ).append(
                            world
                        ).append(" chest at ").append(pos).append("\n")
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                })
            }
            if (enderChest.value) {
                enderChests.forEach(Consumer { blockPos: BlockPos ->
                    val pos = "X" + blockPos.getX() + ", Y" + blockPos.getY() + ", Z" + blockPos.getZ()
                    try {
                        writer.append("In the ").append(
                            if (mc.isSingleplayer) "Single Player" else "Server" + Objects.requireNonNull(
                                mc.getCurrentServerData()
                            )!!.serverIP
                        ).append(
                            world
                        ).append(" ender chest at ").append(pos).append("\n")
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                })
            }
            if (shulker.value) {
                shulkers.forEach(Consumer { blockPos: BlockPos ->
                    val pos = "X" + blockPos.getX() + ", Y" + blockPos.getY() + ", Z" + blockPos.getZ()
                    try {
                        writer.append("In the ").append(
                            if (mc.isSingleplayer) "Single Player" else "Server" + Objects.requireNonNull(
                                mc.getCurrentServerData()
                            )!!.serverIP
                        ).append(
                            world
                        ).append(" shulker box at ").append(pos).append("\n")
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                })
            }
            if (donkey.value) {
                donkeys.forEach(Consumer { blockPos: BlockPos ->
                    val pos = "X" + blockPos.getX() + ", Y" + blockPos.getY() + ", Z" + blockPos.getZ()
                    try {
                        writer.append("In the ").append(
                            if (mc.isSingleplayer) "Single Player" else "Server" + Objects.requireNonNull(
                                mc.getCurrentServerData()
                            )!!.serverIP
                        ).append(
                            world
                        ).append(" donkey at ").append(pos).append("\n")
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                })
            }
            writer.close()
            fop.close()
            ChatUtil.sendNoSpamMessage("File output succeed")
        } catch (e: IOException) {
            ChatUtil.sendNoSpamMessage("File output exception:$e")
        } finally {
            chests.clear()
            donkeys.clear()
            shulkers.clear()
            enderChests.clear()
        }
    }
}