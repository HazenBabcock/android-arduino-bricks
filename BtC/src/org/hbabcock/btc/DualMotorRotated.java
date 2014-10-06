package org.hbabcock.btc;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

//
// 2D view, X+Y controls motor1 speed, X-Y controls motor2 speed.
// This is convenient for things like track control where you want
// both motors to go forward / backward when you move up / down.
//
public class DualMotorRotated extends DualMotorView {
	
	public DualMotorRotated(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
    @Override
    protected void onDraw(Canvas canvas) {
    	super.onDraw(canvas);

    	// draw cross
    	mPaintLine.setColor(mColorForeground);
    	canvas.drawLine((float)0, (float)0, (float)mWidth, (float)mHeight, mPaintLine);
    	canvas.drawLine((float)0, (float)mHeight, (float)mWidth, (float)0, mPaintLine);
    	
    	if(mEnabled){
    		mPaintLine.setColor(mColorEnabled);
        	canvas.drawCircle((float)mX, (float)mY, mRadius, mPaintLine);
    	}
    	else{
    		mPaintFill.setColor(mColorDisabled);
        	canvas.drawCircle((float)mX, (float)mY, mRadius, mPaintFill);
    	}
    }
    
    public void update(BluetoothIO bluetoothIO, float clickX, float clickY){
    	super.update(clickX, clickY);

    	if (mEnabled){
    		float fspeed1 = (float)(clickX - mCenterX)/ (float)mCenterX;
    	    float fspeed2 = (float)(clickY - mCenterY)/ (float)mCenterY;

    	    float m1 = (float)(fspeed1 + fspeed2);
    	    float m2 = (float)(fspeed1 - fspeed2);
    	    
    		// motor1 - x + y.
    	    bluetoothIO.setMotor(mMotorId1, calcDir(m1, mSign1), calcSpeed(m1));

    	    // motor2 - x - y.
    	    bluetoothIO.setMotor(mMotorId2, calcDir(m2, mSign2), calcSpeed(m2));
    	}
    }
}
