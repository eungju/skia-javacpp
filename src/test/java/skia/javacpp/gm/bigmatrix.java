package skia.javacpp.gm;

import com.googlecode.javacpp.IntPointer;

import static skia.javacpp.core.*;

public class bigmatrix {
    public static class BigMatrixGM extends GM {
        public BigMatrixGM() {
            this.setBGColor(0xFF66AA99);
        }

        @Override
        protected String onShortName() {
            return "bigmatrix";
        }

        @Override
        protected SkISize onISize() {
            return make_isize(50, 50);
        }

        @Override
        protected void onDraw(SkCanvas canvas) {
            SkMatrix m = new SkMatrix();
            m.reset();
            m.setRotate(33 * SK_Scalar1);
            m.postScale(3000 * SK_Scalar1, 3000 * SK_Scalar1);
            m.postTranslate(6000 * SK_Scalar1, -5000 * SK_Scalar1);
            canvas.concat(m);

            SkPaint paint = new SkPaint();
            paint.setColor(SK_ColorRED);
            paint.setAntiAlias(true);

            m.invert(m);

            SkPath path = new SkPath();

            SkPoint pt = SkPoint.Make(10 * SK_Scalar1, 10 * SK_Scalar1);
            float small = 1 / (500 * SK_Scalar1);

            m.mapPoints(pt, 1);
            path.addCircle(pt.fX(), pt.fY(), small);
            canvas.drawPath(path, paint);

            pt.set(30 * SK_Scalar1, 10 * SK_Scalar1);
            m.mapPoints(pt, 1);
            SkRect rect = SkRect.MakeLTRB(pt.fX() - small, pt.fY() - small,
                    pt.fX() + small, pt.fY() + small);
            canvas.drawRect(rect, paint);

            SkBitmap bmp = new SkBitmap();
            bmp.setConfig(SkBitmap.kARGB_8888_Config, 2, 2);
            bmp.allocPixels();
            bmp.lockPixels();
            IntPointer pixels = new IntPointer(bmp.getPixels());
            pixels.put(0, SkPackARGB32(0xFF, 0xFF, 0x00, 0x00));
            pixels.put(1, SkPackARGB32(0xFF, 0x00, 0xFF, 0x00));
            pixels.put(2, SkPackARGB32(0x80, 0x00, 0x00, 0x00));
            pixels.put(3, SkPackARGB32(0xFF, 0x00, 0x00, 0xFF));
            bmp.unlockPixels();
            pt.set(30 * SK_Scalar1, 30 * SK_Scalar1);
            m.mapPoints(pt, 1);
            SkShader shader = SkShader.CreateBitmapShader(
                    bmp,
                    SkShader.kRepeat_TileMode,
                    SkShader.kRepeat_TileMode);
            SkMatrix s = new SkMatrix();
            s.reset();
            s.setScale(SK_Scalar1 / 1000, SK_Scalar1 / 1000);
            shader.setLocalMatrix(s);
            paint.setShader(shader);
            paint.setAntiAlias(false);
            paint.setFilterBitmap(true);
            rect.setLTRB(pt.fX() - small, pt.fY() - small,
                    pt.fX() + small, pt.fY() + small);
            canvas.drawRect(rect, paint);
        }
    }

    public static GMRegistry.Factory MyFactory = new GMRegistry.Factory() {
        public GM apply() {
            return new BigMatrixGM();
        }
    };
}
