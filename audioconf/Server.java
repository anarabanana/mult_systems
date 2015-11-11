package audioconf;

import org.gstreamer.Caps;
import org.gstreamer.Element;
import org.gstreamer.ElementFactory;
import org.gstreamer.Gst;
import org.gstreamer.Pad;
import org.gstreamer.Pipeline;

/**
 *
 */
public class Server {

	public static String udp_ip = "localhost";
	public static int udp_port = 5001;

	public Server() {
	}

	public static void main(String[] args) {

		args = Gst.init("ServerEdited", args);

		/* create elements */
		Pipeline pipeline = new Pipeline("send_pipeline");

		Element source = ElementFactory.make("alsasrc", "alsasrc");
		Element audioconvert = ElementFactory.make("audioconvert", "audioconvert");

		Element caps = ElementFactory.make("capsfilter", "caps");

		caps.setCaps(Caps.fromString("audio/x-raw-int,channels=1,depth=16,width=16,rate=44100"));

		Element rtpPay = ElementFactory.make("rtpL16pay", "rtpL16pay");

		Element sink = ElementFactory.make("udpsink", "udpsink");
		sink.set("host", udp_ip);
		sink.set("port", udp_port);

		/* put together a pipeline */
		pipeline.addMany(source, audioconvert, caps, rtpPay, sink);
		Element.linkMany(source, audioconvert, caps, rtpPay, sink);

		/* start the pipeline */
		pipeline.play();
		Gst.main();
		pipeline.stop();
	}
}

/*package audioconf;

import org.gstreamer.Element;
import org.gstreamer.ElementFactory;
import org.gstreamer.Gst;
import org.gstreamer.Pipeline;
import org.gstreamer.State;

public class Server {

	public static final String LINUX_AUDIO_SRC = "pulsesrc";
	static Pipeline audiopipe;
	static String AUDIO_FILE = "/home/anar/Desktop/devils_tears";
	static String MULTICAST_IP_ADDR = "224.1.1.1";
	static int AUDIO_UDP_PORT = 3000;

	public static void main(String[] rec) {

		rec = new String[] { "alsasrc", "!", "audioconvert", "!", "audio/x-raw-int,channels=1,depth=16,width=16, " + "rate=44100", "!", "rtpL16pay",
				"!", "udpsink host = localhost port = 5002" };

		rec = Gst.init("Server", rec);

		StringBuilder sb = new StringBuilder();
		for (String s : rec) {
			sb.append(' ');
			sb.append(s);
		}

		audiopipe = Pipeline.launch(sb.substring(1));
		audiopipe.setState(State.PLAYING);
		System.out.println("Server is running");
		Gst.main();

		audiopipe.stop();

	}
}
*/