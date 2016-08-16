package graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteSheet {

	private String path;
	public final int SIZE;
	public final int SPRITE_WIDTH, SPRITE_HEIGHT;
	private int width, height;
	public int[] pixels;

	public static SpriteSheet spawn_level = new SpriteSheet("/textures/spawnlevel.png", 48);
	public static SpriteSheet projectile_wizard = new SpriteSheet("/textures/projectiles/wizard.png", 48);

	public static SpriteSheet player = new SpriteSheet("/textures/player.png", 160, 128);
	public static SpriteSheet player_up = new SpriteSheet(player, 0, 2, 5, 1, 32);
	public static SpriteSheet player_down = new SpriteSheet(player, 0, 0, 5, 1, 32);
	public static SpriteSheet player_left = new SpriteSheet(player, 0, 1, 5, 1, 32);
	public static SpriteSheet player_right = new SpriteSheet(player, 0, 3, 5, 1, 32);
	
	public static SpriteSheet dummy = new SpriteSheet("/textures/orc.png", 96, 128);
	public static SpriteSheet dummy_up = new SpriteSheet(dummy, 0, 0, 3, 1, 32);
	public static SpriteSheet dummy_down = new SpriteSheet(dummy, 0, 2, 3, 1, 32);
	public static SpriteSheet dummy_left = new SpriteSheet(dummy, 0, 1, 3, 1, 32);
	public static SpriteSheet dummy_right = new SpriteSheet(dummy, 0, 3, 3, 1, 32);
	
	public static SpriteSheet archer = new SpriteSheet("/textures/archer.png", 96, 128);
	public static SpriteSheet archer_up = new SpriteSheet(archer, 0, 0, 3, 1, 32);
	public static SpriteSheet archer_down = new SpriteSheet(archer, 0, 2, 3, 1, 32);
	public static SpriteSheet archer_left = new SpriteSheet(archer, 0, 3, 3, 1, 32);
	public static SpriteSheet archer_right = new SpriteSheet(archer, 0, 1, 3, 1, 32);
	
	public static SpriteSheet king = new SpriteSheet("/textures/king.png", 128, 96);
	public static SpriteSheet king_up = new SpriteSheet(king, 0, 0, 1, 3, 32);
	public static SpriteSheet king_down = new SpriteSheet(king, 2, 0, 1, 3, 32);
	public static SpriteSheet king_left = new SpriteSheet(king, 3, 0, 1, 3, 32);
	public static SpriteSheet king_right = new SpriteSheet(king, 1, 0, 1, 3, 32);

	private Sprite[] sprites;

	public SpriteSheet(SpriteSheet sheet, int x, int y, int width, int height, int spriteSize) {
		int xx = x * spriteSize;
		int yy = y * spriteSize;
		int w = width * spriteSize;
		int h = height * spriteSize;
		if (width == height)
			SIZE = width;
		else
			SIZE = -1;
		SPRITE_WIDTH = w;
		SPRITE_HEIGHT = h;
		pixels = new int[w * h];
		for (int y0 = 0; y0 < h; y0++) {
			int yPosition = yy + y0;
			for (int x0 = 0; x0 < w; x0++) {
				int xPosition = xx + x0;
				pixels[x0 + y0 * w] = sheet.pixels[xPosition + yPosition * sheet.SPRITE_WIDTH];
			}
		}

		int frame = 0;
		sprites = new Sprite[width * height];
		for (int ya = 0; ya < height; ya++) {
			for (int xa = 0; xa < width; xa++) {
				int[] spritePixels = new int[spriteSize * spriteSize];
				for (int y0 = 0; y0 < spriteSize; y0++) {
					for (int x0 = 0; x0 < spriteSize; x0++) {
						spritePixels[x0 + y0 * spriteSize] = pixels[(x0 + xa * spriteSize)
								+ (y0 + ya * spriteSize) * SPRITE_WIDTH];
					}
				}
				Sprite sprite = new Sprite(spritePixels, spriteSize, spriteSize);
				sprites[frame++] = sprite;
			}
		}
	}

	public SpriteSheet(String path, int size) {
		this.path = path;
		SIZE = size;
		SPRITE_WIDTH = size;
		SPRITE_HEIGHT = size;
		pixels = new int[SIZE * SIZE];
		load();
	}

	public SpriteSheet(String path, int width, int height) {
		this.path = path;
		SIZE = -1;
		SPRITE_WIDTH = width;
		SPRITE_HEIGHT = height;
		pixels = new int[SPRITE_WIDTH * SPRITE_HEIGHT];
		load();
	}

	public Sprite[] getSprites() {
		return sprites;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int[] getPixels() {
		return pixels;
	}

	private void load() {
		try {
			BufferedImage image = ImageIO.read(SpriteSheet.class.getResource(path));
			width = image.getWidth();
			height = image.getHeight();
			pixels = new int[width * height];
			image.getRGB(0, 0, width, height, pixels, 0, width);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
