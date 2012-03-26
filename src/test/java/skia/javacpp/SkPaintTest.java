package skia.javacpp;

import com.googlecode.javacpp.Pointer;
import org.junit.Test;

import static org.junit.Assert.*;
import static skia.javacpp.core.*;

public class SkPaintTest {
	@Test public void getFontMetrics() {
		SkPaint dut = new SkPaint();
		dut.setTextSize(12);
		dut.setTypeface(SkTypeface.CreateFromName("Arial", SkTypeface.kNormal));
		SkPaint.FontMetrics metrics = new SkPaint.FontMetrics();
		assertEquals(dut.getFontSpacing(), dut.getFontMetrics(metrics, 1), 0);
	}
	
	@Test public void textToGlyphs() {
		SkPaint dut = new SkPaint();
		dut.setTextEncoding(SkPaint.kUTF8_TextEncoding);
		dut.setTypeface(SkTypeface.CreateFromName("Arial", SkTypeface.kNormal));
		short[] glyphs = new short[4];
		dut.textToGlyphs("ABCD", glyphs);
		assertArrayEquals(new short[] {36, 37, 38, 39}, glyphs);
	}

	@Test public void getTextWidths() {
		SkPaint dut = new SkPaint();
		dut.setTypeface(SkTypeface.CreateFromName("Arial", SkTypeface.kNormal));
		dut.setTextSize(12);
		dut.setTextEncoding(SkPaint.kUTF8_TextEncoding);
		Pointer text = SkPaint.encodeText("ABCD", dut.getTextEncoding());
		float[] widths = new float[4];
		dut.getTextWidths(text, text.capacity(), widths, null);
		assertArrayEquals(new float[] {8, 8, 9, 9}, widths, 1F);
	}
}
