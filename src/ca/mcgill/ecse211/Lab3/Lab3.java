package ca.mcgill.ecse211.Lab3;

import ca.mcgill.ecse211.odometer.*;
import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.sensor.EV3ColorSensor;

public class Lab3 {

  // Motor Objects, and Robot related parameters
  private static final EV3LargeRegulatedMotor leftMotor =
      new EV3LargeRegulatedMotor(LocalEV3.get().getPort("A"));
  private static final EV3LargeRegulatedMotor rightMotor =
      new EV3LargeRegulatedMotor(LocalEV3.get().getPort("D"));
  private static final TextLCD lcd = LocalEV3.get().getTextLCD();
  
  public static final double WHEEL_RAD = 2.15;
  public static final double TRACK = 10.3;

  public static void main(String[] args) throws OdometerExceptions {
	  
    Odometer odometer = Odometer.getOdometer(leftMotor, rightMotor, TRACK, WHEEL_RAD); 
    final Navigation Nav = new Navigation(leftMotor, rightMotor, WHEEL_RAD, WHEEL_RAD, TRACK, odometer);

    Display odometryDisplay = new Display(lcd); // No need to change
    
    do {
      // clear the display
      lcd.clear();

      lcd.drawString("   Press  Any   ", 0, 1);
      lcd.drawString("     Button     ", 0, 2);

      Button.waitForAnyPress();    
      
      Thread odoThread = new Thread(odometer);
      odoThread.start();
      Thread odoDisplayThread = new Thread(odometryDisplay);
      odoDisplayThread.start();

      
      new Thread() {
        public void run() {
			Nav.travelTo(2,1);
			Nav.travelTo(1, 1);
			Nav.travelTo(1,2);
			Nav.travelTo(2,0);
        }
      }.start();
    }
    
    while (Button.waitForAnyPress() != Button.ID_ESCAPE);
    System.exit(0);
  }
}
