package nuscoe.prog.fhbgds.entity;

import nuscoe.prog.fhbgds.NuscoeMain;

import org.lwjgl.input.Keyboard;

public class Player extends Entity{

	public int lives = 4;
	
	public Player() {
		this(-10, 230, 80, 160);
	}
	
	public Player(float x, float y, float sizeX, float sizeY){
		super(x, y, sizeX, sizeY);
		this.health = 2;
	}
	
	public void entityUpdate(){
		if(this.health > 2) this.health = 2;
		super.entityUpdate();
	}
	
	public void movePlayer() {
		if(this.yPos + this.sizeY >= NuscoeMain.instance.height * 0.49f - 55) this.jumping = false;
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE) && this.yPos + this.sizeY >= 230 && !jumping){
			this.motionY = -30;
			this.jumping = true;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A) || Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
			this.motionX-= 1.5;
			if(this.motionX < 2)this.facingLeft = true;
		}
		if(this.motionX < -10) this.motionX = -10;
		if(Keyboard.isKeyDown(Keyboard.KEY_D) || Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
			this.motionX+= 1.5;
			if(this.motionX > -2) this.facingLeft = false;
		}
		if(this.motionY > 10) this.motionY = 10;
	}
}
