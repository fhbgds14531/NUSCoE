package nuscoe.prog.fhbgds;


public class Timer extends Thread{

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
		while(NuscoeMain.instance.shouldRun){
			updateTimer();
			for(int i = 0; i < this.elapsedTicks; i++){
				this.doTick();
			}
		}
	}
	
	public void updateTimer(){
		long currentTime = System.nanoTime();
		float diff = (currentTime - this.timeOfLastTick)/1000000000;
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
