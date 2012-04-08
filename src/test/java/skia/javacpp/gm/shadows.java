package skia.javacpp.gm;

import static skia.javacpp.core.*;
import static skia.javacpp.effects.*;

public class shadows {
    static void setup(SkPaint paint, int c, float strokeWidth) {
        paint.setColor(c);
        if (strokeWidth < 0) {
            paint.setStyle(SkPaint.kFill_Style);
        } else {
            paint.setStyle(SkPaint.kStroke_Style);
            paint.setStrokeWidth(strokeWidth);
        }
    }

    public static class ShadowsGM extends GM {
        public SkPath fCirclePath = new SkPath();
        public SkRect fRect = new SkRect();

        public ShadowsGM() {
            this.setBGColor(0xFFDDDDDD);
            fCirclePath.addCircle(SkIntToScalar(20), SkIntToScalar(20), SkIntToScalar(10));
            fRect.set(SkIntToScalar(10), SkIntToScalar(10), SkIntToScalar(30), SkIntToScalar(30));
        }

        @Override
        protected String onShortName() {
            return "shadows";
        }

        @Override
        protected SkISize onISize() {
            return make_isize(200, 80);
        }

        @Override
        protected void onDraw(SkCanvas canvas) {
            SkBlurDrawLooper[] shadowLoopers = new SkBlurDrawLooper[5];
            shadowLoopers[0] = new SkBlurDrawLooper(SkIntToScalar(10), SkIntToScalar(5),
                    SkIntToScalar(10), 0xFF0000FF,
                    SkBlurDrawLooper.kIgnoreTransform_BlurFlag |
                            SkBlurDrawLooper.kOverrideColor_BlurFlag |
                            SkBlurDrawLooper.kHighQuality_BlurFlag);
            shadowLoopers[1] = new SkBlurDrawLooper(SkIntToScalar(10), SkIntToScalar(5),
                    SkIntToScalar(10), 0xFF0000FF,
                    SkBlurDrawLooper.kIgnoreTransform_BlurFlag |
                            SkBlurDrawLooper.kOverrideColor_BlurFlag );
            shadowLoopers[2] = new SkBlurDrawLooper(SkIntToScalar(5), SkIntToScalar(5),
                    SkIntToScalar(10), 0xFF000000,
                    SkBlurDrawLooper.kIgnoreTransform_BlurFlag |
                            SkBlurDrawLooper.kHighQuality_BlurFlag  );
            shadowLoopers[3] = new SkBlurDrawLooper(SkIntToScalar(5), SkIntToScalar(-5),
                    SkIntToScalar(-10), 0x7FFF0000,
                    SkBlurDrawLooper.kIgnoreTransform_BlurFlag |
                            SkBlurDrawLooper.kOverrideColor_BlurFlag |
                            SkBlurDrawLooper.kHighQuality_BlurFlag  );
            shadowLoopers[4] = new SkBlurDrawLooper(SkIntToScalar(0), SkIntToScalar(5),
                    SkIntToScalar(5), 0xFF000000,
                    SkBlurDrawLooper.kIgnoreTransform_BlurFlag |
                            SkBlurDrawLooper.kOverrideColor_BlurFlag |
                            SkBlurDrawLooper.kHighQuality_BlurFlag  );

            class Rec {
                int fColor;
                float fStrokeWidth;
                public Rec(int color, float strokeWidth) {
                    fColor = color;
                    fStrokeWidth = strokeWidth;
                }
            }
            Rec[] gRec = {new Rec(SK_ColorRED, -SK_Scalar1), new Rec(SK_ColorGREEN, SkIntToScalar(4))};

            SkPaint paint = new SkPaint();
            paint.setAntiAlias(true);
            for (int i = 0; i < shadowLoopers.length; ++i) {
                int saveCount = canvas.getSaveCount();
                canvas.save();
                try {
                    paint.setLooper(shadowLoopers[i]);

                    canvas.translate(SkIntToScalar(i*40), SkIntToScalar(0));
                    setup(paint, gRec[0].fColor, gRec[0].fStrokeWidth);
                    canvas.drawRect(fRect, paint);

                    canvas.translate(SkIntToScalar(0), SkIntToScalar(40));
                    setup(paint, gRec[1].fColor, gRec[1].fStrokeWidth);
                    canvas.drawPath(fCirclePath, paint);
                } finally {
                    canvas.restoreToCount(saveCount);
                }
            }
        }
    }

    public static GMRegistry.Factory MyFactory = new GMRegistry.Factory() {
        public GM apply() {
            return new ShadowsGM();
        }
    };
}
