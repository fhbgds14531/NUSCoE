package nuscoe.prog.fhbgds.entity;

import nuscoe.prog.fhbgds.entity.ai.EntityAI;

public class AIEntity extends Entity implements EntityAI{

	public Float[] currentTargetCoords;
	public boolean hasTarget = false;
	public boolean needTravelX = false;
	public boolean needTravelY = false;
	boolean waitingToTravel = false;
	public int travelCooldown;
	
	public AIEntity(float x, float y, float sizeX, float sizeY) {
		super(x, y, sizeX, sizeY);
		this.currentTargetCoords = this.getTargetCoords(this);
	}
	
	public void entityUpdate(){
		super.entityUpdate();
		if(!this.hasTarget && this.travelCooldown <= 0){
			this.currentTargetCoords = this.getTargetCoords(this);
			this.hasTarget = true;
		}else if(!this.hasTarget && this.travelCooldown > 0){
			this.travelCooldown--;
		}else if(this.hasTarget){
			this.continueMoving();
		}
		if(this.lastX == this.xPos && this.lastY == this.yPos && !this.waitingToTravel){
			this.hasTarget = false;
			this.needTravelX = false;
			this.needTravelY = false;
			this.currentTargetCoords = new Float[] {this.xPos, this.yPos};
			this.waitingToTravel = true;
		}
	}
	
	public void continueMoving(){
		if(this.xPos < this.currentTargetCoords[0] + 20 && this.xPos > this.currentTargetCoords[0] - 20){
			this.needTravelX = false;
		}else{
			this.needTravelX = true;
		}
		if(this.yPos < this.currentTargetCoords[1] + 20 && this.yPos > this.currentTargetCoords[1] - 0){
			this.needTravelY = false;
		}else{
			this.needTravelY = true;
		}
		if(!this.needTravelX && !this.needTravelY){
			this.hasTarget = false;
			this.travelCooldown = rand.nextInt(50);
			this.waitingToTravel = true;
		}
		if(this.hasTarget){
			int maxSpeed = 5;
			if(this.needTravelX){
				if(this.currentTargetCoords[0] < this.xPos){
					this.motionX -= 1.2;
				}
				if(this.motionX < -maxSpeed) this.motionX = -maxSpeed;
				if(this.currentTargetCoords[0] > this.xPos){
					this.motionX += 1.2;
				}
				if(this.motionX > maxSpeed) this.motionX = maxSpeed;
			}
			if(this.needTravelY){
				if(this.currentTargetCoords[1] < this.yPos){
					this.motionY -= 1.2;
				}
				if(this.motionY < -maxSpeed) this.motionY = -maxSpeed;
				if(this.currentTargetCoords[1] > this.yPos){
					this.motionY += 1.2;
				}
				if(this.motionY > maxSpeed) this.motionY = maxSpeed;
			}
			this.waitingToTravel = false;
		}
	}
	
	@Override
	public Float[] getTargetCoords(Entity ent) {
		Float[] coords = new Float[] {ent.xPos, ent.yPos};
		
		int coordDistance = 100;
		
		if(rand.nextInt(100) < 50){
			coords[0] += rand.nextInt(coordDistance);
			if(coords[0] > 400 - ent.sizeX) coords[0] = 400 - ent.sizeX;
		}else{
			coords[0] -= rand.nextInt(coordDistance);
			if(coords[0] < -400) coords[0] = -400f;
		}
		if(rand.nextInt(100) < 50){
			coords[1] += rand.nextInt(coordDistance);
			if(coords[1] > 300 - ent.sizeY) coords[1] = 300 - ent.sizeY;
		}else{
			coords[1] -= rand.nextInt(coordDistance);
			if(coords[1] < -300) coords[1] = -300f;
		}
		return coords;
	}

}
