package graphics.ui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import input.Mouse;
import util.Vector2i;

public class UIButton extends UIComponent {

	public UILabel label;
	private UIButtonListener buttonListener;
	private UIActionListener actionListener;

	private Image image;

	private boolean inside = false;
	private boolean pressed = false;
	private boolean ignorePressed = false;
	private boolean ignoreAction = false;

	public UIButton(Vector2i position, Vector2i size, UIActionListener actionListener) {
		super(position, size);
		this.actionListener = actionListener;
		label = new UILabel(new Vector2i(position).add(new Vector2i(4, size.y - 2)), "");
		label.setColor(0x444444);
		label.active = false;
		init();
	}

	public UIButton(Vector2i position, BufferedImage image, UIActionListener actionListener) {
		super(position, new Vector2i(image.getWidth(), image.getHeight()));
		this.actionListener = actionListener;
		setImage(image);
		init();
	}

	private void init() {
		setColor(0xAAAAAA);
		buttonListener = new UIButtonListener();
	}

	public void setButtonListener(UIButtonListener buttonListener) {
		this.buttonListener = buttonListener;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public void setText(String text) {
		if (text == "")
			label.active = false;
		else
			label.text = text;
	}

	public void ignoreNextPress() {
		ignoreAction = true;
	}

	public void update() {
		Rectangle rect = new Rectangle(getAbsolutePosition().x, getAbsolutePosition().y, size.x, size.y);
		boolean leftClick = Mouse.getButton() == MouseEvent.BUTTON1;
		if (rect.contains(new Point(Mouse.getX(), Mouse.getY()))) {
			if (!inside) {
				if (leftClick)
					ignorePressed = true;
				else
					ignorePressed = false;

				buttonListener.entered(this);
			}
			inside = true;

			if (!pressed && !ignorePressed && leftClick) {
				buttonListener.pressed(this);
				pressed = true;
			} else if (Mouse.getButton() == MouseEvent.NOBUTTON) {
				if (pressed) {
					buttonListener.released(this);
					if (ignoreAction)
						actionListener.perform();
					else
						ignoreAction = false;
					pressed = false;
				}
				ignorePressed = false;
			}
		} else {
			if (inside) {
				buttonListener.exited(this);
				pressed = false;
			}
			inside = false;
		}
	}

	public void render(Graphics g) {
		int x = position.x + offset.x;
		int y = position.y + offset.y;

		if (image != null) {
			g.drawImage(image, x, y, null);
		} else {
			g.setColor(color);
			g.fillRect(x, y, size.x, size.y);

			if (label != null) {
				label.render(g);
			}
		}
	}
}
