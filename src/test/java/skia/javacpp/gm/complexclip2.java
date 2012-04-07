package skia.javacpp.gm;

import static skia.javacpp.core.*;

public class complexclip2 {
    public static class ComplexClip2GM extends GM {
        public ComplexClip2GM() {
            this.setBGColor(SkColorSetRGB(0xDD,0xA0,0xDD));

            float xA =  0 * SK_Scalar1;
            float xB = 10 * SK_Scalar1;
            float xC = 20 * SK_Scalar1;
            float xD = 30 * SK_Scalar1;
            float xE = 40 * SK_Scalar1;
            float xF = 50 * SK_Scalar1;

            float yA =  0 * SK_Scalar1;
            float yB = 10 * SK_Scalar1;
            float yC = 20 * SK_Scalar1;
            float yD = 30 * SK_Scalar1;
            float yE = 40 * SK_Scalar1;
            float yF = 50 * SK_Scalar1;

            fWidth = xF - xA;
            fHeight = yF - yA;

            fRects[0].set(xB, yB, xE, yE);
            fRectColors[0] = SK_ColorRED;

            fRects[1].set(xA, yA, xD, yD);
            fRectColors[1] = SK_ColorGREEN;

            fRects[2].set(xC, yA, xF, yD);
            fRectColors[2] = SK_ColorBLUE;

            fRects[3].set(xA, yC, xD, yF);
            fRectColors[3] = SK_ColorYELLOW;

            fRects[4].set(xC, yC, xF, yF);
            fRectColors[4] = SK_ColorCYAN;

            fTotalWidth = kCols * fWidth + SK_Scalar1 * (kCols + 1) * kPadX;
            fTotalHeight = kRows * fHeight + SK_Scalar1 * (kRows + 1) * kPadY;

            int[] ops = {
                    SkRegion.kDifference_Op,
                    SkRegion.kIntersect_Op,
                    SkRegion.kUnion_Op,
                    SkRegion.kXOR_Op,
                    SkRegion.kReverseDifference_Op,
                    SkRegion.kReplace_Op,
            };

            SkRandom r = new SkRandom();
            for (int i = 0; i < kRows; ++i) {
                for (int j = 0; j < kCols; ++j) {
                    for (int k = 0; k < 5; ++k) {
                        int x = r.nextU();
                        if (x < 0) {
                            x = (Integer.MAX_VALUE % ops.length) + Math.abs(x);
                            x = ops.length - (x % ops.length) - 1;
                        } else {
                            x = x % ops.length;
                        }
                        fOps[j*kRows+i][k] = ops[x];
                    }
                }
            }
        }

        protected static final int kRows = 5;
        protected static final int kCols = 5;
        protected static final int kPadX = 20;
        protected static final int kPadY = 20;

        @Override
        protected String onShortName() {
            return "complexclip2";
        }

        @Override
        protected SkISize onISize() {
            return make_isize(SkScalarRoundToInt(fTotalWidth),
                    SkScalarRoundToInt(fTotalHeight));
        }

        @Override
        protected void onDraw(SkCanvas canvas) {
            SkPaint rectPaint = new SkPaint();
            rectPaint.setStyle(SkPaint.kStroke_Style);
            rectPaint.setStrokeWidth(-1);

            SkPaint fillPaint = new SkPaint();
            fillPaint.setColor(SkColorSetRGB(0xA0,0xDD,0xA0));

            for (int i = 0; i < kRows; ++i) {
                for (int j = 0; j < kCols; ++j) {
                    canvas.save();
                    canvas.translate(kPadX * SK_Scalar1 + (fWidth + kPadX * SK_Scalar1)*j,
                            kPadY * SK_Scalar1 + (fHeight + kPadY * SK_Scalar1)*i);
                    canvas.save();
                    for (int k = 0; k < 5; ++k) {
                        canvas.clipRect(fRects[k], fOps[j*kRows+i][k]);
                    }
                    canvas.drawRect(SkRect.MakeWH(fWidth, fHeight), fillPaint);
                    canvas.restore();
                    for (int k = 0; k < 5; ++k) {
                        rectPaint.setColor(fRectColors[k]);
                        canvas.drawRect(fRects[k], rectPaint);
                    }
                    canvas.restore();
                }
            }
        }
        private SkRect[] fRects = new SkRect[5];
        {
            for (int i = 0; i < fRects.length; i++) {
                fRects[i] = new SkRect();
            }
        }
        private int[] fRectColors = new int[5];
        private int[][] fOps = new int[kRows * kCols][5];
        private float fWidth;
        private float fHeight;
        private float fTotalWidth;
        private float fTotalHeight;
    }

    public static GMRegistry.Factory MyFactory = new GMRegistry.Factory() {
        public GM apply() {
            return new ComplexClip2GM();
        }
    };
}
