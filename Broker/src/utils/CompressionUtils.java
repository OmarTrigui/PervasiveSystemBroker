package utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

public class CompressionUtils {
	
	
	public static byte[] getCompressedImage(byte[] rgb) {

		BufferedImage image;
		int width;
		int height;

		try {
			image = ImageIO.read(new ByteArrayInputStream(rgb));
			width = image.getWidth();
			height = image.getHeight();

			for (int i = 0; i < height; i++) {

				for (int j = 0; j < width; j++) {

					Color c = new Color(image.getRGB(j, i));
					int red = (int) (c.getRed() * 0.299);
					int green = (int) (c.getGreen() * 0.587);
					int blue = (int) (c.getBlue() * 0.114);
					Color newColor = new Color(red + green + blue,

					red + green + blue, red + green + blue);

					image.setRGB(j, i, newColor.getRGB());
				}
			}

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "jpg", baos);
			baos.flush();
			byte[] imageInByte = baos.toByteArray();
			baos.close();

			return imageInByte;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
