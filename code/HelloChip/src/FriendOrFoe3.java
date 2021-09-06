

import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;

public class FriendOrFoe3 {

	private static UnregulatedMotor motorA;
	private static UnregulatedMotor motorB;
	private static ColorSensor sensor;
	private static UltrasonicSensor uss;
	private static float[] sensorValue;
	private static int pillarsFound = 0;
	private static double inf = Double.POSITIVE_INFINITY;
	private static float dist = (float) inf;
	private static Tune tune;

	public static void main(String[] args) throws InterruptedException {
		motorA = new UnregulatedMotor(MotorPort.A);
		motorB = new UnregulatedMotor(MotorPort.B);
		sensor = new ColorSensor();
		uss = new UltrasonicSensor(SensorPort.S4);
		tune = new Tune();

		System.out.println("GO EXPLORE, YOU LITTLE SHIT");

		while (pillarsFound != 6) {
			searchPillars();
		}

	}

	private static void searchPillars() throws InterruptedException {
		if (checkColor() == 1) {
			System.out.println("I dont want white");
			turnAround();
		} else {
			findObject();
			rideToObject();
		}

	}

	private static void detectColor() throws InterruptedException {
		// Red pillar:
		if (checkColor() == 2) {
			System.out.println("red found");
			// aggressive behaviour
			tune.playAggressiveTune();
			driveBackwardSlow();
			Thread.sleep(1000);
			driveForwardFast();
			Thread.sleep(1000);
			stop();
			pillarsFound++;
		}

		// Blue pillar:
		if (checkColor() == 3) {
			System.out.println("blue found");
			// romantic behaviour
			stop();
			tune.playHappyTune();
			pillarsFound++;
		}

	}

	private static void driveBackwardSlow() {
		motorA.setPower(-20);
		motorB.setPower(-20);

	}

	private static void driveForwardFast() {
		motorA.setPower(80);
		motorB.setPower(80);

	}

	private static void findObject() {
		System.out.println("Finding object");
		float initDist = dist;
		while (dist >= initDist) {
			dist = uss.getRange();
			turnLeft();
		}

	}

	private static void rideToObject() throws InterruptedException {
		float initDist = dist;
		while (dist > 0.15 && dist < 1) {
			dist = uss.getRange();

			if (dist <= initDist + 0.03) {
				initDist = dist;
				driveForward();
				System.out.println("Driving the right way");
			} else {
				System.out.println("Out of sight, find object");
				findObject();
			}

		}

		stop();
		while (noColorDetected()) {
			driveForwardSlow();
		}

		stop();
		System.out.println("Color detection");
		detectColor();
		Thread.sleep(4000);
		initDist = (float) inf;
		dist = uss.getRange();

	}

	private static void driveForwardSlow() {
		motorA.setPower(20);
		motorB.setPower(20);
	}

	private static boolean noColorDetected() {
		return checkColor() != 2 && checkColor() != 3;
	}

	private static void turnAround() {
		int count = 0;
		while (count < 400) {
			motorA.setPower(-30);
			motorB.setPower(30);
			count++;
		}

	}

	private static void turnLeft() {
		motorA.setPower(-30);
		motorB.setPower(30);

	}

	private static void stop() {
		motorA.setPower(0);
		motorB.setPower(0);

	}

	private static void driveForward() {
		motorA.setPower(50);
		motorB.setPower(50);

	}

	public static void setMotor(UnregulatedMotor motor, double power) {
		if (power > 0) {
			motor.forward();
			motor.setPower((int) power);
		} else {
			power = power * (-1);
			motor.backward();
			motor.setPower((int) power);
		}
	}

	public static int checkColor() {

		sensorValue = new float[3];
		for (int i = 0; i < 3; i++) {
			sensorValue[i] = sensor.rgbSample()[i];
		}

		if (sensorValue[0] > 0.01 && sensorValue[1] > 0.01 && sensorValue[2] > 0.01) {
			// white
			System.out.println("white detected");
			return 1;
		}
		if (sensorValue[0] > 0.01 && sensorValue[1] < 0.01 && sensorValue[2] < 0.01) {
			// red
			System.out.println("red detected");
			return 2;
		}
		if (sensorValue[0] < 0.01 && sensorValue[1] > 0.007 && sensorValue[2] < 0.01) {
			// blue
			System.out.println("blue detected");
			return 3;
		} else {
			// black
			System.out.println("black detected");
			return 4;
		}
	}

}

/*
 * UnregulatedMotor motorA = new UnregulatedMotor(MotorPort.A); UnregulatedMotor
 * motorB = new UnregulatedMotor(MotorPort.B); ColorSensor sensor = new
 * ColorSensor();
 * 
 * float sensorValue0; float sensorValue1; float sensorValue2;
 * 
 * System.out.println("Detect white "); Button.waitForAnyPress(); float white1 =
 * sensor.rgbSample()[0]; float white2 = sensor.rgbSample()[1]; float white3 =
 * sensor.rgbSample()[2];
 * 
 * System.out.println("Detect black "); Button.waitForAnyPress(); float black1 =
 * sensor.rgbSample()[0]; float black2 = sensor.rgbSample()[1]; float black3 =
 * sensor.rgbSample()[2];
 * 
 * System.out.println("Detect red"); Button.waitForAnyPress(); float red1 =
 * sensor.rgbSample()[0]; float red2 = sensor.rgbSample()[1]; float red3 =
 * sensor.rgbSample()[2];
 * 
 * System.out.println("Detect blue"); Button.waitForAnyPress(); float blue1 =
 * sensor.rgbSample()[0]; float blue2 = sensor.rgbSample()[1]; float blue3 =
 * sensor.rgbSample()[2];
 * 
 * System.out.println("GO EXPLORE, YOU LITTLE SHIT");
 * 
 * while (true) { sensorValue0 = sensor.rgbSample()[0]; sensorValue1 =
 * sensor.rgbSample()[1]; sensorValue2 = sensor.rgbSample()[2];
 * 
 * if ((sensorValue0 >= red1 - 0.0004 && sensorValue0 <= red1 + 0.0004) &&
 * (sensorValue1 >= red2 - 0.0004 && sensorValue1 <= red2 + 0.0004) &&
 * (sensorValue2 >= red3 - 0.0004 && sensorValue2 <= red3 + 0.0004)) {
 * System.out.println("red detected"); } if ((sensorValue0 >= blue1 - 0.0003 &&
 * sensorValue0 <= blue1 + 0.0003) && (sensorValue1 >= blue2 - 0.0003 &&
 * sensorValue1 <= blue2 + 0.0003) && (sensorValue2 >= blue3 - 0.0003 &&
 * sensorValue2 <= blue3 + 0.0003)) { System.out.println("blue detected"); } //
 * if ((sensorValue0 >= white1 - 0.0004 && sensorValue0 <= white1 + 0.0004) &&
 * (sensorValue1 >= white2 - 0.0004 && sensorValue1 <= white2 + 0.0004) &&
 * (sensorValue2 >= white3 - 0.0004 && sensorValue2 <= white3 + 0.0004)) {
 * System.out.println("white detected"); } if ((sensorValue0 >= black1 - 0.0003
 * && sensorValue0 <= black1 + 0.0003) && (sensorValue1 >= black2 - 0.0003 &&
 * sensorValue1 <= black2 + 0.0003) && (sensorValue2 >= black3 - 0.0003 &&
 * sensorValue2 <= black3 + 0.0003)) { System.out.println("black detected"); } }
 */
