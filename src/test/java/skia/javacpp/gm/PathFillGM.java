package skia.javacpp.gm;

import skia.javacpp.core;

import static skia.javacpp.core.*;

public class PathFillGM extends GM {
    static interface MakePathProc {
        public float apply(SkPath path);
    }

    static MakePathProc make_frame = new MakePathProc() {
        public float apply(SkPath path) {
            SkRect r = SkRect.MakeLTRB(SkIntToScalar(10), SkIntToScalar(10),
                    SkIntToScalar(630), SkIntToScalar(470));
            path.addRoundRect(r, SkIntToScalar(15), SkIntToScalar(15));

            SkPaint paint = new SkPaint();
            paint.setStyle(SkPaint.kStroke_Style);
            paint.setStrokeWidth(SkIntToScalar(5));
            paint.getFillPath(path, path);
            return SkIntToScalar(15);
        }
    };

    static MakePathProc make_triangle = new MakePathProc() {
        public float apply(SkPath path) {
            final int[] gCoord = {
                    10, 20, 15, 5, 30, 30
            };
            path.moveTo(SkIntToScalar(gCoord[0]), SkIntToScalar(gCoord[1]));
            path.lineTo(SkIntToScalar(gCoord[2]), SkIntToScalar(gCoord[3]));
            path.lineTo(SkIntToScalar(gCoord[4]), SkIntToScalar(gCoord[5]));
            path.close();
            path.offset(SkIntToScalar(10), SkIntToScalar(0));
            return SkIntToScalar(30);
        }
    };

    static MakePathProc make_rect = new MakePathProc() {
        public float apply(SkPath path) {
            SkRect r = SkRect.MakeLTRB(SkIntToScalar(10), SkIntToScalar(10),
                    SkIntToScalar(30), SkIntToScalar(30));
            path.addRect(r);
            path.offset(SkIntToScalar(10), SkIntToScalar(0));
            return SkIntToScalar(30);
        }
    };

    static MakePathProc make_oval = new MakePathProc() {
        public float apply(SkPath path) {
            SkRect r = SkRect.MakeLTRB(SkIntToScalar(10), SkIntToScalar(10),
                    SkIntToScalar(30), SkIntToScalar(30));
            path.addOval(r);
            path.offset(SkIntToScalar(10), SkIntToScalar(0));
            return SkIntToScalar(30);
        }
    };

    static MakePathProc make_sawtooth = new MakePathProc() {
        public float apply(SkPath path) {
            float x = SkIntToScalar(20);
            float y = SkIntToScalar(20);
            final float x0 = x;
            final float dx = SK_Scalar1 * 5;
            final float dy = SK_Scalar1 * 10;

            path.moveTo(x, y);
            for (int i = 0; i < 32; i++) {
                x += dx;
                path.lineTo(x, y - dy);
                x += dx;
                path.lineTo(x, y + dy);
            }
            path.lineTo(x, y + (2 * dy));
            path.lineTo(x0, y + (2 * dy));
            path.close();
            return SkIntToScalar(30);
        }
    };

    static float make_star(SkPath path, int n) {
        final float c = SkIntToScalar(45);
        final float r = SkIntToScalar(20);

        float rad = -SK_ScalarPI / 2;
        final float drad = (n >> 1) * SK_ScalarPI * 2 / n;

        path.moveTo(c, c - r);
        for (int i = 1; i < n; i++) {
            rad += drad;
            float cosV = SkScalarCos(rad), sinV = SkScalarSin(rad);
            path.lineTo(c + SkScalarMul(cosV, r), c + SkScalarMul(sinV, r));
        }
        path.close();
        return r * 2 * 6 / 5;
    }

    static MakePathProc make_star_5 = new MakePathProc() {
        public float apply(SkPath path) { return make_star(path, 5); }
    };

    static MakePathProc make_star_13 = new MakePathProc() {
        public float apply(SkPath path) { return make_star(path, 13); }
    };

    static final MakePathProc[] gProcs = {
            make_frame,
            make_triangle,
            make_rect,
            make_oval,
            make_sawtooth,
            make_star_5,
            make_star_13
    };

    static final int N = gProcs.length;

    private SkPath[]  fPath = new SkPath[N];
    private float[] fDY = new float[N];
    public PathFillGM() {
        for (int i = 0; i < N; i++) {
            fPath[i] = new SkPath();
            fDY[i] = gProcs[i].apply(fPath[i]);
        }
    }

    @Override
    protected String onShortName() {
        return "pathfill";
    }

    @Override
    protected SkISize onISize() {
        return make_isize(640, 480);
    }

    @Override
    protected void onDraw(SkCanvas canvas) {
        SkPaint paint = new SkPaint();
        paint.setAntiAlias(true);

        for (int i = 0; i < N; i++) {
            canvas.drawPath(fPath[i], paint);
            canvas.translate(SkIntToScalar(0), fDY[i]);
        }
    }
}
