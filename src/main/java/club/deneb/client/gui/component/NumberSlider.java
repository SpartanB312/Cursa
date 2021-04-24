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
public class NumberSlider<T extends Number> extends ValueButton<T> {

    boolean sliding = false;

    public NumberSlider(Value<T> value, int width, int height, Panel father) {
        this.width = width;
        this.height = height;
        this.father = father;
        this.setValue(value);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        if (!getSetting().visible()) sliding = false;

        int color = new Color(GuiManager.INSTANCE.getRed(),GuiManager.INSTANCE.getGreen(),GuiManager.INSTANCE.getBlue(),192).getRGB();
        int fontColor = new Color(255, 255, 255).getRGB();

        //Background
        Gui.drawRect(x, y, x + width, y + height,0x85000000);

        double iwidth = 0;
        String displayvalue = "0";

        int sliderWidth = (width - 2);

        if (getSetting().getValue() instanceof Double) {
            displayvalue = String.format("%.1f", getSetting().getValue().doubleValue());
            double percentBar = (getSetting().getValue().doubleValue() - getSetting().getMin().doubleValue()) / (getSetting().getMax().doubleValue() - getSetting().getMin().doubleValue());
            iwidth = sliderWidth * percentBar;
        } else if (getSetting().getValue() instanceof Float) {
            displayvalue = String.format("%.1f", getSetting().getValue().floatValue());
            double percentBar = (getSetting().getValue().floatValue() - getSetting().getMin().floatValue()) / (getSetting().getMax().floatValue() - getSetting().getMin().floatValue());
            iwidth = sliderWidth * percentBar;
        } else if (getSetting().getValue() instanceof Integer) {
            displayvalue = String.format("%.1f", getSetting().getValue().floatValue());
            double percentBar = (getSetting().getValue().floatValue() - getSetting().getMin().floatValue()) / (getSetting().getMax().floatValue() - getSetting().getMin().floatValue());
            iwidth = sliderWidth * percentBar;
        }

        Gui.drawRect(x + 1, y + 1, x + 1 + (int) iwidth, y + height, color);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        if (this.sliding) {
            if (getSetting() instanceof DoubleValue) {
                DoubleValue doubleValue = (DoubleValue)getSetting();
                double diff = doubleValue.getMax().doubleValue() - doubleValue.getMin().doubleValue();
                double val = doubleValue.getMin().doubleValue() + (MathHelper.clamp((mouseX - (double) (x + 1)) / (double) sliderWidth, 0, 1)) * diff;
                doubleValue.setValue(val);
            } else if (getSetting() instanceof FloatValue) {
                FloatValue floatValue = (FloatValue)getSetting();
                double diff = floatValue.getMax().floatValue() - floatValue.getMin().floatValue();
                double val = floatValue.getMin().floatValue() + (MathHelper.clamp((mouseX - (double) (x + 1)) / (double) sliderWidth, 0, 1)) * diff;
                floatValue.setValue((float) val);
            } else if (getSetting() instanceof IntValue) {
                IntValue intValue = (IntValue)getSetting();
                double diff = intValue.getMax().intValue() - intValue.getMin().intValue();
                double val = intValue.getMin().intValue() + (MathHelper.clamp((mouseX - (double) (x + 1)) / (double) sliderWidth, 0, 1)) * diff;
                intValue.setValue((int) val);
            }
        }

        font.drawString(getSetting().getName(), x + 3, (int) (y + height / 2 - font.getHeight() / 2f) + 2, fontColor);
        font.drawString(String.valueOf(displayvalue), x + width - 1 - font.getStringWidth(String.valueOf(displayvalue)), (int) (y + height / 2 - font.getHeight() / 2f) + 2, ColorUtil.getHoovered(0x909090,isHovered(mouseX, mouseY).test(this)));
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {

        if (!getSetting().visible() || !isHovered(mouseX, mouseY).test(this))
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
