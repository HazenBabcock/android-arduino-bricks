package org.hbabcock.btc;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

public class VerticalSingleMotorSlider extends SingleMotorView {
	public VerticalSingleMotorSlider(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
    @Override
    protected void onDraw(Canvas canvas) {
    	super.onDraw(canvas);

    	if(mEnabled){
    		mPaintLine.setColor(mColorEnabled);
        	canvas.drawCircle((float)(mWidth/2), (float)mY, mRadius, mPaintLine);
    	}
    	else{
    		mPaintFill.setColor(mColorDisabled);
        	canvas.drawCircle((float)(mWidth/2), (float)mY, mRadius, mPaintFill);
    	}
    }
    
    public void update(BluetoothIO bluetoothIO, float clickX, float clickY){
    	super.update(clickX, clickY);

    	if (mEnabled){
    		float fspeed = (float)(clickY - mCenterY)/ (float)mCenterY;
    	    bluetoothIO.setMotor(mMotorId1, calcDir(fspeed, mSign1), calcSpeed(fspeed));
    	}
    }

}
