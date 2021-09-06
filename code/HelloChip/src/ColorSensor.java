
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

public class ColorSensor {
	EV3ColorSensor colorSensor;
	SampleProvider redProvider;
	SampleProvider rgbProvider;
	float[] redSample;
	float[] rgbSample;

	public ColorSensor() {
		colorSensor = new EV3ColorSensor(SensorPort.S1);
		redProvider = colorSensor.getRedMode();
		redSample = new float[redProvider.sampleSize()];
		rgbProvider = colorSensor.getRGBMode();
		rgbSample = new float[rgbProvider.sampleSize()];
	}

	public float[] redSample() {
		redProvider.fetchSample(redSample, 0);
		return redSample;
	}

	public float[] rgbSample() {
		rgbProvider.fetchSample(rgbSample, 0);
		return rgbSample;
	}
}