package skia.javacpp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static skia.javacpp.core.*;
import static skia.javacpp.effects.*;
import static skia.javacpp.images.*;

public class SkiaTest {
	SkBitmap bitmap;
	SkDevice device;
	SkCanvas canvas;
	
	@Before public void beforeEach() {
		bitmap = new SkBitmap();
		bitmap.setConfig(SkBitmap.kARGB_8888_Config, 256, 256);
		bitmap.allocPixels();
		bitmap.eraseColor(SK_ColorBLUE);
		device = new SkDevice(bitmap);
		canvas = new SkCanvas(device);
		device.unref();
	}
	
	@After public void afterEach() {
		canvas.unref();
	}
	
	static void force_all_opaque(SkBitmap bitmap) {
		bitmap.lockPixels();
		try {
		     for (int y = 0; y < bitmap.height(); y++) {
		         for (int x = 0; x < bitmap.width(); x++) {
		             int v = bitmap.getAddr32(x, y).get();
		             bitmap.getAddr32(x, y).put(v | (SK_A32_MASK << SK_A32_SHIFT));
		         }
		     }
		 } finally {
			 bitmap.unlockPixels();
		 }
	}

	public static void saveToFile(String name, SkBitmap bitmap) {
		//force_all_opaque(bitmap);
		if (!SkImageEncoder.EncodeFile("target/" + name + ".png", bitmap, SkImageEncoder.kPNG_Type, 100)) {
			System.err.println("Error while encoding the bitmap to png file.");
		}
	}
	
	public void drawWithStroke(SkPaint paint) {
		paint.setARGB(255, 255, 0, 0);
		paint.setStrokeWidth(10);
		paint.setStyle(SkPaint.kStroke_Style);
		SkPath path = new SkPath();
		path.moveTo(50, 50);
		path.lineTo(100, 200);
		path.lineTo(200, 50);
		canvas.drawPath(path, paint);
	}

	@Test public void stroke_cap_butt() {
		SkPaint paint = new SkPaint();
		paint.setStrokeCap(SkPaint.kButt_Cap);
		drawWithStroke(paint);
		saveToFile("stroke_cap_butt", bitmap);
	}

	@Test public void stroke_cap_round() {
		SkPaint paint = new SkPaint();
		paint.setStrokeCap(SkPaint.kRound_Cap);
		drawWithStroke(paint);
		saveToFile("stroke_cap_round", bitmap);
	}

	@Test public void stroke_cap_square() {
		SkPaint paint = new SkPaint();
		paint.setStrokeCap(SkPaint.kSquare_Cap);
		drawWithStroke(paint);
		saveToFile("stroke_cap_square", bitmap);
	}

	@Test public void stroke_join_miter() {
		SkPaint paint = new SkPaint();
		paint.setStrokeJoin(SkPaint.kMiter_Join);
		drawWithStroke(paint);
		saveToFile("stroke_join_miter", bitmap);
	}

	@Test public void stroke_join_round() {
		SkPaint paint = new SkPaint();
		paint.setStrokeJoin(SkPaint.kRound_Join);
		drawWithStroke(paint);
		saveToFile("stroke_join_round", bitmap);
	}

	@Test public void stroke_bevel_miter() {
		SkPaint paint = new SkPaint();
		paint.setStrokeJoin(SkPaint.kBevel_Join);
		drawWithStroke(paint);
		saveToFile("stroke_join_bevel", bitmap);
	}

	public void drawPath(SkPath path) {
		SkPaint paint = new SkPaint();
		paint.setARGB(255, 255, 0, 0);
		paint.setStrokeWidth(1);
		paint.setStyle(SkPaint.kStroke_Style);
		canvas.drawPath(path, paint);
	}
	
	@Test public void path_quad() {
		SkPath path = new SkPath();
		path.moveTo(100, 100);
		path.quadTo(200, 200, 200, 100);
		drawPath(path);
		saveToFile("path_quad", bitmap);
	}

	@Test public void path_cubic() {
		SkPath path = new SkPath();
		path.moveTo(100, 100);
		path.cubicTo(100, 200, 200, 200, 200, 100);
		drawPath(path);
		saveToFile("path_cubic", bitmap);
	}

