package skia.javacpp.gm;

import static skia.javacpp.core.*;

public class FillTypeGM extends GM {
    private SkPath fPath = new SkPath();
    public FillTypeGM() {
        this.setBGColor(0xFFDDDDDD);
        final float radius = SkIntToScalar(45);
        fPath.addCircle(SkIntToScalar(50), SkIntToScalar(50), radius);
        fPath.addCircle(SkIntToScalar(100), SkIntToScalar(100), radius);
    }

    @Override
    protected String onShortName() {
        return "filltypes";
    }

    @Override
    protected SkISize onISize() {
        return make_isize(835, 840);
    }

    void showPath(SkCanvas canvas, int x, int y, int ft,
                  float scale, SkPaint paint) {

        final SkRect r = SkRect.MakeLTRB(0, 0, SkIntToScalar(150), SkIntToScalar(150));

        canvas.save();
        canvas.translate(SkIntToScalar(x), SkIntToScalar(y));
        canvas.clipRect(r);
        canvas.drawColor(SK_ColorWHITE);
        fPath.setFillType(ft);
        canvas.translate(r.centerX(), r.centerY());
        canvas.scale(scale, scale);
        canvas.translate(-r.centerX(), -r.centerY());
        canvas.drawPath(fPath, paint);
        canvas.restore();
    }

    void showFour(SkCanvas canvas, float scale, SkPaint paint) {
        showPath(canvas,   0,   0, SkPath.kWinding_FillType,
                scale, paint);
        showPath(canvas, 200,   0, SkPath.kEvenOdd_FillType,
                scale, paint);
        showPath(canvas,  00, 200, SkPath.kInverseWinding_FillType,
                scale, paint);
        showPath(canvas, 200, 200, SkPath.kInverseEvenOdd_FillType,
                scale, paint);
    }

    @Override
    protected void onDraw(SkCanvas canvas) {
        canvas.translate(SkIntToScalar(20), SkIntToScalar(20));

        SkPaint paint = new SkPaint();
        final float scale = SkIntToScalar(5)/4;

        paint.setAntiAlias(false);

        showFour(canvas, SK_Scalar1, paint);
        canvas.translate(SkIntToScalar(450), 0);
        showFour(canvas, scale, paint);

        paint.setAntiAlias(true);

        canvas.translate(SkIntToScalar(-450), SkIntToScalar(450));
        showFour(canvas, SK_Scalar1, paint);
        canvas.translate(SkIntToScalar(450), 0);
        showFour(canvas, scale, paint);
    }

    public static GMRegistry.Factory factory = new GMRegistry.Factory() {
        public GM apply() {
            return new FillTypeGM();
        }
    };
}
