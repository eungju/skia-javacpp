package skia.javacpp.gm;

import static skia.javacpp.core.*;
import static skia.javacpp.effects.*;

public class GradTextGM extends GM {
    static SkShader make_grad(float width) {
        int colors[] = { SK_ColorRED, 0x0000FF00, SK_ColorBLUE };
        SkPoint pts[] = { SkPoint.Make(0, 0), SkPoint.Make(width, 0) };
        return SkGradientShader.CreateLinear(pts, colors, null, SkShader.kMirror_TileMode);
    }

    static SkShader make_grad2(float width) {
        int colors[] = { SK_ColorRED, SK_ColorGREEN, SK_ColorBLUE };
        SkPoint pts[] = { SkPoint.Make(0, 0), SkPoint.Make(width, 0) };
        return SkGradientShader.CreateLinear(pts, colors, null, SkShader.kMirror_TileMode);
    }

    @Override
    protected String onShortName() {
        return "gradtext";
    }

    @Override
    protected SkISize onISize() { return make_isize(500, 480); }

    static void draw_text(SkCanvas canvas, SkPaint paint) {
        String text = "When in the course of human events";
        canvas.drawText(text, 0, 0, paint);
    }

    static void draw_text3(SkCanvas canvas, SkPaint paint) {
        SkPaint p = new SkPaint(paint);

        p.setAntiAlias(false);
        draw_text(canvas, p);
        p.setAntiAlias(true);
        canvas.translate(0, paint.getTextSize() * 4/3);
        draw_text(canvas, p);
        p.setLCDRenderText(true);
        canvas.translate(0, paint.getTextSize() * 4/3);
        draw_text(canvas, p);
    }

    @Override
    protected void onDraw(SkCanvas canvas) {
        SkPaint paint = new SkPaint();
        paint.setTextSize(SkIntToScalar(26));

        SkISize size = this.getISize();
        SkRect r = SkRect.MakeWH(SkIntToScalar(size.width()),
                SkIntToScalar(size.height()) / 2);
        canvas.drawRect(r, paint);

        canvas.translate(SkIntToScalar(20), paint.getTextSize());

        for (int i = 0; i < 2; ++i) {
            paint.setShader(make_grad(SkIntToScalar(80))).unref();
            draw_text3(canvas, paint);

            canvas.translate(0, paint.getTextSize() * 2);

            paint.setShader(make_grad2(SkIntToScalar(80))).unref();
            draw_text3(canvas, paint);

            canvas.translate(0, paint.getTextSize() * 2);
        }
    }

    public static GMRegistry.Factory factory = new GMRegistry.Factory() {
        public GM apply() {
            return new GradTextGM();
        }
    };
}
