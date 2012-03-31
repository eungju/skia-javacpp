package skia.javacpp.gm;

import static skia.javacpp.core.*;
import static skia.javacpp.gm.StrokesGM.*;

public class Strokes2GM extends GM {
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
