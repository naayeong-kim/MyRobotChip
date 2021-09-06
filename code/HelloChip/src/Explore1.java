
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.robotics.Gyroscope;
import lejos.utility.Delay;

public class Explore1 {
	public static float black;
	public static float white;
	public static float offset;
	public static UnregulatedMotor motorA;
	public static UnregulatedMotor motorB;
	public static ColorSensor sensor;
	public static Gyroscope gyro;
	public static PIDController control_PID;
	public static UltrasonicSensor uss;
	public static double inf = Double.POSITIVE_INFINITY;
	public static float dist = (float) inf;
	public static float angle = 0;
	public static Tune song = new Tune();

	public static void main(String[] args) {
		gyro = new GyroSensor(SensorPort.S2);
		motorA = new UnregulatedMotor(MotorPort.A);
		motorB = new UnregulatedMotor(MotorPort.B);
		sensor = new ColorSensor();
		offset = calibrate(sensor);
		control_PID = new PIDController(motorA, motorB, offset);
		uss = new UltrasonicSensor(SensorPort.S4);

		Button.waitForAnyEvent();
		// findObject();
		// rideToObject();
		grid();
		search();
		maze();

	}

	/**
	 * walks through the grid
	 */
	private static void grid() {
		reachGrid();
		turnToPosition();
		goAround();
		toInnerSide3();
		ToMiddle();
		// song.playTune();

	}

	/**
	 * searches for the nearest pillar
	 */
	private static void search() {
		findObject();
		rideToObject();
		Sound.setVolume(50);
		Sound.beepSequenceUp();

	}

	private static void maze() {
		float distance = 50;
		float sensorValue = 0;
		Delay.msDelay(3000);
		System.out.println("Maze has started");

		while (distance > 0.45) {
			System.out.println("Wandering through the maze");
			sensorValue = sensor.redSample()[0];
			control_PID.control(sensorValue);
			distance = uss.getRange();
		}
		while (distance > 0.20) {
			System.out.println("The end is near");
			driveForward();
			distance = uss.getRange();
		}
		stop();
		Sound.setVolume(100);
		Sound.beep();

	}

	private static void reachGrid() {

		float sensorValue = 0;
		while (sensorValue <= offset) {
			driveForward();
			sensorValue = sensor.redSample()[0];
		}
		stop();

	}

	private static void turnToPosition() {
		gyro.recalibrateOffset();
		turn90Right();
		findOffset();

	}

	private static void goAround() {
		float sensorValue = 0;
		while (gyro.getAngle() < 360 && gyro.getAngle() > -360) {
			sensorValue = sensor.redSample()[0];
			control_PID.control(sensorValue);
		}

	}

	private static void toInnerSide3() {
		float sensorValue = 1;
		driveForward();
		Delay.msDelay(400);
		turn90Left();
		System.out.println("turned left");
		while (sensorValue > black) {
			sensorValue = sensor.redSample()[0];
			driveForward();
		}
		stop();
		turn90Right();
		gyro.reset();

	}

	// private static void toInnerSide2() {
	// float sensorValue = sensor.redSample()[0];
	// while (sensorValue < white) {
	// sensorValue = sensor.redSample()[0];
	// motorA.setPower(5);
	// motorB.setPower(40);
	// }
	// System.out.println("White found");
	// findOffset();
	// }
	//
	// /**
	// * goes to the inner side of the line
	// */
	// private static void toInnerSide() {
	// float sensorValue = 1;
	// Delay.msDelay(2500);
	// int gyroOld = gyro.getAngle();
	// Delay.msDelay(200);
	// turnLeft(gyroOld);
	// System.out.println("turned to position");
	// Delay.msDelay(2000);
	// while (sensorValue > (black)) {
	// sensorValue = sensor.redSample()[0];
	// driveForward();
	// }
	// System.out.println("INNER SIDE FOUND");
	// stop();
	// findOffset();
	//
	// }

