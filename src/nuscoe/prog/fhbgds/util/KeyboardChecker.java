package nuscoe.prog.fhbgds.util;

import nuscoe.prog.fhbgds.NuscoeMain;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

public class KeyboardChecker {

	public static void checkEventKeys(){
		while(Keyboard.next()){
			if(Keyboard.getEventKeyState()){
				if(Keyboard.getEventKey() == Keyboard.KEY_H){
					NuscoeMain.instance.healPlayer();
				}
				
				if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE){
					NuscoeMain.instance.isPaused = !NuscoeMain.instance.isPaused;
				}
				
				if(Keyboard.getEventKey() == Keyboard.KEY_F){
					NuscoeMain.instance.spawnProjectiles(NuscoeMain.instance.getPlayer());
				}
				if(Keyboard.getEventKey() == Keyboard.KEY_L){
					NuscoeMain.instance.addLife(1);
				}
				if(Keyboard.getEventKey() == Keyboard.KEY_SEMICOLON){
					NuscoeMain.instance.addLife(-1);
				}
				if(Keyboard.getEventKey() == Keyboard.KEY_RCONTROL){
					NuscoeMain.instance.exit(0);
				}
				if(Keyboard.getEventKey() == Keyboard.KEY_F11){
					DisplayModeSwitcher.setDisplayMode(NuscoeMain.instance.width, NuscoeMain.instance.height, !Display.isFullscreen());
				}
			}
		}
	}
	
}
