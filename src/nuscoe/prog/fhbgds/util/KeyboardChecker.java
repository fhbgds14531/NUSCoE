package nuscoe.prog.fhbgds.util;

import nuscoe.prog.fhbgds.NuscoeMain;

import org.lwjgl.input.Keyboard;

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
				
				if(Keyboard.getEventKey() == Keyboard.KEY_SPACE){
					NuscoeMain.instance.spawnPlayerProjectiles();
				}
				if(Keyboard.getEventKey() == Keyboard.KEY_L){
					NuscoeMain.instance.addLife(1);
				}
				if(Keyboard.getEventKey() == Keyboard.KEY_SEMICOLON){
					NuscoeMain.instance.addLife(-1);
				}
			}
		}
	}
	
}
