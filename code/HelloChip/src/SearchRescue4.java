
import lejos.hardware.Button;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.robotics.Gyroscope;
import lejos.utility.Delay;

public class SearchRescue4 {
	public static float black;
	public static float white;
	public static float offset;
	public static UnregulatedMotor motorA;
	public static UnregulatedMotor motorB;
	public static ColorSensor sensor;
	public static Gripper gripper;
	public static Gyroscope gyro;
	public static PIDController control_PID;
	public static UltrasonicSensor uss;
	public static int pillarFound = 0;
	public static boolean notStop = true;
	public static boolean stop;
	public static int counter = 0;
	private static float[] sensorValue;
	private static float colorValue;
	private static boolean healthyRed;
	private static boolean hasFood;
	private static int nrOfFood ;

	public static void main(String[] args) {
		motorA = new UnregulatedMotor(MotorPort.A);
		motorB = new UnregulatedMotor(MotorPort.B);
		gripper = new Gripper(MotorPort.D);
		gyro = new GyroSensor(SensorPort.S2);
		sensor = new ColorSensor();
		offset = calibrate(sensor);
		control_PID = new PIDController(motorA, motorB, offset);
		uss = new UltrasonicSensor(SensorPort.S4);
		System.out.println("Go work, little shit");
		hasFood = false;
		stop = false ;
		nrOfFood = 0 ;
		Button.waitForAnyPress();
		islandDropOff();
		followLine();

	}

	private static void islandDropOff() {
		reachLine();
		turnRight(35);
		WalkForward();
	}

	private static void reachLine() {
		float sensorValue = 0;
		while (sensorValue <= offset) {
			motorA.setPower(50);
			motorB.setPower(50);
			sensorValue = sensor.redSample()[0];
		}
		stop();

	}

	private static void turnToPosition() {
		gyro.recalibrateOffset();
		turnRight(90);
	}

	private static void turnRight(int turnAngle) {
		gyro.reset();
		while (gyro.getAngle() <= turnAngle && gyro.getAngle() >= -turnAngle) {
			motorA.setPower(30);
			motorB.setPower(-30);
		}
	}

	private static void turnLeft(int turnAngle) {
		gyro.reset();
		while (gyro.getAngle() < turnAngle && gyro.getAngle() > -turnAngle) {
			motorA.setPower(-30);
			motorB.setPower(30);
		}
	}

	private static void followLine() {
		while (stop == false) {
			colorValue = sensor.redSample()[0];
			control_PID.control(colorValue);
			if (uss.getRange() < 0.30 && pillarFound == 0) {
				pillarFound++;
				findColour();
				System.out.println("1st pillar detected");
				if (healthyRed == true) {
					turnLeft(68);
					int count = 0;
					while (count <= 550) {
						WalkForward();
						count++;
					}
				}
				// else
				// turnRight(75);
			}
			if (hasFood == false) {
				if (uss.getRange() < 0.15 && pillarFound >= 1) {
					System.out.println(" food detected");
					hasFood = true;
					nrOfFood ++ ;
					foodGrapActivate();
					if (healthyRed == false) {
						turnLeft(190);
					}
					// else {
					// turnLeft(180);
					// }
				}
			}
			if (uss.getRange() < 0.30 && pillarFound >= 1 && hasFood == true) {
				dropFood();
				
				if (nrOfFood < 4 && hasFood == false) {
					turnLeft(190);
				}
			}
			
			if (nrOfFood == 4 && hasFood == false) {
				stop = true ;
			}

		}
	}

	private static void dropFood() {
		int count = 0;
		while (count <= 550) {
			WalkForward();
			count++;
		}
		stop();
		gripper.Grab();
		Delay.msDelay(2000);
		gripper.Close();
		islandDropOff ();
	}

	private static void findColour() {
		int count = 0;
		while (count < 12000) {
			WalkForward();
			count++;
		}
		if (count == 12000) {
			stop();
			detectColour();
			count--;
			System.out.println("detect colour achieved");
		}
		while (count < 12000 && count > 1000) {
			WalkBackward();
			count--;
		}
		count = 0;
	}

	private static void detectColour() {
		switch (checkColor()) {
		case 2:
			System.out.println("Red: the healthy food is on the left");
			healthyRed = true;
			break;

		case 3:
			System.out.println("Blue: the healthy food is on the right");
			healthyRed = false;

		default:
			System.out.println("You failed to find pillar");
			break;
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
		if (sensorValue[0] < 0.01 && sensorValue[1] > 0.005 && sensorValue[2] < 0.01) {
			// blue
			System.out.println("blue detected");
			return 3;
		} else {
			// black
			System.out.println("black detected");
			return 4;
		}
	}

	private static void foodGrapActivate() {
		while (uss.getRange() >= 0.12) {
			colorValue = sensor.redSample()[0];
			control_PID.control(colorValue);
		}
		if (uss.getRange() < 0.12) {
			System.out.println("grab is activated");
			stop();
			ActivateGripper();
		}

	}

	private static void ActivateGripper() {
		gripper.Grab();
		Delay.msDelay(2000);
		WalkForward();
		Delay.msDelay(2000);
		stop();
		gripper.Close();
		WalkBackward();
		Delay.msDelay(2000);

	}

	public static void WalkForward() {
		while (counter <= 1) {
			motorA.setPower(15);
			motorB.setPower(15);
			counter++;
		}
		counter = 0;

	}

	public static void WalkBackward() {
		while (counter <= 1) {
			motorA.setPower(-15);
			motorB.setPower(-15);
			counter++;
		}
		counter = 0;
	}

	// private static void turnBack() {
	// System.out.println("turn around");
	// float angle = gyro.getAngle();
	// while (gyro.getAngle() <=- angle + 180 && gyro.getAngle() >= angle) {
	// motorA.setPower(30);
	// motorB.setPower(-30);
	// }
	// }

	private static void stop() {
		motorA.setPower(0);
		motorB.setPower(0);
	}

	private static float calibrate(ColorSensor sensor) {
//		white = calWhite(sensor);
//		black = calBlack(sensor);
		white = calColor(sensor, "white");
		black = calColor(sensor, "black");
		float offset = (white + black) / 2;
		return offset;
	}

	private static float calColor(ColorSensor sensor, String colorName) {
		System.out.println("Detect " + colorName);
		Button.waitForAnyPress();
		float color = sensor.redSample()[0];
		System.out.println(color);
		return color;
	}
//		float offset = (white + black) / 2;
//		return offset;
	

//	private static float calWhite(ColorSensor sensor) {
//		System.out.println("Detect white");
//		Button.waitForAnyPress();
//		float white = sensor.redSample()[0];
//		System.out.println(white);
//		return white;
//	}
//
//	private static float calBlack(ColorSensor sensor) {
//		System.out.println("Detect black");
//		Button.waitForAnyPress();
//		float black = sensor.redSample()[0];
//		System.out.println(black);
//		return black;
//	}

}