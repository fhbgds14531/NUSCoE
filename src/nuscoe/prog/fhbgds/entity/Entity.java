package nuscoe.prog.fhbgds.entity;

import nuscoe.prog.fhbgds.NuscoeMain;
import nuscoe.prog.fhbgds.phys.BoundingBox;

public class Entity {
	public static int lastEntityID = 0;
	public int entityID;
	public float xPos;
	public float yPos;
	float lastX, lastY;
	public float motionX, motionY;
	public float sizeX, sizeY;
	BoundingBox bb;
	
	public Entity(float x, float y, float sizeX, float sizeY){
		this.xPos = x;
		this.yPos = y;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.bb = new BoundingBox(this.xPos, this.yPos, this.sizeX, this.sizeY);
	}
	
	public void entityUpdate(){
		lastX = this.xPos;
		lastY = this.yPos;
		this.motionX *= 0.9f;
		this.motionY *= 0.9f;
		
		Entity[] ea = NuscoeMain.instance.getLoadedEntities();
		for(int i = 0; i < ea.length; i++){
			if(ea[i].entityID != this.entityID){
				if(this.bb.isInside(ea[i].bb)){
					if(ea[i].xPos < this.xPos) this.motionX = 1.2f;
					if(ea[i].xPos > this.xPos) this.motionX = -1.2f;
					if(ea[i].yPos < this.yPos) this.motionY = 1.2f;
					if(ea[i].yPos > this.yPos) this.motionY = -1.2f;
				}
			}
		}
		
		this.xPos += this.motionX;
		this.yPos += this.motionY;
	}
	
}
