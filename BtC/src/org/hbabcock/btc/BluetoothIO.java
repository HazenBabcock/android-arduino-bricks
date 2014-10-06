/*
 * Bluetooth IO class.
 * 
 * Much of the code comes from here:
 *   https://github.com/luugiathuy/Remote-Bluetooth-Android
 * 
 * And here:
 *   http://developer.android.com/guide/topics/connectivity/bluetooth.html
 *   
 * Hazen 03/14
 */

package org.hbabcock.btc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/*
 * Variables and methods listed alphabetically.
 */
public class BluetoothIO {

    private static final boolean DEBUG = true;

    // The magic UUID that you have to use to get a serial over BlueTooth connection..
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final String TAG = "BluetoothIO";

    private static final int NFIELDS = 3;
    private static final int NMOTORS = 8;
    private static final int LIVE = 0;
    private static final int DIRECTION = 1;
    private static final int SPEED = 2;
    
    private final BluetoothAdapter mBluetoothAdapter;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    private SynchronizedMotorData mMotorData;
    private final Handler mHandler;
    
    public BluetoothIO(Context context, Handler handler) {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mHandler = handler;
        
        // Initialize motor data.
    }

    public void connect(BluetoothDevice device) {
    	if (DEBUG) Log.d(TAG, "connect to: " + device);

    	if (mConnectThread != null) {
    		mConnectThread.cancel(); 
    		mConnectThread = null;
    	}
    	if (mConnectedThread != null) {
    		mConnectedThread.cancel(); 
    		mConnectedThread = null;
    	}

    	mConnectThread = new ConnectThread(device);
    	mConnectThread.start();
    }
    
    public void connected(BluetoothSocket socket){
    	if (DEBUG) Log.d(TAG, "connected");
    	 
    	mMotorData = new SynchronizedMotorData();
    	
    	mConnectedThread = new ConnectedThread(socket);
    	mConnectedThread.start();
    	 
    	Message msg = mHandler.obtainMessage(MainFragment.MESSAGE_CONNECT);
     	mHandler.sendMessage(msg);
    }
    
    /*
     * Handles IO on the connection.
     */
    private class ConnectedThread extends Thread {
    	private boolean running = true;
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
     
        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }
     
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }
     
        public void run() {
            //
            // Loop until an exception occurs.
            //
            // Even if the direction/speed has not changed we need to keep updating
            // the motor, otherwise the Arduino is programmed to stop it.
            //
            while (running) {
                try {
                	// Iterate over motor data, sending new data if necessary.
                	for(int i=0; i<NMOTORS; i++){
                        if (mMotorData != null){
                        	if (mMotorData.live(i)){
                        		mmOutStream.write(mMotorData.getMsg(i));
                        	}
                        	Thread.sleep(10);
                        	if (!running){
                        		return;
                        	}
                        }
                	}
                } 
                catch (IOException e) {
                    Log.e(TAG, "disconnected", e);
                    //connectionLost();
                    break;
                }
                catch (InterruptedException e) {
                    Log.e(TAG, "sleep interrupted", e);
                    break;
                }
            }
        }
     
        public void cancel() {
            if (DEBUG) Log.d(TAG, "cancel (ConnectedThread)");
            try {
            	running = false;
            	mmInStream.close();
            	mmOutStream.close();
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    private void connectionLost() {
    	Message msg = mHandler.obtainMessage(MainFragment.MESSAGE_TOAST);
    	Bundle bundle = new Bundle();
    	bundle.putString(MainFragment.TOAST, "Device connection was lost");
    	msg.setData(bundle);
    	mHandler.sendMessage(msg);    	
    	
    	stop();
    }

    /*
     * Creates the connection.
     */
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
     
        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) { }
            mmSocket = tmp;
        }
     
        public void run() {
            mBluetoothAdapter.cancelDiscovery();
     
            try {
                mmSocket.connect();
            } catch (IOException connectException) {
                try {
                    mmSocket.close();
                } catch (IOException closeException) { }
                return;
            }
     
            connected(mmSocket);
        }
     
        public void cancel() {
            if (DEBUG) Log.d(TAG, "cancel (ConnectThread)");
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    public void setMotor(int motorId, byte dir, byte speed) {
    	if (mMotorData != null){
    		if ((motorId >= 0) && (motorId < NMOTORS)){
        		mMotorData.setMotor(motorId, dir, speed);
    		}
    		else {
    			Log.w(TAG, "Motor id out of range (" + motorId + ")!");
    		}
    	}
    }

    public void stop() {
        if (DEBUG) Log.d(TAG, "stop");
        if (mConnectThread != null){
        	mConnectThread.cancel(); 
        	mConnectThread = null;
        }
        if (mConnectedThread != null) {
        	mConnectedThread.cancel(); 
        	mConnectedThread = null;
        }
        mMotorData = null;
        
    	Message msg = mHandler.obtainMessage(MainFragment.MESSAGE_DISCONNECT);
    	mHandler.sendMessage(msg);
    }

    public void stopMotor(int motorId){
    	if (mMotorData != null){
    		if ((motorId >= 0) && (motorId < NMOTORS)){
        		mMotorData.stopMotor(motorId);
    		}
    		else {
    			Log.w(TAG, "Motor id out of range(" + motorId + ")!");
    		}
    	}
    }
    
    private class SynchronizedMotorData {
    	private static final byte RUNNING = 2;
    	private static final byte STOPPING = 1;
    	private static final byte STOPPED = 0;
    	
        private byte[] mData = new byte[NFIELDS*NMOTORS];

        public SynchronizedMotorData(){
        	for(int i=0; i<NMOTORS; i++){
        		mData[i*NFIELDS + LIVE] = 0;
        		mData[i*NFIELDS + DIRECTION] = 0;
        		mData[i*NFIELDS + SPEED] = 0;
        	}
        }
        
        public synchronized byte[] getMsg(int motorId) {
        	byte[] msg = new byte[4];
      
        	msg[0] = (byte)motorId;
        	msg[1] = mData[motorId*NFIELDS + DIRECTION];
        	msg[2] = mData[motorId*NFIELDS + SPEED];
        	msg[3] = (byte)255;

        	return msg;
        }
        
        public synchronized boolean live(int motorId){
            switch (mData[motorId*NFIELDS + LIVE]) {
            case RUNNING:
            	return true;
            case STOPPING:
            	mData[motorId*NFIELDS + LIVE] = STOPPED;
            	return true;
            case STOPPED:
            	return false;
            default:
            	return false;
            }
        }

        public synchronized void setMotor(int motorId, byte dir, byte speed) {
	    	//if (DEBUG) Log.d(TAG, "setMotor " + dir + "," + speed);
            mData[motorId*NFIELDS + LIVE] = RUNNING;
            mData[motorId*NFIELDS + DIRECTION] = dir;
            mData[motorId*NFIELDS + SPEED] = speed;
        }
        
        public synchronized void stopMotor(int motorId){
            mData[motorId*NFIELDS + LIVE] = STOPPING;        	
            mData[motorId*NFIELDS + SPEED] = 0;
        }
    }
}
