package nuscoe.prog.fhbgds.util;

import java.io.BufferedOutputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OutStream extends PrintStream {

	public OutStream(FileDescriptor f) {
		super(new BufferedOutputStream(new FileOutputStream(f), 128), true);
	}

	public void print(String s){
		if (s == null) s = "null";
		if(s.startsWith("!cpk")){
			this.catchCPK(s);
			return;
		}
		super.print(this.formatString(s));
	}

	public void print(Object o){
		this.print(String.valueOf(o));
	}
	
	public void print(int i){
		this.print(String.valueOf(i));
	}
	
	public void print(boolean b){
		this.print(b? "true": "false");
	}
	
	public void print(char char0){
		this.print(String.valueOf(char0)); // long float char
	}
	
	public void print(long l){
		this.print(String.valueOf(l));
	}
	
	public void print(float f){
		this.print(String.valueOf(f));
	}
	
	public void print(char[] chars){
		this.print(String.valueOf(chars));
	}
	
	public void print(double d){
		this.print(String.valueOf(d));
	}
	
	private String formatString(String s){
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss a");
		String date = sdf.toLocalizedPattern();
		Date d = new Date();
		date = sdf.format(d);
		String newString = "[" + date + "] " + s;
		return newString;
	}

	public void catchCPK(String s){ //"!cpk_<num>:(<add/mod>)Sky Blue,1-1-1"
		if(s.contains("(mod)")) this.changeColor(s);
		if(s.contains("(add)")) this.addColor(s);
	}
	
	private void addColor(String s) {
		String colorName = s.substring(s.indexOf(")") + 1, s.indexOf(","));
		String rgb = s.substring(s.indexOf("," + 1));
		String cpkNum = s.substring(s.indexOf("_") + 1, s.indexOf(":"));
		rgb = rgb.replace(",", "");
		rgb = rgb.replace("-", "f, ");
		rgb = rgb.replace("", "");
		rgb += "f";
		float r = Float.valueOf(rgb.substring(0, rgb.indexOf("f")));
		rgb = rgb.substring(rgb.indexOf(",") + 1);
		float g = Float.valueOf(rgb.substring(0, rgb.indexOf("f")));
		rgb = rgb.substring(rgb.indexOf(",") + 1);
		float b = Float.valueOf(rgb);
		this.println("\"" + s + "\"");
		this.println("Colorpack " + cpkNum + " is replacing \"" + colorName + "\"\'s values with: " + r + "f, " + g + "f, " + b + "f.");
		try{
			Color color = new Color(r, g, b, colorName);
			Colors.addColor(color);
		}catch(Throwable e){
			System.err.println(e.getMessage());
		}
	}

	private void changeColor(String s){
		String colorName = s.substring(s.indexOf(")") + 1, s.indexOf(";"));
		String rgb = s.substring(s.indexOf(";"));
		String cpkNum = s.substring(s.indexOf("_") + 1, s.indexOf(":"));
		rgb = rgb.replace(";", "");
		rgb = rgb.replace("-", "f, ");
		rgb += "f";
		float r = Float.valueOf(rgb.substring(0, rgb.indexOf("f")));
		rgb = rgb.substring(rgb.indexOf(",") + 1);
		float g = Float.valueOf(rgb.substring(0, rgb.indexOf("f")));
		rgb = rgb.substring(rgb.indexOf(",") + 1);
		float b = Float.valueOf(rgb);
		this.println("\"" + s + "\"");
		this.println("Colorpack " + cpkNum + " is replacing \"" + colorName + "\"\'s values with: " + r + "f, " + g + "f, " + b + "f.");
		Colors.replaceValues(colorName, new Float[]{r, g, b});
	}
	
}
