package nuscoe.prog.fhbgds.phys;

import nuscoe.prog.fhbgds.NuscoeMain;
import nuscoe.prog.fhbgds.entity.Entity;
import nuscoe.prog.fhbgds.util.Color;
import nuscoe.prog.fhbgds.util.Colors;

public class Block {

	private BoundingBox bb;
	private float x,y,sizeX,sizeY;
	private int blockID;
	public static int lastID = -1;
	public Color color;
	
	public Block(float xPos, float yPos, float sizeX, float sizeY){
		this(xPos, yPos, sizeX, sizeY, "White");
	}
	
	public Block(float xPos, float yPos, float sizeX, float sizeY, String colorName){
		this.x = xPos;
		this.y = yPos;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.bb = new BoundingBox(x, y, sizeX, sizeY);
		this.color = Colors.getColorByName(colorName);
	}

	public Block(float xPos, float yPos, float sizeX, float sizeY, Color color){
		this.x = xPos;
		this.y = yPos;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.bb = new BoundingBox(x, y, sizeX, sizeY);
		this.color = color;
	}
	
	public void doBlockUpdate(){
		Entity[] ea = NuscoeMain.instance.getLoadedEntities();
		for(int i = 0; i < ea.length; i++){
			if(ea[i] != null){
				if(this.getBoundingBox().isInside(ea[i].getBoundingBox())){
					if(ea[i].yPos < this.y && ea[i].motionY > 0){
						ea[i].motionY = -.01f;
						ea[i].yPos = this.y - ea[i].sizeY - 0.1f;
						ea[i].moveBBTo(ea[i].xPos, ea[i].yPos);
					}else if(ea[i].yPos > this.y && ea[i].motionY < 0){
						ea[i].motionY = .01f;
						ea[i].yPos = this.y + this.sizeY + 0.1f;
						ea[i].moveBBTo(ea[i].xPos, ea[i].yPos);
					}else if(ea[i].xPos < this.x && ea[i].motionX > 0){
						ea[i].motionX = -.01f;
						ea[i].xPos = this.x - ea[i].sizeX - 0.1f;
						ea[i].moveBBTo(ea[i].xPos, ea[i].yPos);
					}else if(ea[i].xPos > this.x && ea[i].motionX < 0){
						ea[i].motionX = .01f;
						ea[i].xPos = this.x + this.sizeX + 0.1f;
						ea[i].moveBBTo(ea[i].xPos, ea[i].yPos);
					}else{
						ea[i].motionX = 0;
						ea[i].motionY = 0;
						ea[i].moveBBTo(ea[i].xPos, ea[i].yPos);
					}
				}
			}
		}
	}
	
	public int getBlockID(){
		return blockID;
	}
	
	public void setBlockID(int i){
		this.blockID = i;
	}
	
	public Float[] getSizeAndPos(){
		return new Float[] {this.x, this.y, this.sizeX, this.sizeY};
	}
	
	public BoundingBox getBoundingBox(){
		return this.bb;
	}
}
