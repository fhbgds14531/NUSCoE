package nuscoe.prog.fhbgds.entity;

import nuscoe.prog.fhbgds.NuscoeMain;
import nuscoe.prog.fhbgds.entity.ai.EntityAI;

public class Follower extends AIEntity implements EntityAI{
	
	Entity targetEntity;

	public Follower(float x, float y, float sizeX, float sizeY, Entity entity) {
		super(x, y, sizeX, sizeY);
		this.targetEntity = entity;
	}
	
	public void entityUpdate(){
		super.entityUpdate();
		this.travelCooldown = 0;
		this.currentTargetCoords = this.getTargetCoords(targetEntity);
		if(rand.nextFloat() < 0.01) this.targetEntity = NuscoeMain.instance.getRandomEntity(this);
	}

	@Override
	public Float[] getTargetCoords(Entity theEntity) {
		Float[] coords = new Float[] {theEntity.xPos, theEntity.yPos};
		return coords;
	}

}
