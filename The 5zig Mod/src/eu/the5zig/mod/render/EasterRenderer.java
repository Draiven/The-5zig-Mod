package eu.the5zig.mod.render;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.gui.Gui;
import eu.the5zig.mod.listener.EasterListener;
import eu.the5zig.mod.util.GLUtil;

import java.awt.image.BufferedImage;

public class EasterRenderer {

	private final int TOTAL_FRAMES = 12;
	private int currentFrame = 0;
	private int xOff;
	private BufferedImage bufferedImage;
	private Object resourceLocation;

	private EasterListener easterListener;

	public EasterRenderer(EasterListener easterListener) {
		this.easterListener = easterListener;
	}

	public void render() {
		if (easterListener.isRunning()) {
			if (bufferedImage == null && easterListener.getBufferedImage() != null) {
				resourceLocation = The5zigMod.getVars().loadDynamicImage("easter", bufferedImage = easterListener.getBufferedImage());
			}
			if (resourceLocation != null) {
				The5zigMod.getVars().bindTexture(resourceLocation);
				int scaleFactor = 6;
				int width = 400 / scaleFactor;
				int height = 400 / scaleFactor;
				for (int y = 0; y < The5zigMod.getVars().getHeight(); y += height) {
					for (int x = -width; x < The5zigMod.getVars().getWidth(); x += width) {
						GLUtil.color(1, 1, 1, 1);
						Gui.drawModalRectWithCustomSizedTexture(x + xOff, y, Math.abs(currentFrame) * width, 0, width, height, width * TOTAL_FRAMES, height);
					}
				}
				currentFrame++;
				xOff = (xOff + 1) % width;
			}
		}
	}

}
