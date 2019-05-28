package eu.the5zig.mod.modules.items.player;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.gui.ingame.ItemStack;
import eu.the5zig.mod.modules.items.ItemStackItem;

public class MainHand extends ItemStackItem {

	@Override
	public void registerSettings() {
		getProperties().addSetting("attributes", true);
		getProperties().addSetting("durability", DurabilityStyle.RELATIVE, DurabilityStyle.class);
	}

	@Override
	protected ItemStack getStack(boolean dummy) {
		return dummy ? The5zigMod.getVars().getItemByName("minecraft:diamond_sword") : The5zigMod.getVars().getItemInMainHand();
	}
}