	@Test public void path_arc() {
		SkPath path = new SkPath();
		path.moveTo(100, 100);
		path.arcTo(100, 200, 200, 200, 100);
		drawPath(path);
		saveToFile("path_arc", bitmap);
	}

	@Test public void path_close() {
		SkPath path = new SkPath();
		path.moveTo(100, 100);
		path.lineTo(100, 200);
		path.lineTo(200, 200);
		path.close();
		drawPath(path);
		saveToFile("path_close", bitmap);
	}

	@Test public void drawRect() {
		SkPaint paint = new SkPaint();
		paint.setARGB(255, 255, 0, 0);
		paint.setStrokeWidth(1);
		paint.setStyle(SkPaint.kStroke_Style);
		canvas.drawRect(SkRect.MakeXYWH(100, 100, 100, 100), paint);
		saveToFile("rect", bitmap);
	}

	public void drawWithPathEffect(SkPathEffect pathEffect) {
		SkPaint paint = new SkPaint();
		paint.setARGB(255, 255, 0, 0);
		paint.setStrokeWidth(1);
		paint.setStyle(SkPaint.kStroke_Style);
		paint.setPathEffect(pathEffect).unref();
		SkPath path = new SkPath();
		path.moveTo(50, 50);
		path.lineTo(100, 200);
		path.lineTo(200, 50);
		canvas.drawPath(path, paint);
	}
	
	@Test public void corner_path_effect() {
		drawWithPathEffect(new SkCornerPathEffect(32));
		saveToFile("corner_path_effect", bitmap);
	}

	@Test public void dash_path_effect() {
		drawWithPathEffect(new SkDashPathEffect(new float[] {4, 4}, 2, false));
		saveToFile("dash_path_effect", bitmap);
	}

	@Test public void discrete_path_effect() {
		drawWithPathEffect(new SkDiscretePathEffect(4, 4));
		saveToFile("discrete_path_effect", bitmap);
	}

	@Test public void path_1d_path_effect() {
		drawWithPathEffect(new SkPath1DPathEffect(createPatternPath(), 8, 0, SkPath1DPathEffect.kRotate_Style));
		saveToFile("path_1d_path_effect", bitmap);
	}
	
	@Test public void compose_path_effect() {
		SkPathEffect outer = new SkDashPathEffect(new float[] {4, 4}, 2, false);
		SkPathEffect inner = new SkCornerPathEffect(32);
		drawWithPathEffect(new SkComposePathEffect(outer, inner));
		outer.unref();
		inner.unref();
		saveToFile("compose_path_effect", bitmap);
	}

	@Test public void sum_path_effect() {
		SkPathEffect first = new SkDashPathEffect(new float[] {4, 4}, 2, false);
		SkPathEffect second = new SkCornerPathEffect(32);
		drawWithPathEffect(new SkSumPathEffect(first, second));
		first.unref();
		second.unref();
		saveToFile("sum_path_effect", bitmap);
	}

	SkBitmap createPattern() {
		SkBitmap pattern = new SkBitmap();
		pattern.setConfig(SkBitmap.kARGB_8888_Config, 16, 16);
		pattern.allocPixels();
		pattern.eraseColor(0);
		SkDevice device = new SkDevice(pattern);
		SkCanvas canvas = new SkCanvas(device);
		device.unref();
		SkPath path = createPatternPath();
		SkPaint paint = new SkPaint();
		paint.setColor(SK_ColorGREEN);
		paint.setStyle(SkPaint.kStroke_Style);
		canvas.drawPath(path, paint);
		canvas.unref();
		return pattern;
	}

	SkPath createPatternPath() {
		SkPath path = new SkPath();
		path.moveTo(0, 0);
		path.lineTo(2, 8);
		path.lineTo(4, 0);
		return path;
	}
	
	@Test public void bitmap_shader() {
		SkPaint paint = new SkPaint();
		paint.setStyle(SkPaint.kFill_Style);
		SkBitmap pattern = createPattern();
		SkShader shader = SkShader.CreateBitmapShader(pattern, SkShader.kRepeat_TileMode, SkShader.kRepeat_TileMode);
		paint.setShader(shader).unref();
		canvas.drawRect(SkRect.MakeXYWH(100, 100, 100, 100), paint);
		saveToFile("bitmap_shader", bitmap);
	}

