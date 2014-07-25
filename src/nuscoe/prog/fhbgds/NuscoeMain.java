package nuscoe.prog.fhbgds;

import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.io.File;
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
import nuscoe.prog.fhbgds.phys.Block;
import nuscoe.prog.fhbgds.util.Colors;
import nuscoe.prog.fhbgds.util.DisplayModeSwitcher;
import nuscoe.prog.fhbgds.util.KeyboardChecker;
import nuscoe.prog.fhbgds.util.OutStream;
import nuscoe.prog.fhbgds.util.Textures;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

public class NuscoeMain {
	
	static Timer timer;
	protected boolean shouldRun = true;
	
	public static NuscoeMain instance;
	
	public HashMap<Integer, Entity> ents = new HashMap<Integer, Entity>();
	public HashMap<Integer, Block> blocks = new HashMap<Integer, Block>();
	Player thePlayer;
	public int width;
	public int height;
	public int playerDamageCooldown;
	Random rand = new Random();
	public boolean isPaused = true;
	
	public DisplayMode normal;
	
	public NuscoeMain(){
		long start = System.nanoTime();
		try {
			System.setOut(new OutStream(FileDescriptor.out));
			System.setErr(new OutStream(FileDescriptor.err));
			this.width = 1280;
			this.height = 720;
			normal = new DisplayMode(width, height);
			Display.setDisplayMode(normal);
			Display.setTitle("Loading...");
			Display.create();
			instance = this;
			Textures.load();
			thePlayer = new Player();
			this.addEntity(thePlayer);
			for(int i = 0; i < 5; i++){
				AIEntity e = new Follower(rand.nextInt(width)-width * 0.495f, rand.nextInt(height)-height/2.5f, 40, 80, thePlayer);
				this.addEntity(e);
			}
			this.getReadyToDraw();
			Block block = new Block(-this.width * 0.495f - 2, this.height * 0.49f - 50, this.width - 6, this.height - (this.height * 0.91f), Colors.midnightGrass);
			this.addBlock(block);
//TODO			System.out.println("!cpk_1:(mod)Sky Blue;0-1-1");
			long end = System.nanoTime();
			float s = (end - start);
			s/=1000000000;
			String sr = String.valueOf(s);
			sr = sr.substring(0, 4);
			System.out.println("Initialized game in " + sr + " seconds.");
			this.updateEntities();
			this.run();
		} catch (Exception e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(-1);
		}
	}
	
	public Entity getRandomEntity(Entity requestor){
		int index = rand.nextInt(this.getLoadedEntities().length);
		Entity e = this.getLoadedEntities()[index];
		if(e != null){
			if(requestor != null && requestor.entityID == e.entityID) return getRandomEntity(requestor);
			return e;
		}else{
			return getRandomEntity(requestor);
		}
	}
	
	public Entity[] getLoadedEntities(){
		Entity[] entities = new Entity[ents.size()];
		Iterator<Entry<Integer, Entity>> it = ents.entrySet().iterator();
		int count = 0;
		while(it.hasNext()){
			Entry<Integer, Entity> e = it.next();
			entities[count++] = e.getValue();
		}
		return entities;
	}
	
	public Player getPlayer(){
		Player player;
		player = thePlayer;
		return player;
	}
	
	public void addLife(int i){
		thePlayer.lives += i;
		if(thePlayer.lives > 24) thePlayer.lives = 24;
		if(thePlayer.lives < 0)  thePlayer.lives =  0;
	}
	
