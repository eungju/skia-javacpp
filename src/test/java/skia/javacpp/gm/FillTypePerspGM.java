package skia.javacpp.gm;

import static skia.javacpp.core.*;
import static skia.javacpp.effects.*;

public class FillTypePerspGM extends GM {
    private SkPath fPath = new SkPath();

    public FillTypePerspGM() {
        final float radius = SkIntToScalar(45);
        fPath.addCircle(SkIntToScalar(50), SkIntToScalar(50), radius);
        fPath.addCircle(SkIntToScalar(100), SkIntToScalar(100), radius);
    }

    @Override
    protected String onShortName() {
        return "filltypespersp";
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

    void showFour(SkCanvas canvas, float scale, boolean aa) {

        SkPaint paint = new SkPaint();
        SkPoint center = SkPoint.Make(SkIntToScalar(100), SkIntToScalar(100));
        int[] colors = {SK_ColorBLUE, SK_ColorRED, SK_ColorGREEN};
        float[] pos = {0, SK_ScalarHalf, SK_Scalar1};
        SkShader s = SkGradientShader.CreateRadial(center,
                SkIntToScalar(100),
                colors,
                pos,
                SkShader.kClamp_TileMode);
        paint.setShader(s).unref();
        paint.setAntiAlias(aa);

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
        // do perspective drawPaint as the background;
        SkPaint bkgnrd = new SkPaint();
        SkPoint center = SkPoint.Make(SkIntToScalar(100),
                SkIntToScalar(100));
        int[] colors = {SK_ColorBLACK, SK_ColorCYAN,
                SK_ColorYELLOW, SK_ColorWHITE};
        float[] pos = {0, SK_ScalarHalf / 2,
                3 * SK_ScalarHalf / 2, SK_Scalar1};
        SkShader s = SkGradientShader.CreateRadial(center,
                SkIntToScalar(1000),
                colors,
                pos,
                SkShader.kClamp_TileMode);
        bkgnrd.setShader(s).unref();
        canvas.save();
        canvas.translate(SkIntToScalar(100), SkIntToScalar(100));
        SkMatrix mat = new SkMatrix();
        mat.reset();
        mat.setPerspY(SkScalarToPersp(SK_Scalar1 / 1000));
        canvas.concat(mat);
        canvas.drawPaint(bkgnrd);
        canvas.restore();

        // draw the paths in perspective
        SkMatrix persp = new SkMatrix();
        persp.reset();
        persp.setPerspX(SkScalarToPersp(-SK_Scalar1 / 1800));
        persp.setPerspY(SkScalarToPersp(SK_Scalar1 / 500));
        canvas.concat(persp);

        canvas.translate(SkIntToScalar(20), SkIntToScalar(20));
        final float scale = SkIntToScalar(5)/4;

        showFour(canvas, SK_Scalar1, false);
        canvas.translate(SkIntToScalar(450), 0);
        showFour(canvas, scale, false);

        canvas.translate(SkIntToScalar(-450), SkIntToScalar(450));
        showFour(canvas, SK_Scalar1, true);
        canvas.translate(SkIntToScalar(450), 0);
        showFour(canvas, scale, true);
    }
}
