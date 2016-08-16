package level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import entity.Entity;
import entity.mob.Mob;
import entity.mob.Player;
import entity.particle.Particle;
import entity.projectile.Projectile;
import entity.spawner.ParticleSpawner;
import events.Event;
import graphics.Screen;
import graphics.layers.Layer;
import level.tile.Tile;
import util.Vector2i;

public class Level extends Layer {

	protected int width, height;
	protected int[] tilesInt;
	protected int[] tiles;
	protected int tile_size;
	
	private int xScroll, yScroll;

	private List<Entity> entities = new ArrayList<Entity>();
	private List<Projectile> projectiles = new ArrayList<Projectile>();
	private List<Particle> particles = new ArrayList<Particle>();
	private List<Mob> players = new ArrayList<Mob>();

	private Comparator<Node> nodeSorter = new Comparator<Node>() {
		public int compare(Node n0, Node n1) {
			if (n1.fCost < n0.fCost)
				return 1;
			if (n1.fCost > n0.fCost)
				return -1;
			return 0;
		}
	};

	public static Level spawn = new SpawnLevel("/levels/spawn.png");

	public Level(int width, int height) {
		this.width = width;
		this.height = height;
		tilesInt = new int[width * height];
		generateLevel();
	}

	public Level(String path) {
		loadLevel(path);
		generateLevel();
	}

	protected void generateLevel() {

	}

	protected void loadLevel(String path) {

	}

