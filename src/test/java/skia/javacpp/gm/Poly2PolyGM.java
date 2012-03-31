package skia.javacpp.gm;

import static skia.javacpp.core.*;

public class Poly2PolyGM extends GM {
    @Override
    public String onShortName() {
        return "poly2poly";
    }

    @Override
    public SkISize onISize() {
        return make_isize(835, 840);
    }

    static void doDraw(SkCanvas canvas, SkPaint paint, int isrc[], int idst[], int count) {
        SkMatrix matrix = new SkMatrix();
        SkPoint[] src = new SkPoint[count], dst = new SkPoint[count];

        for (int i = 0; i < count; i++) {
            src[i] = new SkPoint();
            src[i].set(SkIntToScalar(isrc[2*i+0]), SkIntToScalar(isrc[2*i+1]));
            dst[i] = new SkPoint();
            dst[i].set(SkIntToScalar(idst[2*i+0]), SkIntToScalar(idst[2*i+1]));
        }

        canvas.save();
        matrix.setPolyToPoly(src, dst);
        canvas.concat(matrix);

        paint.setColor(SK_ColorGRAY);
        paint.setStyle(SkPaint.kStroke_Style);
        final float D = SkIntToScalar(64);
        canvas.drawRectCoords(0, 0, D, D, paint);
        canvas.drawLine(0, 0, D, D, paint);
        canvas.drawLine(0, D, D, 0, paint);

        SkPaint.FontMetrics fm = new SkPaint.FontMetrics();
        paint.getFontMetrics(fm);
        paint.setColor(SK_ColorRED);
        paint.setStyle(SkPaint.kFill_Style);
        float x = D/2;
        float y = D/2 - (fm.fAscent() + fm.fDescent())/2;
        String str = String.valueOf(count);
        canvas.drawText(str, x, y, paint);

        canvas.restore();
    }

    @Override
    protected void onDraw(SkCanvas canvas) {
        SkPaint paint = new SkPaint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(SkIntToScalar(4));
        paint.setTextSize(SkIntToScalar(40));
        paint.setTextAlign(SkPaint.kCenter_Align);

        canvas.save();
        canvas.translate(SkIntToScalar(10), SkIntToScalar(10));
        // translate (1 point)
        final int src1[] = { 0, 0 };
        final int dst1[] = { 5, 5 };
        doDraw(canvas, paint, src1, dst1, 1);
        canvas.restore();

        canvas.save();
        canvas.translate(SkIntToScalar(160), SkIntToScalar(10));
        // rotate/uniform-scale (2 points)
        final int src2[] = { 32, 32, 64, 32 };
        final int dst2[] = { 32, 32, 64, 48 };
        doDraw(canvas, paint, src2, dst2, 2);
        canvas.restore();

        canvas.save();
        canvas.translate(SkIntToScalar(10), SkIntToScalar(110));
        // rotate/skew (3 points)
        final int src3[] = { 0, 0, 64, 0, 0, 64 };
        final int dst3[] = { 0, 0, 96, 0, 24, 64 };
        doDraw(canvas, paint, src3, dst3, 3);
        canvas.restore();

        canvas.save();
        canvas.translate(SkIntToScalar(160), SkIntToScalar(110));
        // perspective (4 points)
        final int src4[] = { 0, 0, 64, 0, 64, 64, 0, 64 };
        final int dst4[] = { 0, 0, 96, 0, 64, 96, 0, 64 };
        doDraw(canvas, paint, src4, dst4, 4);
        canvas.restore();
    }

    public static GMRegistry.Factory factory = new GMRegistry.Factory() {
        public GM apply() {
            return new Poly2PolyGM();
        }
    };
}
