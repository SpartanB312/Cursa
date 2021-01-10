package com.deneb.client.value;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by KillRED on 07/23/20
 * Updated by B_312 on 01/09/21
 */
public class MValue extends Value<MValue.Mode> {
	
	private final List<Mode> modes;
	private final String modeName;
	private int index;

    public List<Mode> getModes() {
		return modes;
	}

	public MValue(String modeName, Mode... modes) {
        super(modeName, null);
        this.modeName = modeName;
		this.modes = Arrays.asList(modes);
		index = this.modes.indexOf(getToggledMode());
    }

	public MValue visibility(Predicate<Mode> predicate) {
		return (MValue) super.visibility(predicate);
	}


    public Mode getMode(String name) {
    	Mode m = null;
    	for(Mode mode : modes) {
    		if(mode.getName().equals(name)) {
    			m = mode;
    		}
    	}
    	return m;
    }

    public Mode getToggledMode(){
		Mode m = null;
		for(Mode mode : modes) {
			if(mode.isToggled()) {
				m = mode;
			}
		}
		return m;
	}

	public void setModeWithName(String s){
		for(Mode mode : modes) {
			mode.setToggled(mode.getName().equals(s));
		}
	}

	public void forwardLoop() {
		if (index < modes.size() - 1) {
			index++;
		} else {
			index = 0;
		}
		for (Mode mode : modes) {
			mode.setToggled(mode == modes.get(index));
		}
	}

	public String getModeName() {
		return modeName;
	}

	public static class Mode {

		private String name;
		private boolean toggled;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public boolean isToggled() {
			return toggled;
		}
		public void setToggled(boolean toggled) {
			this.toggled = toggled;
		}
		public Mode(String name, boolean toggled) {
			this.name = name;
			this.toggled = toggled;
		}

		public Mode(String name) {
			this.name = name;
			this.toggled = false;
		}

	}
}
