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

import java.io.FileDescriptor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;

import nuscoe.prog.fhbgds.entity.AIEntity;
import nuscoe.prog.fhbgds.entity.Entity;
import nuscoe.prog.fhbgds.entity.Follower;
import nuscoe.prog.fhbgds.entity.Player;
import nuscoe.prog.fhbgds.entity.Projectile;
import nuscoe.prog.fhbgds.util.Colors;
import nuscoe.prog.fhbgds.util.KeyboardChecker;
import nuscoe.prog.fhbgds.util.OutStream;

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
	int genericCooldown;
	Random rand = new Random();
	public boolean isPaused = true;
	
	public NuscoeMain(){
		long start = System.nanoTime();
		try {
			System.setOut(new OutStream(FileDescriptor.out));
			System.setErr(new OutStream(FileDescriptor.err));
			this.width = 800;
			this.height = 600;
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setTitle("Loading...");
			Display.create();
			instance = this;
			thePlayer = new Player();
			this.addEntity(thePlayer);
			for(int i = 0; i < 30; i++){
				AIEntity e = new Follower(rand.nextInt(800)-400, rand.nextInt(500)-250,10,10, thePlayer);
				this.addEntity(e);
			}
			this.getReadyToDraw();
			long end = System.nanoTime();
			float s = (end - start);
			s/=1000000000;
			String sr = String.valueOf(s);
			sr = sr.substring(0, 4);
			System.out.println("Initialized game in " + sr + " seconds.");
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
	
	public void addLife(int i){
		thePlayer.lives += i;
		if(thePlayer.lives > 24) thePlayer.lives = 24;
		if(thePlayer.lives < 0)  thePlayer.lives =  0;
	}
	
	public void run(){
		Display.setTitle("NUSCoE");
		while(!Display.isCloseRequested()){
			for(int i = 0; i < timer.elapsedTicks; i++){
				timer.doTick();
			}
			Colors.setGLColorTo("Sky Blue");
			drawRectangle(-450, -350, 850, 650);
			Colors.setGLColorTo("Grass");
			drawRectangle(-450, 250, 850, 650);
			Colors.setGLColorTo("Light Grey");
			drawRectangle(-450, -350, 850, 95);
			Colors.resetGLColor();
			this.drawEntities();
			Colors.setGLColorTo("Black");
			float x = (22 * thePlayer.lives);
			x += (07 + thePlayer.lives);
			if(thePlayer.lives <= 1) x = 30;
			drawRectangle(-390, -290, x, 30);
			Colors.setGLColorTo("Light Grey");
			x =  (22 * thePlayer.lives);
			x += (03 + thePlayer.lives);
			if(thePlayer.lives <= 1) x = 26;
			drawRectangle(-388, -288, x, 26);
			Colors.setGLColorTo("White");
			for(int i = 0; i < thePlayer.lives; i++){
				drawRectangle(-385 + (i * 23), -285, 20, 20);
			}
			Colors.setGLColorTo("Black");
			drawRectangle(390, -290, -206, 20);
			Colors.setGLColorTo("Light Grey");
			drawRectangle(388, -288, -202, 16);
			if(thePlayer.health > 0){
				if(thePlayer.health >= 1.25) Colors.setGLColorTo("Pastel Green");
				if(thePlayer.health > 0.75 && thePlayer.health <= 1.25) Colors.setGLColorTo("Yellow");
				if(thePlayer.health > 0.25 && thePlayer.health <= 0.75) Colors.setGLColorTo("Orange");
				if(thePlayer.health <= 0.25) Colors.setGLColorTo("Pure Red");
				int health = (int) (thePlayer.health * 100);
				drawRectangle(387, -287, -health, 14);
			}
			Colors.resetGLColor();
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
				if(ent.hasTarget){
					Colors.setGLColorTo("Pastel Green");
				}
			}else{
				Colors.setGLColorTo("White");
			}
			drawRectangle(e1.xPos, e1.yPos, e1.sizeX, e1.sizeY);
		}
	}
	
	public synchronized void doTick(){
		KeyboardChecker.checkEventKeys();
		if(!this.isPaused) this.updateEntities();
		if(!this.isPaused && this.genericCooldown > 0) this.genericCooldown--;
	}
	
	public void damagePlayer(float amount){
		if(amount == 0) return; 
		if(amount < 0) amount = -amount;
		if(amount > 1) amount = 1;
		this.thePlayer.health -= amount;
	}
	
	public void spawnPlayerProjectiles(){
		if(this.getPlayer().isDead) return;
		if(this.isPaused) return;
		if(this.genericCooldown > 0) return;
		float x1 = this.getPlayer().xPos;
		float y1 = this.getPlayer().yPos;
		float sizeX = this.getPlayer().sizeX;
		float sizeY = this.getPlayer().sizeY;
		
		float x = x1 + (0.5f*sizeX);
		float y = y1 + (0.5f*sizeY);
		
		x -= 2.5f;
		y -= 2.5f;
		
		Entity[] proj = new Entity[16];
		proj[0]  = new Projectile(x, y, 5, 5,  6.0f,  0.0f, this.getPlayer());
		proj[1]  = new Projectile(x, y, 5, 5,  0.0f,  6.0f, this.getPlayer());
		proj[2]  = new Projectile(x, y, 5, 5, -6.0f,  0.0f, this.getPlayer());
		proj[3]  = new Projectile(x, y, 5, 5,  0.0f, -6.0f, this.getPlayer());
		proj[4]  = new Projectile(x, y, 5, 5,  6.0f,  6.0f, this.getPlayer());
		proj[5]  = new Projectile(x, y, 5, 5, -6.0f,  6.0f, this.getPlayer());
		proj[6]  = new Projectile(x, y, 5, 5,  6.0f, -6.0f, this.getPlayer());
		proj[7]  = new Projectile(x, y, 5, 5, -6.0f, -6.0f, this.getPlayer());
		
		proj[8]  = new Projectile(x, y, 5, 5,  6.0f,  3.0f, this.getPlayer());
		proj[9]  = new Projectile(x, y, 5, 5,  3.0f,  6.0f, this.getPlayer());
		proj[10] = new Projectile(x, y, 5, 5, -6.0f,  3.0f, this.getPlayer());
		proj[11] = new Projectile(x, y, 5, 5,  3.0f, -6.0f, this.getPlayer());
		proj[12] = new Projectile(x, y, 5, 5, -3.0f,  6.0f, this.getPlayer());
		proj[13] = new Projectile(x, y, 5, 5, -6.0f, -3.0f, this.getPlayer());
		proj[14] = new Projectile(x, y, 5, 5, -3.0f, -6.0f, this.getPlayer());
		proj[15] = new Projectile(x, y, 5, 5,  6.0f, -3.0f, this.getPlayer());
		
		for(int i = 0; i < proj.length; i++){
			if(proj[i] != null) this.addEntity(proj[i]);
		}
		this.damagePlayer(0.05f);
		thePlayer.sizeX -= 0.75f;
		thePlayer.sizeY -= 0.75f;
		thePlayer.getBoundingBox().expand(-0.75f, -0.75f);
		if(thePlayer.sizeX <= 3 || thePlayer.sizeY <= 3) thePlayer.isDead = true;
		this.genericCooldown = 30;
	}
	
	public void healPlayer(){
		if(thePlayer.lives <= 0) return;
		if(thePlayer.health >= 1.75 && !thePlayer.isDead) return;
		Entity[] newEntities = new Entity[loadedEntities.entrySet().size()];
		int count = 0;
		thePlayer.health = 1.75f;
		thePlayer.lives--;
		thePlayer.sizeX = 20;
		thePlayer.sizeY = 20;
		thePlayer.getBoundingBox().expandTo(20f, 20f);
		if(thePlayer.isDead){
			thePlayer.isDead = false;
			this.addEntity(thePlayer);
			Iterator<Entry<Integer, Entity>> it = loadedEntities.entrySet().iterator();
			while(it.hasNext()){
				Entry<Integer, Entity> entry = it.next();
				if(entry.getValue() instanceof Player || entry.getValue() instanceof Projectile){}else{
					entry.getValue().isDead = true;
					newEntities[count++] = new Follower(entry.getValue().xPos, entry.getValue().yPos, entry.getValue().sizeX, entry.getValue().sizeY, thePlayer);
				}
			}
			for(int i = 0; i < newEntities.length; i++){
				if(newEntities[i] != null){
					this.addEntity(newEntities[i]);
				}
			}
		}
	}
	
	public void updateEntities(){
		HashMap<Integer, Entity> removalMap = new HashMap<Integer, Entity>();
		thePlayer.movePlayer();
		thePlayer.growAndHeal();
		Iterator<Entry<Integer, Entity>> it1 = loadedEntities.entrySet().iterator();
		Entity[] newEnts = new Entity[loadedEntities.entrySet().size()];
		int count = 0;
		while(it1.hasNext()){
			Entry<Integer, Entity> e = it1.next();
			Entity e1 = e.getValue();
			if(!e1.isDead && ((!thePlayer.isDead && e1 instanceof Follower) || !(e1 instanceof Follower))){
				e1.entityUpdate();
				if(e1.xPos > 400 - e1.sizeX && !(e1 instanceof Projectile)) e1.xPos = 400 - e1.sizeX;
				if(e1.xPos < -400 && !(e1 instanceof Projectile)) e1.xPos = -400;
				if(e1.yPos > 250 - e1.sizeY) e1.yPos = 250 - e1.sizeY;
				if(e1.yPos < -250 && !(e1 instanceof Projectile)) e1.yPos = -250;
			}else{
				removalMap.put(e.getKey(), e.getValue());
				if(thePlayer.isDead && e1 instanceof Follower){
					Entity e2 = new AIEntity(e1.xPos, e1.yPos, e1.sizeX, e1.sizeY);
					newEnts[count++] = e2;
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
	
	public void getReadyToDraw(){
		glMatrixMode(GL_PROJECTION);
		glEnable(GL_TEXTURE);
		glEnable(GL_TEXTURE_2D);
		glLoadIdentity();
		GL11.glOrtho(-400, 400, 300, -300, 1, -1);//left right down up
		GL11.glViewport(0, 0, width, height);
		glMatrixMode(GL_MODELVIEW);	
		new Colors();
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