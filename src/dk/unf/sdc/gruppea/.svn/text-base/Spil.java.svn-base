package dk.unf.sdc.gruppea;

import java.util.Random;

import android.os.Handler;
import android.util.Log;

public class Spil {
// importrandom
	
	private static final String tag = "SpilFrame";

	private long timeOfLastUpdate;
	private long timeLeft;
	boolean lost;
	
	private boolean hasBomb;

	private final Handler mHandler;
	private boolean isServer;
	private boolean isGameRunning;
	private boolean firstTimeRecieve;
	
	private static final int MESSAGEUPDATE_STOP = -1;
	private static final int MESSAGEUPDATE_GOOD = 0;

	public Spil(Handler handler) {
		mHandler = handler;
		isGameRunning = false;
		firstTimeRecieve = true;
	}

	public void startGameAsServer() {
		
		timeOfLastUpdate = System.currentTimeMillis();
		Random randomStart = new Random();
		timeLeft = randomStart.nextInt(25000) + 5000;
		
		isGameRunning = true;
		firstTimeRecieve = false;
		hasBomb = true;
		mHandler.obtainMessage(SpilActivity.MESSAGE_UPDATEVIEW).sendToTarget();
	}
	
	public void setupGameAsServer() {
		isServer = true;
		mHandler.obtainMessage(SpilActivity.MESSAGE_UPDATEVIEW).sendToTarget();
	}

	private void startGameAsClient() {
		isServer = false;
		isGameRunning = true;
		firstTimeRecieve = false;
		
	}


	public void receiveBomb(String stimeLeft) {
		if(firstTimeRecieve){
			startGameAsClient();
			
		}
		timeOfLastUpdate = System.currentTimeMillis();
		timeLeft = Long.valueOf(stimeLeft);
		hasBomb = true;
		mHandler.obtainMessage(SpilActivity.MESSAGE_UPDATEVIEW).sendToTarget();
	}
//Sender bomben såfrent tiden ikke overskredet
	
	public void sendBomb() {
		int i = updateBomb();
		
		if (i == MESSAGEUPDATE_GOOD) {
			
			mHandler.obtainMessage(SpilActivity.MESSAGE_SENDBOMB).sendToTarget();
			
			hasBomb = false;
			
			mHandler.obtainMessage(SpilActivity.MESSAGE_UPDATEVIEW).sendToTarget();
		}
		
	
		
	} // updaterer bombe tidsmæssigt, og tjekker for dens udløb

	public void restart(){
		isGameRunning = false;
		isServer = true;
		mHandler.obtainMessage(SpilActivity.MESSAGE_UPDATEVIEW).sendToTarget();
		
	}
	
	private int updateBomb() {
		long current = System.currentTimeMillis();
		
		timeLeft = timeLeft - (current - timeOfLastUpdate);
		timeOfLastUpdate = current;
		
		
		if (timeLeft <= 0) {
			lost = true;
			mHandler.obtainMessage(SpilActivity.MESSAGE_LOST).sendToTarget();
			return MESSAGEUPDATE_STOP;
		}
		return MESSAGEUPDATE_GOOD;

		
	}

	public boolean isServer() {
		return isServer;
	}

	public boolean isGameRunning() {
		return isGameRunning;
		
	}
	public boolean hasBomb() {
		return hasBomb;
	}
	public long getTimeLeft() {
		return timeLeft;
	}
	public void setTimeLeft(long t) {
		Random randomStart = new Random();
		timeLeft = randomStart.nextInt(25000) + 5000; 
	}
}
