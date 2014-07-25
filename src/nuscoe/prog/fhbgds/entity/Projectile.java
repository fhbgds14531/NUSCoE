package nuscoe.prog.fhbgds.entity;


public class Projectile extends Entity {
	
	Entity firer;
	int lifespan;
	public boolean facingLeft;

	public Projectile(float x, float y, float sizeX, float sizeY, float motionX, float motionY, Entity firer, int lifespan) {
		super(x, y, sizeX, sizeY);
		this.motionX = motionX;
		this.motionY = motionY;
		this.firer = (Entity) firer;
		this.lifespan = lifespan;
		this.affectedByGravity = false;
	}
	
	public Projectile(float x, float y, float sizeX, float sizeY, float motionX, float motionY, Entity firer) {
		this(x, y, sizeX, sizeY, motionX, motionY, firer, 120);
	}

	public void entityUpdate(){
		if(this.lifespan <= 0 || this.health < -.5){
			this.isDead = true;
			return;
		}
		this.xPos += this.motionX;
		this.yPos += this.motionY;
		this.getBoundingBox().moveTo(this.xPos, this.yPos);
		this.lifespan--;
		if(this.motionX > 0){
			this.facingLeft = false;
		}else{
			this.facingLeft = true;
		}
		super.entityUpdate();
	}
	
	
}