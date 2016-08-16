package entity.mob;

import java.util.List;

import graphics.AnimatedSprite;
import graphics.Screen;
import graphics.SpriteSheet;

public class Chaser extends Mob {

	private AnimatedSprite up = new AnimatedSprite(SpriteSheet.king_up, 32, 32, 3);
	private AnimatedSprite down = new AnimatedSprite(SpriteSheet.king_down, 32, 32, 3);
	private AnimatedSprite left = new AnimatedSprite(SpriteSheet.king_left, 32, 32, 3);
	private AnimatedSprite right = new AnimatedSprite(SpriteSheet.king_right, 32, 32, 3);

	private AnimatedSprite animSprite = down;

	private double xa = 0;
	private double ya = 0;
	private double speed = 1;

	public Chaser(int x, int y) {
		this.x = x * 16;
		this.y = y * 16;
		sprite = animSprite.getSprite();
	}

	private void move() {
		xa = 0;
		ya = 0;

		List<Mob> players = level.getNearbyPlayers(this, 50);
		if (players.size() > 0) {
			Mob player = players.get(0);
			if (x < player.getX())
				xa += speed;
			if (x > player.getX())
				xa -= speed;
			if (y < player.getY())
				ya += speed;
			if (y > player.getY())
				ya -= speed;
		}

		if (xa != 0 || ya != 0) {
			move(xa, ya);
			walking = true;
		} else
			walking = false;
	}

	public void update() {
		move();
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
	}

	public void render(Screen screen) {
		sprite = animSprite.getSprite();
		screen.renderMob(x - 16, y - 16, this);
	}
}
