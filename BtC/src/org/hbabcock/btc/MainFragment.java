package org.hbabcock.btc;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/* 
 * To modify this for a different project you will need to do at least the following:
 * 
 * 1. Layout the controls you want in main_fragment.xml.
 * 2. Add class variables to MainFragment for each of the controls.
 * 3. Update enableUI().
 * 4. Update onCreateView().
 */
public class MainFragment extends Fragment {
	
    // Message types sent from the BluetoothIO Handler
	public static final int MESSAGE_CONNECT = 1;
	public static final int MESSAGE_DISCONNECT = 2;
    public static final int MESSAGE_TOAST = 3;
	
	// Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    
    private static final String TAG = "MainFragment";
    public static final String TOAST = "toast";
	
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothIO mBluetoothIO = null;

    // Controls to use in the UI.
    private VerticalSingleMotorSlider mRotate = null;
    private DualMotorRotated mTracks = null;
    private DualMotorNormal mArm = null;
    private DualMotorNormal mGripper = null;
    
	private View mView = null;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_CONNECT:
            	enableUI(true);
            	break;
            case MESSAGE_DISCONNECT:
            	enableUI(false);
            	break;
            case MESSAGE_TOAST:
                Toast.makeText(getActivity(), msg.getData().getString(TOAST), Toast.LENGTH_SHORT).show();
                break;
            }
        }
    };

    private void enableUI(boolean connected){
    	
    	// Controls to enable/disable when Bluetooth starts/stops.
    	if (connected){
    		mRotate.setEnabled(true);
    		mTracks.setEnabled(true);
    		mArm.setEnabled(true);
    		mGripper.setEnabled(true);    		
    	}
    	else{
    		mRotate.setEnabled(false);
    		mTracks.setEnabled(false);
    		mArm.setEnabled(false);
    		mGripper.setEnabled(false);    		
    	}
    }
    
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        case REQUEST_CONNECT_DEVICE:
            if (resultCode == Activity.RESULT_OK) {
                String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                mBluetoothIO.connect(device);
            }
            break;
        case REQUEST_ENABLE_BT:
            if (resultCode == Activity.RESULT_OK) {
                mBluetoothIO = new BluetoothIO(getActivity(), mHandler);                
            } else {
                Toast.makeText(getActivity(), "Bluetooth Not Enabled", Toast.LENGTH_SHORT).show();
            }
        }
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		getActivity().setTitle("");
		setHasOptionsMenu(true);
		
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(getActivity(), "Bluetooth is not available", Toast.LENGTH_LONG).show();
        }
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.main_fragment_options, menu);
	}
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
		Log.i(TAG, "onCreateView");

		mView = inflater.inflate(R.layout.main_fragment, parent, false);

		//
		// Initialize UI control elements & configure them as necessary to control
		// the appropriate motor(s).
		//
		// For single motor sliders:
		//   configure(motorId, direction) 
		//
		// For joystick/touchpad:
		//   configure(motorId1, direction1, motorId2, direction2)
		//
    	mRotate = (VerticalSingleMotorSlider)mView.findViewById(R.id.rotate);
    	mRotate.configure(3, 0);
    	mRotate.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
	            if (event.getAction()==MotionEvent.ACTION_UP){
	            	mRotate.stop(mBluetoothIO);
	            }
	            else {
	            	mRotate.update(mBluetoothIO, event.getX(), event.getY());
	            }
	            return true;
			}
    	});    	

    	mTracks = (DualMotorRotated)mView.findViewById(R.id.tracks);
    	mTracks.configure(0, 1, 4, 1);
    	mTracks.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
	            if (event.getAction()==MotionEvent.ACTION_UP){
					mTracks.stop(mBluetoothIO);
	            }
	            else {
					mTracks.update(mBluetoothIO, event.getX(), event.getY());
	            }
	            return true;
			}
    	});
    	
    	mArm = (DualMotorNormal)mView.findViewById(R.id.arm);
    	mArm.configure(1, 0, 5, 0);
    	mArm.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
	            if (event.getAction()==MotionEvent.ACTION_UP){
	            	mArm.stop(mBluetoothIO);
	            }
	            else {
	            	mArm.update(mBluetoothIO, event.getX(), event.getY());
	            }
	            return true;
			}
    	});

    	mGripper = (DualMotorNormal)mView.findViewById(R.id.gripper);
    	mGripper.configure(2, 0, 6, 0);
    	mGripper.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
	            if (event.getAction()==MotionEvent.ACTION_UP){
	            	mGripper.stop(mBluetoothIO);
	            }
	            else {
	            	mGripper.update(mBluetoothIO, event.getX(), event.getY());
	            }
	            return true;
			}
    	});
    	
    	enableUI(false);

		return mView;
	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_scan:
            	Intent i = new Intent(getActivity(), DeviceListActivity.class);
            	startActivityForResult(i, REQUEST_CONNECT_DEVICE);
            	return true;
            case R.id.action_disconnect:
            	if (mBluetoothIO != null)
            		mBluetoothIO.stop();
            	return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }	

	@Override
	public void onStart() {
		super.onStart();        
		if (!mBluetoothAdapter.isEnabled()) {
			Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(i, REQUEST_ENABLE_BT);
		}
		else{
            mBluetoothIO = new BluetoothIO(getActivity(), mHandler);
		}
	}
	
	@Override
	public void onStop() {
	    super.onStop();
	    
    	if (mBluetoothIO != null)
    		mBluetoothIO.stop();
	}
}
