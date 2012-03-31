package skia.javacpp.gm;

import com.googlecode.javacpp.IntPointer;

import static skia.javacpp.core.*;

public class NoColorBleedGM extends GM {
    public NoColorBleedGM() {
        this.setBGColor(0xFFDDDDDD);
    }

    @Override
    protected String onShortName() {
        return "nocolorbleed";
    }

    @Override
    protected SkISize onISize() {
        return make_isize(200, 200);
    }

    @Override
    protected void onDraw(SkCanvas canvas) {
        SkBitmap sprite = new SkBitmap();
        sprite.setConfig(SkBitmap.kARGB_8888_Config, 4, 4, 4 * (Integer.SIZE / 8));
        int[] spriteData = {
            SK_ColorBLACK,  SK_ColorCYAN,    SK_ColorMAGENTA, SK_ColorYELLOW,
                    SK_ColorBLACK,  SK_ColorWHITE,   SK_ColorBLACK,   SK_ColorRED,
                    SK_ColorGREEN,  SK_ColorBLACK,   SK_ColorWHITE,   SK_ColorBLUE,
                    SK_ColorYELLOW, SK_ColorMAGENTA, SK_ColorCYAN,    SK_ColorBLACK
        };
        sprite.allocPixels();
        sprite.lockPixels();
        IntPointer addr = sprite.getAddr32(0, 0);
        for (int i = 0; i < spriteData.length; ++i) {
            addr.put(i, SkPreMultiplyColor(spriteData[i]));
        }
        sprite.unlockPixels();

        // We draw a magnified subrect of the sprite
        // sample interpolation may cause color bleeding around edges
        // the subrect is a pure white area
        SkIRect srcRect = new SkIRect();
        SkRect dstRect = new SkRect();
        SkPaint paint = new SkPaint();
        paint.setFilterBitmap(true);
        //First row : full texture with and without filtering
        srcRect.setXYWH(0, 0, 4, 4);
        dstRect.setXYWH(SkIntToScalar(0), SkIntToScalar(0)
                , SkIntToScalar(100), SkIntToScalar(100));
        canvas.drawBitmapRect(sprite, srcRect, dstRect, paint);
        dstRect.setXYWH(SkIntToScalar(100), SkIntToScalar(0)
                , SkIntToScalar(100), SkIntToScalar(100));
        canvas.drawBitmapRect(sprite, srcRect, dstRect);
        //Second row : sub rect of texture with and without filtering
        srcRect.setXYWH(1, 1, 2, 2);
        dstRect.setXYWH(SkIntToScalar(25), SkIntToScalar(125)
                , SkIntToScalar(50), SkIntToScalar(50));
        canvas.drawBitmapRect(sprite, srcRect, dstRect, paint);
        dstRect.setXYWH(SkIntToScalar(125), SkIntToScalar(125)
                , SkIntToScalar(50), SkIntToScalar(50));
        canvas.drawBitmapRect(sprite, srcRect, dstRect);
    }

    public static GMRegistry.Factory factory = new GMRegistry.Factory() {
        public GM apply() {
            return new NoColorBleedGM();
        }
    };
}
