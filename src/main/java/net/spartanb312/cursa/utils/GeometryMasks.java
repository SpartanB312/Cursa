package net.spartanb312.cursa.utils;

public class GeometryMasks {

    public static final class Quad {
        public static final int DOWN = 0x01;
        public static final int UP = 0x02;
        public static final int NORTH = 0x04;
        public static final int SOUTH = 0x08;
        public static final int WEST = 0x10;
        public static final int EAST = 0x20;
        public static final int ALL = DOWN | UP | NORTH | SOUTH | WEST | EAST;
    }

}
