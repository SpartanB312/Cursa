package club.deneb.client.event.events.entity

import club.deneb.client.event.MinecraftEvent
import net.minecraft.entity.MoverType

class PlayerMoveEvent(var type: MoverType, var x: Double, var y: Double, var z: Double) : MinecraftEvent()