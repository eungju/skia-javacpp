package skia.javacpp.gm;

import static skia.javacpp.core.*;
import static skia.javacpp.effects.*;

public class ColorMatrixGM extends GM {
    static final int WIDTH = 500;
    static final int HEIGHT = 500;

    static class SkOnce {
        public SkOnce() { fOnce = false; };

        public boolean once() {
            if (fOnce) {
                return false;
            }
            fOnce = true;
            return true;
        }

        private boolean fOnce;
    };

    private SkOnce fOnce = new SkOnce();
    private void init() {
        if (fOnce.once()) {
            fBitmap = createBitmap(64, 64);
        }
    }

    public ColorMatrixGM() {
        this.setBGColor(0xFF808080);
    }

    @Override
    protected String onShortName() {
        return "colormatrix";
    }

    @Override
    protected SkISize onISize() {
        return make_isize(WIDTH, HEIGHT);
    }

    SkBitmap createBitmap(int width, int height) {
        SkBitmap bm = new SkBitmap();
        bm.setConfig(SkBitmap.kARGB_8888_Config, width, height);
        bm.allocPixels();
        SkCanvas canvas = new SkCanvas(bm);
        canvas.autoUnref();
        canvas.clear(0x0);
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                SkPaint paint = new SkPaint();
                paint.setColor(SkColorSetARGB(255, x * 255 / width, y * 255 / height, 0));
                canvas.drawRect(SkRect.MakeXYWH(x, y, 1, 1), paint);
            }
        }
        return bm;
    }

    @Override
    protected void onDraw(SkCanvas canvas) {
        this.init();

        SkPaint paint = new SkPaint();
        SkColorMatrix matrix = new SkColorMatrix();
        SkColorMatrixFilter filter = new SkColorMatrixFilter();
        paint.setColorFilter(filter).unref();

        matrix.setIdentity();
        filter.setMatrix(matrix);
        canvas.drawBitmap(fBitmap, 0, 0, paint);

        matrix.setRotate(SkColorMatrix.kR_Axis, 90);
        filter.setMatrix(matrix);
        canvas.drawBitmap(fBitmap, 80, 0, paint);

        matrix.setRotate(SkColorMatrix.kG_Axis, 90);
        filter.setMatrix(matrix);
        canvas.drawBitmap(fBitmap, 160, 0, paint);

        matrix.setRotate(SkColorMatrix.kB_Axis, 90);
        filter.setMatrix(matrix);
        canvas.drawBitmap(fBitmap, 240, 0, paint);

        matrix.setSaturation(SkFloatToScalar(0.0f));
        filter.setMatrix(matrix);
        canvas.drawBitmap(fBitmap, 0, 80, paint);

        matrix.setSaturation(SkFloatToScalar(0.5f));
        filter.setMatrix(matrix);
        canvas.drawBitmap(fBitmap, 80, 80, paint);

        matrix.setSaturation(SkFloatToScalar(1.0f));
        filter.setMatrix(matrix);
        canvas.drawBitmap(fBitmap, 160, 80, paint);

        matrix.setSaturation(SkFloatToScalar(2.0f));
        filter.setMatrix(matrix);
        canvas.drawBitmap(fBitmap, 240, 80, paint);

        matrix.setRGB2YUV();
        filter.setMatrix(matrix);
        canvas.drawBitmap(fBitmap, 0, 160, paint);

        matrix.setYUV2RGB();
        filter.setMatrix(matrix);
        canvas.drawBitmap(fBitmap, 80, 160, paint);

        float s1 = SK_Scalar1;
        float s255 = SkIntToScalar(255);
        // Move red into alpha, set color to white
        float[] data = {
                0,  0, 0, 0, s255,
                0,  0, 0, 0, s255,
                0,  0, 0, 0, s255,
                s1, 0, 0, 0, 0,
                };

        filter.setArray(data);
        canvas.drawBitmap(fBitmap, 160, 160, paint);
    }

    private SkBitmap fBitmap;

    public static GMRegistry.Factory MyFactory = new GMRegistry.Factory() {
        public GM apply() {
            return new ColorMatrixGM();
        }
    };}
