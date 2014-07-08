package nuscoe.prog.fhbgds.entity;

import nuscoe.prog.fhbgds.NuscoeMain;
import nuscoe.prog.fhbgds.phys.BoundingBox;
import nuscoe.prog.fhbgds.util.Damage;

public class Entity {
	public static int lastEntityID = 0;
	public int entityID;
	public float xPos;
	public float yPos;
	float lastX, lastY;
	public float motionX, motionY;
	public float sizeX, sizeY;
	public float health = 1.0f;
	public boolean isDead = false;
	private BoundingBox bb;
	
	public Entity(float x, float y, float sizeX, float sizeY){
		this.xPos = x;
		this.yPos = y;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.bb = (new BoundingBox(this.xPos, this.yPos, this.sizeX, this.sizeY));
	}
	
	public void entityUpdate(){
		if(!(this instanceof Player) && !(this instanceof Projectile)){
			if(this.health > 1) this.health = 1;
		}
		lastX = this.xPos;
		lastY = this.yPos;
		if(!(this instanceof Projectile)) this.motionX *= 0.8f;
		if(!(this instanceof Projectile)) this.motionY *= 0.8f;
		
		Entity[] ea = NuscoeMain.instance.getLoadedEntities();
		for(int i = 0; i < ea.length; i++){
			int count = 0;
			if(ea[i].entityID != this.entityID && count < 3 && !(ea[i] instanceof Projectile)){
				if(this.getBoundingBox().isInside(ea[i].getBoundingBox())){
					if(ea[i].motionX > this.motionX + 3 && !(this instanceof Projectile)) this.motionX = ea[i].motionX;
					if(ea[i].motionX < this.motionX - 3 && !(this instanceof Projectile)) this.motionX = ea[i].motionX;
					if(ea[i].motionY > this.motionY + 3 && !(this instanceof Projectile)) this.motionY = ea[i].motionY;
					if(ea[i].motionY < this.motionY - 3 && !(this instanceof Projectile)) this.motionY = ea[i].motionY;
					if(ea[i].xPos > this.xPos && !(this instanceof Projectile)) this.motionX *= 0.8;
					if(ea[i].xPos < this.xPos && !(this instanceof Projectile)) this.motionX *= 1.1;
					if(ea[i].yPos > this.yPos && !(this instanceof Projectile)) this.motionY *= 0.8;
					if(ea[i].yPos < this.yPos && !(this instanceof Projectile)) this.motionY *= 1.1;
					count ++;
					if(!NuscoeMain.instance.getPlayer().isDead && NuscoeMain.instance.getPlayer().lives >= 0){
						if(ea[i] instanceof Player && this instanceof Follower){
							Damage.damageEntity(ea[i], Damage.getFollowerSource((Follower)this), 0.05f);
						}else if(!(ea[i] instanceof Projectile) && this instanceof Player){
							Damage.damageEntity(ea[i], Damage.sourcePlayer, 0.1f);
						}else if(this instanceof Projectile && !(ea[i] instanceof Player)){
							Damage.damageEntity(ea[i], Damage.sourcePlayerProjectile, 0.25f);
						}else if(this instanceof Follower && ea[i] instanceof Follower){
							Follower e = (Follower)this;
							if(e.targetEntity.entityID != ea[i].entityID) break;
							Damage.damageEntity(ea[i], Damage.getFollowerSource((Follower)this), 0.1f);
						}
					}
				}
			}
		}
		
		if(this.motionX >  5 && !(this instanceof Projectile)) this.motionX =  5;
		if(this.motionX < -5 && !(this instanceof Projectile)) this.motionX = -5;
		if(this.motionY >  5 && !(this instanceof Projectile)) this.motionY =  5;
		if(this.motionY < -5 && !(this instanceof Projectile)) this.motionY = -5;
		
		if(!(this instanceof Projectile)) this.xPos += this.motionX;
		if(!(this instanceof Projectile)) this.yPos += this.motionY;
		
		if(!(this instanceof Projectile)) this.getBoundingBox().moveTo(this.xPos, this.yPos);
	}

	public BoundingBox getBoundingBox() {
		return bb;
	}
}
