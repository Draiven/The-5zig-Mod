import eu.the5zig.mod.MinecraftFactory;
import eu.the5zig.mod.gui.IOverlay;
import eu.the5zig.mod.util.GLUtil;
import eu.the5zig.mod.util.IResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class Overlay extends beh implements IOverlay {

	private static final IResourceLocation texture = MinecraftFactory.getVars().createResourceLocation("textures/gui/achievement/achievement_background.png");
	private static final Object LOCK = new Object();
	private static Overlay[] activeOverlays;

	public long timeStarted;
	public int offset;
	private bao mc;
	private String title;
	private String subtitle;
	private int width;
	private int height;
	private int index = -1;

	Overlay() {
		super(bao.B());
		mc = bao.B();
	}

	public static void updateOverlayCount(int count) {
		activeOverlays = new Overlay[count];
	}

	public static void renderAll() {
		synchronized (LOCK) {
			for (int activeOverlaysSize = activeOverlays.length, i = activeOverlaysSize - 1; i >= 0; i--) {
				Overlay activeOverlay = activeOverlays[i];
				if (activeOverlay == null)
					continue;
				activeOverlay.render();
			}
		}
	}

	@Override
	public void displayMessage(String title, String subtitle) {
		this.title = MinecraftFactory.getVars().shortenToWidth(title, 140);
		this.subtitle = MinecraftFactory.getVars().shortenToWidth(subtitle, 140);

		this.timeStarted = MinecraftFactory.getVars().getSystemTime();

		synchronized (LOCK) {
			for (int i = 0; i < activeOverlays.length; i++) {
				if (activeOverlays[i] == null) {
					setOffset(i);
					break;
				}
			}
			if (index == -1) {
				setOffset(activeOverlays.length - 1);
			}
		}
	}

	@Override
	public void displayMessage(String title, String subtitle, Object uniqueReference) {
		displayMessage(title, subtitle);
	}

	private void setOffset(int index) {
		this.index = index;
		activeOverlays[index] = this;
		this.offset = index * 32;
	}

	@Override
	public void displayMessage(String message) {
		displayMessage("The 5zig Mod", message);
	}

	@Override
	public void displayMessage(String message, Object uniqueReference) {
		displayMessage(message);
	}

	@Override
	public void displayMessageAndSplit(String message) {
		List<String> split = MinecraftFactory.getVars().splitStringToWidth(message, 140);
		String title = null;
		String subTitle = null;
		for (int i = 0; i < split.size(); i++) {
			if (i == 0)
				title = split.get(0);
			if (i == 1)
				subTitle = split.get(1);
		}
		displayMessage(title, subTitle);
	}

	@Override
	public void displayMessageAndSplit(String message, Object uniqueReference) {
		displayMessageAndSplit(message);
	}

	private void updateScale() {
		GL11.glViewport(0, 0, mc.d, mc.e);
		GLUtil.matrixMode(GL11.GL_PROJECTION);
		GLUtil.loadIdentity();
		GLUtil.matrixMode(GL11.GL_MODELVIEW);
		GLUtil.loadIdentity();
		this.width = this.mc.d;
		this.height = this.mc.e;
		bca scaledResolution = new bca(this.mc, mc.d, mc.e);
		this.width = scaledResolution.a();
		this.height = scaledResolution.b();
		GLUtil.clear(256);
		GLUtil.matrixMode(GL11.GL_PROJECTION);
		GLUtil.loadIdentity();
		GL11.glOrtho(0.0D, (double) width, (double) height, 0.0D, 1000.0D, 3000.0D);
		GLUtil.matrixMode(GL11.GL_MODELVIEW);
		GLUtil.loadIdentity();
		GLUtil.translate(0.0F, 0.0F, -2000.0F);
	}

	private void render() {
		if ((this.mc == null) || (this.timeStarted == 0L)) {
			return;
		}
		double delta = (MinecraftFactory.getVars().getSystemTime() - this.timeStarted) / 3000.0D;
		if (delta < 0.0D || delta > 1.0D) {
			this.timeStarted = 0L;
			activeOverlays[index] = null;
			return;
		}

		updateScale();

		GLUtil.disableDepth();
		GLUtil.depthMask(false);

		delta *= 2.0D;
		if (delta > 1.0D) {
			delta = 2.0D - delta;
		}
		delta *= 4.0D;
		delta = 1.0D - delta;
		if (delta < 0.0D) {
			delta = 0.0D;
		}
		delta = Math.pow(delta, 3);

		int x = width - 160;
		int y = offset - (int) (delta * 32.0D);
		GLUtil.color(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		MinecraftFactory.getVars().bindTexture(texture);
		GL11.glDisable(GL11.GL_LIGHTING);

		b(x, y, 96, 202, 160, 32); // drawTexturedModalRect
		if (this.title != null) {
			MinecraftFactory.getVars().drawString(this.title, x + 5, y + 7, -256);
		}
		if (this.subtitle != null) {
			MinecraftFactory.getVars().drawString(this.subtitle, x + 5, y + 18, -1);
		}
		bam.c();

		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(32826);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GLUtil.depthMask(true);
		GLUtil.enableDepth();
	}

	public void b() {
		this.timeStarted = 0L;
		this.title = null;
		this.subtitle = null;
	}

}