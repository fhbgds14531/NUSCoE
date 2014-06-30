package nuscoe.prog.fhbgds;

import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_TEXTURE;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;

import nuscoe.prog.fhbgds.entity.AIEntity;
import nuscoe.prog.fhbgds.entity.Entity;
import nuscoe.prog.fhbgds.entity.EntityPlayerFollower;
import nuscoe.prog.fhbgds.entity.Player;
import nuscoe.prog.fhbgds.util.KeyboardChecker;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class NuscoeMain {
	
	static Timer timer;
	protected boolean shouldRun = true;
	public static NuscoeMain instance;
	public HashMap<Integer, Entity> loadedEntities = new HashMap<Integer, Entity>();
	Player thePlayer;
	int width, height;
	Random rand = new Random();
	
	public NuscoeMain(){
		try {
			this.width = 800;
			this.height = 600;
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setTitle("Loading...");
			Display.create();
			instance = this;
			thePlayer = new Player(-10, -10, 20, 20);
			this.addEntity(thePlayer);
			for(int i = 0; i < 15; i++){
				EntityPlayerFollower e = new EntityPlayerFollower(rand.nextInt(800)-400, rand.nextInt(600)-300,10,10);
				this.addEntity(e);
			}
			this.getReadyToDraw();
			this.run();
		} catch (Exception e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(-1);
		}
	}
	
	public Entity[] getLoadedEntities(){
		Entity[] entities = new Entity[loadedEntities.size()];
		Iterator<Entry<Integer, Entity>> it = loadedEntities.entrySet().iterator();
		int count = 0;
		while(it.hasNext()){
			Entry<Integer, Entity> e = it.next();
			entities[count++] = e.getValue();
		}
		return entities;
	}
	
	public Player getPlayer(){
		Player player = new Player(thePlayer.xPos, thePlayer.yPos, thePlayer.sizeX, thePlayer.sizeY);
		return player;
	}
	
	public void run(){
		Display.setTitle("NUSCoE");
		while(!Display.isCloseRequested()){
			for(int i = 0; i < timer.elapsedTicks; i++){
				timer.doTick();
			}
			GL11.glColor3f(.3f, .3f, .3f);
			drawRectangle(-450, -350, 850, 650);
			GL11.glColor3f(1, 1, 1);
			this.drawEntities();
			Display.update();
			Display.sync(60);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		}
		this.shouldRun = false;
		Display.destroy();
		System.exit(0);
	}
	
	public void drawEntities(){
		Iterator<Entry<Integer, Entity>> it = loadedEntities.entrySet().iterator();
		while(it.hasNext()){
			Entry<Integer, Entity> e = it.next();
			Entity e1 = e.getValue();
			if(e1 instanceof AIEntity){
				AIEntity ent = (AIEntity) e1;
				if(ent.traveling){
					GL11.glColor3f(1, 0.4f, 0.4f);
				}
			}else{
				GL11.glColor3f(1, 1, 1);
			}
			drawRectangle(e1.xPos, e1.yPos, e1.sizeX, e1.sizeY);
		}
	}
	
	public synchronized void doTick(){
		KeyboardChecker.checkEventKeys();
		thePlayer.movePlayer();
		Iterator<Entry<Integer, Entity>> it = loadedEntities.entrySet().iterator();
		while(it.hasNext()){
			Entry<Integer, Entity> e = it.next();
			Entity e1 = e.getValue();
			e1.entityUpdate();
			if(e1.xPos > 400 - e1.sizeX) e1.xPos = 400 - e1.sizeX;
			if(e1.xPos < -400) e1.xPos = -400;
			if(e1.yPos > 300 - e1.sizeY) e1.yPos = 300 - e1.sizeY;
			if(e1.yPos < -300) e1.yPos = -300;
		}
	}
	
	public void getReadyToDraw(){
		glMatrixMode(GL_PROJECTION);
		glEnable(GL_TEXTURE);
		glEnable(GL_TEXTURE_2D);
		glLoadIdentity();
		GL11.glOrtho(-400, 400, 300, -300, 1, -1);//left right down up
		GL11.glViewport(0, 0, width, height);
		glMatrixMode(GL_MODELVIEW);	
	}
	
	public void addEntity(Entity e){
		Entity.lastEntityID++;
		e.entityID = Entity.lastEntityID;
		loadedEntities.put(e.entityID, e);
	}
	
	public static void drawRectangle(float startX, float startY, float sizeX, float sizeY){
		glPushMatrix();
			glBegin(GL_TRIANGLES);
				glVertex2f(startX, startY);
				glVertex2f(startX, startY + sizeY);
				glVertex2f(startX + sizeX, startY);
			
				glVertex2f(startX + sizeX, startY);
				glVertex2f(startX + sizeX, startY + sizeY);
				glVertex2f(startX, startY + sizeY);
			glEnd();
		glPopMatrix();
	}
	
	public static void main(String[] args){
		timer = new Timer(30);
		Thread timerThread = new Thread(timer);
		timerThread.setName("Timer");
		timerThread.start();
		new NuscoeMain();
	}
}