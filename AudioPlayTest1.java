import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import org.gstreamer.ElementFactory;
import org.gstreamer.Gst;
import org.gstreamer.Pipeline;
import org.gstreamer.State;
import org.gstreamer.elements.PlayBin2;

/**
 * A basic, non-gui player
 */
public class AudioPlayTest1 extends GUI{
	
	private static  GUI gui;
	static Pipeline pipe;
	static boolean isRecord = true;
	static boolean isMute = true;
	static Pipeline audiopipe;
	static PlayBin2 playbin;
	static int volume = 50;
	private static DefaultListModel filelist = new DefaultListModel();
	static String [] strPlay;
	
	
    public static void main(String[] args) {
    	gui = new GUI();
        args = Gst.init("AudioPlayer", args);
        System.out.println(gui.getSelectedPlayItem().toString());
    }
    public static void record (){
    	
		String [] rec = new String[]{"alsasrc", "!", "audioconvert" , "!", "audioresample" , 
	           		"!", "vorbisenc", "!", "oggmux" , "!" , "filesink location = " +
	           				"/home/anar/workspace/Ex1/src/resources/test"};
			
		rec = Gst.init("AudioPlayer", rec); 
		StringBuilder sb = new StringBuilder();

			for (String s: rec) {
				 sb.append(' ');
				 sb.append(s);  
				 				}
			
			if(isRecord)
			{
				audiopipe = Pipeline.launch(sb.substring(1));	
				audiopipe.play();
				isRecord = false ;}
			else
			{
				audiopipe.stop();
				isRecord = true;
			}

    }
    public static void play (){
    	   		
    	strPlay = new String[]{"/home/anar/workspace/Ex1/src/resources/test", "/home/anar/Desktop/devils_tears"};
    	strPlay = Gst.init("AudioPlayer", strPlay);

        playbin = new PlayBin2("AudioPlayer");
        playbin.setVideoSink(ElementFactory.make("fakesink", "videosink"));
        		     
		playbin.setInputFile(new File(gui.getSelectedPlayItem()));
		
		
		Gst.main();
		
	
    }
    public static void stop (){
   
        playbin.stop();
    }
    public static void mute (){
    	  		
		if(isMute)
		{
		playbin.setVolume(0);
		isMute = false ;}
		else
		{
			playbin.setVolume(volume);
			isMute = true;
		}
    }
 
    public static void volumeUp (){
    	   
		volume = volume + 10;
		playbin.setVolumePercent(volume);
    }
    public static void volumeDown (){
    	   
		volume = volume-10;
		playbin.setVolumePercent(volume);
    }
    
}
