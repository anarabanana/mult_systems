import java.io.File;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import org.gstreamer.ElementFactory;
import org.gstreamer.Gst;
import org.gstreamer.Pipeline;
import org.gstreamer.State;
import org.gstreamer.elements.PlayBin2;
import org.omg.CORBA.PUBLIC_MEMBER;

public class Recorder {

	private GUI gui;
	static Pipeline pipe;
	private boolean isRecord = true;
	private boolean isMute = true;
	private Pipeline audiopipe;
	private PlayBin2 playbin;
	private int volume = 50;
	private String[] strPlay;
	private String recPath = "test";
	private String recDummy = "devils_tears";

	public Recorder(GUI gui) {
		// TODO Auto-generated constructor stub
		this.gui = gui;
	}

	public void record() {

		String[] rec = new String[] { "alsasrc", "!", "audioconvert", "!", "audioresample", "!", "vorbisenc", "!", "oggmux", "!",
				"filesink location = " + recPath };

		rec = Gst.init("AudioPlayer", rec);
		StringBuilder sb = new StringBuilder();

		for (String s : rec) {
			sb.append(' ');
			sb.append(s);
		}

		if (isRecord) {
			audiopipe = Pipeline.launch(sb.substring(1));
			audiopipe.play();
			isRecord = false;
		} else {
			audiopipe.stop();
			isRecord = true;
		}

	}

	public void play() {

		strPlay = Gst.init("AudioPlayer", strPlay);

		playbin = new PlayBin2("AudioPlayer");
		playbin.setVideoSink(ElementFactory.make("fakesink", "videosink"));
		
		System.out.println(gui.getSelectedPlayItem().toString());
		playbin.setInputFile(new File(gui.getSelectedPlayItem().toString()));

		// playbin.setInputFile(new File(recDummy));

		playbin.play();

	}

	public void stop() {

		playbin.stop();
	}

	public void mute() {

		if (isMute) {
			playbin.setVolume(0);
			isMute = false;
		} else {
			playbin.setVolume(volume);
			isMute = true;
		}
	}

	public void volumeUp() {

		volume = volume + 10;
		playbin.setVolumePercent(volume);
	}

	public void volumeDown() {

		volume = volume - 10;
		playbin.setVolumePercent(volume);
	}

}
