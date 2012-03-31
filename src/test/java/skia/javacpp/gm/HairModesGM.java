package skia.javacpp.gm;

import static skia.javacpp.core.*;

public class HairModesGM extends GM {
    static SkCanvas create_canvas(int w, int h) {
        SkBitmap bm = new SkBitmap();
        bm.setConfig(SkBitmap.kARGB_8888_Config, w, h);
        bm.allocPixels();
        bm.eraseColor(0);
        return new SkCanvas(bm);
    }

    static SkBitmap extract_bitmap(SkCanvas canvas) {
        return canvas.getDevice().accessBitmap(false);
    }

    static class Mode {
        int  fMode;
        String         fLabel;
        public Mode(int mode, String label) {
            fMode = mode;
            fLabel = label;
        }
    }
    static final Mode[] gModes = {
        new Mode(SkXfermode.kClear_Mode,    "Clear"     ),
        new Mode(SkXfermode.kSrc_Mode,      "Src"       ),
        new Mode(SkXfermode.kDst_Mode,      "Dst"       ),
        new Mode(SkXfermode.kSrcOver_Mode,  "SrcOver"   ),
        new Mode(SkXfermode.kDstOver_Mode,  "DstOver"   ),
        new Mode(SkXfermode.kSrcIn_Mode,    "SrcIn"     ),
        new Mode(SkXfermode.kDstIn_Mode,    "DstIn"     ),
        new Mode(SkXfermode.kSrcOut_Mode,   "SrcOut"    ),
        new Mode(SkXfermode.kDstOut_Mode,   "DstOut"    ),
        new Mode(SkXfermode.kSrcATop_Mode,  "SrcATop"   ),
        new Mode(SkXfermode.kDstATop_Mode,  "DstATop"   ),
        new Mode(SkXfermode.kXor_Mode,      "Xor"       ),
    };

    static final int gWidth = 64;
    static final int gHeight = 64;
    static final float W = SkIntToScalar(gWidth);
    static final float H = SkIntToScalar(gHeight);

    static float drawCell(SkCanvas canvas, SkXfermode mode, int a0, int a1) {

        SkPaint paint = new SkPaint();
        paint.setAntiAlias(true);

        SkRect r = SkRect.MakeWH(W, H);
        r.inset(W/10, H/10);

        paint.setColor(SK_ColorBLUE);
        paint.setAlpha(a0);
        canvas.drawOval(r, paint);

        paint.setColor(SK_ColorRED);
        paint.setAlpha(a1);
        paint.setXfermode(mode);
        for (int angle = 0; angle < 24; ++angle) {
            float x = SkScalarCos(SkIntToScalar(angle) * (SK_ScalarPI * 2) / 24) * gWidth;
            float y = SkScalarSin(SkIntToScalar(angle) * (SK_ScalarPI * 2) / 24) * gHeight;
            paint.setStrokeWidth(SK_Scalar1 * angle * 2 / 24);
            canvas.drawLine(W/2, H/2, W/2 + x, H/2 + y, paint);
        }

        return H;
    }

    static SkShader make_bg_shader() {
        SkBitmap bm = new SkBitmap();
        bm.setConfig(SkBitmap.kARGB_8888_Config, 2, 2);
        bm.allocPixels();
        bm.getAddr32(0, 0).put(0xFFFFFFFF);
        bm.getAddr32(1, 1).put(0xFFFFFFFF);
        bm.getAddr32(1, 0).put(SkPackARGB32(0xFF, 0xCC, 0xCC, 0xCC));
        bm.getAddr32(0, 1).put(SkPackARGB32(0xFF, 0xCC, 0xCC, 0xCC));

        SkShader s = SkShader.CreateBitmapShader(bm,
                SkShader.kRepeat_TileMode,
                SkShader.kRepeat_TileMode);

        SkMatrix m = new SkMatrix();
        m.setScale(SkIntToScalar(6), SkIntToScalar(6));
        s.setLocalMatrix(m);
        return s;
    }

    private SkPaint fBGPaint = new SkPaint();
    public HairModesGM() {
        fBGPaint.setShader(make_bg_shader()).unref();
    }

    @Override
    protected String onShortName() {
        return "hairmodes";
    }

    @Override
    protected SkISize onISize() { return make_isize(640, 480); }

    @Override
    protected void onDraw(SkCanvas canvas) {
        final SkRect bounds = SkRect.MakeWH(W, H);
        final int gAlphaValue[] = { 0xFF, 0x88, 0x88 };

        canvas.translate(SkIntToScalar(4), SkIntToScalar(4));

        for (int alpha = 0; alpha < 4; ++alpha) {
            canvas.save();
            canvas.save();
            for (int i = 0; i < gModes.length; ++i) {
                if (6 == i) {
                    canvas.restore();
                    canvas.translate(W * 5, 0);
                    canvas.save();
                }
                SkXfermode mode = SkXfermode.Create(gModes[i].fMode);

                canvas.drawRect(bounds, fBGPaint);
                canvas.saveLayer(bounds, null);
                float dy = drawCell(canvas, mode,
                        gAlphaValue[alpha & 1],
                        gAlphaValue[alpha & 2]);
                canvas.restore();

                canvas.translate(0, dy * 5 / 4);
                SkSafeUnref(mode);
            }
            canvas.restore();
            canvas.restore();
            canvas.translate(W * 5 / 4, 0);
        }
    }

    // disable pdf for now, since it crashes on mac
    @Override
    protected int onGetFlags() { return kSkipPDF_Flag; }

    public static GMRegistry.Factory factory = new GMRegistry.Factory() {
        public GM apply() {
            return new HairModesGM();
        }
    };
}
