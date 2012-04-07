package skia.javacpp.gm;

import static skia.javacpp.core.*;

public class points {
    public static class PointsGM extends GM {
        @Override
        protected String onShortName() {
            return "points";
        }

        @Override
        protected SkISize onISize() {
            return make_isize(640, 490);
        }

        static void fill_pts(SkPoint pts[], SkRandom rand) {
            for (int i = 0; i < pts.length; i++) {
                float y = rand.nextUScalar1() * 480;
                float x = rand.nextUScalar1() * 640;
                pts[i] = new SkPoint();
                pts[i].set(x, y);
            }
        }

        @Override
        protected void onDraw(SkCanvas canvas) {
            canvas.translate(SK_Scalar1, SK_Scalar1);

            SkRandom rand = new SkRandom();
            SkPaint p0 = new SkPaint(), p1 = new SkPaint(), p2 = new SkPaint(), p3 = new SkPaint();
            final int n = 99;

            p0.setColor(SK_ColorRED);
            p1.setColor(SK_ColorGREEN);
            p2.setColor(SK_ColorBLUE);
            p3.setColor(SK_ColorWHITE);

            p0.setStrokeWidth(SkIntToScalar(4));
            p2.setStrokeCap(SkPaint.kRound_Cap);
            p2.setStrokeWidth(SkIntToScalar(6));

            SkPoint[] pts = new SkPoint[n];
            fill_pts(pts, rand);

            canvas.drawPoints(SkCanvas.kPolygon_PointMode, pts, p0);
            canvas.drawPoints(SkCanvas.kLines_PointMode, pts, p1);
            canvas.drawPoints(SkCanvas.kPoints_PointMode, pts, p2);
            canvas.drawPoints(SkCanvas.kPoints_PointMode, pts, p3);
        }
    }

    public static GMRegistry.Factory MyFactory = new GMRegistry.Factory() {
        public GM apply() {
            return new PointsGM();
        }
    };
}
