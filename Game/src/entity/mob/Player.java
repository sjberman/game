package entity.mob;

import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import entity.projectile.WizardProjectile;
import events.Event;
import events.EventDispatcher;
import events.EventListener;
import events.types.MousePressedEvent;
import events.types.MouseReleasedEvent;
import game.Game;
import graphics.AnimatedSprite;
import graphics.Screen;
import graphics.SpriteSheet;
import graphics.ui.UIActionListener;
import graphics.ui.UIButton;
import graphics.ui.UIButtonListener;
import graphics.ui.UILabel;
import graphics.ui.UIManager;
import graphics.ui.UIPanel;
import graphics.ui.UIProgressBar;
import input.Keyboard;
import input.Mouse;
import util.ImageUtils;
import util.Vector2i;

public class Player extends Mob implements EventListener {

	private String name;
	private Keyboard input;
	private boolean walking = false;
	private boolean shooting = false;

	private AnimatedSprite up = new AnimatedSprite(SpriteSheet.player_up, 32, 32, 5);
	private AnimatedSprite down = new AnimatedSprite(SpriteSheet.player_down, 32, 32, 5);
	private AnimatedSprite left = new AnimatedSprite(SpriteSheet.player_left, 32, 32, 5);
	private AnimatedSprite right = new AnimatedSprite(SpriteSheet.player_right, 32, 32, 5);

	private AnimatedSprite animSprite = down;

	private int fireRate = 0;

	private UIManager ui;
	private UIProgressBar uiHealthBar;
	private UIButton button;

	private BufferedImage image;

	/*public Player(String name, Keyboard input) {
		this.name = name;
		this.input = input;
		sprite = animSprite.getSprite();
	}*/

	public Player(String name, int x, int y, Keyboard input) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.input = input;
		sprite = animSprite.getSprite();
		fireRate = WizardProjectile.FIRE_RATE;

		ui = Game.getUIManager();
		UIPanel panel = new UIPanel(new Vector2i((300 - 80) * 3, 0), new Vector2i(80 * 3, 168 * 3)).setColor(0x4F4F4F);
		ui.addPanel(panel);
		UILabel nameLabel = new UILabel(new Vector2i(40, 200), name);
		nameLabel.setColor(0xBBBBBB);
		nameLabel.setFont(new Font("Helvetica", Font.BOLD, 24));
		nameLabel.dropShadow = true;
		panel.addComponent(nameLabel);

		uiHealthBar = new UIProgressBar(new Vector2i(10, 215), new Vector2i(80 * 3 - 20, 20));
		uiHealthBar.setColor(0x6A6A6A);
		uiHealthBar.setForegroundColor(0xFF3A3A);
		panel.addComponent(uiHealthBar);

		UILabel hpLabel = new UILabel(new Vector2i(uiHealthBar.position).add(new Vector2i(2, 16)), "HP");
		hpLabel.setColor(0xFFFFFF);
		hpLabel.setFont(new Font("Verdana", Font.PLAIN, 18));
		panel.addComponent(hpLabel);

		//Player default attributes
		health = 100;

		//move buttons somewhere else?? UI class?
		button = new UIButton(new Vector2i(10, 260), new Vector2i(100, 30), new UIActionListener() {
			public void perform() {
				System.exit(0);
			}
		});
		button.setText("Hello");
		panel.addComponent(button);
		panel.addComponent(button.label);

		// IMAGE BUTTON
		try {
			image = ImageIO.read(new File("res/textures/player.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		UIButton imageButton = new UIButton(new Vector2i(10, 360), image, new UIActionListener() {
			public void perform() {
				System.exit(0);
			}
		});
		imageButton.setButtonListener(new UIButtonListener() {
			public void entered(UIButton button) {
				button.setImage(ImageUtils.changeBrightness(image, 70));
			}

			public void exited(UIButton button) {
				button.setImage(image);
			}

			public void pressed(UIButton button) {
				button.setImage(ImageUtils.changeBrightness(image, -50));
				button.ignoreNextPress();
			}

			public void released(UIButton button) {
				button.setImage(image);
			}
		});
		panel.addComponent(imageButton);
	}

	public String getName() {
		return name;
	}

	public void onEvent(Event event) {
		EventDispatcher dispatcher = new EventDispatcher(event);
		dispatcher.dispatch(Event.Type.MOUSE_PRESSED, (Event e) -> onMousePressed((MousePressedEvent) e));
		dispatcher.dispatch(Event.Type.MOUSE_RELEASED, (Event e) -> onMouseReleased((MouseReleasedEvent) e));
	}

	public void update() {
		if (walking)
			animSprite.update();
		else
			animSprite.setFrame(0);

		if (fireRate > 0)
			fireRate--;

		double xa = 0, ya = 0;
		double speed = 2;
		if (input.up) {
			animSprite = up;
			ya -= speed;
		}
		if (input.down) {
			animSprite = down;
			ya += speed;
		}
		if (input.left) {
			animSprite = left;
			xa -= speed;
		}
		if (input.right) {
			animSprite = right;
			xa += speed;
		}

		if (xa != 0 || ya != 0) {
			move(xa, ya);
			walking = true;
		} else
			walking = false;

		updateShooting();

		uiHealthBar.setProgress(health / 100.0);
	}

	private void updateShooting() {
		if (!shooting || fireRate > 0)
			return;

		double dx = Mouse.getX() - Game.getWindowWidth() / 2;
		double dy = Mouse.getY() - Game.getWindowHeight() / 2;
		double dir = Math.atan2(dy, dx);
		shoot(x, y, dir);
		fireRate = WizardProjectile.FIRE_RATE;
	}

	public boolean onMousePressed(MousePressedEvent e) {
		if (e.getX() > 660)
			return false;

		if (e.getButton() == MouseEvent.BUTTON1) {
			shooting = true;
			return true;
		}
		return false;
	}

	public boolean onMouseReleased(MouseReleasedEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			shooting = false;
			return true;
		}
		return false;
	}

	public void render(Screen screen) {
		sprite = animSprite.getSprite();
		// subtracting 16 centers the player (16 is half of player size)
		screen.renderMob(x - 16, y - 16, this);
	}

}
