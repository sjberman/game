package level;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import entity.mob.Chaser;
import entity.mob.Dummy;
import entity.mob.Shooter;
import entity.mob.Star;

public class SpawnLevel extends Level {

	public SpawnLevel(String path) {
		super(path);
	}

	protected void loadLevel(String path) {
		try {
			BufferedImage image = ImageIO.read(SpawnLevel.class.getResource(path));
			int w = width = image.getWidth();
			int h = height = image.getHeight();
			tiles = new int[w * h];
			image.getRGB(0, 0, w, h, tiles, 0, w);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("ERROR: Could not load level file!");
		}

		add(new Shooter(20, 55));
		add(new Dummy(15, 53));
		add(new Chaser(17, 54));
		add(new Star(20, 48));
	}

	protected void generateLevel() {

	}
}
