package nuscoe.prog.fhbgds.phys;

public class BoundingBox {
	float x,y;
	float sizeX, sizeY;
	
	public BoundingBox(float x, float y, float size){
		this(x, y, size, size);
	}
	
	public BoundingBox(float x, float y, float sizeX, float sizeY){
		this.x = x;
		this.y = y;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
	}
	
	public BoundingBox expand(float xAmount, float yAmount){
		BoundingBox bb = new BoundingBox(this.x - xAmount, this.y - yAmount, this.sizeX + (2*xAmount), this.sizeY + (2*yAmount));
		return bb;
		
	}
	
	public boolean isInside(BoundingBox bb){
		if((bb.x + bb.sizeX) < this.x || bb.x > (this.x + this.sizeX)) return false;
		if((bb.y + bb.sizeY) < this.y || bb.y > (this.y + this.sizeY)) return false;
		return true;
	}
	
	public void moveTo(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public void moveBy(float xAmount, float yAmount){
		this.x += xAmount;
		this.y += yAmount;
	}

	public void expandTo(float f, float g) {
		this.sizeX = f;
		this.sizeY = g;
	}
}
