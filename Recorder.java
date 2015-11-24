import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.gstreamer.ElementFactory;
import org.gstreamer.Gst;
import org.gstreamer.Pipeline;
import org.gstreamer.elements.PlayBin2;

/**
 * The audio recorder. Recording and playing is realized with gstreamer.
 * 
 */
public class Recorder {

	/**
	 * The attached GUI element.
	 */
	private GUI gui;

	/**
	 * The audio pipe for recording.
	 */
	private Pipeline audiopipe;

	/**
	 * Playbin element for playing.
	 */
	private PlayBin2 playbin;

	/**
	 * Flag if recording is in progress.
	 */
	private boolean isRecord = true;

	/**
	 * Flag if player is muted.
	 */
	private boolean isMute = true;

	/**
	 * Value for the volume.
	 */
	private int volume = 50;

	/**
	 * Path were the recorded file is stored.
	 */
	public static String recordingsPath = "src/recordings/";

	/**
	 * Stores the name of the file for the recording.
	 */
	private String filename;

	/**
	 * Constructor.
	 * 
	 * @param gui
	 */
	public Recorder(GUI gui) {
		// TODO Auto-generated constructor stub
		this.gui = gui;
	}

	/**
	 * The record function. For starting and stopping the recording the same
	 * function is used, execution depends on the global flag isRecord.
	 */
	public void record() {
		if (isRecord) {
			isRecord = false;

			// Generates a unique filename.
			String currenttime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
			filename = recordingsPath + currenttime + ".ogg";

			// The pipe.
			String[] rec = new String[] { "alsasrc", "!", "audioconvert", "!", "audioresample", "!", "vorbisenc", "!", "oggmux", "!",
					"filesink location = " + filename };

			rec = Gst.init("AudioRecorder", rec);
			StringBuilder sb = new StringBuilder();

			for (String s : rec) {
				sb.append(' ');
				sb.append(s);
			}

			// Start recording
			audiopipe = Pipeline.launch(sb.substring(1));
			audiopipe.play();

		} else {
			// Stop recording and add file to the list.
			audiopipe.stop();
			gui.addNewFiletoList(filename);
			isRecord = true;
		}

	}

	/**
	 * Starts the audio player. Opens the selected file in the GUI list and
	 * plays it with playbin.
	 */
	public void play() {

		String[] args = new String[] { recordingsPath };
		Gst.init("AudioPlayer", args);

		playbin = new PlayBin2("AudioPlayer");
		playbin.setVideoSink(ElementFactory.make("fakesink", "videosink"));
		playbin.setInputFile(new File(gui.getSelectedPlayItem()));

		playbin.play();

	}

	/**
	 * Stops the audio player.
	 */
	public void stop() {
		playbin.stop();
	}

	/**
	 * Mutes the audio player.
	 */
	public void mute() {
		if (isMute) {
			playbin.setVolume(0);
			isMute = false;
		} else {
			playbin.setVolume(volume);
			isMute = true;
		}
	}

	/**
	 * Increases the volume of the audio player.
	 */
	public void volumeUp() {
		volume = volume + 10;
		playbin.setVolumePercent(volume);
	}

	/**
	 * Decreases the volume of the audio player.
	 */
	public void volumeDown() {
		volume = volume - 10;
		playbin.setVolumePercent(volume);
	}

}
