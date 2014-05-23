package com.ignatieff.mid;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

public class MidiInputReceiver implements Receiver {
	 public String name;
	 private Robot r;
	 private boolean listening;
	 
	 private Timer t;
	 
	 public MidiInputReceiver(String name) {
		 try {
			r = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		listening = false;
	    this.name = name;
	    t = new Timer();
	}
	public void send(MidiMessage msg, long timeStamp) {
		if(r==null)return;
	    ShortMessage sm = (ShortMessage)msg;
	    if(sm.getData1() == 21){
	    	listening = (sm.getStatus() == ShortMessage.NOTE_ON);
	    }
	    if(sm.getStatus()==ShortMessage.NOTE_OFF && listening){
	    	switch(sm.getData1()-48){
    			case -7:
    				moveMouse(-20, 0);
    				break;
    			case -6:
    				moveMouse(0, -20);
    				break;
    			case -5:
    				moveMouse(0, 20);
    				break;
    			case -4:
    				moveMouse(20, 0);
    				break;
    			case -2:
    				clickKey(KeyEvent.VK_PAGE_UP);
    				break;
    			case -1:
    				clickKey(KeyEvent.VK_PAGE_DOWN);
    				break;
	    		case 0:
	    			moveMouse(-50, 0);
	    			break;
	    		case 1:
	    			moveMouse(0, -50);
	    			break;
	    		case 2:
	    			moveMouse(0, 50);
	    			break;
	    		case 3:
	    			moveMouse(50, 0);
	    			break;
	    		case 4:
	    			clickMouse(InputEvent.BUTTON1_MASK);
	    			break;
	    		case 5:
	    			clickMouse(InputEvent.BUTTON3_MASK);
	    			break;
	    		case 6:
	    			clickKey(KeyEvent.VK_ENTER);
	    			break;
	    		case 7:
	    			clickKey(KeyEvent.VK_SPACE);
	    			break;
	    		case 8:
	    			clickKey(KeyEvent.VK_ESCAPE);
	    			break;
	    	}
	    }
	}
	
	private void clickKey(final int key){
		r.keyPress(key);
		t.schedule(new TimerTask(){
					@Override
					public void run(){
						r.keyRelease(key);
					}
				}, 10);
	}
	
	private void clickMouse(final int button){
		r.mousePress(button);
		t.schedule(new TimerTask(){
					@Override
					public void run(){
						r.mouseRelease(button);
					}
				}, 10);
	}
	
	private void moveMouse(int dx, int dy){
		Point p = getMouse();
		r.mouseMove(p.x+dx, p.y+dy);
	}
	
	private Point getMouse(){
		Point p = MouseInfo.getPointerInfo().getLocation();
		return p;
	}
	
	public void close() {}
}
