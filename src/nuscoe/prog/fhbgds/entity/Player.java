package nuscoe.prog.fhbgds.entity;

import org.lwjgl.input.Keyboard;

public class Player extends Entity{

	public int lives = 4;
	
	public Player() {
		this(-10, -10, 20, 20);
	}
	
	public Player(float x, float y, float sizeX, float sizeY){
		super(x, y, sizeX, sizeY);
		this.health = 2;
	}
	
	public void growAndHeal(){
		if(this.isDead) return;
		if(this.sizeX < 20 || this.sizeY < 0){
			this.sizeX += 0.1;
			this.sizeY += 0.1;
			this.getBoundingBox().expand(0.1f, 0.1f);
		}
		if(this.health < 1.75f){
			this.health += 0.00075;
		}
	}

	public void entityUpdate(){
		if(this.health > 2) this.health = 2;
		super.entityUpdate();
	}
	
	public void movePlayer() {
		if(Keyboard.isKeyDown(Keyboard.KEY_M)){
			if(this.sizeX > 0){
				this.sizeX -= 0.5f;
				this.sizeY -= 0.5f;
			}
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_UP)) this.motionY-= 1.2;
		if(this.motionY < -10) this.motionY = -10;
		if(Keyboard.isKeyDown(Keyboard.KEY_A) || Keyboard.isKeyDown(Keyboard.KEY_LEFT)) this.motionX-= 1.2;
		if(this.motionX < -10) this.motionX = -10;
		if(Keyboard.isKeyDown(Keyboard.KEY_S) || Keyboard.isKeyDown(Keyboard.KEY_DOWN)) this.motionY+= 1.2;
		if(this.motionY > 10) this.motionY = 10;
		if(Keyboard.isKeyDown(Keyboard.KEY_D) || Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) this.motionX+= 1.2;
		if(this.motionY > 10) this.motionY = 10;
	}
}
