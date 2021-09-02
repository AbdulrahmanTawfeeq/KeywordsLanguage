import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

public class MainInterface extends CenteredFrame {
	JLabel title;
	JTextArea textArea;
	JTextArea textArea2;
	JButton run = new JButton("Run");
	KeywordsLanguage language = new KeywordsLanguage();

	public MainInterface() {
		setMainInterface();
		// setTitle(new JLabel("Keywords Language"));
		setTitle("Keywords Language");
		setTextArea(new JTextArea());
		setTextArea2(new JTextArea());
		setToolBar();

		setVisible(true);
	}

	private void setToolBar() {
		JToolBar toolBar = new JToolBar(JToolBar.TOP);

		run.addActionListener((ActionEvent e) -> {
			String[] text = textArea.getText().split("\n");
			if (text.length > 0) {
				for (int i = 0; i < text.length; i++) {
					if (text[i].trim().length() > 0) {
						execCommand(text[i].trim(), i + 1);
					}
				}
				language.reset();
			}
		});
		toolBar.add(run);
		toolBar.setEnabled(false);
		add(toolBar, BorderLayout.NORTH);
	}

	private void setMainInterface() {
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void setTitle(JLabel title) {
		this.title = title;
		lableProperties(this.title);
		add(this.title, BorderLayout.NORTH);
	}

	public JTextArea getTextArea() {
		return textArea;
	}

	public void setTextArea(JTextArea textArea) {
		this.textArea = textArea;
		this.textArea.setBackground(new Color(150, 150, 150));
		this.textArea.setForeground(Color.BLACK);
		this.textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
		this.textArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
		this.textArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				textArea2.setText("");
			}
		});
		this.textArea.setText("x=10\r\n" + "y=11.5\r\n" + "print(x)\r\n" + "print(x plus y minus x)\r\n" + "type(x)\r\n"
				+ "z=True\r\n" + "type(z)\r\n" + "z=false\r\n" + "type(z)\r\n" + "fun(3)\r\n" + "print('Hello')\r\n"
				+ "string = 'Hello'\r\n" + "type(string)\r\n" + "var = skdl\r\n" + "print(var)");
		JScrollPane jScrollPane = new JScrollPane(textArea);
		jScrollPane.setPreferredSize(new Dimension(getWidth(), 100));
		add(jScrollPane, BorderLayout.CENTER);

	}

	public void setTextArea2Null() {
		this.textArea2.setText("");
	}

	public void setTextArea2(JTextArea textArea) {
		this.textArea2 = textArea;
		this.textArea2.setBackground(new Color(30, 30, 30));
		this.textArea2.setForeground(Color.WHITE);
		this.textArea2.setEditable(false);
		this.textArea2.setBorder(
				BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(30, 30, 30)),
						"Console", TitledBorder.LEFT, TitledBorder.TOP, getFont(), Color.WHITE));
		this.textArea2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
		JScrollPane jScrollPane = new JScrollPane(textArea2);
		jScrollPane.setPreferredSize(new Dimension(getWidth(), 100));
		add(jScrollPane, BorderLayout.SOUTH);
	}

	private void execCommand(String command, int line) {
		switch (command) {
		case "cls":
			textArea.setText("");
			textArea2.setText("");
			break;
		default:
			language.input(command, line);
			if (language.getResult() != null && language.getResult().split(" ")[0].equals("Error")) {
				textArea2.setForeground(Color.RED);
				textArea2.append(language.getResult() + "\n");
			} else if (language.getResult() != null) {
				textArea2.setForeground(Color.WHITE);
				textArea2.append(language.getResult() + "\n");
				language.setResult(null);
			}
			break;
		}
	}

	private void lableProperties(JLabel lable) {
		lable.setHorizontalAlignment(SwingConstants.CENTER);
		lable.setOpaque(true);
		lable.setBackground(new Color(150, 150, 150));
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new MainInterface();
	}

}
