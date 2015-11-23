package audioconf;

import java.util.List;

import org.gstreamer.Caps;
import org.gstreamer.Element;
import org.gstreamer.ElementFactory;
import org.gstreamer.Gst;
import org.gstreamer.Pipeline;
import org.gstreamer.State;

public class Client {

	public static String clients = "";
	public static int udp_port = 5001;
	
	private Client client;
	
	boolean multicast = false;
	
	Pipeline listeningPipe;
	Pipeline sendingPipe;

	public static void main(String[] args) {
		new Client();
	}
	
	public Client() {
		client = this;
	}
	
	public void start() {
		startListeningPipe();
		startSendingPipe();
	}
	
	public void stop() {
		stopSendingPipe();
		stopListeningPipe();
	}
	
	public void stopSendingPipe() {
		sendingPipe.stop();
	}
	
	public void stopListeningPipe() {
		listeningPipe.stop();
	}

	public void startSendingPipe() {
		new Thread() {
			public void run() {
				client.makeSendingPipe();
			}
		}.start();	
	}
	
	public void startListeningPipe() {
		new Thread() {
			public void run() {
				client.makeListeningPipe();
			}
		}.start();	
	}
	
	public void restartSendingPipe() {
		sendingPipe.stop();
		startSendingPipe();
	}
	
	public void restartListeningPipe() {
		listeningPipe.stop();
		startListeningPipe();
	}
	
	private void makeListeningPipe() {
		String[] args = {};
		Gst.init("AudioPipe", args);

		listeningPipe = new Pipeline("AudioPipe");

		// Create elements.
		Element src = ElementFactory.make("udpsrc", "Source");
		if (multicast) {
			System.out.println("multicast!");
			src.set("multicast-group", clients);
			src.set("auto-multicast", true);
			src.set("multicast-iface", "eth0");
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
		listeningPipe.addMany(src, buffer, depay, convert, sink);
		src.link(buffer, depay, convert, sink);
		
		System.out.println("START ListeningPipe");
		// Start
		listeningPipe.setState(State.PLAYING);
		Gst.main();
		listeningPipe.setState(State.NULL);

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
		sendingPipe = new Pipeline("send_pipeline");

		Element source = ElementFactory.make("alsasrc", "alsasrc");
		Element audioconvert = ElementFactory.make("audioconvert", "audioconvert");

		Element caps = ElementFactory.make("capsfilter", "caps");

		caps.setCaps(Caps.fromString("audio/x-raw-int,channels=1,depth=16,width=16,rate=44100"));

		Element rtpPay = ElementFactory.make("rtpL16pay", "rtpL16pay");
		
		Element sink;
		if (multicast) {
			sink = ElementFactory.make("udpsink", "udpsink");
//			String[] arg = clients.split(",");
			sink.set("host", clients);
			sink.set("port", udp_port);
			sink.set("auto-multicast", true);
		} else {
			sink = ElementFactory.make("multiudpsink", "udpsink");
			sink.set("clients", clients);
		}
			
//		Element sink = ElementFactory.make("udpsink", "udpsink");
//		sink.set("host", "localhost");
//		sink.set("port", udp_port);

		/* put together a pipeline */
		sendingPipe.addMany(source, audioconvert, caps, rtpPay, sink);
		Element.linkMany(source, audioconvert, caps, rtpPay, sink);

		System.out.println("START SendingPipe");
		/* start the pipeline */
		sendingPipe.play();
		Gst.main();
//		sendingPipe.stop();
	}
	
	public void setClients(String clts) {
		System.out.println(clts);
		clients = clts;
	}
	
	public void setPort(int port) {
		udp_port = port;
	}
	
	public void setMulticast(boolean mult) {
		multicast = mult;
	}

}