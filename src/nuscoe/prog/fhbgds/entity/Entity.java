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
	public float health = 1.0f;
	public boolean isDead = false;
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
		
		if(this.health <= 0){
			this.isDead = true;;
		}
		
		Entity[] ea = NuscoeMain.instance.getLoadedEntities();
		for(int i = 0; i < ea.length; i++){
			int count = 0;
			if(ea[i].entityID != this.entityID && count < 3){
				if(this.bb.isInside(ea[i].bb)){
					if(ea[i].motionX > this.motionX + 3) this.motionX = ea[i].motionX;
					if(ea[i].motionX < this.motionX - 3) this.motionX = ea[i].motionX;
					if(ea[i].motionY > this.motionY + 3) this.motionY = ea[i].motionY;
					if(ea[i].motionY < this.motionY - 3) this.motionY = ea[i].motionY;
					if(ea[i].xPos > this.xPos) this.motionX *= 0.8;
					if(ea[i].xPos < this.xPos) this.motionX *= 1.1;
					if(ea[i].yPos > this.yPos) this.motionY *= 0.8;
					if(ea[i].yPos < this.yPos) this.motionY *= 1.1;
					count ++;
					this.health -= 0.15;
				}
			}
		}
		
		if(this.motionX >  5) this.motionX =  5;
		if(this.motionX < -5) this.motionX = -5;
		if(this.motionY >  5) this.motionY =  5;
		if(this.motionY < -5) this.motionY = -5;
		
		this.xPos += this.motionX;
		this.yPos += this.motionY;
		
		this.bb.moveTo(this.xPos, this.yPos);
	}
	
}
