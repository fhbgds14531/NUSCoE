package nuscoe.prog.fhbgds.entity.ai;

import nuscoe.prog.fhbgds.entity.Entity;

import java.util.Random;

public interface EntityAI {
	
	Random rand = new Random();
	
	public Float[] getTargetCoords(Entity theEntity);
	
}
