/*
 * Bluetooth motor control using a Arduino Uno, 2 Adafruit motor shields (#1438) 
 * and a Adafruit Bluefruit EZ-Link Bluetooth adapter (#1628).
 *
 * Hazen 09/14
 */

#include <Wire.h>
#include <Adafruit_MotorShield.h>
#include "utility/Adafruit_PWMServoDriver.h"

#define n_motors 8

int watchdog = 0;
Adafruit_MotorShield AFMS1 = Adafruit_MotorShield(0x60);
Adafruit_MotorShield AFMS2 = Adafruit_MotorShield(0x61);
Adafruit_DCMotor *motors[n_motors];

void setup()
{
  motors[0] = AFMS1.getMotor(1);
  motors[1] = AFMS1.getMotor(2);
  motors[2] = AFMS1.getMotor(3);
  motors[3] = AFMS1.getMotor(4);
  motors[4] = AFMS2.getMotor(1);
  motors[5] = AFMS2.getMotor(2);
  motors[6] = AFMS2.getMotor(3);
  motors[7] = AFMS2.getMotor(4);

  AFMS1.begin();
  AFMS2.begin();
  for(int i=0;i<n_motors;i++){
    motors[i]->run(RELEASE);
  }
  
  Serial.begin(9600);
  Serial.println("GTG");
}

void loop()
{
  uint8_t motorByte;
  uint8_t dirByte;
  uint8_t speedByte;
  uint8_t syncByte;

  // Wait till for a complete (4 byte) command.
  if (Serial.available() >= 4) {
    // Read the command.
    // byte 1 - motor number (0 .. number of motors, 255 resets the watchdog).
    // byte 2 - direction (0 = forward, everything else = backward).  
    // byte 3 - motor speed.
    // byte 4 - sync byte (255).
    //
    motorByte = Serial.read();
    dirByte = Serial.read();
    speedByte = Serial.read();
    syncByte = Serial.read();

    // Check that we are still in sync with the controller.
    if(syncByte == 255){
  
      // Reset watchdog timer.
      if(motorByte == 255){
        watchdog = 0;
      }
      
      // Set the motor speed.
      else{
        watchdog = 0;
        setMotorSpeed(motorByte, dirByte, speedByte);
      }
    }
    else{
      
      // Pull bytes out of the stream until we hit the next sync byte.
      while(syncByte != 255){
        if (Serial.available() > 0){
          syncByte = Serial.read();
        }
        delay(10);
      }        
    }    
  }
  else{
    delay(10);
  }

  // Update watchdog timer.
  watchdog = watchdog + 1;
  
  // Stop motors.
  if(watchdog > 100){
    for(int i=0;i<n_motors;i++){
      motors[i]->run(RELEASE); 
    }
    watchdog = 0;
  }
}

void setMotorSpeed(uint8_t motorByte, uint8_t dirByte, uint8_t speedByte)
{
  Adafruit_DCMotor *motor;
  
  if(motorByte < n_motors){
    motor = motors[motorByte];
    if (dirByte == 0){
      motor->run(FORWARD);
    }
    else{
      motor->run(BACKWARD);
    }
  
    if (speedByte == 0){
      motor->run(RELEASE);
    }
    motor->setSpeed(speedByte);
  }
}

