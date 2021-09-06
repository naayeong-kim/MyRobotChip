
import lejos.hardware.Sound;
import lejos.utility.Delay;

public class Tune {
	public final static int[] PIANO = new int[] { 4, 25, 500, 7000, 5 };

	public void playHappyTune() {
		Sound.setVolume(50);
		Sound.playNote(PIANO, 466, 900); // bes
		Sound.playNote(PIANO, 440, 900); // a
		Sound.playNote(PIANO, 466, 600); // bes
		Sound.playNote(PIANO, 392, 300); // g
		Sound.playNote(PIANO, 440, 300); // a
		Sound.playNote(PIANO, 466, 300); // bes
		Sound.playNote(PIANO, 392, 300); // g
		Sound.playNote(PIANO, 440, 600); // a
		Sound.playNote(PIANO, 523, 300); // c
		Sound.playNote(PIANO, 587, 600); // d
		Sound.playNote(PIANO, 494, 300); // b
		Sound.playNote(PIANO, 523, 1500); // c

	}

	public void playAggressiveTune() {
		Sound.setVolume(50);
		Sound.playNote(PIANO, 330, 300); // e
		Sound.playNote(PIANO, 330, 300);
		Sound.playNote(PIANO, 330, 300);

		Sound.playNote(PIANO, 262, 900);

		Delay.msDelay(300);

		Sound.playNote(PIANO, 294, 300);
		Sound.playNote(PIANO, 294, 300);
		Sound.playNote(PIANO, 294, 300);

		Sound.playNote(PIANO, 247, 900);

	}

}
