
import lejos.hardware.Button;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.utility.Delay;

public class Gripper {

	private static UnregulatedMotor motorA;
	private static UnregulatedMotor motorB;
	private static EV3MediumRegulatedMotor gripper;

	public Gripper(Port port) {
		gripper = new EV3MediumRegulatedMotor(port);
	}

	// 560 first
	public void Grab() {
		gripper.rotate(780);
	}

	public void Close() {
		gripper.rotate(780 * -1);
	}

}