	public void run(){
		Display.setTitle("NUSCoE");
		DisplayModeSwitcher.setDisplayMode(width, height, true);
		while(!Display.isCloseRequested()){
			for(int i = 0; i < timer.elapsedTicks; i++){
				timer.doTick();
			}
			Colors.setGLColorTo(Colors.black);
			this.drawRectangle(-1000, -1000, 2000, 2000);
			Colors.setGLColorTo(Colors.midnightBlue);
			this.drawRectangle(-this.width * 0.4975f, -this.height * 0.425f, this.width * 0.995f, this.height * 0.92f);
			Colors.setGLColorTo(Colors.lightGrey);
			this.drawRectangle(-this.width/2, -this.height/2, this.width, this.height * 0.075f);
			Colors.resetGLColor();
			this.drawEntities();
			this.drawBlocks();
			Colors.setGLColorTo(Colors.black);
			float x = (22 * thePlayer.lives);
			x += (7 + thePlayer.lives);
			if(thePlayer.lives <= 1) x = 30;
			this.drawRectangle(-this.width * 0.495f, -this.height * 0.49f, x, 30);
			Colors.setGLColorTo(Colors.mediumGrey);
			x =  (22 * thePlayer.lives);
			x += ( 3 + thePlayer.lives);
			if(thePlayer.lives <= 1) x = 26;
			this.drawRectangle(-this.width * 0.495f + 2, -this.height * 0.49f + 2, x, 26);
			Colors.setGLColorTo(Colors.white);
			for(int i = 0; i < thePlayer.lives; i++){
				this.drawTexturedRectangle(-this.width * 0.495f + 4 + (i * 23), -this.height * 0.49f + 4, 20, 20, Textures.oneUp);
			}
			Colors.setGLColorTo(Colors.black);
			this.drawRectangle(this.width * 0.495f, -this.height * 0.49f, -413, 30);
			Colors.setGLColorTo(Colors.backgroundGrey);
			this.drawRectangle(this.width * 0.495f - 2, -this.height * 0.49f + 2, -409, 26);
			if(thePlayer.health > 0){
				if(thePlayer.health >= 1.25) Colors.setGLColorTo(Colors.pastelGreen);
				if(thePlayer.health > 0.75 && thePlayer.health <= 1.25) Colors.setGLColorTo(Colors.yellow);
				if(thePlayer.health > 0.25 && thePlayer.health <= 0.75) Colors.setGLColorTo(Colors.orange);
				if(thePlayer.health <= 0.25) Colors.setGLColorTo(Colors.pureRed);
				int health = (int) (thePlayer.health * 100);
				this.drawRectangle(this.width * 0.495f - 3, -this.height * 0.49f + 4, -health * 2.027775f, 22);
			}
			Colors.resetGLColor();
			Display.update();
			Display.sync(60);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		}
		this.exit(0);
	}
	
	public void exit(int status){
		this.shouldRun = false;
		Display.destroy();
		System.exit(status);
	}
	
	public void drawBlocks(){
		Iterator<Entry<Integer, Block>> it = blocks.entrySet().iterator();
		while(it.hasNext()){
			Entry<Integer, Block> e = it.next();
			Block b = e.getValue();
			Colors.setGLColorTo(b.color.getName());
			Float[] fa = b.getSizeAndPos();
			this.drawRectangle(fa[0], fa[1], fa[2], fa[3]);
		}
	}
	
	public void drawEntities(){
		Iterator<Entry<Integer, Entity>> it = ents.entrySet().iterator();
		while(it.hasNext()){
			Entry<Integer, Entity> e = it.next();
			Entity e1 = e.getValue();
			if(e1 instanceof AIEntity){
				AIEntity ent = (AIEntity) e1;
				if(ent.hasTarget){
					Colors.setGLColorTo(Colors.pastelGreen);
				}
			}else{
				Colors.setGLColorTo(Colors.white);
			}
			if(!(e1 instanceof Player) && !(e1 instanceof Projectile)){
				if(e1.facingLeft) this.drawTexturedRectangle(e1.xPos, e1.yPos, e1.sizeX, e1.sizeY, Textures.followerLeft); else this.drawTexturedRectangle(e1.xPos, e1.yPos, e1.sizeX, e1.sizeY, Textures.followerRight); 
			}else if(e1 instanceof Player){
				if(e1.facingLeft){
					if(!e1.jumping){
						this.drawTexturedRectangle(e1.xPos +5, e1.yPos +5, e1.sizeX, e1.sizeY, Textures.playerIFL);
					}else{
						this.drawTexturedRectangle(e1.xPos +5, e1.yPos +5, e1.sizeX, e1.sizeY, Textures.playerJFL);
					}
				}else{
					if(!e1.jumping){
						this.drawTexturedRectangle(e1.xPos +5, e1.yPos +5, e1.sizeX, e1.sizeY, Textures.playerIFR);
					}else{
						this.drawTexturedRectangle(e1.xPos +5, e1.yPos +5, e1.sizeX, e1.sizeY, Textures.playerJFR);
					}
				}
			}else{
				if(e1 instanceof Projectile){
					if(e1.facingLeft){
						this.drawTexturedRectangle(e1.xPos, e1.yPos, e1.sizeX, e1.sizeY, Textures.fireLeft);
					}else{
						this.drawTexturedRectangle(e1.xPos, e1.yPos, e1.sizeX, e1.sizeY, Textures.fireRight);
					}
				}
			}
		}
	}
	
