package entity.mob;

import java.util.List;

import graphics.AnimatedSprite;
import graphics.Screen;
import graphics.SpriteSheet;
import level.Node;
import util.Vector2i;

public class Star extends Mob {
	private AnimatedSprite up = new AnimatedSprite(SpriteSheet.king_up, 32, 32, 3);
	private AnimatedSprite down = new AnimatedSprite(SpriteSheet.king_down, 32, 32, 3);
	private AnimatedSprite left = new AnimatedSprite(SpriteSheet.king_left, 32, 32, 3);
	private AnimatedSprite right = new AnimatedSprite(SpriteSheet.king_right, 32, 32, 3);

	private AnimatedSprite animSprite = down;

	private double xa = 0;
	private double ya = 0;
	private int time = 0;
	private List<Node> path = null;

	public Star(int x, int y) {
		this.x = x * 16;
		this.y = y * 16;
		sprite = animSprite.getSprite();
	}

	private void move() {
		xa = 0;
		ya = 0;

		int px = level.getPlayerAt(0).getX();
		int py = level.getPlayerAt(0).getY();
		Vector2i start = new Vector2i(getX() / 16, getY() / 16);
		Vector2i destination = new Vector2i(px / 16, py / 16);
		if (time % 3 == 0)
			path = level.findPath(start, destination);
		if (path != null) {
			if (path.size() > 0) {
				Vector2i vec = path.get(path.size() - 1).tile;
				if (x < vec.getX() * 16)
					xa++;
				if (x > vec.getX() * 16)
					xa--;
				if (y < vec.getY() * 16)
					ya++;
				if (y > vec.getY() * 16)
					ya--;
			}
		}

		if (xa != 0 || ya != 0) {
			move(xa, ya);
			walking = true;
		} else
			walking = false;
	}

	public void update() {
		time++;
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
