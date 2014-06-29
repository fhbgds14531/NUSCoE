package nuscoe.prog.fhbgds;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class NuscoeMain {
	
	public Timer timer;
	static Thread timerThread;
	protected boolean shouldRun = true;
	public static NuscoeMain instance;
	
	public NuscoeMain(){
		try {
			Display.setDisplayMode(new DisplayMode(800, 600));
			Display.setTitle("Loading...");
			Display.create();
			instance = this;
			this.run();
		} catch (Exception e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}
	}
	
	public void run(){
		Display.setTitle("NUSCoE");
		while(!Display.isCloseRequested()){
			Display.update();
		}
		this.shouldRun = false;
		Display.destroy();
		
	}
	
	public synchronized void doTick(){
		System.out.println(".");
	}
	
	public static void main(String[] args){
		timerThread = new Timer(20);
		timerThread.setName("Timer");
		timerThread.start();
		new NuscoeMain();
	}
}