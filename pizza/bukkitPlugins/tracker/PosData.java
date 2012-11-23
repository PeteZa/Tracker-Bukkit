package pizza.bukkitPlugins.tracker;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
/**
 * Class for storing whether or not a track will be left at the current x, and y location.
 * \n This will be stored in a metadata value (Key is track)
 * @author pizza
 *
 */
public class PosData  implements Callable<Object> {
	// private class for storing data
	private class Cell{
		int x;
		int y;
		boolean leaveTrack;
		public Cell(int a,int b){
			x = a/MineTracker.getInstance().getTrackingDist();
			y = b/MineTracker.getInstance().getTrackingDist();
			leaveTrack = false; // default is not to leave a track
		}
	}
	private Object loc; // stored object ( a cell) to be used by the call method	/**
	/**
	 * A new constructor
	 * @param l the position of the player 
	 */
	public PosData(Location l){
		loc = new Cell(l.getBlockX(), l.getBlockZ()); // store a new track in cell
	}
	/**
	 * It returns the cell
	 */
	public Object call() throws Exception {
		return loc;
	}
	////////////////////////////////////////////////////
	// Static function methods to deal with meta data///
	////////////////////////////////////////////////////
	/**
	 * Function for easily finding and invalidating a player's metadata 
	 * @param p The player to invalidate metadata for
	 */
	public static void invalidate(Player p) { 
		List<MetadataValue> bob = p.getMetadata("Track"); // Get the values for the string
		MetadataValue val = find(bob); // find the correct value (There could be more then one if another plugin uses the key "Track")
		val.invalidate(); 
	}
	/**
	 * Get the x and y positions of the players current location
	 * @param p The player to check
	 * @return  Returns the x, and y position (divided by the trackDistance)
	 */
	public static int[] getPosData(Player p){
		List<MetadataValue> bob = p.getMetadata("Track"); // get the tracks
		int[] ret = new int[2]; // prep return array
		MetadataValue val = find(bob); // find the correct value
		ret[0] =((Cell)val.value()).x;
		ret[1] =((Cell)val.value()).y;
		return ret;
		
	}
	/**
	 * Set if the player will leave a track on the current area
	 * @param p the player to set to leave a track for
	 */
	public static void setTrack(Player p){
		// finding their metadata
		List<MetadataValue> bob = p.getMetadata("Track");
		MetadataValue val = find(bob);
		if(p.hasMetadata("notrack")) // if the player doesn't leave tracks
			return;
		((Cell)val.value()).leaveTrack = true; // set whether they leave a track to true
	}
	/**
	 * checks if the player is going to leave a track or not
	 * @param p the player to check
	 * @return if they will leave a track
	 */
	public static boolean getTrack(Player p){
		List<MetadataValue> bob = p.getMetadata("Track");
		MetadataValue val = find(bob);
		return ((Cell)val.value()).leaveTrack;
	}
	private static MetadataValue find(List<MetadataValue> l){
		Iterator<MetadataValue> iter = l.iterator(); // make a iterator
		while(iter.hasNext()){ // go through the list
			MetadataValue temp = iter.next(); //get each vale, it will need to be saved, since it may be used twice
			if(temp.getOwningPlugin().equals(MineTracker.getInstance())){  // make sure the plugin for the metadata is the one I made
				return temp; 
			}
		}
		return null; // Was not found
	}

}
