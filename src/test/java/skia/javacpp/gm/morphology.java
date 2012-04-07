package skia.javacpp.gm;

import static skia.javacpp.core.*;
import static skia.javacpp.effects.*;

public class morphology {
    static final int WIDTH = 640;
    static final int HEIGHT = 480;

    public static class MorphologyGM extends GM {
        public MorphologyGM() {
            this.setBGColor(0xFF000000);
            fOnce = false;
        }

        @Override
        protected String onShortName() {
            return "morphology";
        }

        void make_bitmap() {
            fBitmap.setConfig(SkBitmap.kARGB_8888_Config, 135, 135);
            fBitmap.allocPixels();
            SkDevice device = new SkDevice(fBitmap);
            device.autoUnref();
            SkCanvas canvas = new SkCanvas(device);
            canvas.autoUnref();
            canvas.clear(0x0);
            SkPaint paint = new SkPaint();
            paint.setAntiAlias(true);
            String str1 = "ABC";
            String str2 = "XYZ";
            paint.setColor(0xFFFFFFFF);
            paint.setTextSize(64);
            canvas.drawText(str1, 10, 55, paint);
            canvas.drawText(str2, 10, 110, paint);
        }

        @Override
        protected SkISize onISize() {
            return make_isize(WIDTH, HEIGHT);
        }

        @Override
        protected void onDraw(SkCanvas canvas) {
            if (!fOnce) {
                make_bitmap();
                fOnce = true;
            }
            class Sample {
                int fRadiusX, fRadiusY;
                boolean fErode;
                float fX, fY;
                public Sample(int radiusX, int radiusY, boolean erode, float x, float y) {
                    fRadiusX = radiusX;
                    fRadiusY = radiusY;
                    fErode = erode;
                    fX = x;
                    fY = y;
                }
            }

            Sample[] samples = {
                new Sample(0, 0, false, 0,   0),
                new Sample(0, 2, false, 140, 0),
                new Sample(2, 0, false, 280, 0),
                new Sample(2, 2, false, 420, 0),
                new Sample(0, 0, true,  0,   140),
                new Sample(0, 2, true,  140, 140),
                new Sample(2, 0, true,  280, 140),
                new Sample(2, 2, true,  420, 140),
            };
            String str = "The quick brown fox jumped over the lazy dog.";
            SkPaint paint = new SkPaint();
            for (int i = 0; i < samples.length; ++i) {
                if (samples[i].fErode) {
                    paint.setImageFilter(new SkErodeImageFilter(
                    samples[i].fRadiusX,
                    samples[i].fRadiusY)).unref();
                } else {
                    paint.setImageFilter(new SkDilateImageFilter(
                    samples[i].fRadiusX,
                    samples[i].fRadiusY)).unref();
                }
                SkRect bounds = SkRect.MakeXYWH(samples[i].fX,
                        samples[i].fY,
                        140, 140);
                canvas.saveLayer(bounds, paint);
                canvas.drawBitmap(fBitmap, samples[i].fX, samples[i].fY);
                canvas.restore();
            }
        }

        private SkBitmap fBitmap = new SkBitmap();
        private boolean fOnce;
    }

    public static GMRegistry.Factory MyFactory = new GMRegistry.Factory() {
        public GM apply() {
            return new MorphologyGM();
        }
    };
}