	public synchronized void doTick(){
		KeyboardChecker.checkEventKeys();
		if(!this.isPaused){
			this.updateBlocks();
			this.updateEntities();
			if(this.playerDamageCooldown > 0) this.playerDamageCooldown--;
		}
	}
	
	public void damagePlayer(float amount){
		if(amount == 0) return; 
		if(amount < 0) amount = -amount;
		if(amount > 1) amount = 1;
		this.thePlayer.health -= amount;
	}
	
	public void spawnProjectiles(Entity entity){
		if(entity.isDead) return;
		if(this.isPaused) return;
		if(entity instanceof Player && this.playerDamageCooldown > 0) return;
		float x1 = entity.xPos;
		float y1 = entity.yPos;
		float sizeX = entity.sizeX;
		float sizeY = entity.sizeY;
		
		float x = x1 + (0.5f*sizeX);
		float y = y1 + (0.5f*sizeY);
		
		x -= 2.5f;
		y -= 2.5f;
		
		Entity[] proj = new Entity[16];
		//facing left:  2, 5, 7, 10, 13
		//facing right: 0, 4, 6, 08, 15
		proj[0]  = new Projectile(x, y, 2, 2,  6.0f,  0.0f, entity);
		proj[1]  = new Projectile(x, y, 2, 2,  0.0f,  6.0f, entity);
		proj[2]  = new Projectile(x, y, 2, 2, -6.0f,  0.0f, entity);
		proj[3]  = new Projectile(x, y, 2, 2,  0.0f, -6.0f, entity);
		proj[4]  = new Projectile(x, y, 2, 2,  6.0f,  6.0f, entity);
		proj[5]  = new Projectile(x, y, 2, 2, -6.0f,  6.0f, entity);
		proj[6]  = new Projectile(x, y, 2, 2,  6.0f, -6.0f, entity);
		proj[7]  = new Projectile(x, y, 2, 2, -6.0f, -6.0f, entity);
		
		proj[8]  = new Projectile(x, y, 2, 2,  6.0f,  3.0f, entity);
		proj[9]  = new Projectile(x, y, 2, 2,  3.0f,  6.0f, entity);
		proj[10] = new Projectile(x, y, 2, 2, -6.0f,  3.0f, entity);
		proj[11] = new Projectile(x, y, 2, 2,  3.0f, -6.0f, entity);
		proj[12] = new Projectile(x, y, 2, 2, -3.0f,  6.0f, entity);
		proj[13] = new Projectile(x, y, 2, 2, -6.0f, -3.0f, entity);
		proj[14] = new Projectile(x, y, 2, 2, -3.0f, -6.0f, entity);
		proj[15] = new Projectile(x, y, 2, 2,  6.0f, -3.0f, entity);

		if(thePlayer.facingLeft){
//			this.addEntity(proj[2]);
//			this.addEntity(proj[5]);
//			this.addEntity(proj[7]);
//			this.addEntity(proj[10]);
//			this.addEntity(proj[13]);
			this.addEntity(new Projectile(x - 20, y, thePlayer.sizeX / 2, thePlayer.sizeX / 4, -15, 0, entity, 15));
		}else{
			this.addEntity(new Projectile(x + 20, y, thePlayer.sizeX / 2, thePlayer.sizeX / 4,  15, 0, entity, 15));
//			this.addEntity(proj[0]);
//			this.addEntity(proj[4]);
//			this.addEntity(proj[6]);
//			this.addEntity(proj[8]);
//			this.addEntity(proj[15]);
		}
		if(entity instanceof Player) this.damagePlayer(0.05f);
	}
	
