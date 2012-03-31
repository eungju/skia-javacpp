package skia.javacpp.gm;

import static skia.javacpp.core.*;

public class FontScalerGM extends GM {
    public FontScalerGM() {
        this.setBGColor(0xFFFFFFFF);
    }

    @Override
    protected String onShortName() {
        return "fontscaler";
    }

    @Override
    protected SkISize onISize() {
        return make_isize(1450, 750);
    }

    static void rotate_about(SkCanvas canvas,
                             float degrees,
                             float px, float py) {
        canvas.translate(px, py);
        canvas.rotate(degrees);
        canvas.translate(-px, -py);
    }

    @Override
    protected void onDraw(SkCanvas canvas) {
        SkPaint paint = new SkPaint();

        paint.setAntiAlias(true);
        paint.setLCDRenderText(true);
        //With freetype the default (normal hinting) can be really ugly.
        //Most distros now set slight (vertical hinting only) in any event.
        paint.setHinting(SkPaint.kSlight_Hinting);
        SkSafeUnref(paint.setTypeface(SkTypeface.CreateFromName("Times Roman", SkTypeface.kNormal)));

        String text = "Hamburgefons ooo mmm";
        final int textLen = text.length();

        for (int j = 0; j < 2; ++j) {
            for (int i = 0; i < 6; ++i) {
                float x = SkIntToScalar(10);
                float y = SkIntToScalar(20);

                int saveCount = canvas.getSaveCount();
                canvas.save();
                try {
                    canvas.translate(SkIntToScalar(50 + i * 230),
                            SkIntToScalar(20));
                    rotate_about(canvas, SkIntToScalar(i * 5), x, y * 10);

                    {
                        SkPaint p = new SkPaint();
                        p.setAntiAlias(true);
                        SkRect r = new SkRect();
                        r.set(x - SkIntToScalar(3), SkIntToScalar(15),
                                x - SkIntToScalar(1), SkIntToScalar(280));
                        canvas.drawRect(r, p);
                    }

                    int index = 0;
                    for (int ps = 6; ps <= 22; ps++) {
                        paint.setTextSize(SkIntToScalar(ps));
                        canvas.drawText(text, x, y, paint);
                        y += paint.getFontMetrics(null);
                        index += 1;
                    }
                } finally {
                    canvas.restoreToCount(saveCount);
                }
            }
            canvas.translate(0, SkIntToScalar(360));
            paint.setSubpixelText(true);
        }
    }

    public static GMRegistry.Factory factory = new GMRegistry.Factory() {
        public GM apply() {
            return new FontScalerGM();
        }
    };
}
