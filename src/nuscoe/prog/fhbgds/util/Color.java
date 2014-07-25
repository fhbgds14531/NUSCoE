package nuscoe.prog.fhbgds.util;

public class Color {

	String name;
	
	float red;
	float green;
	float blue;
	
	public Color(float red, float green, float blue, String uniqueName) throws InvalidNameException{
		if(Colors.isNameUnique(uniqueName)){
			this.red = red;
			this.green = green;
			this.blue = blue;
			this.name = uniqueName;
		}else{
			throw new InvalidNameException("The name \"" + uniqueName + "\" is not unique.");
		}
	}
	
	public String getName(){
		return new String(this.name);
	}

	public void setRGB(Float[] values){
		this.red   = values[0];
		this.green = values[1];
		this.blue  = values[2];
	}
}
