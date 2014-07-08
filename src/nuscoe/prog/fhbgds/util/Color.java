package nuscoe.prog.fhbgds.util;

public class Color {

	String name;
	
	float red;
	float green;
	float blue;
	
	public Color(float red, float green, float blue, String uniqueName) throws InvalidNameException{
		if(Colors.isNameUniuqe(uniqueName)){
			this.red = red;
			this.green = green;
			this.blue = blue;
			this.name = uniqueName;
		}else{
			throw new InvalidNameException("The name \"" + uniqueName + "\" is not unique.");
		}
	}
}
