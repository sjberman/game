package graphics;

import java.util.Random;

import entity.mob.Chaser;
import entity.mob.Mob;
import entity.mob.Star;
import entity.projectile.Projectile;
import level.tile.Tile;

public class Screen {

	public int width, height;
	public int[] pixels;
	public final int MAP_SIZE = 64;
	public final int MAP_SIZE_MASK = MAP_SIZE - 1;
	public int xOffset, yOffset;
	public int[] tiles = new int[MAP_SIZE * MAP_SIZE];
	private Random random = new Random();

	private final int ALPHA_COLOR = 0xffFF00FF;

	public Screen(int width, int height) {
		this.width = width;
		this.height = height;
		pixels = new int[width * height];

		for (int i = 0; i < MAP_SIZE * MAP_SIZE; i++) {
			tiles[i] = random.nextInt(0xffffff);
		}
	}

	public void clear() {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0;
		}
	}

	public void renderTextCharacter(int xPosition, int yPosition, Sprite sprite, int color, boolean fixed) {
		if (fixed) {
			xPosition -= xOffset;
			yPosition -= yOffset;
		}
		for (int y = 0; y < sprite.getHeight(); y++) {
			int yAbsolute = y + yPosition;
			for (int x = 0; x < sprite.getWidth(); x++) {
				int xAbsolute = x + xPosition;
				if (xAbsolute < 0 || xAbsolute >= width || yAbsolute < 0 || yAbsolute >= height)
					continue;
				int col = sprite.pixels[x + y * sprite.getWidth()];
				if (col != ALPHA_COLOR)
					pixels[xAbsolute + yAbsolute * width] = color;
			}
		}
	}

	public void renderSprite(int xPosition, int yPosition, Sprite sprite, boolean fixed) {
		if (fixed) {
			xPosition -= xOffset;
			yPosition -= yOffset;
		}
		for (int y = 0; y < sprite.getHeight(); y++) {
			int yAbsolute = y + yPosition;
			for (int x = 0; x < sprite.getWidth(); x++) {
				int xAbsolute = x + xPosition;
				if (xAbsolute < 0 || xAbsolute >= width || yAbsolute < 0 || yAbsolute >= height)
					continue;
				int color = sprite.pixels[x + y * sprite.getWidth()];
				if (color != ALPHA_COLOR)
					pixels[xAbsolute + yAbsolute * width] = color;
			}
		}
	}

	public void renderTile(int xPosition, int yPosition, Tile tile) {
		xPosition -= xOffset; // reverses direction of map (ex: moving right
								// causes map to move left)
		yPosition -= yOffset;
		for (int y = 0; y < tile.sprite.SIZE; y++) {
			int yAbsolute = y + yPosition;
			for (int x = 0; x < tile.sprite.SIZE; x++) {
				int xAbsolute = x + xPosition;
				if (xAbsolute < -tile.sprite.SIZE || xAbsolute >= width || yAbsolute < 0 || yAbsolute >= height)
					break;
				if (xAbsolute < 0)
					xAbsolute = 0;
				pixels[xAbsolute + yAbsolute * width] = tile.sprite.pixels[x + y * tile.sprite.SIZE];
			}
		}
	}

	public void renderProjectile(int xPosition, int yPosition, Projectile p) {
		xPosition -= xOffset; // reverses direction of map (ex: moving right
								// causes map to move left)
		yPosition -= yOffset;
		for (int y = 0; y < p.getSpriteSize(); y++) {
			int yAbsolute = y + yPosition;
			for (int x = 0; x < p.getSpriteSize(); x++) {
				int xAbsolute = x + xPosition;
				if (xAbsolute < -p.getSpriteSize() || xAbsolute >= width || yAbsolute < 0 || yAbsolute >= height)
					break;
				if (xAbsolute < 0)
					xAbsolute = 0;
				int color = p.getSprite().pixels[x + y * p.getSpriteSize()];
				if (color != ALPHA_COLOR)
					pixels[xAbsolute + yAbsolute * width] = color;
			}
		}
	}

	public void renderMob(int xPosition, int yPosition, Mob mob) {
		xPosition -= xOffset; // reverses direction of map (ex: moving right
								// causes map to move left)
		yPosition -= yOffset;
		for (int y = 0; y < 32; y++) {
			int yAbsolute = y + yPosition;
			for (int x = 0; x < 32; x++) {
				int xAbsolute = x + xPosition;
				if (xAbsolute < -32 || xAbsolute >= width || yAbsolute < 0 || yAbsolute >= height)
					break;
				if (xAbsolute < 0)
					xAbsolute = 0;
				int color = mob.getSprite().pixels[x + y * 32];
				if (mob instanceof Chaser && color == 0xff472BBF)
					color = 0xffBA0015;
				if (mob instanceof Star && color == 0xff472BBF)
					color = 0xffE8E83A;
				if (color != ALPHA_COLOR)
					pixels[xAbsolute + yAbsolute * width] = color;
			}
		}
	}

	// remove this and use Java draw rectangle?
	public void drawRect(int xPosition, int yPosition, int width, int height, int color, boolean fixed) {
		if (fixed) {
			xPosition -= xOffset;
			yPosition -= yOffset;
		}
		for (int x = xPosition; x < xPosition + width; x++) {
			if (x < 0 || x >= this.width || yPosition >= this.height)
				continue;
			if (yPosition > 0)
				pixels[x + yPosition * this.width] = color;
			if (yPosition + height >= this.height)
				continue;
			if (yPosition + height > 0)
				pixels[x + (yPosition + height) * this.width] = color;
		}
		for (int y = yPosition; y <= yPosition + height; y++) {
			if (y < 0 || y >= this.height || xPosition >= this.width)
				continue;
			if (xPosition > 0)
				pixels[xPosition + y * this.width] = color;
			if (xPosition + width >= this.width)
				continue;
			if (xPosition + width > 0)
				pixels[(xPosition + width) + y * this.width] = color;
		}
	}

	public void fillRect(int xPosition, int yPosition, int width, int height, int color, boolean fixed) {
		if (fixed) {
			xPosition -= xOffset;
			yPosition -= yOffset;
		}

		for (int y = 0; y < height; y++) {
			int ya = yPosition + y;
			if (ya < 0 || ya >= this.height)
				continue;
			for (int x = 0; x < width; x++) {
				int xa = xPosition + x;
				if (xa < 0 || xa >= this.width)
					continue;
				pixels[xa + ya * this.width] = color;
			}
		}
	}

	public void setOffset(int xOffset, int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
}
