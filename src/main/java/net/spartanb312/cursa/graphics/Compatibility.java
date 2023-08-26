package net.spartanb312.cursa.graphics;

import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.GLContext;

public class Compatibility {

    private static final ContextCapabilities contextCapabilities = GLContext.getCapabilities();

    public static final boolean openGL11 = contextCapabilities.OpenGL11;
    public static final boolean openGL12 = contextCapabilities.OpenGL12;
    public static final boolean openGL13 = contextCapabilities.OpenGL13;
    public static final boolean openGL14 = contextCapabilities.OpenGL14;
    public static final boolean openGL15 = contextCapabilities.OpenGL15;
    public static final boolean openGL20 = contextCapabilities.OpenGL20;
    public static final boolean openGL21 = contextCapabilities.OpenGL21;
    public static final boolean openGL30 = contextCapabilities.OpenGL30;
    public static final boolean openGL31 = contextCapabilities.OpenGL31;
    public static final boolean openGL32 = contextCapabilities.OpenGL32;
    public static final boolean openGL33 = contextCapabilities.OpenGL33;
    public static final boolean openGL40 = contextCapabilities.OpenGL40;
    public static final boolean openGL41 = contextCapabilities.OpenGL41;
    public static final boolean openGL42 = contextCapabilities.OpenGL42;
    public static final boolean openGL43 = contextCapabilities.OpenGL43;
    public static final boolean openGL44 = contextCapabilities.OpenGL44;
    public static final boolean openGL45 = contextCapabilities.OpenGL45;
    public static final boolean extFramebufferObject = contextCapabilities.GL_EXT_framebuffer_object && !contextCapabilities.OpenGL30;

}
