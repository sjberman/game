package graphics;

public class Sprite {

	public final int SIZE;
	private int x, y;
	private int width, height;
	public int[] pixels;
	protected SpriteSheet sheet;

	private final static int ALPHA_COLOR = 0xffFF00FF;

	// these are based on positions/sizes of "images" on spritesheet
	public static Sprite voidSprite = new Sprite(16, 0);

	// Spawn Level Sprites
	public static Sprite spawn_grass = new Sprite(16, 0, 0, SpriteSheet.spawn_level);
	public static Sprite spawn_water = new Sprite(16, 1, 0, SpriteSheet.spawn_level);
	public static Sprite spawn_brick_wall = new Sprite(16, 2, 0, SpriteSheet.spawn_level);
	public static Sprite spawn_gravel = new Sprite(16, 0, 1, SpriteSheet.spawn_level);
	public static Sprite spawn_stone_wall = new Sprite(16, 1, 1, SpriteSheet.spawn_level);
	public static Sprite spawn_dirt = new Sprite(16, 2, 1, SpriteSheet.spawn_level);

	// Projectile Sprites
	public static Sprite projectile_wizard = new Sprite(16, 0, 0, SpriteSheet.projectile_wizard);
	public static Sprite projectile_arrow = new Sprite(16, 1, 0, SpriteSheet.projectile_wizard);

	// Particles
	public static Sprite particle_normal = new Sprite(3, 0xAAAAAA);

	protected Sprite(SpriteSheet sheet, int width, int height) {
		if (width == height)
			SIZE = width;
		else
			SIZE = -1;
		this.width = width;
		this.height = height;
		this.sheet = sheet;
	}

	public Sprite(int size, int x, int y, SpriteSheet sheet) {
		SIZE = size;
		this.width = size;
		this.height = size;
		pixels = new int[SIZE * SIZE];
		this.x = x * size;
		this.y = y * size;
		this.sheet = sheet;
		load();
	}

	public Sprite(int width, int height, int color) {
		SIZE = -1;
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
		setColor(color);
	}

	public Sprite(int size, int color) {
		SIZE = size;
		this.width = size;
		this.height = size;
		pixels = new int[SIZE * SIZE];
		setColor(color);
	}

	public Sprite(int[] spritePixels, int width, int height) {
		if (width == height)
			SIZE = width;
		else
			SIZE = -1;
		this.width = width;
		this.height = height;
		this.pixels = new int[spritePixels.length];
		for (int i = 0; i < spritePixels.length; i++) {
			this.pixels[i] = spritePixels[i];
		}
	}

	public static Sprite rotate(Sprite sprite, double angle) {
		return new Sprite(rotate(sprite.pixels, sprite.width, sprite.height, angle), sprite.width, sprite.height);
	}

	private static int[] rotate(int[] pixels, int width, int height, double angle) {
		int[] result = new int[width * height];

		double nx_x = rotateX(-angle, 1.0, 0.0);
		double nx_y = rotateY(-angle, 1.0, 0.0);
		double ny_x = rotateX(-angle, 0.0, 1.0);
		double ny_y = rotateY(-angle, 0.0, 1.0);

		double x0 = rotateX(-angle, -width / 2.0, -height / 2.0) + width / 2.0;
		double y0 = rotateY(-angle, -width / 2.0, -height / 2.0) + height / 2.0;

		for (int y = 0; y < height; y++) {
			double x1 = x0;
			double y1 = y0;
			for (int x = 0; x < width; x++) {
				int xx = (int) x1;
				int yy = (int) y1;
				if (xx < 0 || xx >= width || yy < 0 || yy >= height)
					result[x + y * width] = ALPHA_COLOR;
				else
					result[x + y * width] = pixels[xx + yy * width];
				x1 += nx_x;
				y1 += nx_y;
			}
			x0 += ny_x;
			y0 += ny_y;
		}

		return result;
	}

	private static double rotateX(double angle, double x, double y) {
		double cos = Math.cos(angle - Math.PI / 2);
		double sin = Math.sin(angle - Math.PI / 2);
		return x * cos + y * -sin;
	}

	private static double rotateY(double angle, double x, double y) {
		double cos = Math.cos(angle - Math.PI / 2);
		double sin = Math.sin(angle - Math.PI / 2);
		return x * sin + y * cos;
	}

	// do i need this?
	public static Sprite[] split(SpriteSheet sheet) {
		int amount = (sheet.getWidth() * sheet.getHeight()) / (sheet.SPRITE_WIDTH * sheet.SPRITE_HEIGHT);
		Sprite[] sprites = new Sprite[amount];
		int current = 0;
		int[] pixels = new int[sheet.SPRITE_WIDTH * sheet.SPRITE_HEIGHT];

		for (int yPosition = 0; yPosition < sheet.getHeight() / sheet.SPRITE_HEIGHT; yPosition++) {
			for (int xPosition = 0; xPosition < sheet.getWidth() / sheet.SPRITE_WIDTH; xPosition++) {

				for (int y = 0; y < sheet.SPRITE_HEIGHT; y++) {
					for (int x = 0; x < sheet.SPRITE_WIDTH; x++) {
						int xo = x + xPosition * sheet.SPRITE_WIDTH;
						int yo = y + yPosition * sheet.SPRITE_HEIGHT;
						pixels[x + y * sheet.SPRITE_WIDTH] = sheet.getPixels()[xo + yo * sheet.getWidth()];
					}
				}
				sprites[current] = new Sprite(pixels, sheet.SPRITE_WIDTH, sheet.SPRITE_HEIGHT);
				current++;
			}
		}
		return sprites;
	}

	private void setColor(int color) {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = color;
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	private void load() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				pixels[x + y * width] = sheet.pixels[(x + this.x) + (y + this.y) * sheet.SPRITE_WIDTH];
			}
		}
	}
}
