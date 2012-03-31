package skia.javacpp.gm;

import com.googlecode.javacpp.ShortPointer;

import static skia.javacpp.core.*;

public class XfermodesGM extends GM {
    static void make_bitmaps(int w, int h, SkBitmap src, SkBitmap dst) {
        src.setConfig(SkBitmap.kARGB_8888_Config, w, h);
        src.allocPixels();
        src.eraseColor(0);

        SkCanvas c = new SkCanvas(src);
        SkPaint p = new SkPaint();
        SkRect r = new SkRect();
        float ww = SkIntToScalar(w);
        float hh = SkIntToScalar(h);
    
        p.setAntiAlias(true);
        p.setColor(0xFFFFCC44);
        r.set(0, 0, ww*3/4, hh*3/4);
        c.drawOval(r, p);
    
        dst.setConfig(SkBitmap.kARGB_8888_Config, w, h);
        dst.allocPixels();
        dst.eraseColor(0);
        c.setBitmapDevice(dst);
    
        p.setColor(0xFF66AAFF);
        r.set(ww/3, hh/3, ww*19/20, hh*19/20);
        c.drawRect(r, p);
    }

    SkBitmap    fBG = new SkBitmap();
    SkBitmap    fSrcB = new SkBitmap(), fDstB = new SkBitmap();
    boolean        fOnce;

    void draw_mode(SkCanvas canvas, SkXfermode mode, int alpha,
                   float x, float y) {
        SkPaint p = new SkPaint();

        canvas.drawBitmap(fSrcB, x, y, p);
        p.setAlpha(alpha);
        p.setXfermode(mode);
        canvas.drawBitmap(fDstB, x, y, p);
    }

    void init() {
        if (!fOnce) {
            // Do all this work in a temporary so we get a deep copy
            short[] localData = { (short) 0xFFFF, (short) 0xCCCF, (short) 0xCCCF, (short) 0xFFFF };
            SkBitmap scratchBitmap = new SkBitmap();
            scratchBitmap.setConfig(SkBitmap.kARGB_4444_Config, 2, 2, 4);
            scratchBitmap.setPixels(new ShortPointer(localData));
            scratchBitmap.setIsOpaque(true);
            scratchBitmap.copyTo(fBG, SkBitmap.kARGB_4444_Config);

            make_bitmaps(W, H, fSrcB, fDstB);
            fOnce = true;
        }
    }

    public static final int W = 64;
    public static final int H = 64;
    public XfermodesGM() { fOnce = false; }

    @Override
    protected String onShortName() {
        return "xfermodes";
    }

    @Override
    protected SkISize onISize() {
        return make_isize(790, 640);
    }

    @Override
    protected void onDraw(SkCanvas canvas) {
        this.init();

        canvas.translate(SkIntToScalar(10), SkIntToScalar(20));

        class Mode {
            int fMode;
            String fLabel;
            public Mode(int mode, String label) {
                fMode = mode;
                fLabel = label;
            }
        }
        Mode[] gModes = {
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

            new Mode(SkXfermode.kPlus_Mode,         "Plus"          ),
            new Mode(SkXfermode.kMultiply_Mode,     "Multiply"      ),
            new Mode(SkXfermode.kScreen_Mode,       "Screen"        ),
            new Mode(SkXfermode.kOverlay_Mode,      "Overlay"       ),
            new Mode(SkXfermode.kDarken_Mode,       "Darken"        ),
            new Mode(SkXfermode.kLighten_Mode,      "Lighten"       ),
            new Mode(SkXfermode.kColorDodge_Mode,   "ColorDodge"    ),
            new Mode(SkXfermode.kColorBurn_Mode,    "ColorBurn"     ),
            new Mode(SkXfermode.kHardLight_Mode,    "HardLight"     ),
            new Mode(SkXfermode.kSoftLight_Mode,    "SoftLight"     ),
            new Mode(SkXfermode.kDifference_Mode,   "Difference"    ),
            new Mode(SkXfermode.kExclusion_Mode,    "Exclusion"     ),
        };

        final float w = SkIntToScalar(W);
        final float h = SkIntToScalar(H);
        SkShader s = SkShader.CreateBitmapShader(fBG,
                SkShader.kRepeat_TileMode,
                SkShader.kRepeat_TileMode);
        SkMatrix m = new SkMatrix();
        m.setScale(SkIntToScalar(6), SkIntToScalar(6));
        s.setLocalMatrix(m);

        SkPaint labelP = new SkPaint();
        labelP.setAntiAlias(true);
        labelP.setTextAlign(SkPaint.kCenter_Align);

        final int W = 5;

        float x0 = 0;
        for (int twice = 0; twice < 2; twice++) {
            float x = x0, y = 0;
            for (int i = 0; i < gModes.length; i++) {
                SkXfermode mode = SkXfermode.Create(gModes[i].fMode);
                try {
                    SkRect r = new SkRect();
                    r.set(x, y, x+w, y+h);

                    SkPaint p = new SkPaint();
                    p.setStyle(SkPaint.kFill_Style);
                    p.setShader(s);
                    canvas.drawRect(r, p);

                    canvas.saveLayer(r, null, SkCanvas.kARGB_ClipLayer_SaveFlag);
                    draw_mode(canvas, mode, twice != 0 ? 0x88 : 0xFF, r.fLeft(), r.fTop());
                    canvas.restore();

                    r.inset(-SK_ScalarHalf, -SK_ScalarHalf);
                    p.setStyle(SkPaint.kStroke_Style);
                    p.setShader(null);
                    canvas.drawRect(r, p);

        //            #if 1
                    canvas.drawText(gModes[i].fLabel,
                            x + w/2, y - labelP.getTextSize()/2, labelP);
        //            #endif
                    x += w + SkIntToScalar(10);
                    if ((i % W) == W - 1) {
                        x = x0;
                        y += h + SkIntToScalar(30);
                    }
                } finally {
                    SkSafeUnref(mode);
                }
            }
            x0 += SkIntToScalar(400);
        }
        s.unref();
    }

    public static GMRegistry.Factory factory = new GMRegistry.Factory() {
        public GM apply() {
            return new XfermodesGM();
        }
    };
}
