package net.runelite.client.plugins.pkerwarning;


//Uncomment this code and comment out the remainder
// if you just want a tab on your screen that highlights
// red when a PKER is near
//import net.runelite.client.ui.overlay.Overlay;
//import net.runelite.client.ui.overlay.OverlayLayer;
//import net.runelite.client.ui.overlay.OverlayPosition;
//import net.runelite.client.ui.overlay.components.PanelComponent;
//import net.runelite.client.ui.overlay.components.TitleComponent;
//
//import javax.inject.Inject;
//import java.awt.*;
//
//public class PkerWarningOverlay extends Overlay
//{
//	private final PanelComponent panelComponent = new PanelComponent();
//	private static final int FLASH_DURATION_MS = 1000; // Duration of the flash in milliseconds
//	private long flashStartTime = -1;
//	private boolean potentialPker;
//
//	@Inject
//	private PkerWarningOverlay()
//	{
//		setPosition(OverlayPosition.TOP_LEFT);
//		setLayer(OverlayLayer.ALWAYS_ON_TOP);
//	}
//
//	public void setPotentialPker(boolean potentialPker)
//	{
//		this.potentialPker = potentialPker;
//	}
//
//	@Override
//	public Dimension render(Graphics2D graphics)
//	{
//		panelComponent.getChildren().clear();
//
//		if (potentialPker)
//		{
//			flashStartTime = System.currentTimeMillis();
//		}
//
//		Color flashColor;
//		long timeElapsed = System.currentTimeMillis() - flashStartTime;
//
//		if (timeElapsed < FLASH_DURATION_MS)
//		{
//			float fraction = (float) timeElapsed / FLASH_DURATION_MS;
//			int red = (int) (Color.RED.getRed() * (1 - fraction) + Color.WHITE.getRed() * fraction);
//			int green = (int) (Color.RED.getGreen() * (1 - fraction) + Color.WHITE.getGreen() * fraction);
//			int blue = (int) (Color.RED.getBlue() * (1 - fraction) + Color.WHITE.getBlue() * fraction);
//			flashColor = new Color(red, green, blue);
//		}
//		else
//		{
//			flashColor = Color.WHITE;
//		}
//
//		panelComponent.getChildren().add(TitleComponent.builder()
//				.text("Pker Warning")
//				.color(flashColor)
//				.build());
//
//		return panelComponent.render(graphics);
//	}
//}
//
//
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

import javax.inject.Inject;
import java.awt.*;

public class PkerWarningOverlay extends Overlay
{

	private static final int FLASH_DURATION_MS = 1000; // Duration of the flash in milliseconds
	private long flashStartTime = -1;
	private boolean potentialPker;
	private final Client client;
	private final PkerWarningPlugin plugin;


	@Inject
	private PkerWarningOverlay(Client client, PkerWarningPlugin plugin)
	{
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_SCENE);
		this.client = client;
		this.plugin = plugin;
	}

		public void setPotentialPker(boolean potentialPker)
	{
		this.potentialPker = potentialPker;
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		Player localPlayer = client.getLocalPlayer();

		if (localPlayer == null)
		{
			return null;
		}

		if (potentialPker)
		{
			flashStartTime = System.currentTimeMillis();
		}

		long timeElapsed = System.currentTimeMillis() - flashStartTime;

		if (timeElapsed < FLASH_DURATION_MS)
		{
			float fraction = (float) timeElapsed / FLASH_DURATION_MS;
			int alpha = (int) (0.1 * 255 * (1 - fraction));
			Color flashColor = new Color(255, 0, 0, alpha);

			// Draw a semi-transparent red rectangle over the entire screen
			graphics.setColor(flashColor);
			graphics.fillRect(0, 0, client.getCanvas().getWidth(), client.getCanvas().getHeight());
		}

		return null; // Return null because we're not using the panel component anymore
	}
}

