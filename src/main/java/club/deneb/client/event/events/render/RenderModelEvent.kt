package club.deneb.client.event.events.render

import club.deneb.client.event.MinecraftEvent

class RenderModelEvent(var rotating:Boolean = false,var pitch:Float = 0f) : MinecraftEvent()