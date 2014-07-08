package nuscoe.prog.fhbgds.entity;


public class Projectile extends Entity {
	
	Entity firer;
	int lifespan = 120;

	public Projectile(float x, float y, float sizeX, float sizeY, float motionX, float motionY, Entity firer) {
		super(x, y, sizeX, sizeY);
		this.motionX = motionX;
		this.motionY = motionY;
		this.firer = (Entity) firer;
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
		super.entityUpdate();
	}
	
	
}