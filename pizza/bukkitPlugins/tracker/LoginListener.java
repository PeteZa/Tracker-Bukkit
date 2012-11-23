package pizza.bukkitPlugins.tracker;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.metadata.LazyMetadataValue;
/**
 * A listener for player logins
 * @author pizza
 *
 */
public class LoginListener implements Listener{
	/**
	 * Sets up the metadata when a player logs in
	 * @param e The login event for the player logging in
	 */
	@EventHandler
	public void onLogin(PlayerLoginEvent e){
		Player p = e.getPlayer();
		if(!p.hasMetadata("Track")){ // if they don't have any meta data, make a new one
			p.setMetadata("Track", new LazyMetadataValue(MineTracker.getInstance(), new PosData(p.getLocation())));
		}
		else{
			PosData.invalidate(p); // destroy their old data and make a new one
			p.setMetadata("Track", new LazyMetadataValue(MineTracker.getInstance(), new PosData(p.getLocation())));
		}
	}

}
