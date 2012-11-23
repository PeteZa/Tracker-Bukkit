package pizza.bukkitPlugins.tracker;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.metadata.LazyMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
/**
 * The main class for the plugin
 * @author pizza
 *
 */
public class MineTracker extends JavaPlugin {
	private static MineTracker plugin;
	private TrackLogger logMan;
	private YamlConfiguration config;
	private  int[] trackableS;
	private int trackingDist;
	private long trackLastTime;
	private boolean allTracking;
		/**
	 * Method used by the server to start the plugin
	 */
	public void onEnable(){
		plugin = this;
		// Check and create new config file with defaults
		File configF = new File(this.getDataFolder()+ "/config.yml");
		config = YamlConfiguration.loadConfiguration(configF);
		if(!configF.exists()){
			List<Integer> value = new LinkedList<Integer>();
			value.add(2);
			value.add(3);
			value.add(12);
			value.add(13);
			value.add(18);
			value.add(110);
			value.add(111);
			value.add(82);
			value.add(88);
			value.add(78);
			value.add(80);
			this.getDataFolder().mkdir();
			YamlConfiguration MakeC = new YamlConfiguration();
			MakeC.set("Track degrade time (in minutes)", 20L);
			MakeC.set("Track save distance", 20);
			MakeC.set("Disable Trackable Surfaces", false);
			MakeC.set("Trackable Surfaces", value );
			try {
				MakeC.save(new File(this.getDataFolder()+"/config.yml"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// get the config and load into memory
		config = YamlConfiguration.loadConfiguration(configF);
		trackLastTime = (20*60*config.getLong("Track degrade time (in minutes)"));
		allTracking= MineTracker.getInstance().getConfig().getBoolean("Disable Trackable Surfaces");
		List<Integer> toArray= MineTracker.getInstance().getConfig().getIntegerList("Trackable Surfaces");
		int [] array = new int[toArray.size()];
		for(int i=0; i < array.length;i++){
			array[i] = toArray.get(i).intValue();
		}
		trackableS = array;// set up the surfaces to leave tracks on
		trackingDist = MineTracker.getInstance().getConfig().getInt("Track save distance");
		// Event registration
		logMan = new TrackLogger(); // Start logging tracks
		this.getServer().getPluginManager().registerEvents(logMan, this);
		this.getServer().getPluginManager().registerEvents(new LoginListener(), this);
		this.getServer().getPluginManager().registerEvents(new TeleportListener(), this);
		this.getServer().getLogger().info("Tacker Enabled");
	}
	/**
	 * Method used by the server to end the plugin
	 */
	public void onDisable(){
		this.getServer().getLogger().info("Tacker disabled");
	}
	/**
	 * This method is called by the server when a player(or server console) does a command
	 */
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if(cmd.getName().equalsIgnoreCase("track")){
			Player send = this.getServer().getPlayer(sender.getName()); // get the player
			if(send== null) // if they did not exist
				return false;
			sender.sendMessage(logMan.getTracks(send.getLocation().getBlockX(),send.getLocation().getBlockZ())); //get the tracks at their current location
			return true;
		}
		else if((cmd.getName().equalsIgnoreCase("autotrack"))){
			Player p = this.getServer().getPlayer(sender.getName());
			if(p == null) // Was a server
				return false;	
			if(p.hasMetadata("autotrack")){ // if they had autotrack on
				p.removeMetadata("autotrack", this);
				p.sendMessage("AutoTracking Disabled");
			}
			else{ // if it was off
				p.setMetadata("autotrack", new LazyMetadataValue(this, new Callable<Object>(){				
					@Override public Object call() throws Exception { return new Object();}}));
				p.sendMessage("AutoTracking enabled");
			}
			return true;
		}
		return false;
	}
	/**
	 * This is so you can get the plugin easily
	 * @return The current plugin initialized by the server
	 */
	public static MineTracker getInstance(){
		return plugin;// I will not create a instance of this, since the server will auto create it.
	}
	/**
	 * 
	 * @return The movement listener that handles the leaving of tracks
	 */
	public TrackLogger getTrackLogger(){
		return logMan;
	}
	/**
	 * @return The current configuration
	 */
	public YamlConfiguration getConfig(){
		return config;
	}
	public int[] getTrackableS() {
		return trackableS;
	}
	public int getTrackingDist() {
		return trackingDist;
	}
	public boolean isAllTracking() {
		return allTracking;
	}
	public long getTrackLastTime() {
		return trackLastTime;
	}
	


}
