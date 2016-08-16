package graphics;

public class Font {

	private static SpriteSheet font = new SpriteSheet("/fonts/arial.png", 16);
	private static Sprite[] characters = Sprite.split(font);

	private static String charTable = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" //
			+ "abcdefghijklmnopqrstuvwxyz" //
			+ "0123456789.,'" //
			+ "'\"\";:!@$%()-+";

	public Font() {

	}

	public void render(int x, int y, String text, Screen screen) {
		render(x, y, 0, 0, text, screen);
	}

	public void render(int x, int y, int color, String text, Screen screen) {
		render(x, y, 0, color, text, screen);
	}

	public void render(int x, int y, int spacing, int color, String text, Screen screen) {
		int line = 0;
		int xOffset = 0;
		for (int i = 0; i < text.length(); i++) {
			xOffset += 16 + spacing;
			int yOffset = 0;
			char currentChar = text.charAt(i);
			if (currentChar == ' ')
				continue;
			if (currentChar == '\n') {
				xOffset = 0;
				line++;
				continue;
			}
			yOffset = line * 20;
			if (currentChar == 'g' || currentChar == 'j' || currentChar == 'p' || currentChar == 'q'
					|| currentChar == 'y' || currentChar == ',')
				yOffset += 4;
			int index = charTable.indexOf(currentChar);
			screen.renderTextCharacter(x + xOffset, y + yOffset, characters[index], color, false);
		}
	}
}
