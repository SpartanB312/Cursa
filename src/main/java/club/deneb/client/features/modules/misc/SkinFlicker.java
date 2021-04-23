package club.deneb.client.features.modules.misc;

import club.deneb.client.features.Category;
import club.deneb.client.features.Module;
import club.deneb.client.value.Value;
import net.minecraft.entity.player.EnumPlayerModelParts;

import java.util.Random;

/**
 * Created by 086 on 30/01/2018.
 */
@Module.Info(name = "SkinFlicker", description = "Toggle the jacket layer rapidly for a cool skin effect", category = Category.MISC)
public class SkinFlicker extends Module {

    Value<String> mode = setting("Mode", "HORIZONTAL",listOf("HORIZONTAL","VERTICAL","RANDOM"));
    Value<Integer> slowness = setting("Slowness",2,1,10);

    private final static EnumPlayerModelParts[] PARTS_HORIZONTAL = new EnumPlayerModelParts[]{
            EnumPlayerModelParts.LEFT_SLEEVE,
            EnumPlayerModelParts.JACKET,
            EnumPlayerModelParts.HAT,
            EnumPlayerModelParts.LEFT_PANTS_LEG,
            EnumPlayerModelParts.RIGHT_PANTS_LEG,
            EnumPlayerModelParts.RIGHT_SLEEVE
    };

    private final static EnumPlayerModelParts[] PARTS_VERTICAL = new EnumPlayerModelParts[]{
            EnumPlayerModelParts.HAT,
            EnumPlayerModelParts.JACKET,
            EnumPlayerModelParts.LEFT_SLEEVE,
            EnumPlayerModelParts.RIGHT_SLEEVE,
            EnumPlayerModelParts.LEFT_PANTS_LEG,
            EnumPlayerModelParts.RIGHT_PANTS_LEG,
    };

    private Random r = new Random();
    private int len = EnumPlayerModelParts.values().length;

    @Override
    public void onTick() {
        switch (mode.getValue()) {
            case "RANDOM":
                if (mc.player.ticksExisted % slowness.getValue() != 0) return;
                mc.gameSettings.switchModelPartEnabled(EnumPlayerModelParts.values()[r.nextInt(len)]);
                break;
            case "VERTICAL":
            case "HORIZONTAL":
                int i = (mc.player.ticksExisted / slowness.getValue()) % (PARTS_HORIZONTAL.length * 2); // *2 for on/off
                boolean on = false;
                if (i >= PARTS_HORIZONTAL.length) {
                    on = true;
                    i -= PARTS_HORIZONTAL.length;
                }
                mc.gameSettings.setModelPartEnabled(mode.getValue().equals("VERTICAL") ? PARTS_VERTICAL[i] : PARTS_HORIZONTAL[i], on);
        }
    }

}
