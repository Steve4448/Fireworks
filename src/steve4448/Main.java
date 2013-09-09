package steve4448;

import java.awt.Dimension;

import javax.swing.JFrame;

public class Main extends JFrame {
	public static void main(String[] args) {
		new Main();
	}

	public Main() {
		super("Firework's");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(new Dimension(800, 600));
		this.add(new FireworkFrame(this));
		this.setVisible(true);
	}
}
