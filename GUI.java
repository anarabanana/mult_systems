import java.awt.EventQueue;
import javax.swing.JFrame;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.Font;
import java.io.File;
import java.io.FileFilter;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.UIManager;


public class GUI extends JFrame {

	private JFrame frmAudio;
	static String iconsPath = "src/resources/";
	DefaultListModel filelist = new DefaultListModel<>();
	//final JList list = new JList(filelist);
	FileFilter ff =new FileFilter() {
		
		@Override
		public boolean accept(File pathname) {
			// TODO Auto-generated method stub
			 String name = pathname.getName().toLowerCase();
		        return name.endsWith(".ogg") || name.endsWith(".mp3");
			
		}
	};
	final JList list = new JList(new File("/home/ashraf/workspace/multi2/src/resources/recordings").listFiles(ff));
	private Recorder audio;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
	
		EventQueue.invokeLater(new Runnable() {
			
			public void run() {
				try {
					GUI window = new GUI();
					window.frmAudio.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
		audio = new Recorder(this);
		
		filelist.addElement("/home/ashraf/workspace/multi2/src/resources");
		filelist.addElement("/home/anar/Desktop/devils_tears");
		}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAudio = new JFrame();
		frmAudio.setAlwaysOnTop(true);
		frmAudio.getContentPane().setFont(new Font("Dialog", Font.PLAIN, 12));
		frmAudio.getContentPane().setForeground(SystemColor.controlHighlight);
		frmAudio.getContentPane().setBackground(Color.WHITE);
		frmAudio.setTitle("Audio recorder");
		frmAudio.setIconImage(Toolkit.getDefaultToolkit().getImage(iconsPath +"MicrophoneHot.png"));
		frmAudio.setBounds(100, 100, 450, 323);
		frmAudio.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAudio.getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JButton record_btn = new JButton("");
		record_btn.setHorizontalAlignment(SwingConstants.LEFT);
		record_btn.setIcon(new ImageIcon(iconsPath + "RecordPressed (1).png"));
		
		record_btn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				
			audio.record();
		
				}
		});
		frmAudio.getContentPane().add(record_btn, "4, 4");
		
		JButton volume_up_btn = new JButton("");
		volume_up_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				audio.volumeUp();
			}
		});
		
		JButton stop_btn = new JButton("");
		stop_btn.setIcon(new ImageIcon(iconsPath + "Stop1Normal (1).png"));
		stop_btn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
				
				audio.stop();
				}
		});
		frmAudio.getContentPane().add(stop_btn, "6, 4");
		
		JButton play_btn = new JButton("");
		play_btn.setIcon(new ImageIcon(iconsPath+"Play1Pressed (1).png"));
		
		play_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				audio.play();
			}
		});
		frmAudio.getContentPane().add(play_btn, "8, 4, left, default");
		volume_up_btn.setIcon(new ImageIcon(iconsPath+"VolumeNormalRed.png"));
		frmAudio.getContentPane().add(volume_up_btn, "4, 10");
		
		JButton volume_down_btn = new JButton("");
		volume_down_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {		
				
				audio.volumeDown();
			}
			
		});
		volume_down_btn.setIcon(new ImageIcon(iconsPath+ "VolumeDisabled.png"));
		frmAudio.getContentPane().add(volume_down_btn, "6, 10");
		
		JButton mute_btn = new JButton("");
		
		mute_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				audio.mute();		
			}
		});

		mute_btn.setIcon(new ImageIcon(iconsPath + "mute-2.png"));
		frmAudio.getContentPane().add(mute_btn, "8, 10");

		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if (arg0.getValueIsAdjusting() == false) {
					
				System.out.println(list.getSelectedValue());
				}
				}
		});
		list.setBorder(UIManager.getBorder("List.focusCellHighlightBorder"));
		list.setBackground(Color.WHITE);
		frmAudio.getContentPane().add(list, "4, 16, 10, 4, default, fill");
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setAutoscrolls(true);
		//list.setBounds(MAXIMIZED_HORIZ, MAXIMIZED_VERT, MAXIMIZED_BOTH, MAXIMIZED_BOTH);
		list.setVisibleRowCount(-1);

	}


	public String getSelectedPlayItem() {
	
		return (String) list.getSelectedValue();
		
	}
	public void refreshFileList()
	{
		
	}

}