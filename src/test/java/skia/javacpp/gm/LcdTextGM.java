package skia.javacpp.gm;

import static skia.javacpp.core.*;

public class LcdTextGM extends GM {
    public LcdTextGM() {
        final int pointSize = 36;
        textHeight = SkIntToScalar(pointSize);
    }

    @Override
    protected String onShortName() {
        return "lcdtext";
    }

    @Override
    protected SkISize onISize() { return make_isize(640, 480); }

    @Override
    protected void onDraw(SkCanvas canvas) {

        y = textHeight;
        drawText(canvas, "TEXT: SubpixelTrue LCDRenderTrue",
                true,  true);
        drawText(canvas, "TEXT: SubpixelTrue LCDRenderFalse",
                true,  false);
        drawText(canvas, "TEXT: SubpixelFalse LCDRenderTrue",
                false, true);
        drawText(canvas, "TEXT: SubpixelFalse LCDRenderFalse",
                false, false);
    }

    void drawText(SkCanvas canvas, String string,
                  boolean subpixelTextEnabled, boolean lcdRenderTextEnabled) {
        SkPaint paint = new SkPaint();
        paint.setColor(SK_ColorBLACK);
        paint.setDither(true);
        paint.setAntiAlias(true);
        paint.setSubpixelText(subpixelTextEnabled);
        paint.setLCDRenderText(lcdRenderTextEnabled);
        paint.setTextSize(textHeight);

        canvas.drawText(string, 0, y, paint);
        y += textHeight;
    }

    private float y, textHeight;

    public static GMRegistry.Factory factory = new GMRegistry.Factory() {
        public GM apply() {
            return new LcdTextGM();
        }
    };
}
