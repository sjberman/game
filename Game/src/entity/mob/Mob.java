package entity.mob;

import entity.Entity;
import entity.projectile.Projectile;
import entity.projectile.WizardProjectile;
import graphics.Screen;

public abstract class Mob extends Entity {

	protected boolean moving = false;
	protected boolean walking = false;

	protected int health;

	protected enum Direction {
		UP, DOWN, LEFT, RIGHT
	}

	protected Direction dir;

	public void move(double xa, double ya) {
		if (xa > 0)
			dir = Direction.RIGHT;
		if (xa < 0)
			dir = Direction.LEFT;
		if (ya > 0)
			dir = Direction.DOWN;
		if (ya < 0)
			dir = Direction.UP;

		while (xa != 0) {
			if (Math.abs(xa) > 1) {
				if (!collision(abs(xa), ya)) {
					this.x += abs(xa);
				}
				xa -= abs(xa);
			} else {
				if (!collision(abs(xa), ya)) {
					this.x += xa;
				}
				xa = 0;
			}
		}

		while (ya != 0) {
			if (Math.abs(ya) > 1) {
				if (!collision(xa, abs(ya))) {
					this.y += abs(ya);
				}
				ya -= abs(ya);
			} else {
				if (!collision(xa, abs(ya))) {
					this.y += ya;
				}
				ya = 0;
			}
		}
	}

	private int abs(double value) {
		if (value < 0)
			return -1;
		return 1;
	}

	public abstract void update();

	public abstract void render(Screen screen);

	protected void shoot(double x, double y, double dir) {
		Projectile p = new WizardProjectile(x, y, dir);
		level.add(p);
	}

	private boolean collision(double xa, double ya) {
		// convert tiles into pixel level precision, check if solid
		boolean solid = false;
		for (int c = 0; c < 4; c++) {
			double xt = ((x + xa) - c % 2 * 15) / 16;
			double yt = ((y + ya) - c / 2 * 15) / 16;
			int ix = (c % 2 == 0) ? (int) Math.floor(xt) : (int) Math.ceil(xt);
			int iy = (c / 2 == 0) ? (int) Math.floor(yt) : (int) Math.ceil(yt);

			if (level.getTile(ix, iy).solid())
				solid = true;
		}
		return solid;
	}
}
