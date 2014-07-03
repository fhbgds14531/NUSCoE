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
import nuscoe.prog.fhbgds.entity.Follower;
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
			thePlayer = new Player();
			this.addEntity(thePlayer);
			for(int i = 0; i < 15; i++){
				AIEntity e = new Follower(rand.nextInt(800)-400, rand.nextInt(600)-300,10,10, thePlayer);
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
	
	public Entity getRandomEntity(Entity requestor){
		int index = rand.nextInt(this.loadedEntities.entrySet().size());
		Entity e = this.getLoadedEntities()[index];
		if(e != null){
			if(requestor != null && requestor.entityID == e.entityID) return getRandomEntity(requestor);
			return e;
		}else{
			return getRandomEntity(requestor);
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
		Player player = thePlayer;
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
			}else if(e1 instanceof Player){
				GL11.glColor3f(1, e1.health, e1.health);
			}else{
				GL11.glColor3f(1, 1, 1);
			}
			drawRectangle(e1.xPos, e1.yPos, e1.sizeX, e1.sizeY);
		}
	}
	
	public synchronized void doTick(){
		KeyboardChecker.checkEventKeys();
		
		HashMap<Integer, Entity> removalMap = new HashMap<Integer, Entity>();
		thePlayer.movePlayer();
		Iterator<Entry<Integer, Entity>> it1 = loadedEntities.entrySet().iterator();
		Entity[] newEnts = new Entity[loadedEntities.entrySet().size()];
		int count = 0;
		while(it1.hasNext()){
			Entry<Integer, Entity> e = it1.next();
			Entity e1 = e.getValue();
			AIEntity ent = null;
			if(e1 instanceof AIEntity){
				ent = (AIEntity)e1;
			}
			if(!e1.isDead && ((!thePlayer.isDead && e1 instanceof Follower) || !(e1 instanceof Follower))){
				e1.entityUpdate();
				if(e1.xPos > 400 - e1.sizeX) e1.xPos = 400 - e1.sizeX;
				if(e1.xPos < -400) e1.xPos = -400;
				if(e1.yPos > 300 - e1.sizeY) e1.yPos = 300 - e1.sizeY;
				if(e1.yPos < -300) e1.yPos = -300;
			}else{
				removalMap.put(e.getKey(), e.getValue());
				if(thePlayer.isDead && e1 instanceof Follower){
					Entity e2 = new AIEntity(e1.xPos, e1.yPos, e1.sizeX, e1.sizeY);
					newEnts[count++] = e2;
				}
			}
			if(ent != null){
				if(ent.convert){
					Follower f = new Follower(ent.xPos, ent.yPos,ent.sizeX,ent.sizeY, this.getRandomEntity(ent));
					removalMap.put(e.getKey(), e.getValue());
					newEnts[count++] = f;
				}
			}
		}
		Iterator<Entry<Integer, Entity>> it = removalMap.entrySet().iterator();
		while(it.hasNext()){
			Entry<Integer, Entity> e = it.next();
			loadedEntities.remove(e.getKey());
		}
		for(int i = 0; i < newEnts.length; i++){
			if(newEnts[i] != null){
				this.addEntity(newEnts[i]);
			}
		}
	}
	
	public void damagePlayer(){
		thePlayer.health -= 0.1;
		if(thePlayer.health < 0.03) thePlayer.health = 1;
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