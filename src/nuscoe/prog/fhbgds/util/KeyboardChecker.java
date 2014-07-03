package nuscoe.prog.fhbgds.util;

import nuscoe.prog.fhbgds.NuscoeMain;
import nuscoe.prog.fhbgds.entity.AIEntity;
import nuscoe.prog.fhbgds.entity.Entity;
import nuscoe.prog.fhbgds.entity.Follower;

import org.lwjgl.input.Keyboard;

public class KeyboardChecker {

	public static void checkEventKeys(){
		while(Keyboard.next()){
			if(Keyboard.getEventKeyState()){
				if(Keyboard.getEventKey() == Keyboard.KEY_H){
					NuscoeMain.instance.damagePlayer();
				}
				
				if(Keyboard.getEventKey() == Keyboard.KEY_F){
					Entity entity = NuscoeMain.instance.getRandomEntity(null);
					if(entity instanceof AIEntity && !(entity instanceof Follower)){
						AIEntity ent = (AIEntity) entity;
						ent.convert = true;
					}
				}
			}
		}
	}
	
}
