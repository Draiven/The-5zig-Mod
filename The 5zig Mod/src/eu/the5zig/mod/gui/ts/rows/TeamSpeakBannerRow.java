package eu.the5zig.mod.gui.ts.rows;

import eu.the5zig.mod.gui.Gui;
import eu.the5zig.mod.gui.elements.IButton;
import eu.the5zig.mod.gui.elements.RowExtended;
import eu.the5zig.mod.render.Base64Renderer;
import eu.the5zig.teamspeak.api.ServerImage;
import eu.the5zig.teamspeak.api.ServerInfo;
import eu.the5zig.util.Utils;

import java.net.URL;

public class TeamSpeakBannerRow implements RowExtended {

	private final Base64Renderer renderer;
	private final URL pointingURL;
	private final int width;
	private final int height;

	private boolean hover;

	public TeamSpeakBannerRow(ServerInfo serverInfo, int width) {
		this.width = width;
		ServerImage serverBanner = serverInfo.getServerBanner();
		if (serverBanner != null && serverBanner.getImage() != null) {
			renderer = Base64Renderer.getRenderer(serverBanner.getImage(), "ts/" + serverInfo.getUniqueId() + "_banner");
			pointingURL = serverBanner.getPointingURL();
			float factor = (float) serverBanner.getImage().getWidth() / (float) width;
			height = (int) ((float) serverBanner.getImage().getHeight() / factor);
		} else {
			renderer = null;
			pointingURL = null;
			height = 0;
		}
	}

	@Override
	public void draw(int x, int y) {
	}

	@Override
	public int getLineHeight() {
		return height == 0 ? 0 : height + 2;
	}

	@Override
	public void draw(int x, int y, int slotHeight, int mouseX, int mouseY) {
		hover = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
		if (renderer != null) {
			renderer.renderImage(x, y, width, height);
			if (hover && pointingURL != null) {
				Gui.drawRect(x, y, x + width, y + height, 0x44000000);
			}
		}
	}

	@Override
	public IButton mousePressed(int mouseX, int mouseY) {
		if (hover && pointingURL != null) {
			Utils.openURL(pointingURL.toString());
		}

		return null;
	}
}