	@Test public void color_shader() {
		SkPaint paint = new SkPaint();
		paint.setStyle(SkPaint.kFill_Style);
		SkShader shader = new SkColorShader(core.SK_ColorRED);
		paint.setShader(shader).unref();
		canvas.drawRect(SkRect.MakeXYWH(100, 100, 100, 100), paint);
		saveToFile("color_shader", bitmap);
	}

	@Test public void linear_gradient_shader() {
		SkPaint paint = new SkPaint();
		paint.setStyle(SkPaint.kFill_Style);
		SkShader shader = SkGradientShader.CreateLinear(new SkPoint[] { SkPoint.Make(100, 100), SkPoint.Make(199, 199)}, new int[] { SK_ColorGREEN, SK_ColorRED }, null, SkShader.kClamp_TileMode, null);
		paint.setShader(shader).unref();
		canvas.drawRect(SkRect.MakeXYWH(0, 0, 256, 256), paint);
		saveToFile("linear_gradient_shader", bitmap);
	}

	@Test public void radial_gradient_shader() {
		SkPaint paint = new SkPaint();
		paint.setStyle(SkPaint.kFill_Style);
		SkShader shader = SkGradientShader.CreateRadial(SkPoint.Make(150, 150), 50, new int[] { SK_ColorGREEN, SK_ColorRED }, null, SkShader.kClamp_TileMode, null);
		paint.setShader(shader).unref();
		canvas.drawRect(SkRect.MakeXYWH(100, 100, 100, 100), paint);
		saveToFile("radial_gradient_shader", bitmap);
	}

	@Test public void two_point_radial_gradient_shader() {
		SkPaint paint = new SkPaint();
		paint.setStyle(SkPaint.kFill_Style);
		SkShader shader = SkGradientShader.CreateTwoPointRadial(SkPoint.Make(120, 120), 50, SkPoint.Make(120, 180), 50, new int[] { SK_ColorBLUE, SK_ColorRED }, null, SkShader.kRepeat_TileMode, null);
		paint.setShader(shader).unref();
		canvas.drawRect(SkRect.MakeXYWH(0, 0, 256, 256), paint);
		saveToFile("two_point_radial_gradient_shader", bitmap);
	}

	@Test public void sweep_gradient_shader() {
		SkPaint paint = new SkPaint();
		paint.setStyle(SkPaint.kFill_Style);
		SkShader shader = SkGradientShader.CreateSweep(150, 150, new int[] { SK_ColorBLUE, SK_ColorRED }, null, null);
		paint.setShader(shader).unref();
		canvas.drawRect(SkRect.MakeXYWH(0, 0, 256, 256), paint);
		saveToFile("sweep_gradient_shader", bitmap);
	}

	public void drawText(SkPaint paint) {
		paint.setColor(SK_ColorBLACK);
		paint.setTextEncoding(SkPaint.kUTF8_TextEncoding);
		paint.setTypeface(SkTypeface.CreateFromName("NanumGothic", SkTypeface.kBold)).unref();
		paint.setTextSize(12);
		canvas.clear(SK_ColorWHITE);
		canvas.translate(128, 128);
		for (int d = 0; d < 360; d +=15) {
			canvas.rotate(15);
			canvas.drawText("안녕하세요", 64, 0, paint);
		}
	}
	
	@Test public void draw_text() {
		SkPaint paint = new SkPaint();
		paint.setAntiAlias(false);
		drawText(paint);
		saveToFile("draw_text", bitmap);
	}

	@Test public void draw_text_aa() {
		SkPaint paint = new SkPaint();
		paint.setAntiAlias(true);
		paint.setEmbeddedBitmapText(true);
		drawText(paint);
		saveToFile("draw_text_aa", bitmap);
	}

	@Test public void blur_draw_looper() {
		SkPaint paint = new SkPaint();
		paint.setLooper(new SkBlurDrawLooper(10, 0, 0, SK_ColorGREEN, SkBlurDrawLooper.kNone_BlurFlag)).unref();
		drawWithStroke(paint);
		saveToFile("blur_draw_looper", bitmap);
	}
}