	public void healPlayer(){
		if(thePlayer.lives <= 0) return;
		if(thePlayer.health >= 1.75 && !thePlayer.isDead) return;
		Entity[] newEntities = new Entity[this.getLoadedEntities().length];
		int count = 0;
		thePlayer.health = 1.75f;
		thePlayer.lives--;
		if(thePlayer.isDead){
			this.playerDamageCooldown = 30;
			thePlayer.isDead = false;
			this.addEntity(thePlayer);
			Iterator<Entry<Integer, Entity>> it = ents.entrySet().iterator();
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
	
	public void updateBlocks(){
		Iterator<Entry<Integer, Block>> it = blocks.entrySet().iterator();
		while(it.hasNext()){
			Entry<Integer, Block> e = it.next();
			Block b = e.getValue();
			b.doBlockUpdate();
		}
	}
	
	public void updateEntities(){
		HashMap<Integer, Entity> removalMap = new HashMap<Integer, Entity>();
		thePlayer.movePlayer();
		Iterator<Entry<Integer, Entity>> it1 = ents.entrySet().iterator();
		Entity[] newEnts = new Entity[this.getLoadedEntities().length];
		int count = 0;
		while(it1.hasNext()){
			Entry<Integer, Entity> e = it1.next();
			Entity e1 = e.getValue();
			if(!e1.isDead && ((!thePlayer.isDead && e1 instanceof Follower) || !(e1 instanceof Follower))){
				e1.entityUpdate();
				//this.width * 0.495f, this.height * 0.49f
				if(e1.xPos > this.width * 0.495f - e1.sizeX && !(e1 instanceof Projectile)) e1.xPos = this.width * 0.495f - e1.sizeX;
				if(e1.xPos < -(this.width * 0.495f) && !(e1 instanceof Projectile)) e1.xPos = -(this.width * 0.495f);
				if(e1.yPos > this.height * 0.49f - e1.sizeY - 50) e1.yPos = this.height * 0.49f - e1.sizeY - 50;
				if(e1.yPos < -this.height * 0.49f + 47 && !(e1 instanceof Projectile)) e1.yPos = -this.height * 0.49f + 47;
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
			ents.remove(e.getKey());
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
		GL11.glOrtho(-width/2, width/2, height/2, -height/2, 1, -1);//left right down up
		GL11.glViewport(0, 0, width, height);
		glMatrixMode(GL_MODELVIEW);	
		new Colors();
	}
	
	public void addEntity(Entity e){
		Entity.lastEntityID++;
		e.entityID = Entity.lastEntityID;
		ents.put(e.entityID, e);
	}
	
	public void addBlock(Block b){
		Block.lastID++;
		b.setBlockID(Block.lastID);
		blocks.put(b.getBlockID(), b);
	}
	
	public void drawRectangle(float startX, float startY, float sizeX, float sizeY){
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
	
	public void drawTexturedRectangle(float startX, float startY, float sizeX, float sizeY, Texture texture){
		GL11.glEnable(GL11.GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		GL11.glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
		glPushMatrix();
			glBegin(GL_TRIANGLES);
				glTexCoord2f(0, 0);
				glVertex2f(startX, startY);
				glTexCoord2f(0, 1);
				glVertex2f(startX, startY + sizeY);
				glTexCoord2f(1, 0);
				glVertex2f(startX + sizeX, startY);
				
				glTexCoord2f(1, 0);
				glVertex2f(startX + sizeX, startY);
				glTexCoord2f(1, 1);
				glVertex2f(startX + sizeX, startY + sizeY);
				glTexCoord2f(0, 1);
				glVertex2f(startX, startY + sizeY);
			glEnd();
		glPopMatrix();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public static void main(String[] args){
		File f = new File("colorpacks");
		if(!f.exists()){
			f.mkdir();
		}else{

		}
		startGame();
	}

	public static void startGame(){
		try {
			timer = new Timer(30);
		} catch (Throwable e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		Thread timerThread = new Thread(timer);
		timerThread.setName("Timer");
		timerThread.start();
		new NuscoeMain();
	}
}