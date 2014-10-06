package org.hbabcock.btc;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

public class HorizontalSingleMotorSlider extends SingleMotorView {
    private static final String TAG = "HorizontalSingleMotorSlider";

	public HorizontalSingleMotorSlider(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
    @Override
    protected void onDraw(Canvas canvas) {
    	super.onDraw(canvas);

    	if(mEnabled){
    		mPaintLine.setColor(mColorEnabled);
        	canvas.drawCircle((float)mX, (float)(mHeight/2), mRadius, mPaintLine);
    	}
    	else{
    		mPaintFill.setColor(mColorDisabled);
        	canvas.drawCircle((float)mX, (float)(mHeight/2), mRadius, mPaintFill);
    	}
    }
    
    public void update(BluetoothIO bluetoothIO, float clickX, float clickY){
    	super.update(clickX, clickY);

    	if (mEnabled){
    		float fspeed = (float)(clickX - mCenterX)/ (float)mCenterX;
    	    bluetoothIO.setMotor(mMotorId1, calcDir(fspeed, mSign1), calcSpeed(fspeed));
    	}
    }
}
