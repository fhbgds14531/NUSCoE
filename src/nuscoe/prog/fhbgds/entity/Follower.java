package nuscoe.prog.fhbgds.entity;

import nuscoe.prog.fhbgds.NuscoeMain;
import nuscoe.prog.fhbgds.entity.ai.EntityAI;

public class Follower extends AIEntity implements EntityAI{
	
	Entity targetEntity;

	public Follower(float x, float y, float sizeX, float sizeY, Entity entity) {
		super(x, y, sizeX, sizeY);
		this.targetEntity = NuscoeMain.instance.getPlayer();
		this.hasTarget = true;
	}
	
	public void entityUpdate(){
		super.entityUpdate();
		this.travelCooldown = 0;
		this.currentTargetCoords = this.getTargetCoords(targetEntity);
		if(rand.nextFloat() < 0.01) this.getNewTarget();
		if(this.targetEntity.isDead) this.hasTarget = false;
		if(!this.hasTarget) this.getNewTarget();
	}

	@Override
	public Float[] getTargetCoords(Entity theEntity) {
		Float[] coords = new Float[] {theEntity.xPos + (theEntity.sizeX / 2), theEntity.yPos + (theEntity.sizeY / 2)};
		return coords;
	}
	
	public void getNewTarget(){
		if(rand.nextFloat() < 0.01 || !NuscoeMain.instance.getPlayer().isDead){
			Entity e = NuscoeMain.instance.getRandomEntity(this);
			if(this.targetEntity.entityID == e.entityID) this.getNewTarget();
			return;
		}else{
			this.targetEntity = NuscoeMain.instance.getPlayer();
		}
		this.currentTargetCoords = this.getTargetCoords(targetEntity);
		if(this.targetEntity != null) System.out.println("Follower " + this.entityID + " is now targeting " + this.targetEntity.getClass().getName().substring((this.targetEntity.getClass().getName().lastIndexOf(".") + 1)) + " (ID: " + this.targetEntity.entityID + ").");
	}

}
