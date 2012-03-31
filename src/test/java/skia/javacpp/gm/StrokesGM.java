package skia.javacpp.gm;

import static skia.javacpp.core.*;

public class StrokesGM extends GM {
    static final int W = 400;
    static final int H = 400;
    static final int N = 50;

    static final float SW = SkIntToScalar(W);
    static final float SH = SkIntToScalar(H);

    static void rnd_rect(SkRect r, SkPaint paint, SkRandom rand) {
        float x = rand.nextUScalar1() * W;
        float y = rand.nextUScalar1() * H;
        float w = rand.nextUScalar1() * (W >>> 2);
        float h = rand.nextUScalar1() * (H >>> 2);
        float hoffset = rand.nextSScalar1();
        float woffset = rand.nextSScalar1();

        r.set(x, y, x + w, y + h);
        r.offset(-w/2 + woffset, -h/2 + hoffset);

        paint.setColor(rand.nextU());
        paint.setAlpha(0xFF);
    }

    @Override
    protected String onShortName() {
        return "strokes_round";
    }

    @Override
    protected SkISize onISize() {
        return make_isize(W, H*2);
    }

    @Override
    protected void onDraw(SkCanvas canvas) {
        SkPaint paint = new SkPaint();
        paint.setStyle(SkPaint.kStroke_Style);
        paint.setStrokeWidth(SkIntToScalar(9)/2);

        for (int y = 0; y < 2; y++) {
            paint.setAntiAlias(y != 0);
            int saveCount = canvas.getSaveCount();
            canvas.save();
            try {
                canvas.translate(0, SH * y);
                canvas.clipRect(SkRect.MakeLTRB(
                        SkIntToScalar(2), SkIntToScalar(2)
                        , SW - SkIntToScalar(2), SH - SkIntToScalar(2)
                ));

                SkRandom rand = new SkRandom();
                for (int i = 0; i < N; i++) {
                    SkRect r = new SkRect();
                    rnd_rect(r, paint, rand);
                    canvas.drawOval(r, paint);
                    rnd_rect(r, paint, rand);
                    canvas.drawRoundRect(r, r.width()/4, r.height()/4, paint);
                    rnd_rect(r, paint, rand);
                }
            } finally {
                canvas.restoreToCount(saveCount);
            }
        }
    }
}
