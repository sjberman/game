package game;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import entity.mob.Player;
import events.Event;
import events.EventListener;
import graphics.Screen;
import graphics.layers.Layer;
import graphics.ui.UIManager;
import input.Keyboard;
import input.Mouse;
import level.Level;
import level.TileCoordinate;
import net.player.NetPlayer;

public class Game extends Canvas implements Runnable, EventListener {
	private static final long serialVersionUID = 1L;

	private static int width = 300 - 80;
	private static int height = 168;
	private static int scale = 3;

	public static String title = "Rain";

	private Thread thread;
	private JFrame frame;
	private Keyboard key;
	private Level level;
	private Player player;
	private boolean running = false;

	private static UIManager uiManager;

	private Screen screen;
	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

	private List<Layer> layerStack = new ArrayList<Layer>();

	public Game() {
		Dimension size = new Dimension(width * scale + 80 * 3, height * scale);
		setPreferredSize(size);

		screen = new Screen(width, height);
		uiManager = new UIManager();
		frame = new JFrame();
		key = new Keyboard();
		level = Level.spawn;
		addLayer(level);

		TileCoordinate playerSpawn = new TileCoordinate(20, 63);
		player = new Player("Sayber", playerSpawn.x(), playerSpawn.y(), key);
		level.add(player);
		level.add(new NetPlayer());

		addKeyListener(key);

		Mouse mouse = new Mouse(this);
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
	}

	public static UIManager getUIManager() {
		return uiManager;
	}

	public static int getWindowWidth() {
		return width * scale;
	}

	public static int getWindowHeight() {
		return height * scale;
	}

	public void addLayer(Layer layer) {
		layerStack.add(layer);
	}

	public synchronized void start() {
		running = true;
		thread = new Thread(this, "Display");
		thread.start();
	}

	public synchronized void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 60.0; // 1/60th of a second
		double delta = 0;
		int frames = 0;
		int updates = 0;
		requestFocus();
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				update(); // updates 60 times per second
				updates++;
				delta--;
			}
			render();
			frames++;
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				frame.setTitle(title + "  |  " + updates + "ups, " + frames + " fps");
				updates = 0;
				frames = 0;
			}
		}
		stop();
	}

	public void onEvent(Event event) {
		for (int i = layerStack.size() - 1; i >= 0; i--) {
			layerStack.get(i).onEvent(event);
		}
	}

	public void update() {
		key.update();
		uiManager.update();

		// update layers here
		for (int i = 0; i < layerStack.size(); i++) {
			layerStack.get(i).update();
		}
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		screen.clear();
		int xScroll = player.getX() - screen.width / 2;
		int yScroll = player.getY() - screen.height / 2;
		level.setScroll(xScroll, yScroll);

		// render layers here
		for (int i = 0; i < layerStack.size(); i++) {
			layerStack.get(i).render(screen);
		}

		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = screen.pixels[i];
		}

		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, width * scale, height * scale, null);
		uiManager.render(g);
		g.dispose();
		bs.show();
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.frame.setResizable(false);
		game.frame.setTitle(Game.title);
		game.frame.add(game);
		game.frame.pack();
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);

		game.start();
	}
}
