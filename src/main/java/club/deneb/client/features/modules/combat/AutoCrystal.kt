package club.deneb.client.features.modules.combat

import club.deneb.client.client.FriendManager.isFriend
import club.deneb.client.client.GuiManager
import club.deneb.client.event.events.client.PacketEvent
import club.deneb.client.event.events.render.RenderEvent
import club.deneb.client.event.events.render.RenderModelEvent
import club.deneb.client.features.Category
import club.deneb.client.features.Module
import club.deneb.client.features.ModuleManager
import club.deneb.client.utils.*
import club.deneb.client.utils.CrystalUtil.calculateDamage
import club.deneb.client.utils.CrystalUtil.canFacePlace
import club.deneb.client.utils.CrystalUtil.canSeeBlock
import club.deneb.client.utils.CrystalUtil.enumFacing
import club.deneb.client.utils.CrystalUtil.getVecDistance
import club.deneb.client.utils.CrystalUtil.glBillboardDistanceScaled
import club.deneb.client.utils.CrystalUtil.isEating
import club.deneb.client.utils.CrystalUtil.playerPos
import club.deneb.client.utils.Timer
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityEnderCrystal
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.projectile.EntityArrow
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.network.play.client.CPacketPlayer
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
import net.minecraft.network.play.server.SPacketSoundEffect
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.NonNullList
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.util.*
import java.util.stream.Collectors
import kotlin.math.floor

/**
 * Created by B_312 on 01/15/21
 * Updated by B_312 on 04/24/21
 */
@Module.Info(
    name = "AutoCrystal",
    category = Category.COMBAT,
    description = "Automatically place crystal to kill enemy"
)
class AutoCrystal : Module() {

    private val page = setting("Page", "General", listOf("General", "Calculation", "Render"))

    //General
    private val autoSwitch = setting("AutoSwitch", false).m(page, "General")
    private val targetPlayer = setting("Players", true).m(page, "General")
    private val targetMobs = setting("Mobs", false).m(page, "General")
    private val targetAnimals = setting("Animals", false).m(page, "General")
    private val autoPlace = setting("Place", true).m(page, "General")
    private val autoExplode = setting("Explode", true).m(page, "General")
    private val entityIgnore = setting("EntityIgnore", true).m(page, "General")
    private val multiPlace = setting("MultiPlace", false).m(page, "General")
    private val attackSpeed = setting("AttackSpeed", 35.0, 0.0, 50.0).m(page, "General")
    private val placeSpeed = setting("PlaceSpeed", 35.0, 0.0, 50.0).m(page, "General")
    private val workingDistance = setting("Distance", 7.0, 0.0, 8.0).m(page, "General")
    private val placeRange = setting("PlaceRange", 6.5, 0.0, 8.0).m(page, "General")
    private val hitRange = setting("HitRange", 5.5, 0.0, 8.0).m(page, "General")
    private val rotate = setting("Rotate", true).m(page, "General")
    private val rayTrace = setting("RayTrace", false).m(page, "General")

    //Calculation
    private val oneThirtyPlace = setting("1.13Place", false).m(page, "Calculation")
    private val wallPlace = setting("WallPlace", true).m(page, "Calculation")
    private val wallPlaceRange = setting("WallPlaceRange", 3.0, 0.0, 20.0).m(page, "Calculation")
    private val wallBreak = setting("WallBreak", true).m(page, "Calculation")
    private val wallBreakRange = setting("WallBreakRange", 3.0, 0.0, 20.0).m(page, "Calculation")
    private val noSuicide = setting("NoSuicide", true).m(page, "Calculation")
    private val facePlace = setting("FacePlace", true).m(page, "Calculation")
    private val blastHealth = setting("MinHealthFace", 10.0, 0.0, 20.0).m(page, "Calculation").b(facePlace)
    private val placeMinDmg = setting("PlaceMinDamage", 4.5, 0.0, 20.0).m(page, "Calculation")
    private val placeMaxSelf = setting("PlaceMaxSelf", 10.0, 0.0, 36.0).m(page, "Calculation")
    private val attackMode = setting("HitMode", "Smart", listOf("Smart", "Always")).m(page, "Calculation")
    private val breakMinDmg = setting("BreakMinDmg", 4.5, 0.0, 36.0).m(page, "Calculation").m(attackMode, "Smart")
    private val breakMaxSelf = setting("BreakMaxSelf", 12.0, 0.0, 36.0).m(page, "Calculation").m(attackMode, "Smart")
    private val popTotemTry = setting("PopTotemTry", true).m(page, "Calculation").m(attackMode, "Smart")
    private val ghostHand = setting("GhostHand", false).m(page, "Calculation")
    private val pauseWhileEating = setting("PauseWhileEating", false).m(page, "Calculation")
    private val clientSideConfirm = setting("ClientSideConfirm", false).m(page, "Calculation")

