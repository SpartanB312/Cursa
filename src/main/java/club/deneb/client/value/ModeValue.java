package club.deneb.client.value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by KillRED on 07/23/20
 * Updated by B_312 on 01/09/21
 */
public class ModeValue extends Value<ModeValue.Mode> {
	
	private List<Mode> modes = new ArrayList<>();
	private final String modeName;
	private int index;

    public List<Mode> getModes() {
		return modes;
	}

	public ModeValue(String modeName, Mode... modes) {
        super(modeName, null);
        this.modeName = modeName;
		this.modes = Arrays.asList(modes);
		index = this.modes.indexOf(getToggledMode());
    }

	public ModeValue(String modeName) {
		super(modeName, null);
		this.modeName = modeName;
	}

	public ModeValue(String modeName, String defaultMode , String... modes1) {
		super(modeName, null);
		this.modeName = modeName;
		this.modes = new ArrayList<>();
		Arrays.asList(modes1).forEach(m -> modes.add(new Mode(m,defaultMode.equals(m))));
		index = this.modes.indexOf(getToggledMode());
	}

	public ModeValue(String modeName, List<Mode> modes) {
		super(modeName, null);
		this.modeName = modeName;
		this.modes = modes;
		index = this.modes.indexOf(getToggledMode());
	}

	public ModeValue v(Predicate<Object> predicate) {
		return (ModeValue) super.v(predicate);
	}

	public ModeValue page(PageValue.Page page) {
		return (ModeValue) super.v(page.p());
	}

	public ModeValue b(BooleanValue value) {
		return (ModeValue) super.v(v -> value.getValue());
	}

	public ModeValue r(BooleanValue value) {
		return (ModeValue) super.v(v -> !value.getValue());
	}

	public ModeValue c(double min, Value value, double max){
		if(value instanceof IntValue) {
			return (ModeValue) super.v(v -> ((IntValue)value).getValue() <= max && ((IntValue)value).getValue() >= min);
		}
		if(value instanceof FloatValue) {
			return (ModeValue) super.v(v -> ((FloatValue)value).getValue() <= max && ((FloatValue)value).getValue() >= min);
		}
		if(value instanceof DoubleValue) {
			return (ModeValue) super.v(v -> ((DoubleValue)value).getValue() <= max && ((DoubleValue)value).getValue() >= min);
		}
		return (ModeValue) super.v(v -> true);
	}

	public ModeValue c(double min, Value value){
		if(value instanceof IntValue) {
			return (ModeValue) super.v(v -> ((IntValue)value).getValue() >= min);
		}
		if(value instanceof FloatValue) {
			return (ModeValue) super.v(v -> ((FloatValue)value).getValue() >= min);
		}
		if(value instanceof DoubleValue) {
			return (ModeValue) super.v(v -> ((DoubleValue)value).getValue() >= min);
		}
		return (ModeValue) super.v(v -> true);
	}

	public ModeValue c(Value value, double max){
		if(value instanceof IntValue) {
			return (ModeValue) super.v(v -> ((IntValue)value).getValue() <= max);
		}
		if(value instanceof FloatValue) {
			return (ModeValue) super.v(v -> ((FloatValue)value).getValue() <= max);
		}
		if(value instanceof DoubleValue) {
			return (ModeValue) super.v(v -> ((DoubleValue)value).getValue() <= max);
		}
		return (ModeValue) super.v(v -> true);
	}

	public ModeValue m(ModeValue value, String mode){
		this.visibility.add(v -> value.getMode(mode).isToggled());
		return this;
	}

	public boolean page(String page) {
		return getMode(page).isToggled();
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
