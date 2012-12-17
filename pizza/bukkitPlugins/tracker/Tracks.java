package pizza.bukkitPlugins.tracker;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.entity.Player;

/**
 * 
 * @author pizza
 *
 */
public class Tracks{
	
	private class Cell implements Runnable{
		int id;
		
		int dir;
		public Cell(int i, int d ){
			id = i;
			dir = d;
			
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			dir = -1;
		}
	}
	private List<Cell> listTracks;
	public Tracks(){
		listTracks = new LinkedList<Cell>();
	}
	public void addTrack(Player p,int dir){
		if(!exists(p.getEntityId(), dir)){
			Cell bob = new Cell(p.getEntityId(), dir);
			listTracks.add(bob);
			MineTracker.getInstance().getServer().getScheduler().scheduleAsyncDelayedTask(MineTracker.getInstance(),bob , MineTracker.getInstance().getTrackLastTime());
		}
	}
	public ArrayList<String>  getTracks(){
		Iterator<Cell> iter = listTracks.iterator();
		int n = 0,w = 0,e = 0,s = 0;
		while(iter.hasNext()){
			Cell temp = iter.next();
				if(temp.dir == 1){
					n++;
				}
				else if(temp.dir == 2){
					e++;
				}
				else if(temp.dir == 3){
					s++;
				}
				else if(temp.dir == 4)
					w++;
				else
					iter.remove();
					
		}
		ArrayList<String> ret = new ArrayList<String>();
		if(n != 0)
		ret.add(howMany(n) + "headed East. ");
		if(e != 0)
			ret.add(howMany(e) + "headed South. ");
		if(s!= 0)
			ret.add(howMany(s) + "headed West. "); 
		if(w != 0)
			ret.add(howMany(w) + "headed North. ");
		return ret;
	}
	private String howMany(int c){
		if(c == 0)
			return "There are no tracks ";
		else if(c <= 1)
			return "There is one track ";
		else if(c == 3)
			return "There are a couple tracks ";
		else if(c == 3 || c == 4)
			return "There are a few tracks ";
		else 
			return "There are a lot of tracks ";
	}
	private boolean exists(int id, int dir){
		boolean found = false;
		Iterator<Cell> iter = listTracks.iterator();
		while(!found && iter.hasNext()){
			Cell temp = iter.next();
			if(temp.id == id && temp.dir == dir)
				found = true;
		}
		return found;
	}
	

}
