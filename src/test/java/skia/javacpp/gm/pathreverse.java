package skia.javacpp.gm;

import static skia.javacpp.core.*;

public class pathreverse {
    static void test_path(SkCanvas canvas, SkPath path) {
        SkPaint paint = new SkPaint();
        paint.setAntiAlias(true);
        canvas.drawPath(path, paint);

        paint.setStyle(SkPaint.kStroke_Style);
        paint.setColor(SK_ColorRED);
        canvas.drawPath(path, paint);
    }

    static void test_rev(SkCanvas canvas, SkPath path) {
        test_path(canvas, path);

        SkPath rev = new SkPath();
        rev.reverseAddPath(path);
        canvas.save();
        canvas.translate(150, 0);
        test_path(canvas, rev);
        canvas.restore();
    }

    static void test_rev(SkCanvas canvas) {
        SkRect r = SkRect.MakeLTRB(10, 10, 100, 60);

        SkPath path = new SkPath();

        path.addRect(r); test_rev(canvas, path);

        canvas.translate(0, 100);
        path.offset(20, 20);
        path.addRect(r); test_rev(canvas, path);

        canvas.translate(0, 100);
        path.reset();
        path.moveTo(10, 10); path.lineTo(30, 30);
        path.addOval(r);
        r.offset(50, 20);
        path.addOval(r);
        test_rev(canvas, path);

        SkPaint paint = new SkPaint();
        paint.setTextSize(SkIntToScalar(100));
        SkTypeface hira = SkTypeface.CreateFromName("Hiragino Maru Gothic Pro", SkTypeface.kNormal);
        SkSafeUnref(paint.setTypeface(hira));
        path.reset();
        paint.getTextPath("e", 50, 50, path);
        canvas.translate(0, 100);
        test_rev(canvas, path);
    }

    public static class PathReverseGM extends GM {
        @Override
        protected String onShortName() {
            return "path-reverse";
        }

        @Override
        protected SkISize onISize() {
            return make_isize(640, 480);
        }

        @Override
        protected void onDraw(SkCanvas canvas) {
            SkRect r = SkRect.MakeLTRB(10, 10, 100, 60);

            SkPath path = new SkPath();

            path.addRect(r); test_rev(canvas, path);

            canvas.translate(0, 100);
            path.offset(20, 20);
            path.addRect(r); test_rev(canvas, path);

            canvas.translate(0, 100);
            path.reset();
            path.moveTo(10, 10); path.lineTo(30, 30);
            path.addOval(r);
            r.offset(50, 20);
            path.addOval(r);
            test_rev(canvas, path);

            SkPaint paint = new SkPaint();
            paint.setTextSize(SkIntToScalar(100));
            SkTypeface hira = SkTypeface.CreateFromName("Hiragino Maru Gothic Pro", SkTypeface.kNormal);
            SkSafeUnref(paint.setTypeface(hira));
            path.reset();
            paint.getTextPath("e", 50, 50, path);
            canvas.translate(0, 100);
            test_rev(canvas, path);
        }
    }

    public static GMRegistry.Factory MyFactory = new GMRegistry.Factory() {
        public GM apply() {
            return new PathReverseGM();
        }
    };
}
