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
	
}
