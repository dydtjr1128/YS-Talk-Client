import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Shape;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;

import javax.swing.*;
public class MyClient extends JFrame{
	private int myX=0,myY=0;
	private JMenuBar jbar = new JMenuBar();
	private JFrame f = this;
	private final int height = 650;
	private final int weight = 400;
	
	private URL exitURL = getClass().getClassLoader().getResource("exit.png");	
	private ImageIcon exitIcon = new ImageIcon(exitURL);
	private URL minimizeURL = getClass().getClassLoader().getResource("minimize.png");	
	private ImageIcon minimizeIcon = new ImageIcon(minimizeURL);
	private URL trayURL = getClass().getClassLoader().getResource("tray.png");	
	private ImageIcon trayIcon = new ImageIcon(trayURL);
	//그림4.png
	private MyClient() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Shape shape = new RoundRectangle2D.Float(0, 0, weight, height, 50, 50);		
		makeJMenu();
		
		setUndecorated(true);
		setShape(shape);	
		setLocation(300, 300);
		setAutoRequestFocus(true);

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
					myX = e.getX();
					myY = e.getY();
			}		
		});
		addMouseMotionListener(new MouseAdapter() {	
			@Override
			public void mouseDragged(MouseEvent e) {
				setLocation(e.getXOnScreen()-myX, e.getYOnScreen()-myY);
			}
		});	
		MenuItem exititem = new MenuItem("exit");
		PopupMenu menu = new PopupMenu("My Menu");
		menu.add(exititem);
		exititem.addActionListener(new ActionListener() {					
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(1);						
			}
		});
		TrayIcon myTray = new TrayIcon(Toolkit.getDefaultToolkit().getImage(trayURL),"chat",menu);
		SystemTray tray = SystemTray.getSystemTray();
		try {
			tray.add(myTray);
		} catch (AWTException e1) {
			System.out.println(e1.getMessage());
		}
		myTray.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==2)
					f.setState(JFrame.NORMAL);
			}
		});
		setUIFont (new javax.swing.plaf.FontUIResource("휴먼매직체", Font.PLAIN, 18));
		setTitle("클라이언트");
		setSize(weight, height);	
		setContentPane(new LoginPanel(this));		
		setVisible(true);		
	}
	public static void setUIFont(javax.swing.plaf.FontUIResource f) {
	    java.util.Enumeration keys = UIManager.getDefaults().keys();
	    while (keys.hasMoreElements()) {
	        Object key = keys.nextElement();
	        Object value = UIManager.get(key);
	        if (value instanceof javax.swing.plaf.FontUIResource)
	            UIManager.put(key, f);
	    }
	}
	public void makeJMenu(){
		
		//jbar.setSize(400, 50);
		jbar.setPreferredSize(new Dimension(400, 50));
		jbar.setBackground(Color.yellow);
		jbar.setBorderPainted(false);
		jbar.setLayout(null);
		
		JLabel exitlabel = new JLabel(exitIcon);
		
		JLabel minilabel = new JLabel(minimizeIcon);
		jbar.add(exitlabel);
		jbar.add(minilabel);
		exitlabel.setBounds(350, 15, 35, 35);
		minilabel.setBounds(305, 15, 35, 35);
		
		exitlabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton()==1){
					System.exit(1);
				}
			}
		});
		minilabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton()==1){
					f.setState(JFrame.ICONIFIED);
				}					
			}
		});
		setJMenuBar(jbar);
	}
	public static void run(){
		new MyClient();
	}
}
