package audioconf;

import org.gstreamer.Caps;
import org.gstreamer.Element;
import org.gstreamer.ElementFactory;
import org.gstreamer.Gst;
import org.gstreamer.Pipeline;
import org.gstreamer.State;

public class Client {

	public static void main(String[] args) {
		args = Gst.init("AudioPipe", args);

		Pipeline pipe = new Pipeline("AudioPipe");

		// Create elements.
		Element src = ElementFactory.make("udpsrc", "Source");
		src.set("port", Server.udp_port);

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

}