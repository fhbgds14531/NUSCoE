package nuscoe.prog.fhbgds.util;

import org.lwjgl.opengl.GL11;

public class Colors {

	private static Color[] colors = new Color[32];
	
	public Colors(){
		try {
			colors[0]  = new Color(1,    1,    1,    "White"          );
			colors[1]  = new Color(0,    0,    0,    "Black"          );
			colors[2]  = new Color(0.2f, 0.2f, 0.2f, "Dark Grey"      );
			colors[3]  = new Color(0.4f, 0.4f, 0.4f, "Medium Grey"    );
			colors[4]  = new Color(0.8f, 0.8f, 0.8f, "Light Grey"     );
			colors[5]  = new Color(1,    0,    0,    "Pure Red"       );
			colors[6]  = new Color(0,    1,    0,    "Pure Green"     );
			colors[7]  = new Color(0,    0,    1,    "Pure Blue"      );
			colors[8]  = new Color(1,    0.4f, 0.4f, "Pastel Red"     );
			colors[9]  = new Color(0.4f, 1,    0.4f, "Pastel Green"   );
			colors[10] = new Color(0.4f, 0.4f, 1,    "Pastel Blue"    );
			colors[11] = new Color(0.3f, 0.3f, 0.3f, "Background Grey");
			colors[12] = new Color(1,    1,    0,    "Yellow"         );
			colors[13] = new Color(1,    0.53f,0,    "Orange"         );
			colors[13] = new Color(0,    0.6f, 0.8f, "Sky Blue"       );
			colors[14] = new Color(0,    0.55f,0,    "Grass"          );
			colors[15] = new Color(0.42f,0.33f,0.7f, "Sign Green"     );
		} catch (InvalidNameException e) {
			System.err.println(e.getMessage());
		}
	}
	
	public static Color getColorByName(String name){
		for(int i = 0; i < colors.length; i++){
			if(colors[i] != null){
				if(name == colors[i].name) return colors[i];
			}
		}
		return null;
	}
	
	public static boolean isNameUniuqe(String name){
		for(int i = 0; i < colors.length; i++){
			if(colors[i] != null && colors[i].name == name) return false;
		}
		return true;
	}
	
	public static void setGLColorTo(String colorName){
		Color color = getColorByName(colorName);
		if(color == null){
			GL11.glColor3f(1, 1, 1);
			return;
		}
		GL11.glColor3f(color.red, color.green, color.blue);
	}
	
	public static void resetGLColor(){
		setGLColorTo("White");
	}
	
}