    //Render
    private val renderDmg = setting("RenderDamage", false).m(page, "Render")
    private val renderMode = setting("RenderBlock", "Solid", listOf("Solid", "Up", "UpLine", "Full", "Outline", "NoRender")).m(page, "Render")
    private val syncGui = setting("SyncGui", false).m(page, "Render")
    private val red = setting("Red", 255, 0, 255).m(page, "Render").r(syncGui)
    private val green = setting("Green", 0, 0, 255).m(page, "Render").r(syncGui)
    private val blue = setting("Blue", 0, 0, 255).m(page, "Render").r(syncGui)
    private val alpha = setting("Alpha", 70, 0, 255).m(page, "Render")
    private val rainbow = setting("Rainbow", false).m(page, "Render").r(syncGui)
    private val rainbowSpeed = setting("RGB Speed", 1.0f, 0.0f, 10.0f).m(page, "Render").r(syncGui)
    private val saturation = setting("Saturation", 0.65f, 0.0f, 1.0f).m(page, "Render").r(syncGui)
    private val brightness = setting("Brightness", 1.0f, 0.0f, 1.0f).m(page, "Render").r(syncGui)

    private var placements = 0
    private var oldSlot = -1
    private var prevSlot = 0
    private var switchCoolDown = false
    private var togglePitch = false
    private var renderBlock: BlockPos? = null
    private var placeTimer = Timer()
    private var breakTimer = Timer()
    private val renderBlockDmg = HashMap<BlockPos?, Double>()
    private var shouldIgnoreEntity = false
    private var yaw = 0.0
    private var pitch = 0.0
    private var renderPitch = 0.0
    private var isSpoofingAngles = false
    private var target: Entity? = null
    private var rotating = false

