package net.runelite.client.plugins.pkerwarning;

import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.Varbits;
import net.runelite.api.clan.ClanChannel;
import net.runelite.api.clan.ClanChannelMember;
import net.runelite.api.events.GameTick;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import java.util.List;

@PluginDescriptor(
		name = "Pker Alert",
		description = "A plugin made by K R Y A N that detects if a potential PKER is in radius"
)
public class PkerWarningPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private PkerWarningOverlay pkerWarningOverlay;

	@Override
	protected void startUp()
	{
		overlayManager.add(pkerWarningOverlay);
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(pkerWarningOverlay);
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		Player localPlayer = client.getLocalPlayer();
		if (localPlayer == null)
		{
			return;
		}

		// Check if the player is in the Wilderness
		if (!isInWilderness())
		{
			pkerWarningOverlay.setPotentialPker(false);
			return;
		}

		for (Player player : client.getPlayers())
		{
			if (player == null || player == localPlayer)
			{
				continue;
			}

			if (isPotentialPker(player))
			{
				pkerWarningOverlay.setPotentialPker(true);
				return;
			}
		}

		pkerWarningOverlay.setPotentialPker(false);
	}

	private boolean isInWilderness()
	{
		int inWildernessVarbit = client.getVar(Varbits.IN_WILDERNESS);
		return inWildernessVarbit == 1;
	}


	private boolean isPotentialPker(Player player)
	{
		String playerName = player.getName();

		// Check if the player is not on your friends list
		boolean notOnFriendsList = !client.isFriended(playerName, false);

		// Check if the player is not in the same clan chat
		boolean notInClanChat = true;
		ClanChannel clanChannel = client.getClanChannel();
		if (clanChannel != null) {
			List<ClanChannelMember> members = clanChannel.getMembers();
			notInClanChat = members.stream().noneMatch(member -> member.getName().equalsIgnoreCase(playerName));
		}

		return notOnFriendsList && notInClanChat;
	}
}

