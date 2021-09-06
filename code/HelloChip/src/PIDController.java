

import lejos.hardware.motor.UnregulatedMotor;

public class PIDController {
	public static final double K_P = 1500;
	public static final double K_D = 100;
	private float offset;
	public static int T_P = 42; // was 42
	double lastError = 0;
	double derivative = 0;
	int counter = 0;

	UnregulatedMotor motorA;
	UnregulatedMotor motorB;

	public PIDController(UnregulatedMotor motorA, UnregulatedMotor motorB, float offset) {
		this.motorA = motorA;
		this.motorB = motorB;
		this.offset = offset;

	}

	public void control(float sensorValue) {
		double error = sensorValue - offset;

		derivative = error - lastError;
		double turn = K_P * error + K_D * derivative;
		double powerA = T_P + turn;
		double powerB = T_P - turn;

		setMotor(motorA, powerA);
		setMotor(motorB, powerB);

		this.lastError = error;
	}

	public void setMotor(UnregulatedMotor motor, double power) {
		if (power > 0) {
			motor.forward();
			motor.setPower((int) power);
		} else {
			power = power * (-1);
			motor.backward();
			motor.setPower((int) power);
		}
	}

}
