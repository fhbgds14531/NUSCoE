package nuscoe.prog.fhbgds.entity;

import nuscoe.prog.fhbgds.NuscoeMain;
import nuscoe.prog.fhbgds.phys.BoundingBox;
import nuscoe.prog.fhbgds.util.Damage;

public class Entity {
	public static int lastEntityID = -1;
	public int entityID;
	public float xPos;
	public float yPos;
	float lastX, lastY;
	public float motionX, motionY;
	public float sizeX, sizeY;
	public float health = 1.5f;
	public boolean isDead = false;
	private BoundingBox bb;
	public boolean jumping;
	public int jumpCounter = 0;
	public boolean affectedByGravity = true;
	public boolean facingLeft;
	
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
		if(!(this instanceof Projectile)) this.motionX *= 0.75f;
		if(this.motionX < 0.0001 && this.motionX > -0.0001) this.motionX = 0;
		if(!(this instanceof Projectile)) this.motionY *= 0.75f;
		if(this.motionY < 0.0005 && this.motionY > -0.0005) this.motionY = 0;
		
		Entity[] ea = NuscoeMain.instance.getLoadedEntities();
		for(int i = 0; i < ea.length; i++){
			Entity otherEntity = ea[i];
			if(otherEntity.entityID != this.entityID && !(otherEntity instanceof Projectile)){
				if(this.getBoundingBox().isInside(otherEntity.getBoundingBox())){
					if(otherEntity.xPos > this.xPos && !(this instanceof Projectile)) this.motionX *= 0.8;
					if(otherEntity.xPos < this.xPos && !(this instanceof Projectile)) this.motionX *= 1.1;
					if(otherEntity.yPos > this.yPos && !(this instanceof Projectile)) this.motionY *= 0.8;
					if(otherEntity.yPos < this.yPos && !(this instanceof Projectile)) this.motionY *= 1.1;
					if(!NuscoeMain.instance.getPlayer().isDead){
						if(
							otherEntity instanceof Player && 
							this instanceof Follower && 
							NuscoeMain.instance.playerDamageCooldown <= 0
							){
							Damage.damageEntity(otherEntity, Damage.getFollowerSource((Follower)this), 0.33f);
							NuscoeMain.instance.playerDamageCooldown += 10;
						}else if(
								this instanceof Projectile && 
								!(otherEntity instanceof Player)
								){
							Damage.damageEntity(otherEntity, Damage.sourcePlayerProjectile, otherEntity.health);
							this.health = -1;
						}else if(
								this instanceof Follower && 
								otherEntity instanceof Follower
								){
							Follower e = (Follower)this;
							if(e.targetEntity.entityID != e.entityID) break;
							Damage.damageEntity(e, Damage.getFollowerSource((Follower)this), 0.1f);
						}
					}
				}
			}
		}
		
		if(this.jumping){
			this.jumpCounter++;
		}else{
			this.jumpCounter = 0;
		}
		
		if(!this.jumping && ! (this instanceof Projectile || this instanceof Follower)){
			if(this.affectedByGravity) this.motionY += 10;
		}else{
			if(this.jumpCounter > 10){
				if(this.affectedByGravity) this.motionY += 2.5;
			}else{
				if(this.affectedByGravity) this.motionY += 0.75;
			}
		}
		
		if(this.motionX >  5 && !(this instanceof Projectile)) this.motionX =  5;
		if(this.motionX < -5 && !(this instanceof Projectile)) this.motionX = -5;
		if(this.motionY >  7 && !(this instanceof Projectile)) this.motionY =  7;
		if(this.motionY < -10 && !(this instanceof Projectile) && !(this instanceof Player)) this.motionY = -10;
		
		if(!(this instanceof Projectile)) this.xPos += this.motionX;
		if(!(this instanceof Projectile)) this.yPos += this.motionY;
		
		if(!(this instanceof Projectile)) this.moveBBTo(this.xPos, this.yPos);
		if(this.motionX > 0) this.facingLeft = false; else if(this.motionX < 0) this.facingLeft = true;
	}
	
	public void moveBBTo(float x, float y){
		this.bb.moveTo(x, y);
	}

	public BoundingBox getBoundingBox() {
		return bb;
	}
}
