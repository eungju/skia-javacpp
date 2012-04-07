package skia.javacpp.gm;

import static skia.javacpp.core.*;

public class aaclip {
    /** Draw a 2px border around the target, then red behind the target;
     set the clip to match the target, then draw >> the target in blue.
     */

    static void draw (SkCanvas canvas, SkRect target, int x, int y) {
        SkPaint borderPaint = new SkPaint();
        borderPaint.setColor(SkColorSetRGB(0x0, 0xDD, 0x0));
        borderPaint.setAntiAlias(true);
        SkPaint backgroundPaint = new SkPaint();
        backgroundPaint.setColor(SkColorSetRGB(0xDD, 0x0, 0x0));
        backgroundPaint.setAntiAlias(true);
        SkPaint foregroundPaint = new SkPaint();
        foregroundPaint.setColor(SkColorSetRGB(0x0, 0x0, 0xDD));
        foregroundPaint.setAntiAlias(true);

        canvas.save();
        canvas.translate(SkIntToScalar(x), SkIntToScalar(y));
        target.inset(SkIntToScalar(-2), SkIntToScalar(-2));
        canvas.drawRect(target, borderPaint);
        target.inset(SkIntToScalar(2), SkIntToScalar(2));
        canvas.drawRect(target, backgroundPaint);
        canvas.clipRect(target, SkRegion.kIntersect_Op, true);
        target.inset(SkIntToScalar(-4), SkIntToScalar(-4));
        canvas.drawRect(target, foregroundPaint);
        canvas.restore();
    }

    static void draw_square (SkCanvas canvas, int x, int y) {
        SkRect target = SkRect.MakeWH(10 * SK_Scalar1, 10 * SK_Scalar1);
        draw(canvas, target, x, y);
    }

    static void draw_column (SkCanvas canvas, int x, int y) {
        SkRect target = SkRect.MakeWH(1 * SK_Scalar1, 10 * SK_Scalar1);
        draw(canvas, target, x, y);
    }

    static void draw_bar (SkCanvas canvas, int x, int y) {
        SkRect target = SkRect.MakeWH(10 * SK_Scalar1, 1 * SK_Scalar1);
        draw(canvas, target, x, y);
    }

    static void draw_rect_tests (SkCanvas canvas) {
        draw_square(canvas, 10, 10);
        draw_column(canvas, 30, 10);
        draw_bar(canvas, 10, 30);
    }

    /**
     Test a set of clipping problems discovered while writing blitAntiRect,
     and test all the code paths through the clipping blitters.
     Each region should show as a blue center surrounded by a 2px green
     border, with no red.
     */
    public static class AAClipGM extends GM {
        @Override
        protected String onShortName() {
            return "aaclip";
        }

        @Override
        protected SkISize onISize() {
            return make_isize(640, 480);
        }

        @Override
        protected void onDraw(SkCanvas canvas) {
            // Initial pixel-boundary-aligned draw
            draw_rect_tests(canvas);

            // Repeat 4x with .2, .4, .6, .8 px offsets
            canvas.translate(SK_Scalar1 / 5, SK_Scalar1 / 5);
            canvas.translate(SkIntToScalar(50), 0);
            draw_rect_tests(canvas);

            canvas.translate(SK_Scalar1 / 5, SK_Scalar1 / 5);
            canvas.translate(SkIntToScalar(50), 0);
            draw_rect_tests(canvas);

            canvas.translate(SK_Scalar1 / 5, SK_Scalar1 / 5);
            canvas.translate(SkIntToScalar(50), 0);
            draw_rect_tests(canvas);

            canvas.translate(SK_Scalar1 / 5, SK_Scalar1 / 5);
            canvas.translate(SkIntToScalar(50), 0);
            draw_rect_tests(canvas);
        }
    }

    public static GMRegistry.Factory MyFactory = new GMRegistry.Factory() {
        public GM apply() {
            return new AAClipGM();
        }
    };
}
