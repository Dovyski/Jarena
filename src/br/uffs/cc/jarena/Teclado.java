/**
 * This file is free to use and modify as it is for educational use.
 * brought to you by Game Programming Snippets (http://gpsnippets.blogspot.com/)
 * 
 * Revisions:
 * 1.1 Initial Revision 
 * 
 */

package br.uffs.cc.jarena;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * This class was created to show how implement a keyboard polling system
 * using java.awt.event.KeyListener this allows the ability to capture multiple
 * keys being pressed simulatneously. To use this class just simply add it as a
 * key listener to your JFrame and it will be populated with input. 
 */
public final class Teclado implements KeyListener {

	//array of key states made as integers for more possible states. 
	private int[] keys = new int[256];
	
	//one for each ascii character.
	private boolean[] keysUp = new boolean[256];  //true if pressed
	private boolean[] keysDown = new boolean[256]; //true if not pressed
	
	//variable that indicates when any key(s) are being pressed.
	private boolean keyPressed = false;
	
	//variable that indicates that some key was released this frame.
	private boolean keyReleased = false; //cleared every frame.
	
	//a string used as a buffer by widgets or other text input controls
	private String keyCache = "";
	
	// indicates the last key pressed.
	private int lastKeyPressed;
	
	//the only instantiated object
	private static Teclado instance = new Teclado();
	
	/**
	 * Empty Constructor: nothing really needed here.
	 */
	protected Teclado() {
	}

	/**
	 * Singleton accessor the only means of getting the instantiated object.
	 * @return One and only InputManager object.
	 */
	public static Teclado getInstance() {
		return instance;
	}
	
	/**
	 * This function is specified in the KeyListener interface. It sets the 
	 * state elements for whatever key was pressed.
	 * 
	 * @param e The KeyEvent fired by the awt Toolkit
	 */
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode(); 
		
		if(keyCode >= 0 && keyCode < 256) {
			keys[keyCode] 		= (int) System.currentTimeMillis();
			keysDown[keyCode] 	= true;
			keysUp[keyCode] 	= false;
			keyPressed 			= true;
			keyReleased 		= false;
			lastKeyPressed		= keyCode;
		}
	}

	/**
	 * This function is specified in the KeyListener interface. It sets the 
	 * state elements for whatever key was released.
	 * 
	 * @param e The KeyEvent fired by the awt Toolkit.
	 */
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		
		if(keyCode >= 0 && keyCode < 256) {
			keys[keyCode] 		= 0;
			keysUp[keyCode] 	= true;
			keysDown[keyCode] 	= false;
			keyPressed 			= false;
			keyReleased 		= true;
		}
	}

	/**
	 * This function is called if certain keys are pressed namely those used
	 * for text input.
	 * 
	 * @param e The KeyEvent fired by the awt Toolkit.
	 */
	public void keyTyped(KeyEvent e) {	
		keyCache += e.getKeyChar();
		
	}
	
	/**
	 * Returns true if the key (0-256) is being pressed use the KeyEvent.VK_
	 * key variables to check specific keys.
	 * 
	 * @param key The ascii value of the keyboard key being checked
	 * @return true is that key is currently being pressed.
	 */
	public boolean isKeyDown( int key ) {
		return keysDown[key];
	}
	
	/**
	 * Returns true if the key (0-256) is being pressed use the KeyEvent.VK_
	 * key variables to check specific keys.
	 * 
	 * @param key The ascii value of the keyboard key being checked
	 * @return true is that key is currently being pressed.
	 */
	public boolean isKeyUp( int key ) {
		return keysUp[key];
	}

	/**
	 * In case you want to know if a user is pressing a key but don't care which
	 * one.
	 * 
	 * @return true if one or more keys are currently being pressed.
	 */
	public boolean isAnyKeyDown() {
		return keyPressed;
	}
	
	/**
	 * In case you want to know if a user released a key but don't care which
	 * one.
	 * 
	 * @return true if one or more keys have been released this frame. 
	 */
	public boolean isAnyKeyUp() {
		return keyReleased;
	}
	
	/**
	 * Returns the last key pressed.
	 * 
	 * @return code for the last key pressed.
	 */
	public int getLastKeyPressed() {
		return lastKeyPressed;
	}
	
	/**
	 * Only resets the key state up because you don't want keys to be showing
	 * as up forever which is what will happen unless the array is cleared.
	 */
	public void update() {
		//clear out the key up states
		keysUp = new boolean[256];
		keyReleased = false;
		if( keyCache.length() > 1024 ) {
			keyCache = "";
		}
	}	
}