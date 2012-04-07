package skia.javacpp.gm;

import static skia.javacpp.core.*;

public class strokes {
    static final int W = 400;
    static final int H = 400;
    static final int N = 50;

    static final float SW = SkIntToScalar(W);
    static final float SH = SkIntToScalar(H);

    static void rnd_rect(SkRect r, SkPaint paint, SkRandom rand) {
        float x = rand.nextUScalar1() * W;
        float y = rand.nextUScalar1() * H;
        float w = rand.nextUScalar1() * (W >>> 2);
        float h = rand.nextUScalar1() * (H >>> 2);
        float hoffset = rand.nextSScalar1();
        float woffset = rand.nextSScalar1();

        r.set(x, y, x + w, y + h);
        r.offset(-w/2 + woffset, -h/2 + hoffset);

        paint.setColor(rand.nextU());
        paint.setAlpha(0xFF);
    }

    public static class StrokesGM extends GM {
        @Override
        protected String onShortName() {
            return "strokes_round";
        }

        @Override
        protected SkISize onISize() {
            return make_isize(W, H*2);
        }

        @Override
        protected void onDraw(SkCanvas canvas) {
            SkPaint paint = new SkPaint();
            paint.setStyle(SkPaint.kStroke_Style);
            paint.setStrokeWidth(SkIntToScalar(9)/2);

            for (int y = 0; y < 2; y++) {
                paint.setAntiAlias(y != 0);
                int saveCount = canvas.getSaveCount();
                canvas.save();
                try {
                    canvas.translate(0, SH * y);
                    canvas.clipRect(SkRect.MakeLTRB(
                            SkIntToScalar(2), SkIntToScalar(2)
                            , SW - SkIntToScalar(2), SH - SkIntToScalar(2)
                    ));

                    SkRandom rand = new SkRandom();
                    for (int i = 0; i < N; i++) {
                        SkRect r = new SkRect();
                        rnd_rect(r, paint, rand);
                        canvas.drawOval(r, paint);
                        rnd_rect(r, paint, rand);
                        canvas.drawRoundRect(r, r.width()/4, r.height()/4, paint);
                        rnd_rect(r, paint, rand);
                    }
                } finally {
                    canvas.restoreToCount(saveCount);
                }
            }
        }
    }

    public static class Strokes2GM extends GM {
        private SkPath fPath = new SkPath();

        public Strokes2GM() {
            SkRandom rand = new SkRandom();
            fPath.moveTo(0, 0);
            for (int i = 0; i < 13; i++) {
                float x = rand.nextUScalar1() * (W >>> 1);
                float y = rand.nextUScalar1() * (H >>> 1);
                fPath.lineTo(x, y);
            }
        }

        @Override
        protected String onShortName() {
            return "strokes_poly";
        }

        @Override
        protected SkISize onISize() {
            return make_isize(W, H*2);
        }

        static void rotate(float angle, float px, float py, SkCanvas canvas) {
            SkMatrix matrix = new SkMatrix();
            matrix.setRotate(angle, px, py);
            canvas.concat(matrix);
        }

        @Override
        protected void onDraw(SkCanvas canvas) {
            canvas.drawColor(SK_ColorWHITE);

            SkPaint paint = new SkPaint();
            paint.setStyle(SkPaint.kStroke_Style);
            paint.setStrokeWidth(SkIntToScalar(9)/2);

            for (int y = 0; y < 2; y++) {
                paint.setAntiAlias(y != 0);
                int saveCount = canvas.getSaveCount();
                canvas.save();
                try {
                    canvas.translate(0, SH * y);
                    canvas.clipRect(SkRect.MakeLTRB(SkIntToScalar(2),
                            SkIntToScalar(2),
                            SW - SkIntToScalar(2),
                            SH - SkIntToScalar(2)));

                    SkRandom rand = new SkRandom();
                    for (int i = 0; i < N/2; i++) {
                        SkRect r = new SkRect();
                        rnd_rect(r, paint, rand);
                        rotate(SkIntToScalar(15), SW/2, SH/2, canvas);
                        canvas.drawPath(fPath, paint);
                    }
                } finally {
                    canvas.restoreToCount(saveCount);
                }
            }
        }
    }

    public static GMRegistry.Factory MyFactory = new GMRegistry.Factory() {
        public GM apply() {
            return new StrokesGM();
        }
    };

    public static GMRegistry.Factory MyFactory2 = new GMRegistry.Factory() {
        public GM apply() {
            return new Strokes2GM();
        }
    };
}
