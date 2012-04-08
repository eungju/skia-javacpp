package skia.javacpp.gm;

import com.googlecode.javacpp.IntPointer;

import static skia.javacpp.core.*;

public class tinybitmap {
    static SkBitmap make_bitmap() {
        SkBitmap bm = new SkBitmap();

        SkColorTable ctable = new SkColorTable(1);
        IntPointer c = ctable.lockColors();
        c.put(0, SkPackARGB32(0x80, 0x80, 0, 0));
        ctable.unlockColors(true);

        bm.setConfig(SkBitmap.kIndex8_Config, 1, 1);
        bm.allocPixels(ctable);

        bm.lockPixels();
        bm.getAddr8(0, 0).put((byte) 0);
        bm.unlockPixels();
        return bm;
    }

    public static class TinyBitmapGM extends GM {
        private SkBitmap fBM;
        public TinyBitmapGM() {
            this.setBGColor(0xFFDDDDDD);
            fBM = make_bitmap();
        }

        @Override
        protected String onShortName() {
            return "tinybitmap";
        }

        @Override
        protected SkISize onISize() { return make_isize(100, 100); }

        @Override
        protected void onDraw(SkCanvas canvas) {
            SkShader s =
                    SkShader.CreateBitmapShader(fBM, SkShader.kRepeat_TileMode,
                            SkShader.kMirror_TileMode);
            SkPaint paint = new SkPaint();
            paint.setAlpha(0x80);
            paint.setShader(s);
            canvas.drawPaint(paint);
        }
    }

    public static GMRegistry.Factory MyFactory = new GMRegistry.Factory() {
        public GM apply() {
            return new TinyBitmapGM();
        }
    };
}
