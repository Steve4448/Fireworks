package steve4448;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class FireworkFrame extends Component implements MouseListener, Runnable {
	private Main main;
	private Firework[] firework;
	private double gravity = 0.05;
	private int FPS = 100;
	private int fpsPerSec;
	private long lastFpsSet = System.currentTimeMillis();

	public FireworkFrame(Main main) {
		this.firework = new Firework[500];
		this.main = main;
		new Thread(this).start();
		this.addMouseListener(this);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(main.getContentPane().getSize().width, main.getContentPane().getSize().height);
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getPreferredSize().width, getPreferredSize().height);
		int numParticles = 0;
		for(Firework firework2 : firework)
			if(firework2 != null) {
				for(FireworkFlameParticle FFP : firework2.fireworkFlameParticle)
					if(FFP != null) {
						numParticles++;
						g.setColor(FFP.getColor());
						g.fillRect((int)FFP.getX(), (int)FFP.getY(), FFP.getWidth(), FFP.getHeight());
					}
				if(!firework2.hasExploded()) {
					numParticles++;
					g.setColor(firework2.getColor());
					g.fillRect((int)firework2.getX(), (int)firework2.getY(), firework2.getWidth(), firework2.getHeight());
				} else
					for(FireworkParticle fireworkParticle : firework2.fireworkParticle)
						if(fireworkParticle != null) {
							numParticles++;
							g.setColor(fireworkParticle.getColor());
							g.fillRect((int)fireworkParticle.getX(), (int)fireworkParticle.getY(), fireworkParticle.getWidth(), fireworkParticle.getHeight());
						}
			}
		g.setColor(Color.ORANGE);
		g.drawString("Particles: " + numParticles, 0, 16);
		g.drawString("FPS: " + fpsPerSec, 0, 32);
	}

	public void process() {
		for(int i = 0; i < firework.length; i++)
			if(firework[i] != null) {
				for(int i2 = 0; i2 < firework[i].fireworkFlameParticle.length; i2++) {
					FireworkFlameParticle FFP = firework[i].fireworkFlameParticle[i2];
					if(FFP != null) {
						FFP.setX(firework[i].fireworkFlameParticle[i2].getX() + firework[i].fireworkFlameParticle[i2].getxVelocity());
						FFP.setY(firework[i].fireworkFlameParticle[i2].getY() + firework[i].fireworkFlameParticle[i2].getyVelocity());
						Color ffpColor = FFP.getColor();
						int minusAlpha = (int)(Math.random() * Math.random() * 15);
						if(ffpColor.getAlpha() - minusAlpha <= 0) {
							firework[i].fireworkFlameParticle[i2] = null;
							continue;
						}
						FFP.setyVelocity(FFP.getyVelocity() + gravity / 4);
						FFP.setColor(new Color(ffpColor.getRed(), ffpColor.getGreen(), ffpColor.getBlue(), ffpColor.getAlpha() - minusAlpha));
					}
				}
				if(!firework[i].hasExploded()) {
					firework[i].setY(firework[i].getY() - firework[i].getyVelocity());
					for(int i2 = 0; i2 < 2; i2++)
						for(int i3 = 0; i3 < firework[i].fireworkFlameParticle.length; i3++)
							if(firework[i].fireworkFlameParticle[i3] == null) {
								int size = 1 + (int)(Math.random() * 4);
								firework[i].fireworkFlameParticle[i3] = new FireworkFlameParticle(firework[i].getX() + firework[i].getWidth() / 2 - size / 2, firework[i].getY() + firework[i].getHeight(), size, size, Math.random() * 0.5 - Math.random() * 0.5, new Color(100 + (int)(Math.random() * 155), 25 + (int)(Math.random() * 50), 0));
								break;
							}
					if(firework[i].getY() <= firework[i].getEndY()) {
						firework[i].setExploded(true);
						for(int i2 = 0; i2 < firework[i].fireworkParticle.length; i2++) {
							double xVelocity = 0;
							double yVelocity = 0;
							if((int)(Math.random() * 2) == 1)
								xVelocity = 0.1 + Math.random() * Math.random() * 5;
							else
								xVelocity = -0.1 - Math.random() * Math.random() * 5;
							if((int)(Math.random() * 2) == 1)
								yVelocity = 0.1 + Math.random() * Math.random() * 5;
							else
								yVelocity = -0.1 - Math.random() * Math.random() * 5;
							int size = 1 + (int)(Math.random() * 4);
							firework[i].fireworkParticle[i2] = new FireworkParticle(firework[i].getX(), firework[i].getY(), size, size, xVelocity, yVelocity, new Color((int)(Math.random() * 0xFFFFFF)));
						}
					}
				} else {
					for(int i2 = 0; i2 < firework[i].fireworkParticle.length; i2++) {
						FireworkParticle FP = firework[i].fireworkParticle[i2];
						if(FP != null) {
							FP.setX(FP.getX() + FP.getxVelocity());
							FP.setY(FP.getY() + FP.getyVelocity());
							if(FP.getX() > getPreferredSize().width || FP.getX() < 0 || FP.getY() > getPreferredSize().height) {
								firework[i].fireworkParticle[i2] = null;
								continue;
							}
							FP.setyVelocity(FP.getyVelocity() + gravity);
							int minusAlpha = (int)(Math.random() * 5);
							Color fwColor = FP.getColor();
							if(fwColor.getAlpha() - minusAlpha <= 0) {
								firework[i].fireworkParticle[i2] = null;
								continue;
							}
							FP.setColor(new Color(fwColor.getRed(), fwColor.getGreen(), fwColor.getBlue(), fwColor.getAlpha() - minusAlpha));
						}
					}
					boolean noMoreParticles = true;
					for(int i3 = 0; i3 < firework[i].fireworkParticle.length; i3++)
						if(firework[i].hasExploded())
							if(firework[i].fireworkParticle[i3] != null) {
								noMoreParticles = false;
								break;
							}
					if(noMoreParticles)
						firework[i] = null;
				}
			}
	}

	@Override
	public void run() {
		int fpsC = 0;
		while(true)
			try {
				fpsC++;
				if(System.currentTimeMillis() - lastFpsSet >= 1000) {
					fpsPerSec = fpsC;
					fpsC = 0;
					lastFpsSet = System.currentTimeMillis();
				}
				repaint();
				process();
				Thread.sleep(1000 / FPS);
			} catch(Exception e) {
				e.printStackTrace();
			}
	}

	@Override
	public void mousePressed(MouseEvent mE) {
		if(mE.getButton() == MouseEvent.BUTTON1) {
			for(int i = 0; i < firework.length; i++)
				if(firework[i] == null) {
					firework[i] = new Firework(mE.getX(), getPreferredSize().height, 5, 20, 5, mE.getY(), new Color((int)(Math.random() * 0xFFFFFF)));
					break;
				}
		} else if(mE.getButton() == MouseEvent.BUTTON3)
			for(int i = 0; i < 50; i++)
				for(int i2 = 0; i2 < firework.length; i2++)
					if(firework[i2] == null) {
						firework[i2] = new Firework((int)(Math.random() * getPreferredSize().width), getPreferredSize().height, 5, 20, 5, (int)(Math.random() * getPreferredSize().height), new Color((int)(Math.random() * 0xFFFFFF)));
						break;
					}
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}
}
