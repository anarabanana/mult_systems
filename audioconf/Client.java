package audioconf;

import java.util.List;

import org.gstreamer.Caps;
import org.gstreamer.Element;
import org.gstreamer.ElementFactory;
import org.gstreamer.Gst;
import org.gstreamer.Pipeline;
import org.gstreamer.State;

public class Client {

	public static String clients = "localhost:5001,130.240.159.8:5001";
	public static int udp_port = 5001;
	private Client client;
	boolean multicast = true;

	public static void main(String[] args) {
		new Client(args);
	}
	
	public Client(String[] args) {
		client = this;
		new Thread() {
			public void run() {
				client.makeSendingPipe();
				client.makeMulticastSendingPipe();
			}
		}.start();
		new Thread() {
			public void run() {
				client.makeListeningPipe(multicast);
			}
		}.start();
	}

	private void makeListeningPipe(boolean multicast) {
		String[] args = {};
		Gst.init("AudioPipe", args);

		Pipeline pipe = new Pipeline("AudioPipe");

		// Create elements.
		Element src = ElementFactory.make("udpsrc", "Source");
		if (multicast) {
			src.set("multicast-group", "224.1.1.1");
			src.set("auto-multicast", true);
		}
		src.set("port", udp_port);

		String xrtp = "application/x-rtp, media=audio, clock-rate=44100, width=16, height=16, channel=1, channel-position=1, payload=96, encoding-name=L16, encoding-params=1";
		src.getSrcPads().get(0).setCaps(Caps.fromString(xrtp));

		Element buffer = ElementFactory.make("gstrtpjitterbuffer", "Buffer");
		buffer.set("do-lost", true);

		Element depay = ElementFactory.make("rtpL16depay", "Depay");

		Element convert = ElementFactory.make("audioconvert", "Convert");

		Element sink = ElementFactory.make("alsasink", "Destination");

		// Create pipeline.
		pipe.addMany(src, buffer, depay, convert, sink);
		src.link(buffer, depay, convert, sink);
		
		System.out.println("START ListeningPipe");
		// Start
		pipe.setState(State.PLAYING);
		Gst.main();
		pipe.setState(State.NULL);

		// String[] rec = new String[] {
		// "udpsrc port = 5002",
		// "!",
		// "application/x-rtp, media=(string)audio, "
		// +
		// "clock-rate=44100, width=16, height=16, encoding-name=(string)L16, encoding-params=(string)1,"
		// + " channels=(int)1, channel-position=(int)1, payload=(int)96", "!",
		// "gstrtpjitterbuffer do-lost=true", "!", "rtpL16depay",
		// "!", "audioconvert", "!", "alsasink" };

		// rec = Gst.init("Client", rec);
		//
		// StringBuilder sb = new StringBuilder();
		//
		// for (String s : rec) {
		// sb.append(' ');
		// sb.append(s);
		// }

		// audiopipe = Pipeline.launch(sb.substring(1));
		// pipe.setState(State.PLAYING);
		// Gst.main();
		// pipe.setState(State.NULL);
	}

	private void makeSendingPipe() {
		String[] args = {};
		Gst.init("Server", args);

		/* create elements */
		Pipeline pipeline = new Pipeline("send_pipeline");

		Element source = ElementFactory.make("alsasrc", "alsasrc");
		Element audioconvert = ElementFactory.make("audioconvert", "audioconvert");

		Element caps = ElementFactory.make("capsfilter", "caps");

		caps.setCaps(Caps.fromString("audio/x-raw-int,channels=1,depth=16,width=16,rate=44100"));

		Element rtpPay = ElementFactory.make("rtpL16pay", "rtpL16pay");

		Element sink = ElementFactory.make("multiudpsink", "udpsink");
		sink.set("clients", clients);
//		Element sink = ElementFactory.make("udpsink", "udpsink");
//		sink.set("host", "localhost");
//		sink.set("port", udp_port);

		/* put together a pipeline */
		pipeline.addMany(source, audioconvert, caps, rtpPay, sink);
		Element.linkMany(source, audioconvert, caps, rtpPay, sink);

		System.out.println("START SendingPipe");
		/* start the pipeline */
		pipeline.play();
		Gst.main();
		pipeline.stop();
	}

	private void makeMulticastSendingPipe() {
		String[] args = {};
		Gst.init("Server", args);

		/* create elements */
		Pipeline pipeline = new Pipeline("send_pipeline");

		Element source = ElementFactory.make("alsasrc", "alsasrc");
		Element audioconvert = ElementFactory.make("audioconvert", "audioconvert");

		Element caps = ElementFactory.make("capsfilter", "caps");

		caps.setCaps(Caps.fromString("audio/x-raw-int,channels=1,depth=16,width=16,rate=44100"));

		Element rtpPay = ElementFactory.make("rtpL16pay", "rtpL16pay");

//		Element sink = ElementFactory.make("multiudpsink", "multiudpsink");
//		sink.set("clients", clients);
		Element sink = ElementFactory.make("udpsink", "udpsink");
		sink.set("host", "224.1.1.1");
		sink.set("auto-multicast", true);
		sink.set("port", udp_port);

		/* put together a pipeline */
		pipeline.addMany(source, audioconvert, caps, rtpPay, sink);
		Element.linkMany(source, audioconvert, caps, rtpPay, sink);

		System.out.println("START SendingPipe");
		/* start the pipeline */
		pipeline.play();
		Gst.main();
		pipeline.stop();
	}

}