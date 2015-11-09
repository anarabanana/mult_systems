package audioconf;

import org.gstreamer.Element;
import org.gstreamer.ElementFactory;
import org.gstreamer.Gst;
import org.gstreamer.Pipeline;
import org.gstreamer.State;
import org.gstreamer.elements.PlayBin2;


	public class Client {
	
	static Pipeline audiopipe;
	
	static String MULTICAST_IP_ADDR = "224.1.1.1";
	static int AUDIO_UDP_PORT = 3000;
	
	public static void main(String[] args) 
{
		String [] rec = new String[]{"udpsrc port = 5001", "!", "application/x-rtp, media=(string)audio, " +
				"clock-rate=44100, width=16, height=16, encoding-name=(string)L16, encoding-params=(string)1," +
				" channels=(int)1, channel-position=(int)1, payload=(int)96" ,"!","gstrtpjitterbuffer do-lost=true", 
				"!" , "rtpL16depay", "!", "audioconvert", "!", "alsasink"};
		 
		
	rec = Gst.init("Client", rec); 
	StringBuilder sb = new StringBuilder();

	for (String s: rec) {
		 sb.append(' ');
		 sb.append(s);  
		 				}
	
		audiopipe = Pipeline.launch(sb.substring(1));	
		audiopipe.setState(State.PLAYING);
		Gst.main();
		audiopipe.setState(State.NULL);

}
}