	public void update() {
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).update();
			if (entities.get(i).isRemoved())
				entities.remove(i);
		}
		for (int i = 0; i < projectiles.size(); i++) {
			projectiles.get(i).update();
			if (projectiles.get(i).isRemoved())
				projectiles.remove(i);
		}
		for (int i = 0; i < particles.size(); i++) {
			particles.get(i).update();
			if (particles.get(i).isRemoved())
				particles.remove(i);
		}
		for (int i = 0; i < players.size(); i++) {
			players.get(i).update();
			if (players.get(i).isRemoved())
				players.remove(i);
		}
	}
	
	public void onEvent(Event event) {
		getClientPlayer().onEvent(event);
	}

	public List<Projectile> getProjectiles() {
		return projectiles;
	}

	private void time() {

	}

	public boolean tileCollision(int x, int y, int size, int xOffset, int yOffset) {
		// goes through four corners of tile
		// convert tiles into pixel level precision, check if solid
		boolean solid = false;
		for (int c = 0; c < 4; c++) {
			int xt = (x - c % 2 * size + xOffset) / 16;
			int yt = (y - c / 2 * size + yOffset) / 16;
			if (getTile(xt, yt).solid())
				solid = true;
		}
		return solid;
	}
	
	public void setScroll(int xScroll, int yScroll) {
		this.xScroll = xScroll;
		this.yScroll = yScroll;
	}

	public void render(Screen screen) {
		screen.setOffset(xScroll, yScroll);
		int x0 = xScroll / 16; // this divides by 16 (1 pixel = 16 tiles); but
								// bitwise is faster than dividing
		int x1 = (xScroll + screen.width + 16) / 16; // adding 16 to avoid black
														// tile on right edge
														// before tile is
														// rendered
		int y0 = yScroll / 16;
		int y1 = (yScroll + screen.height + 16) / 16; // same as above, bottom
														// edge

		for (int y = y0; y < y1; y++) {
			for (int x = x0; x < x1; x++) {
				getTile(x, y).render(x, y, screen);
			}
		}
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).render(screen);
		}
		for (int i = 0; i < projectiles.size(); i++) {
			projectiles.get(i).render(screen);
		}
		for (int i = 0; i < particles.size(); i++) {
			particles.get(i).render(screen);
		}
		for (int i = 0; i < players.size(); i++) {
			players.get(i).render(screen);
		}
	}

	public void add(Entity e) {
		e.init(this);
		if (e instanceof Particle) {
			particles.add((Particle) e);
		} else if (e instanceof Projectile) {
			projectiles.add((Projectile) e);
		} else if (e instanceof Player) {
			players.add((Player) e);
		} else if (e instanceof ParticleSpawner) {

		} else {
			entities.add(e);
		}
	}
	
	//don't think this is necessary
	public void addPlayer(Mob player) {
		player.init(this);
		players.add(player);
	}

	public List<Mob> getPlayers() {
		return players;
	}

	public Mob getPlayerAt(int index) {
		return players.get(index);
	}

	public Player getClientPlayer() {
		return (Player) players.get(0);
	}

	// A* search algorithm - finds shortest path
	public List<Node> findPath(Vector2i start, Vector2i finish) {
		List<Node> openList = new ArrayList<Node>();
		List<Node> closedList = new ArrayList<Node>();
		Node current = new Node(start, null, 0, Vector2i.getDistance(start, finish));
		openList.add(current);
		while (openList.size() > 0) {
			Collections.sort(openList, nodeSorter);
			current = openList.get(0);
			if (current.tile.equals(finish)) {
				List<Node> path = new ArrayList<Node>();
				while (current.parent != null) {
					path.add(current);
					current = current.parent;
				}
				return path;
			}
			openList.remove(current);
			closedList.add(current);
			for (int i = 0; i < 9; i++) {
				if (i == 4)
					continue;
				int x = current.tile.getX();
				int y = current.tile.getY();
				int xi = (i % 3) - 1;
				int yi = (i / 3) - 1;
				Tile at = getTile(x + xi, y + yi);
				if (at == null)
					continue;
				if (at.solid())
					continue;
				Vector2i a = new Vector2i(x + xi, y + yi);
				double gCost = current.gCost + (Vector2i.getDistance(current.tile, a) == 1 ? 1 : 0.95);
				double hCost = Vector2i.getDistance(a, finish);
				Node node = new Node(a, current, gCost, hCost);
				if (vecInList(closedList, a) && gCost >= current.gCost)
					continue;
				if (!vecInList(openList, a) || gCost < current.gCost)
					openList.add(node);
			}
		}
		return null;
	}

	private boolean vecInList(List<Node> list, Vector2i vector) {
		for (Node n : list) {
			if (n.tile.equals(vector))
				return true;
		}
		return false;
	}

	public List<Entity> getNearbyEntities(Entity e, int radius) {
		List<Entity> result = new ArrayList<Entity>();
		int ex = e.getX();
		int ey = e.getY();
		for (int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			if (entity.equals(e))
				continue;
			if (entity.equals(particles))
				continue;
			// if(entity.equals(particleSpawners))
			// continue;
			int x = entity.getX();
			int y = entity.getY();
			int dx = x - ex;
			int dy = y - ey;
			double distance = Math.sqrt((dx * dx) + (dy * dy));
			if (distance <= radius)
				result.add(entity);
		}
		return result;
	}

	public List<Mob> getNearbyPlayers(Entity e, int radius) {
		List<Mob> result = new ArrayList<Mob>();
		int ex = e.getX();
		int ey = e.getY();
		for (int i = 0; i < players.size(); i++) {
			Mob player = players.get(i);
			int x = player.getX();
			int y = player.getY();
			int dx = x - ex;
			int dy = y - ey;
			double distance = Math.sqrt((dx * dx) + (dy * dy));
			if (distance <= radius)
				result.add(player);
		}
		return result;
	}

	public Tile getTile(int x, int y) {
		if (x < 0 || y < 0 || x >= width || y >= height)
			return Tile.voidTile;
		if (tiles[x + y * width] == Tile.col_spawn_grass)
			return Tile.spawn_grass;
		if (tiles[x + y * width] == Tile.col_spawn_brick_wall)
			return Tile.spawn_brick_wall;
		if (tiles[x + y * width] == Tile.col_spawn_dirt)
			return Tile.spawn_dirt;
		if (tiles[x + y * width] == Tile.col_spawn_stone_wall)
			return Tile.spawn_stone_wall;
		if (tiles[x + y * width] == Tile.col_spawn_water)
			return Tile.spawn_water;
		if (tiles[x + y * width] == Tile.col_spawn_gravel)
			return Tile.spawn_gravel;
		return Tile.voidTile;
	}
}
