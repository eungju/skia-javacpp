package skia.javacpp.gm;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static skia.javacpp.core.*;

public class ConvexPathsGM extends GM {
    public ConvexPathsGM() {
        this.setBGColor(0xFF000000);
        this.makePaths();
    }

    @Override
    protected String onShortName() {
        return "convexpaths";
    }

    @Override
    protected SkISize onISize() {
        return make_isize(1200, 900);
    }

    void makePaths() {
        // CW
        fPaths.add(new SkPath());
        fPaths.getLast().moveTo(0, 0);
        fPaths.getLast().quadTo(50 * SK_Scalar1, 100 * SK_Scalar1,
                0, 100 * SK_Scalar1);
        fPaths.getLast().lineTo(0, 0);

        // CCW
        fPaths.add(new SkPath());
        fPaths.getLast().moveTo(0, 0);
        fPaths.getLast().lineTo(0, 100 * SK_Scalar1);
        fPaths.getLast().quadTo(50 * SK_Scalar1, 100 * SK_Scalar1,
                0, 0);

        // CW
        fPaths.add(new SkPath());
        fPaths.getLast().moveTo(0, 50 * SK_Scalar1);
        fPaths.getLast().quadTo(50 * SK_Scalar1, 0,
                100 * SK_Scalar1, 50 * SK_Scalar1);
        fPaths.getLast().quadTo(50 * SK_Scalar1, 100 * SK_Scalar1,
                0, 50 * SK_Scalar1);

        // CCW
        fPaths.add(new SkPath());
        fPaths.getLast().moveTo(0, 50 * SK_Scalar1);
        fPaths.getLast().quadTo(50 * SK_Scalar1, 100 * SK_Scalar1,
                100 * SK_Scalar1, 50 * SK_Scalar1);
        fPaths.getLast().quadTo(50 * SK_Scalar1, 0,
                0, 50 * SK_Scalar1);

        fPaths.add(new SkPath());
        fPaths.getLast().addRect(0, 0,
                100 * SK_Scalar1, 100 * SK_Scalar1,
                SkPath.kCW_Direction);

        fPaths.add(new SkPath());
        fPaths.getLast().addRect(0, 0,
                100 * SK_Scalar1, 100 * SK_Scalar1,
                SkPath.kCCW_Direction);

        fPaths.add(new SkPath());
        fPaths.getLast().addCircle(50  * SK_Scalar1, 50  * SK_Scalar1,
                50  * SK_Scalar1, SkPath.kCW_Direction);

        fPaths.add(new SkPath());
        fPaths.getLast().addCircle(50  * SK_Scalar1, 50  * SK_Scalar1,
                40  * SK_Scalar1, SkPath.kCCW_Direction);

        fPaths.add(new SkPath());
        fPaths.getLast().addOval(SkRect.MakeXYWH(0, 0,
                50 * SK_Scalar1,
                100 * SK_Scalar1),
                SkPath.kCW_Direction);

        fPaths.add(new SkPath());
        fPaths.getLast().addOval(SkRect.MakeXYWH(0, 0,
                100 * SK_Scalar1,
                50 * SK_Scalar1),
                SkPath.kCCW_Direction);

        fPaths.add(new SkPath());
        fPaths.getLast().addOval(SkRect.MakeXYWH(0, 0,
                100 * SK_Scalar1,
                5 * SK_Scalar1),
                SkPath.kCCW_Direction);

        fPaths.add(new SkPath());
        fPaths.getLast().addOval(SkRect.MakeXYWH(0, 0,
                SK_Scalar1,
                100 * SK_Scalar1),
                SkPath.kCCW_Direction);

        fPaths.add(new SkPath());
        fPaths.getLast().addRoundRect(SkRect.MakeXYWH(0, 0,
                SK_Scalar1 * 100,
                SK_Scalar1 * 100),
                40 * SK_Scalar1, 20 * SK_Scalar1,
                SkPath.kCW_Direction);

        fPaths.add(new SkPath());
        fPaths.getLast().addRoundRect(SkRect.MakeXYWH(0, 0,
                SK_Scalar1 * 100,
                SK_Scalar1 * 100),
                20 * SK_Scalar1, 40 * SK_Scalar1,
                SkPath.kCCW_Direction);

        // shallow diagonals
        fPaths.add(new SkPath());
        fPaths.getLast().lineTo(100 * SK_Scalar1, SK_Scalar1);
        fPaths.getLast().lineTo(98 * SK_Scalar1, 100 * SK_Scalar1);
        fPaths.getLast().lineTo(3 * SK_Scalar1, 96 * SK_Scalar1);

        /*
        It turns out arcTos are not automatically marked as convex and they
        may in fact be ever so slightly concave.
        fPaths.getLast().arcTo(SkRect::MakeXYWH(0, 0,
                                                  50 * SK_Scalar1,
                                                  100 * SK_Scalar1),
                                 25 * SK_Scalar1,  130 * SK_Scalar1, false);
        */

        // cubics
        fPaths.add(new SkPath());
        fPaths.getLast().cubicTo(1 * SK_Scalar1, 1 * SK_Scalar1,
                10 * SK_Scalar1, 90 * SK_Scalar1,
                0 * SK_Scalar1, 100 * SK_Scalar1);
        fPaths.add(new SkPath());
        fPaths.getLast().cubicTo(100 * SK_Scalar1,  50 * SK_Scalar1,
                20 * SK_Scalar1, 100 * SK_Scalar1,
                0 * SK_Scalar1,   0 * SK_Scalar1);

        // triangle where one edge is a degenerate quad
        fPaths.add(new SkPath());
        fPaths.getLast().moveTo(SkFloatToScalar(8.59375f), 45 * SK_Scalar1);
        fPaths.getLast().quadTo(SkFloatToScalar(16.9921875f),   45 * SK_Scalar1,
                SkFloatToScalar(31.25f),        45 * SK_Scalar1);
        fPaths.getLast().lineTo(100 * SK_Scalar1,              100 * SK_Scalar1);
        fPaths.getLast().lineTo(SkFloatToScalar(8.59375f),      45 * SK_Scalar1);

        // point degenerate
        fPaths.add(new SkPath());
        fPaths.getLast().moveTo(50 * SK_Scalar1, 50 * SK_Scalar1);
        fPaths.getLast().lineTo(50 * SK_Scalar1, 50 * SK_Scalar1);

        fPaths.add(new SkPath());
        fPaths.getLast().moveTo(50 * SK_Scalar1, 50 * SK_Scalar1);
        fPaths.getLast().quadTo(50 * SK_Scalar1, 50 * SK_Scalar1,
                50 * SK_Scalar1, 50 * SK_Scalar1);
        fPaths.add(new SkPath());
        fPaths.getLast().moveTo(50 * SK_Scalar1, 50 * SK_Scalar1);
        fPaths.getLast().cubicTo(50 * SK_Scalar1, 50 * SK_Scalar1,
                50 * SK_Scalar1, 50 * SK_Scalar1,
                50 * SK_Scalar1, 50 * SK_Scalar1);

        // moveTo only paths
        fPaths.add(new SkPath());
        fPaths.getLast().moveTo(0, 0);
        fPaths.getLast().moveTo(0, 0);
        fPaths.getLast().moveTo(SK_Scalar1, SK_Scalar1);
        fPaths.getLast().moveTo(SK_Scalar1, SK_Scalar1);
        fPaths.getLast().moveTo(10 * SK_Scalar1, 10 * SK_Scalar1);

        fPaths.add(new SkPath());
        fPaths.getLast().moveTo(0, 0);
        fPaths.getLast().moveTo(0, 0);

        // line degenerate
        fPaths.add(new SkPath());
        fPaths.getLast().lineTo(100 * SK_Scalar1, 100 * SK_Scalar1);
        fPaths.add(new SkPath());
        fPaths.getLast().quadTo(100 * SK_Scalar1, 100 * SK_Scalar1, 0, 0);
        fPaths.add(new SkPath());
        fPaths.getLast().quadTo(100 * SK_Scalar1, 100 * SK_Scalar1,
                50 * SK_Scalar1, 50 * SK_Scalar1);
        fPaths.add(new SkPath());
        fPaths.getLast().quadTo(50 * SK_Scalar1, 50 * SK_Scalar1,
                100 * SK_Scalar1, 100 * SK_Scalar1);
        fPaths.add(new SkPath());
        fPaths.getLast().cubicTo(0, 0,
                0, 0,
                100 * SK_Scalar1, 100 * SK_Scalar1);

        // small circle. This is listed last so that it has device coords far
        // from the origin (small area relative to x,y values).
        fPaths.add(new SkPath());
        fPaths.getLast().addCircle(0, 0, SkFloatToScalar(0.8f));
    }

    @Override
    protected void onDraw(SkCanvas canvas) {

        SkPaint paint = new SkPaint();
        paint.setAntiAlias(true);
        SkRandom rand = new SkRandom();
        canvas.translate(20 * SK_Scalar1, 20 * SK_Scalar1);
        for (int i = 0; i < fPaths.size(); ++i) {
            canvas.save();
            // position the path, and make it at off-integer coords.
            canvas.translate(SK_Scalar1 * 200 * (i % 5) + SK_Scalar1 / 4,
                    SK_Scalar1 * 200 * (i / 5) + 3 * SK_Scalar1 / 4);
            int color = rand.nextU();
            color |= 0xff000000;
            paint.setColor(color);
            SkASSERT(fPaths.get(i).isConvex());
            canvas.drawPath(fPaths.get(i), paint);
            canvas.restore();
        }
    }

    private LinkedList<SkPath> fPaths = new LinkedList<SkPath>();

    public static GMRegistry.Factory factory = new GMRegistry.Factory() {
        public GM apply() {
            return new ConvexPathsGM();
        }
    };
}
