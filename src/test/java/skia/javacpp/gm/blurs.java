package skia.javacpp.gm;

import static skia.javacpp.core.*;
import static skia.javacpp.effects.*;

public class blurs {
    public static class BlursGM extends GM {
        public BlursGM() {
            this.setBGColor(0xFFDDDDDD);
        }

        @Override
        protected String onShortName() {
            return "blurs";
        }

        @Override
        protected SkISize onISize() {
            return make_isize(700, 500);
        }

        @Override
        protected void onDraw(SkCanvas canvas) {
            int NONE = -999;
            class Rec {
                int fStyle;
                int                         fCx, fCy;
                public Rec(int style, int cx, int cy) {
                    fStyle = style;
                    fCx = cx;
                    fCy = cy;
                }
            }
            final Rec[] gRecs = {
                new Rec(NONE,                                 0,  0),
                new Rec(SkBlurMaskFilter.kInner_BlurStyle,  -1,  0),
                new Rec(SkBlurMaskFilter.kNormal_BlurStyle,  0,  1),
                new Rec(SkBlurMaskFilter.kSolid_BlurStyle,   0, -1),
                new Rec(SkBlurMaskFilter.kOuter_BlurStyle,   1,  0),
            };

            SkPaint paint = new SkPaint();
            paint.setAntiAlias(true);
            paint.setTextSize(SkIntToScalar(25));
            canvas.translate(SkIntToScalar(-40), SkIntToScalar(0));

            int flags = SkBlurMaskFilter.kNone_BlurFlag;
            for (int j = 0; j < 2; j++) {
                canvas.save();
                paint.setColor(SK_ColorBLUE);
                for (int i = 0; i < gRecs.length; i++) {
                    if (gRecs[i].fStyle != NONE) {
                        SkMaskFilter mf = SkBlurMaskFilter.Create(
                                SkIntToScalar(20), gRecs[i].fStyle, flags
                        );
                        paint.setMaskFilter(mf).unref();
                    } else {
                        paint.setMaskFilter(null);
                    }
                    canvas.drawCircle(SkIntToScalar(200 + gRecs[i].fCx*100)
                            , SkIntToScalar(200 + gRecs[i].fCy*100)
                            , SkIntToScalar(50)
                            , paint);
                }
                // draw text
                {
                    SkMaskFilter mf = SkBlurMaskFilter.Create(
                            SkIntToScalar(4)
                            , SkBlurMaskFilter.kNormal_BlurStyle
                            , flags
                    );
                    paint.setMaskFilter(mf).unref();
                    float x = SkIntToScalar(70);
                    float y = SkIntToScalar(400);
                    paint.setColor(SK_ColorBLACK);
                    canvas.drawText("Hamburgefons Style", x, y, paint);
                    canvas.drawText("Hamburgefons Style", x, y + SkIntToScalar(50), paint);
                    paint.setMaskFilter(null);
                    paint.setColor(SK_ColorWHITE);
                    x -= SkIntToScalar(2);
                    y -= SkIntToScalar(2);
                    canvas.drawText("Hamburgefons Style", x, y, paint);
                }
                canvas.restore();
                flags = SkBlurMaskFilter.kHighQuality_BlurFlag;
                canvas.translate(SkIntToScalar(350), SkIntToScalar(0));
            }
        }
    }

    public static GMRegistry.Factory MyFactory = new GMRegistry.Factory() {
        public GM apply() {
            return new BlursGM();
        }
    };
}
