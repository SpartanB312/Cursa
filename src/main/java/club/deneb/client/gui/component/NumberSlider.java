package club.deneb.client.gui.component;

import club.deneb.client.gui.Panel;
import club.deneb.client.utils.ColorUtil;
import club.deneb.client.client.GuiManager;
import club.deneb.client.value.DoubleValue;
import club.deneb.client.value.FloatValue;
import club.deneb.client.value.IntValue;
import club.deneb.client.value.Value;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static club.deneb.client.utils.LambdaUtil.isHovered;

/**
 * Created by B_312 on 01/10/21
 */
public class NumberSlider<T> extends ValueButton<T> {

    boolean sliding = false;

    public NumberSlider(Value<T> value, int width, int height, Panel father) {
        this.width = width;
        this.height = height;
        this.father = father;
        this.setValue(value);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        if (!getValue().visible())
            sliding = false;

        int color = new Color(GuiManager.getINSTANCE().getRed(),GuiManager.getINSTANCE().getGreen(),GuiManager.getINSTANCE().getBlue(),192).getRGB();
        int fontColor = new Color(255, 255, 255).getRGB();

        //Background
        Gui.drawRect(x, y, x + width, y + height,0x85000000);

        double iwidth = 0;
        String displayvalue = "0";

        int sliderWidth = (width - 2);

        if (getValue() instanceof DoubleValue) {
            DoubleValue doubleValue = (DoubleValue) getValue();
            displayvalue = String.format("%.1f", doubleValue.getValue());

            double percentBar = (doubleValue.getValue() - doubleValue.getMin()) / (doubleValue.getMax() - doubleValue.getMin());

            iwidth = sliderWidth * percentBar;
        } else if (getValue() instanceof FloatValue) {
            FloatValue floatValue = (FloatValue) getValue();
            displayvalue = String.format("%.1f", floatValue.getValue());

            double percentBar = (floatValue.getValue() - floatValue.getMin()) / (floatValue.getMax() - floatValue.getMin());

            iwidth = sliderWidth * percentBar;
        } else if (getValue() instanceof IntValue) {
            IntValue intValue = (IntValue) getValue();
            displayvalue = String.valueOf(intValue.getValue());

            double percentBar = (double) (intValue.getValue() - intValue.getMin()) / (double) (intValue.getMax() - intValue.getMin());

            iwidth = sliderWidth * percentBar;
        }

        Gui.drawRect(x + 1, y + 1, x + 1 + (int) iwidth, y + height, color);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        if (this.sliding) {
            if (getValue() instanceof DoubleValue) {
                DoubleValue doubleValue = (DoubleValue) getValue();
                double diff = doubleValue.getMax() - doubleValue.getMin();
                double val = doubleValue.getMin() + (MathHelper.clamp((mouseX - (double) (x + 1)) / (double) sliderWidth, 0, 1)) * diff;
                doubleValue.setValue(val);
            } else if (getValue() instanceof FloatValue) {
                FloatValue floatValue = (FloatValue) getValue();
                double diff = floatValue.getMax() - floatValue.getMin();
                double val = floatValue.getMin() + (MathHelper.clamp((mouseX - (double) (x + 1)) / (double) sliderWidth, 0, 1)) * diff;
                floatValue.setValue((float) val);
            } else if (getValue() instanceof IntValue) {
                IntValue intValue = (IntValue) getValue();
                double diff = intValue.getMax() - intValue.getMin();
                double val = intValue.getMin() + (MathHelper.clamp((mouseX - (double) (x + 1)) / (double) sliderWidth, 0, 1)) * diff;
                intValue.setValue((int) val);
            }
        }

        font.drawString(getValue().getName(), x + 3, (int) (y + height / 2 - font.getHeight() / 2f) + 2, fontColor);
        font.drawString(String.valueOf(displayvalue), x + width - 1 - font.getStringWidth(String.valueOf(displayvalue)), (int) (y + height / 2 - font.getHeight() / 2f) + 2, ColorUtil.getHoovered(0x909090,isHovered(mouseX, mouseY).test(this)));
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {

        if (!getValue().visible() || !isHovered(mouseX, mouseY).test(this))
            return false;
        if (mouseButton == 0) {
            this.sliding = true;
            return true;
        }
        return false;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        sliding = false;
    }


}
