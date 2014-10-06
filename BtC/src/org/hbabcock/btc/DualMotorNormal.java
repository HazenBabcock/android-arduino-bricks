package org.hbabcock.btc;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

//
// 2D view, X axis controls motor1, Y axis controls motor 2.
//
public class DualMotorNormal extends DualMotorView {
	
	public DualMotorNormal(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
    @Override
    protected void onDraw(Canvas canvas) {
    	super.onDraw(canvas);

    	// draw cross
    	mPaintLine.setColor(mColorForeground);
    	canvas.drawLine((float)mCenterX, (float)0, (float)mCenterX, (float)mHeight, mPaintLine);
    	canvas.drawLine((float)0, (float)mCenterY, (float)0, (float)mCenterY, mPaintLine);

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
    		// motor1 - x axis.
    		float fspeed1 = (float)(clickX - mCenterX)/ (float)mCenterX;
    	    bluetoothIO.setMotor(mMotorId1, calcDir(fspeed1, mSign1), calcSpeed(fspeed1));

    	    // motor2 - y axis.
    	    float fspeed2 = (float)(clickY - mCenterY)/ (float)mCenterY;
    	    bluetoothIO.setMotor(mMotorId2, calcDir(fspeed2, mSign2), calcSpeed(fspeed2));
    	}
    }

}
