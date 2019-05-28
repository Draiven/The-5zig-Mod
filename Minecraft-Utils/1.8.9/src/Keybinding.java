import eu.the5zig.mod.util.IKeybinding;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class Keybinding extends avb implements IKeybinding {

	public Keybinding(String description, int keyCode, String category) {
		super(description, keyCode, category);
	}

	public boolean isPressed() {
		return f();
	}

	public int getKeyCode() {
		return i();
	}

	@Override
	public int compareTo(avb o) {
		return super.a(o);
	}
}
