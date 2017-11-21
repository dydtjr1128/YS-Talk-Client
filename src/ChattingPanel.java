import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.im.InputContext;
import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.JPopupMenu.Separator;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import org.w3c.dom.css.RGBColor;

class ChattingPanel extends JPanel {
	private MyTextArea showArea = new MyTextArea();
	private JTextField textfield = new LoginPanel.RoundJTextField(10);
	private String name = null;
	private User user;
	
	private URL attachURL = getClass().getClassLoader().getResource("attach.png");	
	private ImageIcon attachIcon = new ImageIcon(attachURL);
	private URL attachClickURL = getClass().getClassLoader().getResource("attachClick.png");	
	private ImageIcon attachClickIcon = new ImageIcon(attachClickURL);
	private JButton attachBtn = new JButton(attachIcon);
	private JScrollPane scpane;
	//JPanel noWrapPanel;
	private SouthPanel southPanel = new SouthPanel();
	private HTMLEditorKit kit;
	private HTMLDocument doc;
	private ChattingPanel chatPanel = this;
	public ChattingPanel(User user, JFrame mainframe, String name) {
		this.user = user;
		this.name = name;
		
		setLayout(null);
		setBackground(Color.YELLOW);

		/*
		 * noWrapPanel = new JPanel(new BorderLayout());
		 * noWrapPanel.setPreferredSize(new Dimension(200,200));
		 * noWrapPanel.add(showArea); scpane = new JScrollPane(noWrapPanel);
		 * scpane.setPreferredSize(new Dimension(200, 200));
		 * scpane.setViewportView(noWrapPanel); showArea.setAutoscrolls(true);
		 * scpane.setBounds(15, 30, 370, 450);
		 */

		scpane = new JScrollPane(showArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scpane.setBounds(15, 30, 370, 450);
		// showArea.setPreferredSize(new Dimension(300, 300));
		// scpane.setViewportView(showArea);
		// showArea.setBounds(0, 0, 200, 200);

		add(scpane, BorderLayout.CENTER);
		// southPanel.setPreferredSize(new Dimension(400, 100));
		southPanel.setSize(370, 50);
		add(southPanel, BorderLayout.SOUTH);

		showArea.setEditable(false);
		// showArea.setLineWrap(true);
		showArea.setMargin(new Insets(30, 30, 30, 30));

		addMouseListener(new focus());
		showArea.addMouseListener(new focus());

		Thread th = new Thread(showArea);
		th.start();

	}
	public void buttonInit(JButton btn,ImageIcon icon){
		btn.setBorder(BorderFactory.createEmptyBorder());
		btn.setBackground(null);
		btn.setContentAreaFilled(false);
		btn.setPressedIcon(icon);		
		btn.setBorderPainted(false);
	}
	public void getfocus() {
		textfield.requestFocus();
	}

	class focus extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			textfield.requestFocus();
		}
	}

	/*private void appendToPane(JTextPane tp, String msg, Color c) {
		StyleContext sc = StyleContext.getDefaultStyleContext();
		AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

		aset = sc.addAttribute(aset, StyleConstants.FontFamily, "맑은고딕");
		aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

		int len = tp.getDocument().getLength();
		tp.setCaretPosition(len);
		tp.setCharacterAttributes(aset, false);
		tp.replaceSelection(msg);
	}
*/
	public void printHTML(String text, String color) {
		int len = text.length();
		int wordLength = 25 - name.length();
		int n = text.indexOf(" ", 3);
		int count = 0;
		try {
			if (len > wordLength && n == -1 && !text.equals(">> ")) {
				while (len >= wordLength) {
					String temp;
					int sIndex = 0;

					if (sIndex + wordLength - 1 >= text.length()) {
						break;
					} else {
						temp = text.substring(sIndex, sIndex + wordLength);
						// System.out.println(temp + "2");
					}
					text = text.substring(sIndex + wordLength, text.length());
					// System.out.println(text + "3");
					len = text.length();
					kit.insertHTML(doc, doc.getLength(), "<span Color='" + color + "'>" + temp + "</span><br>", 0, 0,
							HTML.Tag.SPAN);
					count++;
				}
				kit.insertHTML(doc, doc.getLength(), "<span Color='" + color + "'>" + text + "</span><br>", 0, 0,
						HTML.Tag.SPAN);
			} else {
				kit.insertHTML(doc, doc.getLength(), "<span Color='" + color + "'>" + text + "</span>", 0, 0,
						HTML.Tag.SPAN);
			}
		} catch (BadLocationException | IOException e1) {
			e1.printStackTrace();
		}
	}

	class MyTextArea extends JTextPane implements Runnable {
		SimpleAttributeSet attribute;
		// StyledDocument doc = this.getStyledDocument();

		Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);

		public MyTextArea() {
			kit = new HTMLEditorKit();
			this.setBorder(BorderFactory.createLineBorder(Color.black));
			setAutoscrolls(true);
			/*
			 * setPreferredSize(null); setBounds(0, 0, 200, 10);
			 * setPreferredSize(new Dimension(200, 400));
			 */
			/*
			 * Style s = doc.addStyle("blue", def);
			 * StyleConstants.setForeground(s, Color.blue); s =
			 * doc.addStyle("black", def); StyleConstants.setForeground(s,
			 * Color.black); s = doc.addStyle("red", def);
			 * StyleConstants.setForeground(s, Color.red); s =
			 * doc.addStyle("맑은고딕", def); StyleConstants.setFontFamily(s,
			 * "맑은고딕"); StyleConstants.setBold(s, true);
			 * StyleConstants.setForeground(s, Color.blue);
			 */
			addHyperlinkListener(new HyperlinkListener() {
				@Override
				public void hyperlinkUpdate(HyperlinkEvent e) {
					if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
						// System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
						// + e.getURL());
						if (Desktop.isDesktopSupported()) {
							try {
								Desktop.getDesktop().browse(e.getURL().toURI());
							} catch (IOException | URISyntaxException e1) {
								System.out.println(e1.getMessage());
							}
						}
					}
				}
			});

			setEditorKit(kit);
			StyleSheet css = kit.getStyleSheet();
			css.addRule("body {margin-left:12px; margin-top:12px; margin-right:12px;}");
			// Font font = new Font("tvN 즐거운이야기 Light", Font.PLAIN, 24);
			Font font = new Font("휴먼매직체", Font.PLAIN, 24);
			String bodyRule = "body { font-family: '" + font.getFamily() + "'; " + "font-size: " + font.getSize()
					+ "pt; background-Color:rgb(255,228,205);}";
			css.addRule(bodyRule);
			doc = (HTMLDocument) kit.createDefaultDocument();
			setDocument(doc);
			// setContentType("text/html; charset=<UTF-8>");
			// setEditorKit(JEditorPane.createEditorKitForContentType("text/html"));
			// setEditorKit(new HTMLEditorKit());
			// setContentType("text/html");

		}

		public void run() {
			boolean isWait = false; 
			while (true) {
				textfield.requestFocus();

				String text = user.read();
				System.out.println(text + "44");
				if (text != null) {
					String splitText[] = text.split(">>");

					int len = showArea.getText().length();
					attribute = new SimpleAttributeSet();
					if (text.startsWith("I&WILL&FILE@SEND@25F25&@$")) {
						user.getFile(text.substring(25),chatPanel);
						try{		
							if(!isWait){
								isWait = true;
								wait();								
							}
							else{
								notify();
								isWait = false;
							}
						}catch (Exception e) {

						}
						continue;
					}
					else if (splitText.length >= 2
							&& (splitText[1].indexOf("https://") != -1 || splitText[1].indexOf("http://") != -1)) {
						printHTML(splitText[0], "blue");
						// printHTML(text.substring(0, 2, "blue");
						// text = text.substring();//
						text = splitText[1];

						printHTML(">><a href=\"" + text + "\">" + text + "</a><br>", "blue");

					} else if (splitText[0].equals(name)) {
						text = text.substring(name.length());
						// showArea.insertIcon(new ImageIcon("그림4.png"));

						// System.out.println(text+"3");
						printHTML(name, "blue");
						printHTML(text + "<br>", "black");

					} else {
						if (text.charAt(0) == '※')
							printHTML(text + "<br>", "red");
						else
							printHTML(text + "<br>", "black");
					}
					showArea.setCaretPosition(doc.getLength());
				}
			}
		}
	}

	public class SouthPanel extends JPanel {
		JProgressBar bar = new JProgressBar();
		JLabel la = new JLabel("0%");
		JPanel ssp = new JPanel();

		public SouthPanel() {
			setBackground(Color.yellow);
			setBounds(15, 500, 370, 30);
			setLayout(new BorderLayout());
			textfield.setFont(new Font("휴먼매직체", Font.PLAIN, 25));
			textfield.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						// user.write("<img src='" +
						// file:\\\C:\Users\CYSN\Documents\다운로드.gif' width='300'
						// height='300'>");
						user.write(textfield.getText());
						textfield.setText("");
					}
				}
			});
			textfield.addFocusListener(new FocusAdapter() {
				@Override
				public void focusGained(FocusEvent e) {
					InputContext context = textfield.getInputContext();
					Character.Subset[] subset = { Character.UnicodeBlock.HANGUL_SYLLABLES };
					context.setCharacterSubsets(subset);
				}
			});
			attachBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JFileChooser chooser = new JFileChooser();
					int choice = chooser.showOpenDialog(null); // 다이어로그를 보여준다.
																// f는 부모
					// 프레임.
					// 리턴값은 열기선택, 취소 중에 하나.

					if (choice == JFileChooser.APPROVE_OPTION) { // 리턴값 체크. 해서
																	// 열기 누른
																	// 경우에는

						ssp.setVisible(true);
						/*
						 * bar.setSize(400, 10); la.setSize(20, 20);
						 */
						bar.setValue(0);
						la.setText("0%");
						File file = chooser.getSelectedFile(); // 선택 파일을 반환한다.
						// System.out.println(file.getName() +" " +
						// file.getPath());

						user.writeFile(file, bar, la, ssp);
						// showArea.insertIcon((Icon) f);
						if (file.getName().contains(".jpg") || file.getName().contains(".png")
								|| file.getName().contains(".gif") || file.getName().contains(".jpeg")) {
							/*
							 * Image img = new Image; ImageIO.write(im,
							 * formatName, output)
							 */

							System.out.println("파일임" + file.getPath());
							printHTML("<img src='file:///" + file.getPath() + "' width='300' height='300'><br>",
									"black");
						}
						// System.out.println("보냄22");
						// remove(la);
						// remove(bar);
						 user.write(file.getName() + "파일보냄");

						// tf.setText(file.getAbsolutePath()); // tf는 JTextField
						// File객체를 통해 File의
						// 경로를 받아 보여줌
						/* 선택한 파일로 부터 스트림을 읽어들여서 얻어놓은 OutputStream에 연결 */

						/* 바이트단위로 읽어서 스트림으로 쓰기 */

						/* 자원정리 */

					}
				}
			});

			bar.setMaximum(0);
			bar.setMaximum(100);
			bar.setStringPainted(true);

			ssp.setLayout(new BorderLayout());
			ssp.add(bar, BorderLayout.CENTER);
			ssp.add(la, BorderLayout.EAST);
			add(ssp, BorderLayout.SOUTH);
			ssp.setVisible(false);
			add(textfield, BorderLayout.CENTER);
			JPanel p = new JPanel();
			p.setLayout(null);
			p.setPreferredSize(new Dimension(50, 50));
			attachBtn.setBounds(0, 0, 50, 50);
			attachBtn.setPressedIcon(attachClickIcon);
			buttonInit(attachBtn, null);
			p.add(attachBtn);
			p.setBackground(null);
			add(p, BorderLayout.EAST);

		}
	}
}