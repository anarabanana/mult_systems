import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
	public static String recordingsPath = "src/recordings/";
	private String recDummy = "devils_tears";
	private String filename;

	// static String timeStamp = new
	// SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());

	public Recorder(GUI gui) {
		// TODO Auto-generated constructor stub
		this.gui = gui;
	}

	public void record() {
		if (isRecord) {
			isRecord = false;
			
			String currenttime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
			filename = recordingsPath + currenttime + ".ogg";
			System.out.println(filename);
			
			String[] rec = new String[] { "alsasrc", "!", "audioconvert", "!", "audioresample", "!", "vorbisenc", "!", "oggmux", "!",
					"filesink location = " + filename };

			rec = Gst.init("AudioRecorder", rec);
			StringBuilder sb = new StringBuilder();

			for (String s : rec) {
				sb.append(' ');
				sb.append(s);
			}
			
			audiopipe = Pipeline.launch(sb.substring(1));
			audiopipe.play();
		} else {
			audiopipe.stop();
//			Gst.deinit();
//			Gst.quit();
//			audiopipe.setState(null);
			System.out.println(filename);
			gui.addNewFiletoList(filename);
			isRecord = true;
		}

	}

	public void play() {

		String[] args = new String[] { recordingsPath };
		strPlay = Gst.init("AudioPlayer", args);

		playbin = new PlayBin2("AudioPlayer");
		playbin.setVideoSink(ElementFactory.make("fakesink", "videosink"));
		System.out.println(gui.getSelectedPlayItem());
		playbin.setInputFile(new File(gui.getSelectedPlayItem()));

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
