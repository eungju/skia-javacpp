package skia.javacpp.gm;

import static skia.javacpp.core.*;
import static skia.javacpp.effects.*;

public class PathEffectGM extends GM {
    static void compose_pe(SkPaint paint) {
        SkPathEffect pe = paint.getPathEffect();
        SkPathEffect corner = new SkCornerPathEffect(25);
        SkPathEffect compose;
        if (pe != null) {
            compose = new SkComposePathEffect(pe, corner);
            corner.unref();
        } else {
            compose = corner;
        }
        paint.setPathEffect(compose).unref();
    }

    static PE_Proc hair_pe = new PE_Proc() {
        public void apply(SkPaint paint) {
            paint.setStrokeWidth(0);
        }
    };

    static PE_Proc hair2_pe = new PE_Proc() {
        public void apply(SkPaint paint) {
            paint.setStrokeWidth(0);
            compose_pe(paint);
        }
    };

    static PE_Proc stroke_pe = new PE_Proc() {
        public void apply(SkPaint paint) {
            paint.setStrokeWidth(12);
            compose_pe(paint);
        }
    };

    static PE_Proc dash_pe = new PE_Proc() {
        public void apply(SkPaint paint) {
            float[] inter = { 20, 10, 10, 10 };
            paint.setStrokeWidth(12);
            paint.setPathEffect(new SkDashPathEffect(inter, 0)).unref();
            compose_pe(paint);
        }
    };

    static final int[] gXY = {
            4, 0, 0, -4, 8, -4, 12, 0, 8, 4, 0, 4
    };

    static void scale(SkPath path, float scale) {
        SkMatrix m = new SkMatrix();
        m.setScale(scale, scale);
        path.transform(m);
    }

    static PE_Proc one_d_pe = new PE_Proc() {
        public void apply(SkPaint paint) {
            SkPath  path = new SkPath();
            path.moveTo(SkIntToScalar(gXY[0]), SkIntToScalar(gXY[1]));
            for (int i = 2; i < gXY.length; i += 2)
                path.lineTo(SkIntToScalar(gXY[i]), SkIntToScalar(gXY[i+1]));
            path.close();
            path.offset(SkIntToScalar(-6), 0);
            scale(path, 1.5f);

            paint.setPathEffect(new SkPath1DPathEffect(path, SkIntToScalar(21), 0,
                    SkPath1DPathEffect.kRotate_Style)).unref();
            compose_pe(paint);
        }
    };

    interface PE_Proc {
        public void apply(SkPaint paint);
    }
    static final PE_Proc[] gPE = { hair_pe, hair2_pe, stroke_pe, dash_pe, one_d_pe };

    static PE_Proc fill_pe = new PE_Proc() {
        public void apply(SkPaint paint) {
            paint.setStyle(SkPaint.kFill_Style);
            paint.setPathEffect(null);
        }
    };

    static PE_Proc discrete_pe = new PE_Proc() {
        public void apply(SkPaint paint) {
            paint.setPathEffect(new SkDiscretePathEffect(10, 4)).unref();
        }
    };

    static SkPathEffect MakeTileEffect() {
        SkMatrix m = new SkMatrix();
        m.setScale(SkIntToScalar(12), SkIntToScalar(12));

        SkPath path = new SkPath();
        path.addCircle(0, 0, SkIntToScalar(5));

        return new SkPath2DPathEffect(m, path);
    }

    static PE_Proc tile_pe = new PE_Proc() {
        public void apply(SkPaint paint) {
            paint.setPathEffect(MakeTileEffect()).unref();
        }
    };

    static final PE_Proc[] gPE2 = { fill_pe, discrete_pe, tile_pe };

    @Override
    protected String onShortName() {
        return "patheffect";
    }

    @Override
    protected SkISize onISize() { return make_isize(800, 600); }

    @Override
    protected void onDraw(SkCanvas canvas) {
        SkPaint paint = new SkPaint();
        paint.setAntiAlias(true);
        paint.setStyle(SkPaint.kStroke_Style);

        SkPath path = new SkPath();
        path.moveTo(20, 20);
        path.lineTo(70, 120);
        path.lineTo(120, 30);
        path.lineTo(170, 80);
        path.lineTo(240, 50);

        int i;
        canvas.save();
        for (i = 0; i < gPE.length; i++) {
            gPE[i].apply(paint);
            canvas.drawPath(path, paint);
            canvas.translate(0, 75);
        }
        canvas.restore();

        path.reset();
        SkRect r = SkRect.MakeLTRB(0, 0, 250, 120);
        path.addOval(r, SkPath.kCW_Direction);
        r.inset(50, 50);
        path.addRect(r, SkPath.kCCW_Direction);

        canvas.translate(320, 20);
        for (i = 0; i < gPE2.length; i++) {
            gPE2[i].apply(paint);
            canvas.drawPath(path, paint);
            canvas.translate(0, 160);
        }

        SkIRect rect = SkIRect.MakeXYWH(20, 20, 60, 60);
        for (i = 0; i < gPE.length; i++) {
            SkPaint p = new SkPaint();
            p.setAntiAlias(true);
            p.setStyle(SkPaint.kFill_Style);
            gPE[i].apply(p);
            canvas.drawIRect(rect, p);
            canvas.translate(75, 0);
        }
    }
}
