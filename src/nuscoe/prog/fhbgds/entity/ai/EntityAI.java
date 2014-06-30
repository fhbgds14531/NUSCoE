package nuscoe.prog.fhbgds.entity.ai;

import java.util.Random;

import nuscoe.prog.fhbgds.entity.Entity;

public interface EntityAI {
	
	Random rand = new Random();
	
	public Float[] getTargetCoords(Entity theEntity);
	
}
