package nuscoe.prog.fhbgds.util;

import java.io.InputStream;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Textures {

	public static Texture playerIFL;
	public static Texture playerIFR;
	public static Texture playerJFL;
	public static Texture playerJFR;
	public static Texture oneUp;
	public static Texture fireLeft;
	public static Texture fireRight;
	public static Texture followerLeft;
	public static Texture followerRight;
	
	public static void load() throws Exception{
		InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream("assets/nuscoe/sprites/player/idle/facing/Left.png");
		playerIFL = TextureLoader.getTexture("PNG", in);
		in = ClassLoader.getSystemClassLoader().getResourceAsStream("assets/nuscoe/sprites/player/idle/facing/Right.png");
		playerIFR = TextureLoader.getTexture("PNG", in);
		in = ClassLoader.getSystemClassLoader().getResourceAsStream("assets/nuscoe/sprites/player/jumping/facing/Left.png");
		playerJFL = TextureLoader.getTexture("PNG", in);
		in = ClassLoader.getSystemClassLoader().getResourceAsStream("assets/nuscoe/sprites/player/jumping/facing/Right.png");
		playerJFR = TextureLoader.getTexture("PNG", in);
		in = ClassLoader.getSystemClassLoader().getResourceAsStream("assets/nuscoe/sprites/player/oneUp/head.png");
		oneUp = TextureLoader.getTexture("PNG", in);
		in = ClassLoader.getSystemClassLoader().getResourceAsStream("assets/nuscoe/sprites/projectile/fire/Left.png");
		fireLeft = TextureLoader.getTexture("PNG", in);
		in = ClassLoader.getSystemClassLoader().getResourceAsStream("assets/nuscoe/sprites/projectile/fire/Right.png");
		fireRight = TextureLoader.getTexture("PNG", in);
		in = ClassLoader.getSystemClassLoader().getResourceAsStream("assets/nuscoe/sprites/enemy/follower/Left.png");
		followerLeft = TextureLoader.getTexture("PNG", in);
		in = ClassLoader.getSystemClassLoader().getResourceAsStream("assets/nuscoe/sprites/enemy/follower/Right.png");
		followerRight = TextureLoader.getTexture("PNG", in);
		
		in.close();
	}
	
}
