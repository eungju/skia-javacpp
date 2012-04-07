package skia.javacpp.gm;

import static skia.javacpp.core.*;

public class verttext {
    static final int TEXT_SIZE = 48;
    static final String gText = "Hello";

    public static class VertTextGM extends GM {
        @Override
        protected String onShortName() {
            return "verttext";
        }

        @Override
        protected SkISize onISize() { return make_isize(640, 480); }

        static void drawBaseline(SkCanvas canvas, SkPaint paint,
                                 float x, float y) {
            float total = paint.measureText(gText);

            SkPaint p = new SkPaint();
            p.setAntiAlias(true);
            p.setColor(0x80FF0000);
            canvas.drawLine(x, y,
                    paint.isVerticalText() ? x : x + total,
                    paint.isVerticalText() ? y + total : y,
                    p);

            p.setColor(0xFF0000FF);
            float[] adv = new float[gText.length()];
            paint.getTextWidths(gText, adv, null);
            for (int i = 0; i < gText.length(); ++i) {
                canvas.drawCircle(x, y, SK_Scalar1 * 3 / 2, p);
                if (paint.isVerticalText()) {
                    y += adv[i];
                } else {
                    x += adv[i];
                }
            }
            canvas.drawCircle(x, y, SK_Scalar1 * 3 / 2, p);
        }

        @Override
        protected void onDraw(SkCanvas canvas) {
            float x = SkIntToScalar(100);
            float y = SkIntToScalar(50);

            for (int i = 0; i < 4; ++i) {
                SkPaint paint = new SkPaint();
                paint.setAntiAlias(true);
                paint.setTextSize(SkIntToScalar(TEXT_SIZE));


                paint.setVerticalText(false);
                drawBaseline(canvas, paint, x, y);
                canvas.drawText(gText, x, y, paint);

                paint.setVerticalText(true);
                drawBaseline(canvas, paint, x, y);
                canvas.drawText(gText, x, y, paint);

                x += SkIntToScalar(40);
                y += SkIntToScalar(120);

                canvas.rotate(SkIntToScalar(-15));
            }
        }
    }

    public static GMRegistry.Factory MyFactory = new GMRegistry.Factory() {
        public GM apply() {
            return new VertTextGM();
        }
    };
}
