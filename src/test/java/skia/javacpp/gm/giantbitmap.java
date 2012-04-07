package skia.javacpp.gm;

import static skia.javacpp.core.*;

public class giantbitmap {
    static final int W = 257;
    static final int H = 161;

    public static class GiantBitmapGM extends GM {
        private SkBitmap fBM;
        private int fMode;
        private boolean fDoFilter;
        private boolean fDoRotate;

        SkBitmap getBitmap() {
            if (null == fBM) {
                fBM = new SkBitmap();
                fBM.setConfig(SkBitmap.kARGB_8888_Config, W, H);
                fBM.allocPixels();
                fBM.eraseColor(SK_ColorWHITE);

                final int[] colors = {
                        SK_ColorBLUE, SK_ColorRED, SK_ColorBLACK, SK_ColorGREEN
                };

                SkCanvas canvas = new SkCanvas(fBM);
                canvas.autoUnref();
                SkPaint paint = new SkPaint();
                paint.setAntiAlias(true);
                paint.setStrokeWidth(SkIntToScalar(20));

                if (false) {
                for (int y = -H*2; y < H; y += 50) {
                    float yy = SkIntToScalar(y);
                    paint.setColor(colors[y/50 & 0x3]);
                    canvas.drawLine(0, yy, SkIntToScalar(W), yy + SkIntToScalar(W),
                            paint);
                }
                } else {
                for (int x = -W; x < W; x += 60) {
                    paint.setColor(colors[x/60 & 0x3]);

                    float xx = SkIntToScalar(x);
                    canvas.drawLine(xx, 0, xx, SkIntToScalar(H),
                            paint);
                }
                }
            }
            return fBM;
        }

        public GiantBitmapGM(int mode, boolean doFilter, boolean doRotate) {
            fBM = null;
            fMode = mode;
            fDoFilter = doFilter;
            fDoRotate = doRotate;
        }

        @Override
        protected String onShortName() {
            String str = "giantbitmap_";
            switch (fMode) {
                case SkShader.kClamp_TileMode:
                    str += "clamp";
                    break;
                case SkShader.kRepeat_TileMode:
                    str += "repeat";
                    break;
                case SkShader.kMirror_TileMode:
                    str += "mirror";
                    break;
                default:
                    break;
            }
            str += fDoFilter ? "_bilerp" : "_point";
            str += fDoRotate ? "_rotate" : "_scale";
            return str;
        }

        @Override
        protected SkISize onISize() { return SkISize.Make(640, 480); }

        @Override
        protected void onDraw(SkCanvas canvas) {
            SkPaint paint = new SkPaint();
            SkShader s = SkShader.CreateBitmapShader(getBitmap(), fMode, fMode);

            SkMatrix m = new SkMatrix();
            if (fDoRotate) {
    //            m.setRotate(SkIntToScalar(30), 0, 0);
                m.setSkew(SK_Scalar1, 0, 0, 0);
    //            m.postScale(2*SK_Scalar1/3, 2*SK_Scalar1/3);
            } else {
                float scale = 11*SK_Scalar1/12;
                m.setScale(scale, scale);
            }
            s.setLocalMatrix(m);

            paint.setShader(s).unref();
            paint.setFilterBitmap(fDoFilter);

            canvas.translate(SkIntToScalar(50), SkIntToScalar(50));

            SkRect r = SkRect.MakeXYWH(-50, -50, 32, 16);
    //        canvas->drawRect(r, paint); return;
            canvas.drawPaint(paint);
        }
    }

    public static GMRegistry.Factory G000 = new GMRegistry.Factory() {
        public GM apply() {
            return new GiantBitmapGM(SkShader.kClamp_TileMode, false, false);
        }
    };
    public static GMRegistry.Factory G100 = new GMRegistry.Factory() {
        public GM apply() {
            return new GiantBitmapGM(SkShader.kRepeat_TileMode, false, false);
        }
    };
    public static GMRegistry.Factory G200 = new GMRegistry.Factory() {
        public GM apply() {
            return new GiantBitmapGM(SkShader.kMirror_TileMode, false, false);
        }
    };
    public static GMRegistry.Factory G010 = new GMRegistry.Factory() {
        public GM apply() {
            return new GiantBitmapGM(SkShader.kClamp_TileMode, true, false);
        }
    };
    public static GMRegistry.Factory G110 = new GMRegistry.Factory() {
        public GM apply() {
            return new GiantBitmapGM(SkShader.kRepeat_TileMode, true, false);
        }
    };
    public static GMRegistry.Factory G210 = new GMRegistry.Factory() {
        public GM apply() {
            return new GiantBitmapGM(SkShader.kMirror_TileMode, true, false);
        }
    };

    public static GMRegistry.Factory G001 = new GMRegistry.Factory() {
        public GM apply() {
            return new GiantBitmapGM(SkShader.kClamp_TileMode, false, true);
        }
    };
    public static GMRegistry.Factory G101 = new GMRegistry.Factory() {
        public GM apply() {
            return new GiantBitmapGM(SkShader.kRepeat_TileMode, false, true);
        }
    };
    public static GMRegistry.Factory G201 = new GMRegistry.Factory() {
        public GM apply() {
            return new GiantBitmapGM(SkShader.kMirror_TileMode, false, true);
        }
    };
    public static GMRegistry.Factory G011 = new GMRegistry.Factory() {
        public GM apply() {
            return new GiantBitmapGM(SkShader.kClamp_TileMode, true, true);
        }
    };
    public static GMRegistry.Factory G111 = new GMRegistry.Factory() {
        public GM apply() {
            return new GiantBitmapGM(SkShader.kRepeat_TileMode, true, true);
        }
    };
    public static GMRegistry.Factory G211 = new GMRegistry.Factory() {
        public GM apply() {
            return new GiantBitmapGM(SkShader.kMirror_TileMode, true, true);
        }
    };
}
