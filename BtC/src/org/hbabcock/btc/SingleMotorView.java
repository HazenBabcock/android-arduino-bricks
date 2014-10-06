package org.hbabcock.btc;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

//
// Designed to be used as a super-class.
//
// Sub-classes:
//   DualMotorView
//   HorizontalSingleMotorSlider
//   VerticalSingleMotorSlider
//
public class SingleMotorView extends View {
	protected static final int mLineWidth = 6;
	protected static final float mRadius = 20;

	protected int mCenterX;
	protected int mCenterY;
	protected boolean mEnabled = false;
	protected int mHeight;
	protected int mWidth;
	protected int mX;
	protected int mY;

	private Rect mBounds;
	
	protected int mColorBackground = Color.BLACK;
	protected int mColorDisabled = Color.RED;
	protected int mColorEnabled = Color.WHITE;
	protected int mColorForeground = Color.DKGRAY;

	protected Paint mPaintFill;
	protected Paint mPaintLine;

	protected int mSign1 = 0;
    protected int mMotorId1 = 0;

	public SingleMotorView(Context context, AttributeSet attrs) {
		super(context, attrs);

    	mPaintFill = new Paint();
    	mPaintFill.setStyle(Paint.Style.FILL);
    	mPaintLine = new Paint();
    	mPaintLine.setStrokeWidth(mLineWidth);
    	mPaintLine.setStyle(Paint.Style.STROKE);
	}

	public byte calcDir(float speed, int sign){
		byte dir = 0;
		if (speed < 0.0){
			dir = 1;
		}
		if (sign == 0){
			dir = (byte)(dir - 1);
		}
		return dir;
	}

	public byte calcSpeed(float fspeed){
		fspeed = (float)Math.abs(fspeed * 255.0);
		if (fspeed > 255.0) fspeed = (float)255.0;
		if (fspeed < 35.0) fspeed = (float)0.0;
		return (byte)fspeed;
	}

	//
	// motorId - which motor, 0 - 7.
	// sign - motor direction, 0 = "forward", 1 = "backward".
	//
	public void configure(int motorId, int sign){
		mMotorId1 = motorId;
		mSign1 = sign;
	}
	
    @Override
    protected void onDraw(Canvas canvas) {
    	super.onDraw(canvas);
        mPaintFill.setColor(mColorBackground);
		canvas.drawRect(mBounds, mPaintFill);
		
        mPaintLine.setColor(mColorForeground);
		canvas.drawRect(mBounds, mPaintLine);
    }
    
    @Override
    protected void onSizeChanged(int newWidth, int newHeight, int xOld, int yOld)
    {
    	super.onSizeChanged(newWidth, newHeight, xOld, yOld);
    	
    	mBounds = new Rect(0, 0, newWidth, newHeight);
    	
    	mWidth = newWidth;
    	mHeight = newHeight;
    	
    	mCenterX = mWidth/2;
    	mCenterY = mHeight/2;
    	
    	mX = mCenterX;
    	mY = mCenterY;
    }
    
    public void update(float clickX, float clickY){
    	mX = (int)clickX;
    	mY = (int)clickY;
    	if (mX < 0) mX = 0;
    	if (mY < 0) mY = 0;
    	if (mX > mWidth) mX = mWidth;
    	if (mX > mHeight) mY = mHeight;    	
    	invalidate();
    }
    
    public void setEnabled(boolean enabled){
    	mEnabled = enabled;
    	invalidate();
    }
    
    protected void stop(BluetoothIO bluetoothIO){
    	bluetoothIO.stopMotor(mMotorId1);
    	mX = mCenterX;
    	mY = mCenterY;
    	invalidate();
    }
    
}
