package pizza.bukkitPlugins.tracker;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.LazyMetadataValue;
/**
 * This is a listener for the player movements, it also stores all the tracks that are one the ground
 * @author pizza
 *
 */
public class TrackLogger implements Listener{	
	private HashMap<String, Tracks> tracks;
	public TrackLogger(){
		tracks = new HashMap<String, Tracks>(200);
	}
	
	/**
	 * Gets the tracks located at the x and y position 
	 * @param x the x location of the track
	 * @param y the y location of the track
	 * @return The string of all the tracks at that location
	 */
	public String getTracks(int x, int y){
	
		int xC =  x/MineTracker.getInstance().getTrackingDist(), yC = y/MineTracker.getInstance().getTrackingDist();
		//MineTracker.plugin.getServer().getLogger().info("Tracking "+ xC + "," + yC);
		if(tracks.containsKey(xC+" " + yC)){
			Tracks track = tracks.get(xC+" " + yC);
			ArrayList<String>  a = track.getTracks();
			if(a.isEmpty())
				return "You find no tracks";
			String ret = "";
			for(int i = 0; i < a.size();i++){
				ret = ret + a.get(i) + System.lineSeparator();
			}
			ret = ret + "END";
			return ret;
		}
		else{
			return "You find no tracks";
		}
	}
	/**
	 * This handles all the creation and storage of tracks, and condition checking
	 * @param e the event
	 */
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e){
		Location loc = e.getTo(); // where the player is moving to
		Player p = e.getPlayer(); // the player
		
		boolean done = PosData.getTrack(p); // If the player has left a track on this area
		if(!done){ // if he has not, it will need to check if he will leave one now.
			if(!MineTracker.getInstance().isAllTracking()){ // If some surfaces do not leave tracks
				int mat = MineTracker.getInstance().getServer().getWorlds().get(0).getBlockAt(loc.getBlockX(), loc.getBlockY()-1, loc.getBlockZ()).getTypeId();
				int stand = loc.getBlock().getTypeId(); // What the player is standing on, e.g snow or water (water will return a 1)
				int check = checkStanding(stand);
				if(check == 2){ // it was snow
					PosData.setTrack(p); // he leaves a track
				}
				else if(check == 0){ // it was empty
					if(checkMat(mat)){ // if the place he is standing on is trackable
						PosData.setTrack(p);
					}
				}
			}
			else // every where the player moves it tracked
			{
				PosData.setTrack(p);
			}
		}
		boolean leaveTrack = PosData.getTrack(p); // check again, now that they may of left a track
		
		int x = loc.getBlockX()/MineTracker.getInstance().getTrackingDist(), y = loc.getBlockZ()/MineTracker.getInstance().getTrackingDist(); // current location
		int [] cl = PosData.getPosData(e.getPlayer()); // the location of their tracks
		if(cl[0] !=x){// has moved off it on the x plane
			PosData.invalidate(e.getPlayer());
			e.getPlayer().setMetadata("Track", new LazyMetadataValue(MineTracker.getInstance(), new PosData(e.getPlayer().getLocation())));
			if(!tracks.containsKey(cl[0]+" " + cl[1]) && leaveTrack){
				tracks.put(cl[0]+" " + cl[1], new Tracks());
			}
			if(x > cl[0]){ // north
				if(leaveTrack){
					Tracks track = tracks.get(cl[0]+" " + cl[1]);
					track.addTrack(p,1);
				}
			}
			else{// south
				if(leaveTrack){
					Tracks track = tracks.get(cl[0]+" " + cl[1]);
					
					track.addTrack(p,3);
				}
			}
			if(p.hasMetadata("autotrack")){
				Pinger.addPLayer(p);
			}
		}
		else if(cl[1] != y){ // moved on the y plane (z in MC)
			PosData.invalidate(e.getPlayer());
			e.getPlayer().setMetadata("Track", new LazyMetadataValue(MineTracker.getInstance(), new PosData(e.getPlayer().getLocation())));
			if(!tracks.containsKey(cl[0]+" " + cl[1]) && leaveTrack){
				tracks.put(cl[0]+" " + cl[1], new Tracks());
			}
			if(y > cl[1]){ //West
				if(leaveTrack){
					Tracks track = tracks.get(cl[0]+" " + cl[1]);
					track.addTrack(p,2);
				}
			}
			else{ // East
				if(leaveTrack){
					Tracks track = tracks.get(cl[0]+" " + cl[1]);
					track.addTrack(p,4);
				}
			}
			if(p.hasMetadata("autotrack")){
				Pinger.addPLayer(p);
			}
		}
	}
	
	
	private  boolean checkMat(int mat){
		int[] trackableSurfaces = MineTracker.getInstance().getTrackableS();
		for(int i = 0; i < trackableSurfaces.length; i++){
			if(mat == trackableSurfaces[i])
				return true;
		}
		return false;
	}
	private int checkStanding(int st){
		if(st == 9){
			return 1; 
		}
		else if(st == 78)
			return 2;
		return 0;
	}
}
