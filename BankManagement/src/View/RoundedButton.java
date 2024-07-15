package View;

import javax.swing.JButton;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

public class RoundedButton extends JButton {

	public RoundedButton(String text) {
		super(text);
		setContentAreaFilled(false); // Ensure the button background is not painted by default
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Set button background color
		g2.setColor(getBackground());
		// Draw rounded rectangle with specified arc width and height
		g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 30, 30));

		super.paintComponent(g);
		g2.dispose();
	}

	@Override
	protected void paintBorder(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Set button border color
		g2.setColor(getForeground());
		// Draw rounded rectangle border with specified arc width and height
		g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 30, 30));

		g2.dispose();
	}

	public void setBounds(double d, double e, int width, int height) {
		// TODO Auto-generated method stub

	}
}
