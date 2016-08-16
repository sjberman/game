package entity.mob;

import graphics.AnimatedSprite;
import graphics.Screen;
import graphics.Sprite;
import graphics.SpriteSheet;

public class Dummy extends Mob {

	private AnimatedSprite up = new AnimatedSprite(SpriteSheet.dummy_up, 32, 32, 3);
	private AnimatedSprite down = new AnimatedSprite(SpriteSheet.dummy_down, 32, 32, 3);
	private AnimatedSprite left = new AnimatedSprite(SpriteSheet.dummy_left, 32, 32, 3);
	private AnimatedSprite right = new AnimatedSprite(SpriteSheet.dummy_right, 32, 32, 3);

	private AnimatedSprite animSprite = down;

	private int time = 0;
	private int xa = 0;
	private int ya = 0;

	public Dummy(int x, int y) {
		this.x = x * 16;
		this.y = y * 16;
		sprite = animSprite.getSprite();
	}

	public void update() {
		// randomly move the NPC around
		time++;
		if (time % (random.nextInt(50) + 30) == 0) {
			xa = random.nextInt(3) - 1;
			ya = random.nextInt(3) - 1;
			if (random.nextInt(3) == 0) {
				xa = 0;
				ya = 0;
			}
		}

		if (walking)
			animSprite.update();
		else
			animSprite.setFrame(0);

		if (ya < 0) {
			animSprite = up;
			dir = Direction.UP;
		} else if (ya > 0) {
			animSprite = down;
			dir = Direction.DOWN;
		} else if (xa < 0) {
			animSprite = left;
			dir = Direction.LEFT;
		} else if (xa > 0) {
			animSprite = right;
			dir = Direction.RIGHT;
		}

		if (xa != 0 || ya != 0) {
			move(xa, ya);
			walking = true;
		} else
			walking = false;
	}

	public void render(Screen screen) {
		sprite = animSprite.getSprite();
		screen.renderMob(x, y, this);
	}
}
