package pizza.bukkitPlugins.tracker;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public class Teller {
	private static Teller teller;
	private static MineTracker plugin;
	private static ArrayList<Player> playerList;
	private class Message  implements Runnable{
		private Player p;
		private int x;
		private int y;
		public Message(Player p,int x,int y){
			this.p = p;
			this.x =x;
			this.y= y;
		}
		@Override
		public void run() {
			playerList.remove(p);
			p.sendMessage(MineTracker.getInstance().getTrackLogger().getTracks(p.getLocation().getBlockX(),p.getLocation().getBlockZ()));
		}
		
	}
	private Teller(){
		plugin = MineTracker.getInstance();
		playerList = new ArrayList<Player>(plugin.getServer().getMaxPlayers());
	}
	public static Teller getInstance(){
		if(teller == null){
			teller = new Teller();
		}
		return teller;
	}
	public static void addPlayerMes(Player p, int x, int y){
		if(playerList.contains(p))
			return;
		playerList.add(p);
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Message(p,x,y), 2L);
	}
}
