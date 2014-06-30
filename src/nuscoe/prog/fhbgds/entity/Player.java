package nuscoe.prog.fhbgds.entity;

import org.lwjgl.input.Keyboard;

public class Player extends Entity{

	public Player(float x, float y, float sizeX, float sizeY) {
		super(x, y, sizeX, sizeY);
	}

	public void movePlayer() {
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) this.motionY-= 1.2;
		if(this.motionY < -10) this.motionY = -10;
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) this.motionX-= 1.2;
		if(this.motionX < -10) this.motionX = -10;
		if(Keyboard.isKeyDown(Keyboard.KEY_S)) this.motionY+= 1.2;
		if(this.motionY > 10) this.motionY = 10;
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) this.motionX+= 1.2;
		if(this.motionY > 10) this.motionY = 10;
	}
}
