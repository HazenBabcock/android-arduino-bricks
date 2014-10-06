package org.hbabcock.btc;

import android.content.Context;
import android.util.AttributeSet;

//
// Designed to be used as a super-class.
//
// Sub-classes:
//   DualMotorNormal
//   DualMotorRotated
//
public class DualMotorView extends SingleMotorView {
	
	protected int mSign2 = 0;
    protected int mMotorId2 = 0;
    
	public DualMotorView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	//
	// motorId1 - which motor, 0 - 7.
	// sign1 - motor direction, 0 = "forward", 1 = "backward".
	// motorId2 - which motor, 0 - 7.
	// sign2 - motor direction, 0 = "forward", 1 = "backward".
	//
	public void configure(int motorId1, int sign1, int motorId2, int sign2){
		mMotorId1 = motorId1;
		mSign1 = sign1;
		mMotorId2 = motorId2;
		mSign2 = sign2;
	}

    protected void stop(BluetoothIO bluetoothIO){
    	bluetoothIO.stopMotor(mMotorId2);
    	super.stop(bluetoothIO);
    }

}
