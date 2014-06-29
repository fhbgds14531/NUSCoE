package nuscoe.prog.fhbgds;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class NuscoeMain {
	
	static Timer timer;
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
			System.exit(-1);
		}
	}
	
	public void run(){
		Display.setTitle("NUSCoE");
		while(!Display.isCloseRequested()){
			for(int i = 0; i < timer.elapsedTicks; i++){
				timer.doTick();
			}
			Display.update();
		}
		this.shouldRun = false;
		Display.destroy();
	}
	
	public synchronized void doTick(){
		System.out.println(".");
	}
	
	public static void main(String[] args){
		timer = new Timer(20);
		Thread timerThread = new Thread(timer);
		timerThread.setName("Timer");
		timerThread.start();
		new NuscoeMain();
	}
}