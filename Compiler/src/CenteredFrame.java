
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class CenteredFrame extends JFrame {
	public CenteredFrame() {
		// TODO Auto-generated constructor stub
		setSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width / 2,
				Toolkit.getDefaultToolkit().getScreenSize().height / 2));
		setLocation(new Point(Toolkit.getDefaultToolkit().getScreenSize().width / 4,
				Toolkit.getDefaultToolkit().getScreenSize().height / 4));
	}
}
