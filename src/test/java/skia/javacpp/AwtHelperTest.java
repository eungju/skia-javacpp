package skia.javacpp;

import com.google.common.base.Stopwatch;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;

import static skia.javacpp.core.*;

public class AwtHelperTest {
	@Test public void toBufferedImage() {
		SkBitmap bitmap = new SkBitmap();
		bitmap.setConfig(SkBitmap.kARGB_8888_Config, 256, 256);
		bitmap.allocPixels();
		bitmap.eraseColor(SK_ColorBLUE);
		Stopwatch s = new Stopwatch().start();
		for (int i = 0; i < 1000; i++) {
			AwtHelper.toBufferedImage(bitmap);
		}
		s.stop();
		System.out.println(s.elapsedMillis());
	}
	
	@Test public void toSkBitmap() {
		BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2d = image.createGraphics();
		g2d.setBackground(Color.RED);
		g2d.clearRect(0, 0, image.getWidth() / 2, image.getHeight() / 2);
		SkBitmap bitmap = AwtHelper.toSkBitmap(image);
		SkiaTest.saveToFile("to_SkBitmap", bitmap);
	}
}
