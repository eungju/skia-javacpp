package skia.javacpp.gm;

import static skia.javacpp.core.*;

public class StrokeRectGM extends GM {
    static final int W = 400;
    static final int H = 400;
    static final int N = 100;

    static final float SW = SkIntToScalar(W);
    static final float SH = SkIntToScalar(H);

    @Override
    protected String onShortName() {
        return "strokerects";
    }

    @Override
    protected SkISize onISize() {
        return make_isize(W*2, H*2);
    }

    static void rnd_rect(SkRect r, SkRandom rand) {
        float x = rand.nextUScalar1() * W;
        float y = rand.nextUScalar1() * H;
        float w = rand.nextUScalar1() * (W >>> 2);
        float h = rand.nextUScalar1() * (H >>> 2);
        float hoffset = rand.nextSScalar1();
        float woffset = rand.nextSScalar1();

        r.set(x, y, x + w, y + h);
        r.offset(-w/2 + woffset, -h/2 + hoffset);
    }

    @Override
    protected void onDraw(SkCanvas canvas) {
        SkPaint paint = new SkPaint();
        paint.setStyle(SkPaint.kStroke_Style);

        for (int y = 0; y < 2; y++) {
            paint.setAntiAlias(y != 0);
            for (int x = 0; x < 2; x++) {
                paint.setStrokeWidth(x * SkIntToScalar(3));

                int saveCount = canvas.getSaveCount();
                canvas.save();
                try {
                    canvas.translate(SW * x, SH * y);
                    canvas.clipRect(SkRect.MakeLTRB(SkIntToScalar(2), SkIntToScalar(2), SW - SkIntToScalar(2), SH - SkIntToScalar(2)));

                    SkRandom rand = new SkRandom();
                    for (int i = 0; i < N; i++) {
                        SkRect r = new SkRect();
                        rnd_rect(r, rand);
                        canvas.drawRect(r, paint);
                    }
                } finally {
                    canvas.restoreToCount(saveCount);
                }
            }
        }
    }
}
