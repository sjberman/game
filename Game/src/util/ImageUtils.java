package util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

public class ImageUtils {

	private ImageUtils() {
	}

	//creates a NEW images
	public static BufferedImage changeBrightness(BufferedImage original, int amount) {
		double amt = (double) amount / 100;
		BufferedImage result = toRGB(original);
		RescaleOp op = new RescaleOp((float) (1 + amt), 0, null);
		return op.filter(result, null);
	}
	
	//do i need to transfer back to indexed?
	public static BufferedImage toRGB(Image i) {
	    BufferedImage rgb = new BufferedImage(i.getWidth(null), i.getHeight(null), BufferedImage.TYPE_INT_RGB);
	    rgb.createGraphics().drawImage(i, 0, 0, null);
	    return rgb;
	}
}
