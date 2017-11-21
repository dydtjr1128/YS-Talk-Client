import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;

import javax.swing.*;
import javax.swing.border.LineBorder;

class LoginPanel extends JPanel {
	// login component
	private JLabel inputID = new JLabel("I  D : ");
	//private JTextField IDField = new JTextField(10);
	private JTextField IDField = new RoundJTextField(10);
	private JLabel inputPW = new JLabel("PW : ");
	private JPasswordField PWField = new RoundJPasswordField(10);
	//Imageicon
	private URL loginURL = getClass().getClassLoader().getResource("login.png");	
	private ImageIcon loginIcon = new ImageIcon(loginURL);
	private URL ysTalkURL = getClass().getClassLoader().getResource("ystalk.png");	
	private ImageIcon ysTalkIcon = new ImageIcon(ysTalkURL);
	private URL loginClickURL = getClass().getClassLoader().getResource("loginClick.png");	
	private ImageIcon loginClickIcon = new ImageIcon(loginClickURL);
	private URL joinURL = getClass().getClassLoader().getResource("join.png");	
	private ImageIcon joinIcon = new ImageIcon(joinURL);
	private URL joinClickURL = getClass().getClassLoader().getResource("joinClick.png");	
	private ImageIcon joinClickIcon = new ImageIcon(joinClickURL);
	// login component
	private JButton loginBtn = new JButton(loginIcon);	
	private JButton joinGroupBtn = new JButton(joinIcon);
	// join component
	private JLabel joinInputID = new JLabel("I  D : ");
	private JTextField joinIDField = new JTextField(10);
	private JLabel joinInputPW = new JLabel("PW : ");
	private JPasswordField joinPWField = new JPasswordField(10);
	private JButton joinBtn = new JButton("°¡ÀÔ");
	private JButton checkIDBtn = new JButton("ID Áßº¹Ã¼Å©");
	
	
	// socket
	User checkSocket = null;
	private JLabel socketConnectLabel = new JLabel();
	public LoginPanel(JFrame mainFrame) {
		setLayout(null);
		setBackground(Color.yellow);
		inputID.setFont(new Font("¸¼Àº°íµñ", Font.CENTER_BASELINE, 20));
		inputID.setBounds(30, 260, 50, 20);
		IDField.setFont(new Font("¸¼Àº°íµñ", Font.ITALIC, 20));
		IDField.setName("IDField");
		IDField.setBounds(80, 255, 150, 40);				
		inputPW.setFont(new Font("¸¼Àº°íµñ", Font.CENTER_BASELINE, 20));
		inputPW.setBounds(30, 310, 60, 20);
		PWField.setFont(new Font("¸¼Àº°íµñ", Font.ITALIC, 20));
		PWField.setBounds(80, 305, 150, 40);
		PWField.setName("PWField");
		System.out.println(loginIcon.getIconHeight() + "  " + loginIcon.getIconWidth());
		loginBtn.setBounds(250, 255, 100, 90);	
		joinGroupBtn.setBounds(250, 360, 100, 40);
		buttonInit(loginBtn,loginClickIcon);
		buttonInit(joinGroupBtn,joinClickIcon);
		add(inputID);
		add(IDField);
		add(inputPW);
		add(PWField);
		add(loginBtn);
		add(joinGroupBtn);
		IDField.addActionListener(new MyActionListener());
		PWField.addActionListener(new MyActionListener());
		loginBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(PWField.getText().equals("changeIP")){						
						checkSocket.SERVER_IP = IDField.getText();
						checkSocket.setup();						
						if(checkSocket.isConnected()){
							socketConnectLabel.setText("¿¬°á µÊ!");
						}
						else{
							socketConnectLabel.setText("¿¬°á ¾ÈµÊ");
						}
					}
					else if (checkSocket.check(IDField.getText(), PWField.getPassword())) {
						//removeAll();
						//ChattingPanel p = new ChattingPanel(checkSocket,mainFrame, IDField.getText());						
						//mainFrame.setContentPane(p);
						RoomTablePanel roomPanel = new RoomTablePanel(checkSocket, mainFrame);
						mainFrame.setContentPane(roomPanel);
						checkSocket.setName(IDField.getText());
						//p.getfocus();
						mainFrame.revalidate();
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				IDField.setText("");
				PWField.setText("");
			}
		});
		joinGroupBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JoinDialog joindialog = new JoinDialog();
				joindialog.setLocation(mainFrame.getX(), mainFrame.getY() + 200);
				joindialog.setVisible(true);
			}
		});
		checkSocket = new User();
		checkSocket.setup();
		System.out.println("¼Â¾÷");
		if(checkSocket.isConnected()){
			socketConnectLabel.setText("¿¬°á µÊ!");
		}
		else{
			socketConnectLabel.setText("¿¬°á ¾ÈµÊ");
		}
		socketConnectLabel.setBounds(300, 500, 100, 30);
		add(socketConnectLabel);
	}
	public void buttonInit(JButton btn,ImageIcon icon){
		btn.setBorder(BorderFactory.createEmptyBorder());
		btn.setBackground(null);
		btn.setContentAreaFilled(false);
		btn.setPressedIcon(icon);		
		btn.setBorderPainted(false);
	}
	public void paintComponent(Graphics g){
		//ImageIcon icon = new ImageIcon("±×¸²1.png");		
		super.paintComponent(g);
		g.setColor(Color.LIGHT_GRAY);
		g.setFont(new Font("¸¼Àº°íµñ", Font.PLAIN, 14));
		g.drawString("Copyright ¨Ï2017. YongSeok.", 100, 430);
		g.drawString("All Rights reserved", 100, 460);
		g.drawImage(ysTalkIcon.getImage(), 85, 20, 220, 220, this);
	}
	public static class RoundJTextField extends JTextField {
	    private Shape shape;
	    int round = 30;
	    public RoundJTextField(int size) {
	        super(size);
	        setOpaque(false); // As suggested by @AVD in comment.
	    }
	    protected void paintComponent(Graphics g) {
	         g.setColor(getBackground());
	         g.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, round, round);
	         super.paintComponent(g);
	    }
	    protected void paintBorder(Graphics g) {
	         g.setColor(getForeground());
	         g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, round, round);
	    }
	    public boolean contains(int x, int y) {
	         if (shape == null || !shape.getBounds().equals(getBounds())) {
	             shape = new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, round, round);
	         }
	         return shape.contains(x, y);
	    }
	}
	public class RoundJPasswordField extends JPasswordField {
	    private Shape shape;
	    int round = 30;
	    public RoundJPasswordField(int size) {
	        super(size);
	        setOpaque(false); // As suggested by @AVD in comment.
	    }
	    protected void paintComponent(Graphics g) {
	         g.setColor(getBackground());
	         g.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, round, round);
	         super.paintComponent(g);
	    }
	    protected void paintBorder(Graphics g) {
	         g.setColor(getForeground());
	         g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, round, round);
	    }
	    public boolean contains(int x, int y) {
	         if (shape == null || !shape.getBounds().equals(getBounds())) {
	             shape = new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, round, round);
	         }
	         return shape.contains(x, y);
	    }
	}
	class JoinDialog extends JDialog {
		boolean joinFlag = false;
		JPanel p = new JPanel(){
			public void paintComponent(Graphics g){
				super.paintComponent(g);
				setBackground(Color.yellow);
			}
		};
		public JoinDialog() {
			setContentPane(p);
			setSize(400, 180);
			p.setLayout(null);		
			joinInputID.setFont(new Font("¸¼Àº°íµñ", Font.CENTER_BASELINE, 20));
			joinInputID.setBounds(30, 20, 50, 20);
			joinIDField.setFont(new Font("¸¼Àº°íµñ", Font.ITALIC, 20));
			joinIDField.setName("joinIDField");
			joinIDField.setBounds(80, 15, 150, 40);
			joinInputPW.setFont(new Font("¸¼Àº°íµñ", Font.CENTER_BASELINE, 20));
			joinInputPW.setBounds(30, 70, 60, 20);
			joinPWField.setFont(new Font("¸¼Àº°íµñ", Font.ITALIC, 20));
			joinPWField.setBounds(80, 65, 150, 40);
			joinPWField.setName("joinPWField");
			joinBtn.setBounds(240, 65, 80, 40);
			checkIDBtn.setBounds(240, 15, 105, 40);
			checkIDBtn.setMargin(new Insets(5, 5, 5, 5));
			p.add(joinInputID);
			p.add(joinIDField);
			p.add(joinInputPW);
			p.add(joinPWField);
			p.add(joinBtn);
			p.add(checkIDBtn);
			joinBtn.setEnabled(false);
			joinIDField.addActionListener(new MyActionListener());
			joinIDField.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					if(e.getKeyCode() != KeyEvent.VK_ENTER){
						joinBtn.setEnabled(false);
					}
				}
			});
			checkIDBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String text = joinIDField.getText() + "MySecretNumber19941128" + joinPWField.getText() + "MySecretNumber19941128" + "check";
					checkSocket.write(text);
					String value = checkSocket.read();
					boolean val = Boolean.parseBoolean(value);
					if(val){
						joinBtn.setEnabled(true);
					}
				}
			});
			joinBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String text = joinIDField.getText() + "MySecretNumber19941128" + joinPWField.getText() + "MySecretNumber19941128" + "join";
					checkSocket.write(text);
					String value = checkSocket.read();
					boolean val = Boolean.parseBoolean(value);
					if(val){
						joinBtn.setEnabled(true);
						JOptionPane.showMessageDialog(null, "°¡ÀÔ¼º°ø!");
						setVisible(false);
					}
					else{
						JOptionPane.showMessageDialog(null, "°¡ÀÔ½ÇÆÐ!!","½ÇÆÐ",JOptionPane.ERROR_MESSAGE);
					}					
				}
			});
			setVisible(false);
		}
	}

	class MyActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JTextField tf = (JTextField) e.getSource();
			if (tf.getName().equals("IDField")) {
				PWField.requestFocus();
			}
			if (tf.getName().equals("PWField")) {
				loginBtn.doClick();
			}
			if (tf.getName().equals("joinIDField")) {
				checkIDBtn.doClick();
			}
		}
	}
}