    @SubscribeEvent
    fun onPacketReceive(event: PacketEvent.Receive) {
        if (mc.player == null) return
        //Clear click delay
        if (mc.player.heldItemMainhand.getItem() === Items.END_CRYSTAL || mc.player.heldItemOffhand.getItem() === Items.END_CRYSTAL) {
            mc.rightClickDelayTimer = 0
        }
        //Sounds confirm makes us update world entity faster
        if (event.packet is SPacketSoundEffect) {
            val packet = event.packet as SPacketSoundEffect
            if (packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() === SoundEvents.ENTITY_GENERIC_EXPLODE) {
                for (e in Minecraft.getMinecraft().world.loadedEntityList) {
                    if (e is EntityEnderCrystal) {
                        if (e.getDistance(packet.x, packet.y, packet.z) <= 6.0f) {
                            e.setDead()
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    fun renderModelRotation(event: RenderModelEvent) {
        if (!rotate.value) return
        //Update pitch
        if (rotating) {
            event.rotating = true
            event.pitch = renderPitch.toFloat()
        }
    }

    @SubscribeEvent
    fun updateRotation(event: RenderWorldLastEvent?) {
        if (!rotate.value) return
        //Update yaw
        if (rotating) {
            mc.player.rotationYawHead = yaw.toFloat()
            mc.player.renderYawOffset = yaw.toFloat()
        }
    }

    @SubscribeEvent
    fun onPacketSend(event: PacketEvent.Send) {
        if (mc.player == null) return
        val packet = event.packet
        if (!rotate.value) {
            return
        }
        //Spoof rotation packets
        if (packet is CPacketPlayer) {
            if (isSpoofingAngles) {
                packet.yaw = yaw.toFloat()
                packet.pitch = pitch.toFloat()
                isSpoofingAngles = false
            }
        }
    }

    /**
     * Calculation of explosion
     * If return true,then we can explode it.
     */
    private fun canHitCrystal(crystal: EntityEnderCrystal): Boolean {
        if (mc.player.getDistance(crystal) > hitRange.value) return false
        if (attackMode.value == "Smart") {
            val selfDamage =
                calculateDamage(crystal.posX, crystal.posY, crystal.posZ, mc.player, mc.player.positionVector)
            if (selfDamage >= mc.player.health + mc.player.absorptionAmount) return false
            val entities = mc.world.playerEntities.stream()
                .filter { e: EntityPlayer -> mc.player.getDistance(e) <= workingDistance.value }
                .filter { e: EntityPlayer -> mc.player !== e }
                .filter { e: EntityPlayer -> !isFriend(e) }
                .sorted(Comparator.comparing { e: EntityPlayer -> mc.player.getDistance(e) })
                .collect(Collectors.toList())
            for (player in entities) {
                if (mc.player.isDead || mc.player.health + mc.player.absorptionAmount <= 0.0f) continue
                var minDamage = breakMinDmg.value
                val maxSelf = breakMaxSelf.value
                if (canFacePlace(player, blastHealth.value)) {
                    minDamage = 1.0
                }
                val target =
                    calculateDamage(crystal.posX, crystal.posY, crystal.posZ, player, player.positionVector).toDouble()
                //If we can pop enemies totem,then we try it!
                if (target > player.health + player.absorptionAmount && selfDamage < mc.player.health + mc.player.absorptionAmount && popTotemTry.value) return true
                if (target < minDamage) continue
                if (selfDamage > target) continue
                if (selfDamage > maxSelf) continue
                return true
            }
        } else return true
        return false
    }

    private fun resetRotation() {
        if (isSpoofingAngles) {
            yaw = mc.player.rotationYaw.toDouble()
            pitch = mc.player.rotationPitch.toDouble()
            isSpoofingAngles = false
            rotating = false
        }
    }

    private fun explodeCrystal(crystal: EntityEnderCrystal) {
        if (rotate.value) lookAtCrystal(crystal)
        if (breakDelayRun(attackSpeed.value)) {
            mc.playerController.attackEntity(mc.player, crystal)
            mc.player.swingArm(if (mc.player.heldItemOffhand.getItem() === Items.END_CRYSTAL) EnumHand.OFF_HAND else EnumHand.MAIN_HAND)
            if (clientSideConfirm.value) {
                //This may cause deSync!
                crystal.setDead()
            }
            mc.player.resetCooldown()
            breakTimer.reset()
        }
    }

    private fun placeCrystal(targetBlock: BlockPos, enumHand: EnumHand) {
        val facing = if (rayTrace.value) enumFacing(targetBlock) else EnumFacing.UP
        if (rotate.value) lookAtPos(targetBlock, facing)
        for (i in 0..2) {
            if (placeDelayRun(placeSpeed.value)) {
                mc.player.connection.sendPacket(
                    CPacketPlayerTryUseItemOnBlock(
                        targetBlock,
                        facing,
                        enumHand,
                        0f,
                        0f,
                        0f
                    )
                )
                placeTimer.reset()
                placements++
            }
        }
    }

    private fun placeDelayRun(speed: Double): Boolean {
        return placeTimer.passed(1000 / speed)
    }

    private fun breakDelayRun(speed: Double): Boolean {
        return breakTimer.passed(1000 / speed)
    }

    private fun findCrystalBlocks(range: Double): List<BlockPos> {
        val positions = NonNullList.create<BlockPos>()
        positions
            .addAll(
                BlockInteractionHelper.getSphere(playerPos, range.toFloat(), range.toInt(),
                    hollow = false,
                    sphere = true,
                    plus_y = 0
                )
                    .stream()
                    .filter { v: BlockPos -> canPlaceCrystal(v, oneThirtyPlace.value) }.collect(Collectors.toList())
            )
        return positions
    }

    private fun drawBlock(blockPos: BlockPos, color: Int) {
        DenebTessellator.prepare(GL11.GL_QUADS)
        if (!renderMode.toggled("NoRender")) {
            if (renderMode.toggled("Solid") || renderMode.toggled("Up")) {
                if (renderMode.toggled("Up")) {
                    DenebTessellator.drawBox(blockPos, color, GeometryMasks.Quad.UP)
                } else {
                    DenebTessellator.drawBox(blockPos, color, GeometryMasks.Quad.ALL)
                }
            } else {
                when {
                    renderMode.toggled("Full") -> {
                        DenebTessellator.drawFullBox(blockPos, 1f, color)
                    }
                    renderMode.toggled("Outline") -> {
                        DenebTessellator.drawBoundingBox(blockPos, 2f, color)
                    }
                    else -> {
                        DenebTessellator.drawBoundingBox(blockPos.add(0, 1, 0), 2f, color)
                    }
                }
            }
        }
        DenebTessellator.release()
        if (renderDmg.value) {
            if (renderBlockDmg.containsKey(blockPos)) {
                GlStateManager.pushMatrix()
                glBillboardDistanceScaled(
                    blockPos.getX().toFloat() + 0.5f,
                    blockPos.getY().toFloat() + 0.5f,
                    blockPos.getZ().toFloat() + 0.5f,
                    mc.player
                )
                val damage = renderBlockDmg[blockPos]!!
                val damageText =
                    "DMG: " + if (floor(damage) == damage) damage.toInt() else String.format("%.1f", damage)
                GlStateManager.disableDepth()
                GlStateManager.translate(-(fontRenderer.getStringWidth(damageText) / 2.0), 0.0, 0.0)
                fontRenderer.drawStringWithShadow(damageText, 0f, 0f, -1)
                GlStateManager.popMatrix()
            }
        }
    }

    private fun canPlaceCrystal(blockPos: BlockPos, newPlace: Boolean): Boolean {
        val boost = blockPos.add(0, 1, 0)
        val boost2 = blockPos.add(0, 2, 0)
        if (mc.world.getBlockState(blockPos).block !== Blocks.BEDROCK && mc.world.getBlockState(blockPos).block !== Blocks.OBSIDIAN) return false
        if (!newPlace) {
            if (mc.world.getBlockState(boost).block !== Blocks.AIR || mc.world.getBlockState(boost2).block !== Blocks.AIR) return false
        } else if (mc.world.getBlockState(boost).block !== Blocks.AIR) return false
        if (shouldIgnoreEntity) {
            //Entity ignore would make the AutoCrystal faster
            return !(mc.world.getEntitiesWithinAABB(EntityItem::class.java, AxisAlignedBB(boost)).isNotEmpty()
                    || mc.world.getEntitiesWithinAABB(EntityPlayer::class.java, AxisAlignedBB(boost)).isNotEmpty()
                    || mc.world.getEntitiesWithinAABB(EntityPlayer::class.java, AxisAlignedBB(boost2)).isNotEmpty()
                    || mc.world.getEntitiesWithinAABB(EntityArrow::class.java, AxisAlignedBB(boost)).isNotEmpty())
        }
        return (mc.world.getEntitiesWithinAABB(Entity::class.java, AxisAlignedBB(boost)).isEmpty()
                && mc.world.getEntitiesWithinAABB(Entity::class.java, AxisAlignedBB(boost2)).isEmpty())
    }

    private fun lookAtPos(block: BlockPos, face: EnumFacing) {
        val v = RotationUtil.getRotationsBlock(block, face, false)
        val v2 = RotationUtil.getRotationsBlock(block.add(0.0, +0.5, 0.0), face, false)
        setYawAndPitch(v[0], v[1], v2[1])
    }

    private fun lookAtCrystal(ent: EntityEnderCrystal) {
        val v = RotationUtil.getRotations(mc.player.getPositionEyes(mc.renderPartialTicks), ent.positionVector)
        val v2 = RotationUtil.getRotations(
            mc.player.getPositionEyes(mc.renderPartialTicks),
            ent.positionVector.add(0.0, -0.5, 0.0)
        )
        setYawAndPitch(v[0], v[1], v2[1])
    }

    private fun setYawAndPitch(yaw1: Float, pitch1: Float, renderPitch1: Float) {
        yaw = yaw1.toDouble()
        pitch = pitch1.toDouble()
        renderPitch = renderPitch1.toDouble()
        isSpoofingAngles = true
        rotating = true
    }

    @SubscribeEvent
    fun doAutoCrystal(event: RenderTickEvent) {
        if (mc.player == null || mc.world == null) return
        if (placeTimer.passed(1050.0) || breakTimer.passed(1050.0)) rotating = false
        if (pauseWhileEating.value && isEating) return
        shouldIgnoreEntity = false
        val c = mc.world.loadedEntityList.stream().filter { e: Entity -> e is EntityEnderCrystal && canHitCrystal(e) }
            .map { e: Entity -> e as EntityEnderCrystal }
            .min(Comparator.comparing { e: EntityEnderCrystal -> mc.player.getDistance(e) }).orElse(null)
        if (autoExplode.value && c != null) {
            if (mc.player.canEntityBeSeen(c) || mc.player.getDistance(c) < wallBreakRange.value && wallBreak.value || !wallBreak.value) {
                explodeCrystal(c)
                if (!multiPlace.value) shouldIgnoreEntity = if (entityIgnore.value) true else return
                if (placements >= 2) {
                    placements = 0
                    shouldIgnoreEntity = if (entityIgnore.value) true else return
                }
            }
        } else {
            resetRotation()
            if (oldSlot != -1) {
                mc.player.inventory.currentItem = oldSlot
                oldSlot = -1
            }
        }
        var crystalSlot =
            if (mc.player.heldItemMainhand.getItem() === Items.END_CRYSTAL) mc.player.inventory.currentItem else -1
        if (crystalSlot == -1) {
            for (l in 0..8) {
                if (mc.player.inventory.getStackInSlot(l).getItem() === Items.END_CRYSTAL) {
                    crystalSlot = l
                    break
                }
            }
        }
        var offhand = false
        if (mc.player.heldItemOffhand.getItem() === Items.END_CRYSTAL) offhand = true else if (crystalSlot == -1) return
        val targetBlock: BlockPos?
        val crystalTarget = calculator()
        target = crystalTarget.target
        targetBlock = crystalTarget.blockPos
        if (target == null) {
            renderBlock = null
            resetRotation()
            return
        }
        renderBlock = targetBlock
        if (ModuleManager.getModuleByName("AutoEz").isEnabled) {
            val autoEz = ModuleManager.getModuleByName("AutoEz") as AutoEz
            autoEz.addTargetedPlayer(target!!.name)
        }
        if (autoPlace.value) {
            val enumHand = if (offhand) EnumHand.OFF_HAND else EnumHand.MAIN_HAND
            if (!offhand && mc.player.inventory.currentItem != crystalSlot) {
                if (autoSwitch.value) {
                    mc.player.inventory.currentItem = crystalSlot
                    resetRotation()
                    switchCoolDown = true
                }
                return
            }
            if (switchCoolDown) {
                switchCoolDown = false
                return
            }
            if(targetBlock != null) {
                placeCrystal(targetBlock, enumHand)
            }
        }
        if (rotate.value && isSpoofingAngles) {
            if (togglePitch) {
                mc.player.rotationPitch += 0.0004f
                togglePitch = false
            } else {
                mc.player.rotationPitch -= 0.0004f
                togglePitch = true
            }
        }
        if (prevSlot != -1 && ghostHand.value) {
            mc.player.inventory.currentItem = prevSlot
            mc.playerController.updateController()
        }
    }

    /**
     * Calculation of target block to place crystal
     * return a target contains coordinate and target entity.
     */
    private fun calculator(): CrystalTarget {
        var damage = 0.5
        var tempBlock: BlockPos? = null
        var target: Entity? = null
        val crystalBlocks = findCrystalBlocks(placeRange.value)
        val entities = entities
        prevSlot = -1
        for (entity2 in entities) {

            //Ignore our self
            if (entity2 !== mc.player) {

                //Ignore dead entity
                if ((entity2 as EntityLivingBase).health <= 0.0f) continue

                //Get the vector of target
                val targetVec = Vec3d(entity2.posX, entity2.posY, entity2.posZ)
                for (blockPos in crystalBlocks) {

                    //Ignored the target far from us
                    if (entity2.getDistanceSq(blockPos) >= workingDistance.value * workingDistance.value) continue

                    //Ignored the target over place range
                    if (mc.player.getDistance(
                            blockPos.x.toDouble(),
                            blockPos.y.toDouble(),
                            blockPos.z.toDouble()
                        ) > placeRange.value
                    ) continue
                    val targetDamage = calculateDamage(
                        blockPos.x + 0.5,
                        (blockPos.y + 1).toDouble(),
                        blockPos.z + 0.5,
                        entity2,
                        targetVec
                    ).toDouble()

                    //Ignored the target damage lower than currently highest target
                    if (targetDamage < damage) continue

                    //Ignored target that damage lower than MinDamage we set
                    //If we can face place set the min damage to 1.0
                    if (targetDamage < (if (facePlace.value)
                            if (canFacePlace(entity2, blastHealth.value)) 1.0 else placeMinDmg.value
                        else placeMinDmg.value)
                    ) continue
                    val healthTarget = entity2.health + entity2.absorptionAmount
                    val healthSelf = mc.player.health + mc.player.absorptionAmount
                    val selfDamage = calculateDamage(
                        blockPos.x + 0.5,
                        (blockPos.y + 1).toDouble(),
                        blockPos.z + 0.5,
                        mc.player
                    ).toDouble()

                    //We dont want to be damaged deeper than enemy
                    if (selfDamage > targetDamage && targetDamage < healthTarget) continue

                    //We dont want to suicide
                    if (selfDamage - 0.5 > healthSelf && noSuicide.value) continue

                    //Ignored the target that damage higher than MaxSelfDamage we set
                    if (selfDamage > placeMaxSelf.value) continue

                    //Wall calculation
                    if (!wallPlace.value
                        && wallPlaceRange.value > 0 && !canSeeBlock(blockPos)
                        && getVecDistance(
                            blockPos,
                            mc.player.posX,
                            mc.player.posY,
                            mc.player.posZ
                        ) >= wallPlaceRange.value
                    ) continue
                    damage = targetDamage
                    tempBlock = blockPos
                    target = entity2

                    //Put render dmg
                    if (renderDmg.value) renderBlockDmg[tempBlock] = targetDamage

                    //GhostHand stuff
                    if (ghostHand.value) ghostHand()
                }
                /*
                  We dont want to make our computer lag.
                  If we have target,then we stop calculating.
                 */if (target != null) break
            }
        }
        return CrystalTarget(tempBlock, target)
    }

    private fun ghostHand() {
        if (mc.player.heldItemMainhand.getItem() !== Items.END_CRYSTAL && mc.player.heldItemOffhand.getItem() !== Items.END_CRYSTAL) {
            for (i in 0..8) {
                val stack = mc.player.inventory.getStackInSlot(i)
                if (stack == ItemStack.EMPTY) continue
                if (stack.getItem() === Items.END_CRYSTAL) {
                    prevSlot = mc.player.inventory.currentItem
                    mc.player.inventory.currentItem = i
                    mc.playerController.updateController()
                }
            }
        }
    }

    private val entities: List<Entity>
        get() {
            val entities: MutableList<Entity> = ArrayList()
            if (targetPlayer.value) {
                entities.addAll(
                    mc.world.playerEntities.stream()
                        .filter { entityPlayer: EntityPlayer -> !isFriend(entityPlayer.name) }
                        .filter { entity: EntityPlayer -> mc.player.getDistance(entity) < workingDistance.value }
                        .collect(Collectors.toList()))
            }
            entities.addAll(
                mc.world.loadedEntityList.stream()
                    .filter { entity: Entity -> EntityUtil.isLiving(entity) && if (EntityUtil.isPassive(entity)) targetAnimals.value else targetMobs.value }
                    .filter { entity: Entity -> entity !is EntityPlayer }
                    .filter { entity: Entity -> mc.player.getDistance(entity) < workingDistance.value }
                    .collect(Collectors.toList()))
            for (ite2 in ArrayList(entities)) {
                if (mc.player.getDistance(ite2) > workingDistance.value) entities.remove(ite2)
                if (ite2 === mc.player) entities.remove(ite2)
            }
            entities.sortWith(Comparator.comparingDouble { entity: Entity ->
                entity.getDistance(mc.player)
                    .toDouble()
            })
            return entities
        }

    private class CrystalTarget(var blockPos: BlockPos?, var target: Entity?)

    val color: Int
        get() {
            val tickColor = floatArrayOf(System.currentTimeMillis() % 11520L / 11520.0f * rainbowSpeed.value)
            val colorRgb = Color.HSBtoRGB(tickColor[0], saturation.value, brightness.value)
            return if (rainbow.value) Color(
                colorRgb shr 16 and 0xFF,
                colorRgb shr 8 and 0xFF,
                colorRgb and 0xFF,
                alpha.value
            ).rgb else Color(red.value, green.value, blue.value, alpha.value).rgb
        }

    override fun onWorldRender(event: RenderEvent) {
        val color =
            if (syncGui.value) Color(GuiManager.red, GuiManager.green, GuiManager.blue, alpha.value).rgb else color
        if (renderBlock != null) drawBlock(renderBlock!!, color)
    }

    override fun onEnable() {
        shouldIgnoreEntity = false
        placeTimer.reset()
        breakTimer.reset()
        renderBlockDmg.clear()
    }

    override fun onDisable() {
        rotating = false
        resetRotation()
        renderBlock = null
        target = null
        yaw = mc.player.rotationYaw.toDouble()
        pitch = mc.player.rotationPitch.toDouble()
    }

    override fun getHudInfo(): String {
        return if (target == null) "" else target!!.name
    }

}