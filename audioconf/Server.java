package audioconf;
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
	
	public static void main(String[] rec) 
{
	
	rec = new String[]{"alsasrc", "!", "audioconvert" , "!","audio/x-raw-int,channels=1,depth=16,width=16, " +
			"rate=44100","!","rtpL16pay","!", "udpsink host = localhost port = 5001"};
	
		

	rec = Gst.init("Server", rec); 
	
	StringBuilder sb = new StringBuilder();
		for (String s: rec) {
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