
import lejos.hardware.Button;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;

public class HelloChip {

	public static void main(String[] args) {
		UnregulatedMotor motorA = new UnregulatedMotor(MotorPort.A);
		UnregulatedMotor motorB = new UnregulatedMotor(MotorPort.B);
		ColorSensor sensor = new ColorSensor();
		float sensorValue;
		float offset = calibrate(sensor);
		PIDController control_PID = new PIDController(motorA, motorB, offset);

		while (true) {

			sensorValue = sensor.redSample()[0];
			control_PID.control(sensorValue);

		}

	}

	private static float calibrate(ColorSensor sensor) {
		System.out.println("Detect black");
		Button.waitForAnyPress();
		float black = sensor.redSample()[0];
		System.out.println(black);
		System.out.println("Detect white");
		Button.waitForAnyPress();
		float white = sensor.redSample()[0];
		System.out.println(white);
		float offset = (white + black) / 2;
		return offset;
	}

}
