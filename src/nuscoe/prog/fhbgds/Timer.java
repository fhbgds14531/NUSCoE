package nuscoe.prog.fhbgds;


public class Timer implements Runnable{
	//1 billion nanoseconds is one second.
	float timerSpeed;
	float ticksPerSecond;
	long timeOfLastTick;
	int elapsedTicks;
	
	public Timer(float tps){
		this.ticksPerSecond = tps;
		this.timeOfLastTick = System.nanoTime();
	}
	
	public void run(){
		while(NuscoeMain.instance == null){try{Thread.sleep(100l);}catch(Exception e){}}
		this.timeOfLastTick = System.nanoTime();
		while(NuscoeMain.instance.shouldRun){
			updateTimer();
		}
	}
	
	public void updateTimer(){
		long currentTime = System.nanoTime();
		float diff = (currentTime - this.timeOfLastTick);
		diff /= 1000000000;
		if(diff >= (1/this.ticksPerSecond)){
			this.elapsedTicks++;
			this.timeOfLastTick = System.nanoTime();
		}
	}
	
	public void doTick(){
		this.elapsedTicks--;
		NuscoeMain.instance.doTick();
	}
}
