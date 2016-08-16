package entity.projectile;

import entity.spawner.ParticleSpawner;
import graphics.Screen;
import graphics.Sprite;

public class WizardProjectile extends Projectile {

	public static final int FIRE_RATE = 10; // higher is slower

	public WizardProjectile(double x, double y, double dir) {
		super(x, y, dir);
		range = 200;
		speed = 4;
		damage = 20;
		sprite = Sprite.rotate(Sprite.projectile_arrow, angle);
		nx = speed * Math.cos(angle);
		ny = speed * Math.sin(angle);
	}

	public void update() {
		if (level.tileCollision((int) (x + nx), (int) (y + ny), 9, 3, 4)) {
			level.add(new ParticleSpawner((int) x, (int) y, 44, 50, level));
			remove();
		}
		move();
	}

	protected void move() {
		x += nx;
		y += ny;

		if (distance() > range)
			remove();
	}

	private double distance() {
		return Math.sqrt((xOrigin - x) * (xOrigin - x) + (yOrigin - y) * (yOrigin - y));
	}

	public void render(Screen screen) {
		screen.renderProjectile((int) x - 12, (int) y, this);
	}
}
