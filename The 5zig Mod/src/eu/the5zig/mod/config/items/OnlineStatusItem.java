package eu.the5zig.mod.config.items;

import com.google.gson.JsonObject;
import eu.the5zig.mod.I18n;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.chat.entity.Friend;
import eu.the5zig.mod.chat.network.packets.PacketProfile;
import eu.the5zig.mod.gui.GuiSettings;

public class OnlineStatusItem extends SliderItem {

	/**
	 * Creates a Config Item that works as a Slider.
	 *
	 * @param key          Der Key of the Item. Used in config File and to translate the Item.
	 * @param category     The Category of the Item. Used by {@link GuiSettings} for finding the corresponding items.
	 * @param defaultValue The Default Value of the Item.
	 */
	public OnlineStatusItem(String key, String category, Float defaultValue) {
		super(key, "", category, defaultValue, 0, 2, 1);
	}

	@Override
	public Float get() {
		return (float) The5zigMod.getDataManager().getProfile().getOnlineStatus().ordinal();
	}

	@Override
	public void set(Float value) {
		if (The5zigMod.getDataManager() != null && The5zigMod.getDataManager().getProfile() != null) {
			The5zigMod.getDataManager().getProfile().setOnlineStatus(Friend.OnlineStatus.values()[value.intValue()]);
		}
	}

	@Override
	public void serialize(JsonObject object) {
	}

	@Override
	public void deserialize(JsonObject object) {
	}

	@Override
	public void action() {
		if (changed) {
			The5zigMod.getNetworkManager().sendPacket(new PacketProfile(The5zigMod.getDataManager().getProfile().getOnlineStatus()));
			changed = false;
		}
	}

	@Override
	public String getCustomValue(float value) {
		Friend.OnlineStatus status = Friend.OnlineStatus.values()[((int) (value * 2.0f))];
		return I18n.translate(status.getName());
	}
}
