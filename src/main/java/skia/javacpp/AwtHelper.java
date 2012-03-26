package skia.javacpp;

import com.googlecode.javacpp.IntPointer;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import static skia.javacpp.core.*;

public class AwtHelper {
	public static BufferedImage toBufferedImage(SkBitmap bitmap) {
		int w = bitmap.width();
		int h = bitmap.height();
		bitmap.lockPixels();
		try {
			IntPointer pixelPtr = new IntPointer(bitmap.getPixels());
			BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB_PRE);
			DataBufferInt dataBuffer = (DataBufferInt) bi.getRaster().getDataBuffer();
			int[] pixels = dataBuffer.getData();
			pixelPtr.get(pixels);
			return bi;
		} finally {
			bitmap.unlockPixels();
		}
	}

	public static SkBitmap toSkBitmap(BufferedImage image) {
		int w = image.getWidth();
		int h = image.getHeight();
		SkBitmap bitmap = new SkBitmap();
		bitmap.setConfig(SkBitmap.kARGB_8888_Config, w, h);
		bitmap.allocPixels();
		bitmap.lockPixels();
		try {
			IntPointer pixelPtr = new IntPointer(bitmap.getPixels());
			int[] samples = image.getRGB(0, 0, w, h, null, 0, w);
			pixelPtr.put(samples);
		} finally {
			bitmap.unlockPixels();
		}
		return bitmap;
	}
	
	public static SkPath toSkPath(Shape shape, AffineTransform transform) {
		SkPath path = new SkPath();
		PathIterator pi = shape.getPathIterator(transform);
		float[] f = new float[6];
		while (!pi.isDone()) {
			int i = pi.currentSegment(f);
			pi.next();
			switch (i) {
				case PathIterator.SEG_MOVETO:
					path.moveTo(f[0], f[1]);
					break;
				case PathIterator.SEG_LINETO:
					path.lineTo(f[0], f[1]);
					break;
				case PathIterator.SEG_QUADTO:
					path.quadTo(f[0], f[1], f[2], f[3]);
					break;
				case PathIterator.SEG_CUBICTO:
					path.cubicTo(f[0], f[1], f[2], f[3], f[4], f[5]);
					break;
				case PathIterator.SEG_CLOSE:
					path.close();
					break;
			}
		}
		return path;
	}
	
	public static int toSkColor(Color color) {
		return SkColorSetARGB(color.getAlpha(), color.getRed(), color.getGreen(), color.getBlue());
	}
	
	public static int[] toSkColorArray(Color... colors) {
		int[] skColors = new int[colors.length];
		for (int i = 0; i < colors.length; i++) {
			skColors[i] = toSkColor(colors[i]);
		}
		return skColors;
	}

	public static SkPoint toSkPoint(Point2D point) {
		return SkPoint.Make((float) point.getX(), (float) point.getY());
	}
	
	public static int toSkShaderTileMode(MultipleGradientPaint.CycleMethod method) {
		switch (method) {
		case NO_CYCLE:
			return SkShader.kClamp_TileMode;
		case REFLECT:
			return SkShader.kMirror_TileMode;
		case REPEAT:
			return SkShader.kRepeat_TileMode;
		}
		throw new IllegalArgumentException("Unsupported cycle method " + method);
	}

}
