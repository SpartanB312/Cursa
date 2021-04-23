package club.deneb.client.command.commands;

import club.deneb.client.features.AbstractModule;
import club.deneb.client.features.ModuleManager;
import club.deneb.client.features.modules.client.NullHUD;
import club.deneb.client.features.modules.client.NullModule;
import club.deneb.client.utils.ChatUtil;
import club.deneb.client.value.*;
import club.deneb.client.command.Command;

import java.util.ArrayList;

/**
 * Created by killRED on 2020
 * Updated by B_312 on 01/15/21
 */
@Command.Info(command = "setting", description = "Set settings for module or HUD.")
public class Settings extends Command {

    @Override
    public void onCall(String s, String[] args) {
        try {

            AbstractModule m = ModuleManager.getModuleByName(args[0]);

            if ((m instanceof NullModule) || (m instanceof NullHUD)) {
                ChatUtil.sendNoSpamErrorMessage("Couldn't find a module &b" + args[0] + "!");
                return;
            }

            ArrayList<Value<?>> settings = m.getValues();

            for (Value<?> value : settings) {
                if (value.getName().equalsIgnoreCase(args[1])) {
                    try {
                        if (value instanceof BooleanValue) {
                            BooleanValue valueT = (BooleanValue) value;
                            if (args[2].equalsIgnoreCase("true")) {
                                valueT.setValue(true);
                            } else if (args[2].equalsIgnoreCase("false")) {
                                valueT.setValue(false);
                            } else {
                                ChatUtil.sendNoSpamErrorMessage("Can not set value to " + args[2]);
                            }
                        }
                        if (value instanceof IntValue) {
                            IntValue valueT = (IntValue) value;
                            int input = Integer.parseInt(args[2]);
                            if (input > valueT.getMax().intValue() || input < valueT.getMin().intValue()) {
                                ChatUtil.sendNoSpamErrorMessage("Can not set value to " + input);
                                return;
                            }
                            valueT.setValue(input);
                        }
                        if (value instanceof DoubleValue) {
                            DoubleValue valueT = (DoubleValue) value;
                            double input = Double.parseDouble(args[2]);
                            if (input > valueT.getMax().doubleValue() || input < valueT.getMin().doubleValue()) {
                                ChatUtil.sendNoSpamErrorMessage("Can not set value to " + input);
                                return;
                            }
                            valueT.setValue(input);
                        }
                        if (value instanceof FloatValue) {
                            FloatValue valueT = (FloatValue) value;
                            float input = Float.parseFloat(args[2]);
                            if (input > valueT.getMax().floatValue() || input < valueT.getMin().floatValue()) {
                                ChatUtil.sendNoSpamErrorMessage("Can not set value to " + input);
                                return;
                            }
                            valueT.setValue(input);
                        }
                        if (value instanceof ModeValue<?>) {
                            ModeValue<?> valueT = (ModeValue<?>) value;
                            valueT.setWithName(args[2]);
                        }

                        ChatUtil.sendNoSpamMessage("Set " + value.getName() + " to " + args[2]);
                    } catch (Exception e) {
                        ChatUtil.sendNoSpamErrorMessage("Can not set value to " + args[2]);
                    }
                } else {
                    ChatUtil.sendNoSpamMessage("Can not find setting call " + args[1]);
                }
            }
        } catch (Exception e) {
            ChatUtil.sendNoSpamErrorMessage(getSyntax());
        }
    }

    @Override
    public String getSyntax() {
        return "setting <module> <valuename> <value>";
    }

}
