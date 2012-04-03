package skia.javacpp.gm;

import static skia.javacpp.core.*;

public class BitmapCopyGM extends GM {
    static final String[] gConfigNames = {
        "unknown config",
                "A1",
                "A8",
                "Index8",
                "565",
                "4444",
                "8888"
    };

    static int[] gConfigs = {
            SkBitmap.kRGB_565_Config,
        SkBitmap.kARGB_4444_Config,
        SkBitmap.kARGB_8888_Config,
    };

    static final int NUM_CONFIGS = gConfigs.length;

    static void draw_checks(SkCanvas canvas, int width, int height) {
        SkPaint paint = new SkPaint();
        paint.setColor(SK_ColorRED);
        canvas.drawRectCoords(0, 0, width / 2, height / 2, paint);
        paint.setColor(SK_ColorGREEN);
        canvas.drawRectCoords(width / 2, 0, width, height / 2, paint);
        paint.setColor(SK_ColorBLUE);
        canvas.drawRectCoords(0, height / 2, width / 2, height, paint);
        paint.setColor(SK_ColorYELLOW);
        canvas.drawRectCoords(width / 2, height / 2, width, height, paint);
    }

    public SkBitmap[]    fDst = new SkBitmap[NUM_CONFIGS];
    {
        for (int i = 0; i < fDst.length; i++) {
            fDst[i] = new SkBitmap();
        }
    }

    public BitmapCopyGM() {
        this.setBGColor(0xFFDDDDDD);
    }

    @Override
    protected String onShortName() {
        return "bitmapcopy";
    }

    @Override
    protected SkISize onISize() {
        return make_isize(540, 330);
    }

    @Override
    protected void onDraw(SkCanvas canvas) {
        SkPaint paint = new SkPaint();
        float horizMargin = (SkIntToScalar(10));
        float vertMargin = (SkIntToScalar(10));

        draw_checks(canvas, 40, 40);
        SkBitmap src = canvas.getDevice().accessBitmap(false);

        for (int i = 0; i < NUM_CONFIGS; ++i) {
            if (!src.deepCopyTo(fDst[i], gConfigs[i])) {
                src.copyTo(fDst[i], gConfigs[i]);
            }
        }

        canvas.clear(0xFFDDDDDD);
        paint.setAntiAlias(true);
        float width = SkIntToScalar(40);
        float height = SkIntToScalar(40);
        if (paint.getFontSpacing() > height) {
            height = paint.getFontSpacing();
        }
        for (int i = 0; i < NUM_CONFIGS; i++) {
            String name = gConfigNames[src.config()];
            float textWidth = paint.measureText(name);
            if (textWidth > width) {
                width = textWidth;
            }
        }
        float horizOffset = width + horizMargin;
        float vertOffset = height + vertMargin;
        canvas.translate(SkIntToScalar(20), SkIntToScalar(20));

        for (int i = 0; i < NUM_CONFIGS; i++) {
            canvas.save();
            // Draw destination config name
            String name = gConfigNames[fDst[i].config()];
            float textWidth = paint.measureText(name);
            float x = (width - textWidth) / 2f;
            float y = paint.getFontSpacing() / 2f;
            canvas.drawText(name, x, y, paint);

            // Draw destination bitmap
            canvas.translate(0, vertOffset);
            x = (width - 40) / 2f;
            canvas.drawBitmap(fDst[i], x, 0, paint);
            canvas.restore();

            canvas.translate(horizOffset, 0);
        }
    }

    @Override
    protected int onGetFlags() { return kSkipPicture_Flag; }

    public static GMRegistry.Factory MyFactory = new GMRegistry.Factory() {
        public GM apply() {
            return new BitmapCopyGM();
        }
    };
}
