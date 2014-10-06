
This is a standard Android project that you should be able to edit with Eclipse or equivalent.

The classes are:
* BluetoothIO.java - Handles IO via Bluetooth with the Arduino.
* DeviceListActivity.java - Lists all the available Bluetooth devices.
* DualMotorNormal.java - Control of two motors simultaneously using a joystick/trackpad like interface.
* DualMotorRotated.java - This is the same as DualMotorNormal accept the axises are rotated by 45 degrees to make control of things like tracked vehicles more intuitive.
* DualMotorView.java - The super-class of DualMotorNormal and DualMotorRotated.
* HorizontalSingleMotorSlider.java - A horizontally oriented slider that controls a single motor.
* MainActivity.java - The main activity.
* MainFragment.java - This handles the UI and this is what you will need to change to re-configure the app to control different projects.
* SingleFragmentActivity.java - An activity that displays a single fragment.
* SingleMotorView.java - The super-class of all the motors controls.
* VerticalSingleMotorSlider.java - A vertically oriented slider that controls a single motor.

Also, the UI layout is specified by main_fragment.xml.
