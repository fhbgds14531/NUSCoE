package nuscoe.prog.fhbgds.util;

import nuscoe.prog.fhbgds.NuscoeMain;
import nuscoe.prog.fhbgds.entity.Entity;
import nuscoe.prog.fhbgds.entity.Follower;
import nuscoe.prog.fhbgds.entity.Player;

public class Damage {

	public int id = -1;
	public Entity source;
	
	public static Damage sourcePlayer = new Damage(0, NuscoeMain.instance.getPlayer());
	public static Damage sourcePlayerProjectile = new Damage(1, NuscoeMain.instance.getPlayer());
	
	private Damage(){
		this(-1, null);
	}
	
	private Damage(int id, Entity source){
		this.id = id;
		this.source = source;
	}
	
	public static void damageEntity(Entity entity, Damage source, float amount){
		if(entity.isDead) return;
		if(source.source == null){
			entity.health -= amount;
		}else{
			entity.health -= amount;
			if(source.source instanceof Player){
				Player player = (Player) source.source;
				if(player.health < 2) source.source.health += (amount/16);
			}else{
				if(source.source.health < 1) source.source.health += (amount/32);
			}
			
		}
		if(entity.health <= 0){
			entity.isDead = true;
			System.out.println(entity.getClass().getName().substring(entity.getClass().getName().lastIndexOf(".") + 1) + " (ID: " + entity.entityID + ") was killed by " + (source.source == null? "UNKNOWN." : source.source.getClass().getName().substring(source.source.getClass().getName().lastIndexOf(".") + 1) + "(ID: " + source.source.entityID + ")."));
		}
	}
	
	public static Damage getFollowerSource(Follower f){
		Damage d = new Damage(2, f);
		return d;
	}
	
}
