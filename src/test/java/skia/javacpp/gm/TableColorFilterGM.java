package skia.javacpp.gm;

import static skia.javacpp.core.*;
import static skia.javacpp.effects.*;

public class TableColorFilterGM extends GM {
    static void make_bm0(SkBitmap bm) {
        int W = 120;
        int H = 120;
        bm.setConfig(SkBitmap.kARGB_8888_Config, W, H);
        bm.allocPixels();
        bm.eraseColor(0);

        SkCanvas canvas = new SkCanvas(bm);
        SkPaint paint = new SkPaint();
        SkPoint pts[] = { SkPoint.Make(0, 0), SkPoint.Make(SkIntToScalar(W), SkIntToScalar(H)) };
        int[] colors = {
                SK_ColorBLACK, SK_ColorGREEN, SK_ColorCYAN,
                SK_ColorRED, 0, SK_ColorBLUE, SK_ColorWHITE
        };
        SkShader s = SkGradientShader.CreateLinear(pts, colors, null, SkShader.kClamp_TileMode);
        paint.setShader(s).unref();
        canvas.drawPaint(paint);
        canvas.unref();
    }
    
    static void make_bm1(SkBitmap bm) {
        int W = 120;
        int H = 120;
        bm.setConfig(SkBitmap.kARGB_8888_Config, W, H);
        bm.allocPixels();
        bm.eraseColor(0);

        SkCanvas canvas = new SkCanvas(bm);
        SkPaint paint = new SkPaint();
        float cx = SkIntToScalar(W)/2;
        float cy = SkIntToScalar(H)/2;
        int[] colors = {
                SK_ColorRED, SK_ColorGREEN, SK_ColorBLUE,
        };
        SkShader s = SkGradientShader.CreateRadial(SkPoint.Make(SkIntToScalar(W)/2,
                SkIntToScalar(H)/2),
                SkIntToScalar(W)/2, colors, null,
                SkShader.kClamp_TileMode);
        paint.setShader(s).unref();
        paint.setAntiAlias(true);
        canvas.drawCircle(cx, cy, cx, paint);
        canvas.unref();
    }

    static void make_table0(byte[] table) {
        for (int i = 0; i < 256; ++i) {
            int n = i >>> 5;
            table[i] = (byte)((n << 5) | (n << 2) | (n >>> 1));
        }
    }
    static void make_table1(byte[] table) {
        for (int i = 0; i < 256; ++i) {
            table[i] = (byte)(i * i / 255);
        }
    }
    static void make_table2(byte[] table) {
        for (int i = 0; i < 256; ++i) {
            float fi = i / 255.0f;
            table[i] = (byte)(Math.sqrt(fi) * 255);
        }
    }

    static SkColorFilter make_cf0() {
        byte[] table = new byte[256]; make_table0(table);
        return SkTableColorFilter.Create(table);
    }
    static SkColorFilter make_cf1() {
        byte[] table = new byte[256]; make_table1(table);
        return SkTableColorFilter.Create(table);
    }
    static SkColorFilter make_cf2() {
        byte[] table = new byte[256]; make_table2(table);
        return SkTableColorFilter.Create(table);
    }
    static SkColorFilter make_cf3() {
        byte[] table0 = new byte[256]; make_table0(table0);
        byte[] table1 = new byte[256]; make_table1(table1);
        byte[] table2 = new byte[256]; make_table2(table2);
        return SkTableColorFilter.CreateARGB(null, table0, table1, table2);
    }

    @Override
    protected String onShortName() {
        return "tablecolorfilter";
    }

    @Override
    protected SkISize onISize() {
        return SkISize.Make(700, 300);
    }

    @Override
    protected void onDraw(SkCanvas canvas) {
        canvas.drawColor(0xFFDDDDDD);
        canvas.translate(20, 20);

        float x = 0, y = 0;

        SkBitmap[] gMakers = new SkBitmap[] { new SkBitmap(), new SkBitmap() };
        make_bm0(gMakers[0]);
        make_bm1(gMakers[1]);
        for (int maker = 0; maker < gMakers.length; ++maker) {
            SkBitmap bm = gMakers[maker];
            SkPaint paint = new SkPaint();
            x = 0;
            canvas.drawBitmap(bm, x, y, paint);
            paint.setColorFilter(make_cf0()).unref();  x += bm.width() * 9 / 8;
            canvas.drawBitmap(bm, x, y, paint);
            paint.setColorFilter(make_cf1()).unref();  x += bm.width() * 9 / 8;
            canvas.drawBitmap(bm, x, y, paint);
            paint.setColorFilter(make_cf2()).unref();  x += bm.width() * 9 / 8;
            canvas.drawBitmap(bm, x, y, paint);
            paint.setColorFilter(make_cf3()).unref();  x += bm.width() * 9 / 8;
            canvas.drawBitmap(bm, x, y, paint);

            y += bm.height() * 9 / 8;
        }
    }

    public static GMRegistry.Factory factory = new GMRegistry.Factory() {
        public GM apply() {
            return new TableColorFilterGM();
        }
    };
}
