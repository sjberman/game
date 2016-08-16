package level.tile;

import graphics.Screen;
import graphics.Sprite;
import level.tile.spawn_level.SpawnGrassTile;
import level.tile.spawn_level.SpawnGravelTile;
import level.tile.spawn_level.SpawnDirtTile;
import level.tile.spawn_level.SpawnWallTile;
import level.tile.spawn_level.SpawnWaterTile;

public class Tile {

	public Sprite sprite;

	public static Tile voidTile = new VoidTile(Sprite.voidSprite);

	public static Tile spawn_grass = new SpawnGrassTile(Sprite.spawn_grass);
	public static Tile spawn_water = new SpawnWaterTile(Sprite.spawn_water);
	public static Tile spawn_brick_wall = new SpawnWallTile(Sprite.spawn_brick_wall);
	public static Tile spawn_gravel = new SpawnGravelTile(Sprite.spawn_gravel);
	public static Tile spawn_stone_wall = new SpawnWallTile(Sprite.spawn_stone_wall);
	public static Tile spawn_dirt = new SpawnDirtTile(Sprite.spawn_dirt);

	public static final int col_spawn_grass = 0xff00FF00;
	public static final int col_spawn_water = 0; // unused
	public static final int col_spawn_brick_wall = 0xff7F0000;
	public static final int col_spawn_gravel = 0; // unused
	public static final int col_spawn_stone_wall = 0xff404040;
	public static final int col_spawn_dirt = 0xff825941;

	public Tile(Sprite sprite) {
		this.sprite = sprite;
	}

	public void render(int x, int y, Screen screen) {
	}

	public boolean solid() {
		return false;
	}
}
