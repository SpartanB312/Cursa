package club.deneb.client.gui.component;

import club.deneb.client.Deneb;
import club.deneb.client.gui.Panel;
import club.deneb.client.gui.font.CFontRenderer;
import net.minecraft.client.Minecraft;

/**
 * Created by B_312 on 01/10/21
 */
public abstract class Component {
    public CFontRenderer font = Deneb.getINSTANCE().getFont();
    public Minecraft mc = Minecraft.getMinecraft();
    public int x,y,width,height;
    public Panel father;
    public boolean isExtended;
    public abstract void render(int mouseX, int mouseY, float partialTicks);
    public abstract boolean mouseClicked(int mouseX, int mouseY, int mouseButton);
    public void mouseReleased(int mouseX, int mouseY, int state) { }
    public void keyTyped(char typedChar, int keyCode) { }

    public void solvePos(){
        this.x = father.getX();
        this.y = father.getY();
    }
}
