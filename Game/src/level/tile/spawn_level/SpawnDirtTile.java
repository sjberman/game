package level.tile.spawn_level;

import graphics.Screen;
import graphics.Sprite;
import level.tile.Tile;

public class SpawnDirtTile extends Tile {

	public SpawnDirtTile(Sprite sprite) {
		super(sprite);
	}

	public void render(int x, int y, Screen screen) {
		// shift back (multiply by 16)
		screen.renderTile(x << 4, y << 4, this);
	}
}
