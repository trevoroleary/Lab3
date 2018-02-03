/*
 * SquareDriver.java
 */
package ca.mcgill.ecse211.Lab3;
import ca.mcgill.ecse211.odometer.Odometer;
import ca.mcgill.ecse211.odometer.OdometerData;
import ca.mcgill.ecse211.odometer.OdometerExceptions;
import lejos.hardware.motor.EV3LargeRegulatedMotor;

/**
 * This class is used to drive the robot on the demo floor.
 */
public class Navigation {
	private static final int FORWARD_SPEED = 400;
	private static final int ROTATE_SPEED = 250;
	private static final int ACCEL = 200;
	private EV3LargeRegulatedMotor leftMotor;
	private EV3LargeRegulatedMotor rightMotor;
	private double leftRadius;
	private double rightRadius;
	private double track;
	private Odometer odo;
	private double TILE_SIZE = 30.48;

  public Navigation(EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor,
      double leftRadius, double rightRadius, double track, Odometer odo) throws OdometerExceptions {
	  
	  this.leftRadius = leftRadius;
	  this.rightRadius = rightRadius;
	  this.leftMotor = leftMotor;
	  this.rightMotor = rightMotor;
	  this.leftRadius = leftRadius;
	  this.rightRadius = rightRadius;
	  this.track = track;
	  this.odo = odo; 
	  
  }
 
  /**
   * This method allows the conversion of a distance to the total rotation of each wheel need to
   * cover that distance.
   * 
   * @param radius
   * @param distance
   * @return
   */
  private static int convertDistance(double radius, double distance) {
    return (int) ((180.0 * distance) / (Math.PI * radius));
  }

  private static int convertAngle(double radius, double width, double angle) {
    return convertDistance(radius, Math.PI * width * angle / 360.0);
  }
  
  public void travelTo(double x, double y){
	  
	  double[] location =  odo.getXYT();
	  x = x*TILE_SIZE;
	  y = y*TILE_SIZE;
	  double dx = x - location[0];
	  double dy = y - location[1];
	  double dTheta = (Math.atan2(dx,dy)*180/3.1415) - location[2];
	  double hypo = Math.sqrt(Math.pow((x - location[0]),2) + Math.pow((y - location[1]), 2));
	  
	  turnTo(dTheta);
	  leftMotor.setAcceleration(ACCEL);
	  rightMotor.setAcceleration(ACCEL);
	  
	  leftMotor.setSpeed(FORWARD_SPEED);
	  rightMotor.setSpeed(FORWARD_SPEED);
	  
	  
	  leftMotor.rotate(convertDistance(leftRadius, hypo), true);
	  rightMotor.rotate(convertDistance(rightRadius, hypo), false);
     
	 
  }
  
  public void turnTo(double theta) {
	  
	  //EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor,
      //double leftRadius, double rightRadius, double track
	  
	  leftMotor.setAcceleration(ACCEL);
	  rightMotor.setAcceleration(ACCEL);
	  leftMotor.setSpeed(ROTATE_SPEED);
	  rightMotor.setSpeed(ROTATE_SPEED);

	  leftMotor.rotate(convertAngle(leftRadius, track, theta), true);
	  rightMotor.rotate(-convertAngle(rightRadius, track, theta), false);
  }
  public boolean isNavigating() {
	  return false;
  }
}
