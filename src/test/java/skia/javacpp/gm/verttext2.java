package skia.javacpp.gm;

import static skia.javacpp.core.*;

public class verttext2 {
    public static class VertText2GM extends GM {
        public VertText2GM() {
            final int pointSize = 24;
            textHeight = SkIntToScalar(pointSize);
            prop = SkTypeface.CreateFromName("Helvetica", SkTypeface.kNormal);
            mono = SkTypeface.CreateFromName("Courier New", SkTypeface.kNormal);
        }

        @Override
        protected String onShortName() {
            return "verttext2";
        }

        @Override
        protected SkISize onISize() { return make_isize(640, 480); }

        @Override
        protected void onDraw(SkCanvas canvas) {

            for (int i = 0; i < 3; ++i) {
                SkPaint paint = new SkPaint();
                paint.setColor(SK_ColorRED);
                paint.setAntiAlias(true);
                y = textHeight;
                canvas.drawLine(0, SkIntToScalar(10),
                        SkIntToScalar(110), SkIntToScalar(10), paint);
                canvas.drawLine(0, SkIntToScalar(240),
                        SkIntToScalar(110), SkIntToScalar(240), paint);
                canvas.drawLine(0, SkIntToScalar(470),
                        SkIntToScalar(110), SkIntToScalar(470), paint);
                drawText(canvas, "Proportional / Top Aligned",
                        prop,  SkPaint.kLeft_Align);
                drawText(canvas, "<   Proportional / Centered   >",
                        prop,  SkPaint.kCenter_Align);
                drawText(canvas, "Monospaced / Top Aligned",
                        mono, SkPaint.kLeft_Align);
                drawText(canvas, "<    Monospaced / Centered    >",
                        mono, SkPaint.kCenter_Align);
                canvas.rotate(SkIntToScalar(-15));
                canvas.translate(textHeight * 4, SkIntToScalar(50));
                if (i > 0) {
                    canvas.translate(0, SkIntToScalar(50));
                }
            }
        }

        void drawText(SkCanvas canvas, String string,
                      SkTypeface family, int alignment) {
            SkPaint paint = new SkPaint();
            paint.setColor(SK_ColorBLACK);
            paint.setAntiAlias(true);
            paint.setVerticalText(true);
            paint.setTextAlign(alignment);
            paint.setTypeface(family);
            paint.setTextSize(textHeight);

            canvas.drawText(string, y,
                    alignment == SkPaint.kLeft_Align ? 10 : 240, paint);
            y += textHeight;
        }

        private float y, textHeight;
        private SkTypeface prop;
        private SkTypeface mono;
    }

    public static GMRegistry.Factory MyFactory = new GMRegistry.Factory() {
        public GM apply() {
            return new VertText2GM();
        }
    };
}
