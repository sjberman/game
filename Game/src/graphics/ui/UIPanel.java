package graphics.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import util.Vector2i;

public class UIPanel {

	private List<UIComponent> components = new ArrayList<UIComponent>();
	private Vector2i position, size;
	private Color color;

	public UIPanel(Vector2i position, Vector2i size) {
		//super(position);
		this.position = position;
		this.size = size;
		color = new Color(0xCACACA);
	}

	public void addComponent(UIComponent component) {
		components.add(component);
	}

	public UIPanel setColor(int color) {
		this.color = new Color(color);
		return this;
	}

	public void update() {
		for (UIComponent component : components) {
			component.setOffset(position);
			component.update();
		}
	}

	public void render(Graphics g) {
		g.setColor(color);
		g.fillRect(position.x, position.y, size.x, size.y);
		for (UIComponent component : components) {
			component.render(g);
		}
	}
}
