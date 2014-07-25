package nuscoe.prog.fhbgds.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.lwjgl.opengl.GL11;

public class Colors {

	static int lastColor = 0;
	private static HashMap<String, Color> colors = new HashMap<String, Color>();
	
		public static Color white;
		public static Color black;
		public static Color darkGrey;
		public static Color mediumGrey;
		public static Color lightGrey;
		public static Color pureRed;
		public static Color pureGreen;
		public static Color pureBlue;
		public static Color pastelRed;
		public static Color pastelGreen;
		public static Color pastelBlue;
		public static Color backgroundGrey;
		public static Color yellow;
		public static Color orange;
		public static Color skyBlue;
		public static Color grassGreen;
		public static Color signGreen;
		public static Color midnightBlue;
		public static Color midnightGrass;
	
	public Colors(){
		try {
			white = new Color(1, 1, 1, "White");
			Colors.addColor(white);
			black = new Color(0, 0, 0, "Black");
			Colors.addColor(black);
			darkGrey = new Color(0.2f, 0.2f, 0.2f, "Dark Grey");
			Colors.addColor(darkGrey);
			mediumGrey = new Color(0.4f, 0.4f, 0.4f, "Medium Grey");
			Colors.addColor(mediumGrey);
			lightGrey = new Color(0.8f, 0.8f, 0.8f, "Light Grey");
			Colors.addColor(lightGrey);
			pureRed = new Color(1, 0, 0, "Pure Red");
			Colors.addColor(pureRed);
			pureGreen = new Color(0, 1, 0, "Pure Green");
			Colors.addColor(pureGreen);
			pureBlue = new Color(0, 0, 1, "Pure Blue");
			Colors.addColor(pureBlue);
			pastelRed = new Color(1, 0.4f, 0.4f, "Pastel Red");
			Colors.addColor(pastelRed);
			pastelGreen = new Color(0.4f, 1, 0.4f, "Pastel Green");
			Colors.addColor(pastelGreen);
			pastelBlue = new Color(0.4f, 0.4f, 1, "Pastel Blue");
			Colors.addColor(pastelBlue);
			backgroundGrey = new Color(0.3f, 0.3f, 0.3f, "Background Grey");
			Colors.addColor(backgroundGrey);
			yellow = new Color(1, 1, 0, "Yellow");
			Colors.addColor(yellow);
			orange = new Color(1, 0.53f, 0, "Orange");
			Colors.addColor(orange);
			skyBlue = new Color(0, 0.6f, 0.8f, "Sky Blue");
			Colors.addColor(skyBlue);
			grassGreen = new Color(0, 0.55f, 0, "Grass");
			Colors.addColor(grassGreen);
			signGreen = new Color(0.42f, 0.33f, 0.7f, "Sign Green");
			Colors.addColor(signGreen);
			midnightBlue = new Color(0, 0, 0.2f, "Midnight Blue");
			Colors.addColor(midnightBlue);
			midnightGrass = new Color(0, 0.33f, 0, "Midnight Grass");
			Colors.addColor(midnightGrass);
		} catch (InvalidNameException e) {
			System.err.println(e.getMessage());
		}
	}
	
	public static Color getColorByName(String name){
		Iterator<Entry<String, Color>> it = colors.entrySet().iterator();
		while(it.hasNext()){
			Entry<String, Color> e = it.next();
			if(e != null){
				if(e.getValue().getName().contentEquals(name)) return e.getValue();
			}
		}
		return Colors.white;
	}
	
	public static boolean isNameUnique(String name){
		if(colors.containsKey(name)) return false;
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
	
	public static void setGLColorTo(Color color){
		if(color == null){
			GL11.glColor3f(1, 1, 1);
			return;
		}
		GL11.glColor3f(color.red, color.green, color.blue);
	}
	
	public static void resetGLColor(){
		setGLColorTo("White");
	}

	public static void addColor(float r, float g, float b, String name) {
		try{Colors.addColor(new Color(r, g, b, name));}catch (InvalidNameException e){System.out.println(e.getMessage());}
	}

	public static void addColor(Color color){
			colors.put(color.getName(), color);
	}

	public static void replaceValues(String colorName, Float[] values){
		Color color = Colors.getColorByName(colorName);
		if(color != null){
			color.setRGB(values);
			System.out.println("Successfully replaced \"" + colorName + "\"\'s values!");
			Color colour = Colors.getColorByName(colorName);
			System.out.println("Values: " + colour.red + ", " + colour.green + ", " + colour.blue);
		}

	}
	
}
