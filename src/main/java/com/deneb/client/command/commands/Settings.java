package com.deneb.client.command.commands;

import com.deneb.client.command.Command;
import com.deneb.client.features.IModule;
import com.deneb.client.features.ModuleManager;
import com.deneb.client.features.modules.client.NullHUD;
import com.deneb.client.features.modules.client.NullModule;
import com.deneb.client.utils.ChatUtil;
import com.deneb.client.value.*;

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
            IModule m = ModuleManager.getModuleByName(args[0]);

            if ((m instanceof NullModule) || (m instanceof NullHUD)) {
                ChatUtil.sendNoSpamErrorMessage("Couldn't find a module &b" + args[0] + "!");
                return;
            }

            ArrayList<Value> settings = m.getValues();

            for (Value value : settings) {
                if (value.getName().toLowerCase().equals(args[1].toLowerCase())) {
                    try {
                        if (value instanceof BValue) {
                            if (args[2].toLowerCase().equals("true")) {
                                value.setValue(true);
                            } else if (args[2].toLowerCase().equals("false")) {
                                value.setValue(false);
                            } else {
                                ChatUtil.sendNoSpamErrorMessage("Can not set value to " + args[2]);
                            }
                        }
                        if (value instanceof IValue) {
                            int input = Integer.parseInt(args[2]);
                            if (input > ((IValue) value).getMax() || input < ((IValue) value).getMin()) {
                                ChatUtil.sendNoSpamErrorMessage("Can not set value to " + input);
                                return;
                            }

                            value.setValue(input);
                        }
                        if (value instanceof DValue) {

                            double input = Double.parseDouble(args[2]);
                            if (input > ((DValue) value).getMax() || input < ((DValue) value).getMin()) {
                                ChatUtil.sendNoSpamErrorMessage("Can not set value to " + input);
                                return;
                            }

                            value.setValue(input);
                        }
                        if (value instanceof FValue) {

                            float input = Float.parseFloat(args[2]);
                            if (input > ((FValue) value).getMax() || input < ((FValue) value).getMin()) {
                                ChatUtil.sendNoSpamErrorMessage("Can not set value to " + input);
                                return;
                            }

                            value.setValue(input);
                        }
                        if (value instanceof MValue) {
                            for (MValue.Mode mode : ((MValue) value).getModes()) {
                                mode.setToggled(mode.getName().toLowerCase().equals(args[2].toLowerCase()));
                            }
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
