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
public class ServerEdited {
	
    public static String udp_ip = "localhost";
    public static String udp_port = "5001";
    
    public ServerEdited() {
    }
    public static void main(String[] args) {

        args = Gst.init("ServerEdited", args);
        
        /* create elements */
        Pipeline pipeline = new Pipeline("send_pipeline");
        
        Element source = ElementFactory.make("alsasrc", "alsasrc");
        Element audioconvert = ElementFactory.make("audioconvert","audioconvert");
        
        Element caps = ElementFactory.make("capsfilter", "caps");
        
        caps.setCaps(Caps.fromString("audio/x-raw-int,channels=1,depth=16,width=16,rate=44100"));
        
        Element rtpPay = ElementFactory.make("rtpL16pay","rtpL16pay");
        
        Element sink = ElementFactory.make("udpsink", "udpsink");
        sink.set("host",udp_ip );
		sink.set("port", udp_port);
        
        
        /* put together a pipeline */
        pipeline.addMany(source,audioconvert,caps,rtpPay,sink);
        Element.linkMany(source,audioconvert,caps,rtpPay,sink);
        
        
        /* start the pipeline */
        pipeline.play();
        Gst.main();
        pipeline.stop();
    }
}

