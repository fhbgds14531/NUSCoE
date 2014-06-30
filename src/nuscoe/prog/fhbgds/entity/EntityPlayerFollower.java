package nuscoe.prog.fhbgds.entity;

import nuscoe.prog.fhbgds.NuscoeMain;
import nuscoe.prog.fhbgds.entity.ai.EntityAI;

public class EntityPlayerFollower extends AIEntity implements EntityAI{

	public EntityPlayerFollower(float x, float y, float sizeX, float sizeY) {
		super(x, y, sizeX, sizeY);
	}
	
	public void entityUpdate(){
		super.entityUpdate();
		this.travelCooldown = 0;
		Player thePlayer = NuscoeMain.instance.getPlayer();
		this.currentTargetCoords = new Float[] {thePlayer.xPos, thePlayer.yPos};
	}

	@Override
	public Float[] getTargetCoords(Entity theEntity) {
		Player thePlayer = NuscoeMain.instance.getPlayer();
		Float[] coords = new Float[] {thePlayer.xPos, thePlayer.yPos};
		return coords;
	}

}