	/**
	 * walks to the middle of the grid and stops there
	 */
	private static void ToMiddle() {
		System.out.println("TO THE MIDDLE");
		gyro.reset();
		float sensorValue = sensor.redSample()[0];
		while (gyro.getAngle() < 170 && gyro.getAngle() > -170) {
			sensorValue = sensor.redSample()[0];
			control_PID.control(sensorValue);
		}
		stop();
		System.out.println("Middle reached");
	}

	private static void findObject() {
		float initDist = 2;
		while (dist >= initDist) {
			dist = uss.getRange();
			System.out.println(dist);
			turnLeft();
		}
		stop();

	}

	private static void rideToObject() {
		float initDist = dist;
		int counter = 0;
		while (dist > 0.25) {
			System.out.println(dist);
			dist = uss.getRange();
			if (dist <= initDist + 0.03) {
				counter = 0;
				initDist = dist;
				driveForward();
			} else {
				if (counter <= 5) {
					motorA.setPower(40);
					motorB.setPower(5);
				} else if (counter > 5 && counter <= 15) {
					motorA.setPower(5);
					motorB.setPower(40);
				} else if (counter > 15)
					findObject();
			}
			counter++;
		}

	}

	/**
	 * turns 35 degrees left
	 * 
	 * @param gyroOld
	 */
	private static void turnLeft(int gyroOld) {
		while ((gyroOld - gyro.getAngle()) < 35) {
			motorA.setPower(30);
			motorA.setPower(-30);

		}
	}

	private static void findOffset() {
		float sensorValue = 0;
		while (gyro.getAngularVelocity() != 0) {
			sensorValue = sensor.redSample()[0];
			control_PID.control(sensorValue);

		}
		gyro.reset();
		System.out.println("Offset found");
	}

	private static float calibrate(ColorSensor sensor) {
		white = calWhite(sensor);
		black = calBlack(sensor);
		float offset = (white + black) / 2;
		return offset;
	}

	private static float calWhite(ColorSensor sensor) {
		System.out.println("Detect white");
		Button.waitForAnyPress();
		float white = sensor.redSample()[0];
		System.out.println(white);
		return white;

	}

	private static float calBlack(ColorSensor sensor) {
		System.out.println("Detect black");
		Button.waitForAnyPress();
		float black = sensor.redSample()[0];
		System.out.println(black);
		return black;

	}

	private static void stop() {
		motorA.setPower(0);
		motorB.setPower(0);

	}

	private static void driveForward() {
		motorA.setPower(50);
		motorB.setPower(50);

	}

	// private static void chooseAngle() {
	//
	// gyro.reset();
	// while (gyro.getAngle() > -360 && gyro.getAngle() < 360) {
	// motorA.setPower(30);
	// motorB.setPower(-30);
	// if (uss.getRange() < dist) {
	// dist = uss.getRange();
	// System.out.println("Distance= " + dist);
	// angle = gyro.getAngle();
	// System.out.println("Angle= " + angle);
	// }
	// }
	// System.out.println("choosing angle finished");
	//
	// }
	//
	// private static void turnToAngle() {
	// while (gyro.getAngle() < angle) {
	// turnLeft();
	// }
	// stop();
	// System.out.println("turn to angle finished");
	//
	// }
	//
	// private static void goToGoal() {
	// dist = uss.getRange();
	// int x = 0;
	// while (dist > 0.15) {
	// dist = uss.getRange();
	// motorA.setPower(40 + x);
	// motorB.setPower(40 - x);
	// if (gyro.getAngle() > angle + 1)
	// x = 8;
	// if (gyro.getAngle() < angle - 1)
	// x = -8;
	// else if (gyro.getAngle() < angle + 1 && gyro.getAngle() > angle - 1)
	// x = 0;
	// }
	// stop();
	// }

	private static void turnLeft() {
		motorA.setPower(-30);
		motorB.setPower(30);

	}

	private static void turn90Right() {
		gyro.reset();
		while (gyro.getAngle() < 90 && gyro.getAngle() > -90) {
			motorA.setPower(30);
			motorB.setPower(-30);
		}
	}

	private static void turn90Left() {
		gyro.reset();
		while (gyro.getAngle() < 90 && gyro.getAngle() > -90) {
			motorA.setPower(-30);
			motorB.setPower(30);
		}
	}
}