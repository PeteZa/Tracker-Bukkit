package pizza.bukkitPlugins.tracker;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.LazyMetadataValue;

/**
 * This checks if a player moves location without walking, since they will need to tracking data
 * @author pizza
 *
 */
public class TeleportListener implements Listener{
	/**
	 * If the player is teleported, they will need new data
	 * @param e the event
	 */
	@EventHandler
	public void onTeleport(PlayerTeleportEvent e){
		Player p = e.getPlayer(); 
		Location a = e.getTo(); // get the place they are teleported to
		PosData.invalidate(p); // invalidate their old data
		p.setMetadata("Track", new LazyMetadataValue(MineTracker.getInstance(), new PosData(a))); // add new location to their data
	}
	/**
	 * If the player dies they will need to get new data
	 * @param e the event
	 */
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e){
		Player p = e.getPlayer();
		Location a = e.getRespawnLocation(); // get where they will repawn
		PosData.invalidate(p); // invalidate their old data
		p.setMetadata("Track", new LazyMetadataValue(MineTracker.getInstance(), new PosData(a))); // add new location to their data
	}

}
