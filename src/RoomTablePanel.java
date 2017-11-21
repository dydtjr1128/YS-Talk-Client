import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.net.Socket;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class RoomTablePanel extends JPanel {
	private User roomSocket;
	private DefaultTableModel roomModel;
	private JTable roomTable;
	private JLabel roomCountLabel;
	private JButton makeRoomBtn;
	private JButton refreshRoomBtn;
	private JScrollPane roomScrollPane;
	private JFrame mainFrame;
	private int selectRoomNumber = -1;
	private String header[] = { "No.", "Room Name", "people" };
	//ImageIcon
	private URL exitURL = getClass().getClassLoader().getResource("exit.png");	
	private ImageIcon exitIcon = new ImageIcon(exitURL);
	private URL makeRoomURL = getClass().getClassLoader().getResource("makeRoom.png");	
	private ImageIcon makeRoomIcon = new ImageIcon(makeRoomURL);
	private URL makeRoomClickURL = getClass().getClassLoader().getResource("makeRoomClick.png");	
	private ImageIcon makeRoomClickIcon = new ImageIcon(makeRoomClickURL);
	private URL refreshURL = getClass().getClassLoader().getResource("refresh.png");	
	private ImageIcon refreshIcon = new ImageIcon(refreshURL);
	private URL refreshClickURL = getClass().getClassLoader().getResource("refreshClick.png");	
	private ImageIcon refreshClickIcon = new ImageIcon(refreshClickURL);
	//
	private int myX=0,myY=0;
	public RoomTablePanel(User roomSocket, JFrame mainFrame) {
		this.roomSocket = roomSocket;
		this.mainFrame = mainFrame;		
		setLayout(new BorderLayout());

	
		
		roomModel = new DefaultTableModel(null, header) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			};
		};

		roomTable = new JTable(roomModel);
		roomTable.setBackground(Color.yellow);
		roomScrollPane = new JScrollPane(roomTable);
		roomScrollPane.getViewport().setBackground(Color.YELLOW);

		roomTable.getTableHeader().setResizingAllowed(false);
		roomTable.getTableHeader().setReorderingAllowed(false);
		roomTable.getTableHeader().setBackground(new Color(255, 228, 205));
		roomTable.getTableHeader().setForeground(Color.blue);

		roomTable.getColumnModel().getColumn(0).setMaxWidth(100);
		roomTable.getColumnModel().getColumn(0).setPreferredWidth(40);
		roomTable.getColumnModel().getColumn(2).setMaxWidth(100);
		roomTable.getColumnModel().getColumn(2).setPreferredWidth(70);

		roomTable.setAutoCreateRowSorter(true);

		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer(); // 디폴트테이블셀렌더러를
																		// 생성
		dtcr.setHorizontalAlignment(SwingConstants.CENTER); // 렌더러의 가로정렬을
															// CENTER로
		TableColumnModel tcm = roomTable.getColumnModel(); // 정렬할 테이블의 컬럼모델을 가져옴
		for (int i = 0; i < tcm.getColumnCount(); i++) {
			tcm.getColumn(i).setCellRenderer(dtcr); // 컬럼모델에서 컬럼의 갯수만큼 컬럼을 가져와
													// for문을 이용하여
			// 각각의 셀렌더러를 아까 생성한 dtcr에 set해줌
		}

		roomTable.setRowHeight(40);
		ListSelectionModel cellSelectionModel = roomTable.getSelectionModel();
		cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		cellSelectionModel.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				int selectRow = roomTable.getSelectedRow();
				if (selectRow >= 0) {
					//System.out.print("Selected: " + roomTable.getSelectedRow() + "    ");
					selectRoomNumber = Integer.parseInt(roomTable.getValueAt(roomTable.getSelectedRow(), 0).toString());
					//System.out.println(selectRoomNumber);
				}
			}
		});
		roomTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==2){
					if(roomSocket.checkEnterRoom(selectRoomNumber)){
						ChattingPanel p = new ChattingPanel(roomSocket,mainFrame, roomSocket.getName());						
						mainFrame.setContentPane(p);						
						//p.getfocus();
						mainFrame.revalidate();					
					}
					//System.out.println(selectRoomNumber+"눌렀어!!");
				}
			}
		});

		roomScrollPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
					myX = e.getX();
					myY = e.getY();
			}		
		});
		roomScrollPane.addMouseMotionListener(new MouseAdapter() {	
			@Override
			public void mouseDragged(MouseEvent e) {
				
				mainFrame.setLocation(e.getXOnScreen()-myX, e.getYOnScreen()-myY);
			}
		});	
		
		add(roomScrollPane, BorderLayout.CENTER);
		add(new SouthPanel(), BorderLayout.SOUTH);
		refreshRoom();

	}
	public void refreshRoom(){
		String data[][] = roomSocket.refreshRoom();		
		roomCountLabel.setText("Exist Room : " + Integer.toString(roomSocket.getRoomCount()));
		roomModel = new DefaultTableModel(data, header) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			};
		};
        roomTable.setModel(roomModel);        

		roomTable.getTableHeader().setResizingAllowed(false);
		roomTable.getTableHeader().setReorderingAllowed(false);
		roomTable.getTableHeader().setBackground(new Color(255, 228, 205));
		roomTable.getTableHeader().setForeground(Color.blue);

		roomTable.getColumnModel().getColumn(0).setMaxWidth(100);
		roomTable.getColumnModel().getColumn(0).setPreferredWidth(40);
		roomTable.getColumnModel().getColumn(2).setMaxWidth(100);
		roomTable.getColumnModel().getColumn(2).setPreferredWidth(70);

		roomTable.setAutoCreateRowSorter(true);

		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer(); // 디폴트테이블셀렌더러를
																		// 생성
		dtcr.setHorizontalAlignment(SwingConstants.CENTER); // 렌더러의 가로정렬을
															// CENTER로
		TableColumnModel tcm = roomTable.getColumnModel(); // 정렬할 테이블의 컬럼모델을 가져옴
		for (int i = 0; i < tcm.getColumnCount(); i++) {
			tcm.getColumn(i).setCellRenderer(dtcr); // 컬럼모델에서 컬럼의 갯수만큼 컬럼을 가져와
													// for문을 이용하여
			// 각각의 셀렌더러를 아까 생성한 dtcr에 set해줌
		}
	}
	public void buttonInit(JButton btn,ImageIcon icon){
		btn.setBorder(BorderFactory.createEmptyBorder());
		btn.setBackground(null);
		btn.setContentAreaFilled(false);
		btn.setPressedIcon(icon);		
		btn.setBorderPainted(false);
	}
	public class SouthPanel extends JPanel {
		private MyDialg roomOptionDialog;

		public SouthPanel() 
		{
			setPreferredSize(new Dimension(400, 50));
			setLayout(null);
			setBackground(Color.gray);

			roomOptionDialog = new MyDialg();

			roomCountLabel = new JLabel("Exist Room : 0", JLabel.CENTER);
			makeRoomBtn = new JButton(makeRoomIcon);
			refreshRoomBtn = new JButton(refreshIcon);
			buttonInit(makeRoomBtn, makeRoomClickIcon);
			buttonInit(refreshRoomBtn, refreshClickIcon);
			

			//roomCountLabel.setPreferredSize(new Dimension(150, 20));
			roomCountLabel.setBounds(0, 0, 150, 55);
			makeRoomBtn.setBounds(150, 0, 150, 55);
			refreshRoomBtn.setBounds(310, 0, 80, 55);
			add(roomCountLabel);
			add(makeRoomBtn);
			add(refreshRoomBtn);
			/*add(roomCountLabel, BorderLayout.WEST);
			add(makeRoomBtn, BorderLayout.CENTER);
			add(refreshRoomBtn, BorderLayout.EAST);*/

			makeRoomBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//roomOptionDialog.setLocation(mainFrame.getLocationOnScreen().x + 50, mainFrame.getLocationOnScreen().y + 200);
					roomOptionDialog.setLocation(mainFrame.getX() + 50, mainFrame.getY() + 200);
					roomOptionDialog.setVisible(true);
					System.out.println(mainFrame.getX() + "  " + mainFrame.getLocationOnScreen().x);										
					

				}
			});
			refreshRoomBtn.addActionListener(new ActionListener() {				
				@Override
				public void actionPerformed(ActionEvent e) {
					refreshRoom();					
				}
			});
		}
	}

	class MyDialg extends JDialog {
		private JButton makeRoomOKBtn;
		private JLabel roomNameLabel = new JLabel("Room Name      : ");
		private JTextField roomNameField;
		private JLabel roomPasswordLabel = new JLabel("Room Password : ");
		private JTextField roomPasswordField;
		private JMenuBar jbar;
		private final int height = 200;
		private final int width = 280;
		private int myX = 0, myY = 0;
		
		public MyDialg() {
			super(mainFrame, "방만들기", true);
			setLayout(null);
			jbar = new JMenuBar();
			// Shape shape = new RoundRectangle2D.Float(0, 0, width+25, height,
			// 20, 20);
			JPanel p = (JPanel) this.getContentPane();
			getRootPane().setBorder(new LineBorder(Color.DARK_GRAY, 2, true));
			// getRootPane().setBorder(new EtchedBorder(EtchedBorder.RAISED,
			// Color.green, Color.red));

			/*
			 * jbar.setBorder(BorderFactory.createLineBorder(Color.black));
			 * p.setBorder(BorderFactory.createLineBorder(Color.black));
			 */
			// p.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 3,
			// true));
			// p.setBorder(new LineBorder(Color.DARK_GRAY, 3, true));
			makeJMenu();
			this.setUndecorated(true);
			// this.setShape(shape);

			makeRoomOKBtn = new JButton("만들기");
			roomNameField = new JTextField(10);
			roomPasswordField = new JTextField(10);

			roomNameLabel.setBounds(20, 30, 165, 30);
			roomNameField.setBounds(175, 30, 100, 30);
			roomPasswordLabel.setBounds(20, 70, 165, 30);
			roomPasswordField.setBounds(175, 70, 100, 30);
			makeRoomOKBtn.setBounds(180, 110, 70, 35);
			makeRoomOKBtn.setMargin(new Insets(5, 5, 5, 5));

			add(roomNameLabel);
			add(roomNameField);
			add(roomPasswordLabel);
			add(roomPasswordField);
			add(makeRoomOKBtn);

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
					setLocation(e.getXOnScreen() - myX, e.getYOnScreen() - myY);
				}
			});
			makeRoomOKBtn.addActionListener(new ActionListener() {				
				@Override
				public void actionPerformed(ActionEvent arg0) {				
					if(roomSocket.makeRoom(roomNameField.getText(),roomPasswordField.getText())){
						setVisible(false);
					}
					else{
						
					}
				}
			});
			setSize(300, 200);
			
		}

		public void makeJMenu() {

			// jbar.setSize(400, 50);
			jbar.setPreferredSize(new Dimension(width, 50));
			jbar.setBackground(Color.yellow);
			jbar.setBorderPainted(false);
			jbar.setLayout(null);

			//JLabel exitlabel = new JLabel(new ImageIcon("exit.png"));
			JLabel exitlabel = new JLabel(exitIcon);

			// JLabel minilabel = new JLabel(new ImageIcon("minimize.png"));
			jbar.add(exitlabel);
			// jbar.add(minilabel);
			exitlabel.setBounds(width - 25, 10, 35, 35);
			// minilabel.setBounds(305, 15, 35, 35);

			exitlabel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getButton() == 1) {
						setVisible(false);
					}
				}
			});
			setJMenuBar(jbar);
		}
	}
}