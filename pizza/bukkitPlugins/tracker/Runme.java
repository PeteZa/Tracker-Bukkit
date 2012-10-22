package pizza.bukkitPlugins.tracker;

import org.bukkit.entity.Player;

public class Runme implements Runnable{
	Player p;
	public Runme(Player p){
		this.p =p;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		p.sendMessage(MineTracker.getInstance().getTrackLogger().getTracks(p.getLocation().getBlockX(),p.getLocation().getBlockZ()));
		Pinger.getList().remove(p);
	}
	
}