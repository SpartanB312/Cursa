package club.deneb.client.utils

object GeometryMasks {

    object Quad {
        const val DOWN = 0x01
        const val UP = 0x02
        const val NORTH = 0x04
        const val SOUTH = 0x08
        const val WEST = 0x10
        const val EAST = 0x20
        const val ALL = DOWN or UP or NORTH or SOUTH or WEST or EAST
    }

}