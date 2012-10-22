package pizza.bukkitPlugins.tracker;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public class Pinger {
	
	private static ArrayList<Player> list;
	public static void addPLayer(Player p){
		if(list == null)
			list = new ArrayList<Player>();
		if(!list.contains(p)){
			list.add(p);	
			MineTracker.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(MineTracker.getInstance(), new Runme(p), 2L);
		}
	}
	public static ArrayList<Player> getList(){
		return list;
	}
}
