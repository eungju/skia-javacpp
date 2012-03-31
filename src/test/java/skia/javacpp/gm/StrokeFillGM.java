package skia.javacpp.gm;

import skia.javacpp.Skia;

import static skia.javacpp.core.*;

public class StrokeFillGM extends GM {
    @Override
    protected String onShortName() {
        return "stroke-fill";
    }

    @Override
    protected SkISize onISize() {
        return make_isize(640, 480);
    }

    static void show_bold(SkCanvas canvas, String text, float x, float y, SkPaint paint) {
        SkPaint p = new SkPaint(paint);
        canvas.drawText(text, x, y, p);
        p.setFakeBoldText(true);
        canvas.drawText(text, x, y + SkIntToScalar(120), p);
    }

    @Override
    protected void onDraw(SkCanvas canvas) {
        float x = SkIntToScalar(100);
        float y = SkIntToScalar(88);

        SkPaint paint = new SkPaint();
        paint.setAntiAlias(true);
        paint.setTextSize(SkIntToScalar(100));
        paint.setStrokeWidth(SkIntToScalar(5));

        SkTypeface face = SkTypeface.CreateFromName("Papyrus", SkTypeface.kNormal);
        SkSafeUnref(paint.setTypeface(face));
        show_bold(canvas, "Hello", x, y, paint);

        face = SkTypeface.CreateFromName("Hiragino Maru Gothic Pro", SkTypeface.kNormal);
        SkSafeUnref(paint.setTypeface(face));
        String hyphen = new String(new byte[] { (byte) 0xE3, (byte) 0x83, (byte) 0xBC }, Skia.UTF_8);
        show_bold(canvas, hyphen, x + SkIntToScalar(300), y, paint);

        paint.setStyle(SkPaint.kStrokeAndFill_Style);

        SkPath path = new SkPath();
        path.setFillType(SkPath.kWinding_FillType);
        path.addCircle(x, y + SkIntToScalar(200), SkIntToScalar(50), SkPath.kCW_Direction);
        path.addCircle(x, y + SkIntToScalar(200), SkIntToScalar(40), SkPath.kCCW_Direction);
        canvas.drawPath(path, paint);

        SkPath path2 = new SkPath();
        path2.setFillType(SkPath.kWinding_FillType);
        path2.addCircle(x + SkIntToScalar(120), y + SkIntToScalar(200), SkIntToScalar(50), SkPath.kCCW_Direction);
        path2.addCircle(x + SkIntToScalar(120), y + SkIntToScalar(200), SkIntToScalar(40), SkPath.kCW_Direction);
        canvas.drawPath(path2, paint);

        path2.reset();
        path2.addCircle(x + SkIntToScalar(240), y + SkIntToScalar(200), SkIntToScalar(50), SkPath.kCCW_Direction);
        canvas.drawPath(path2, paint);
        SkASSERT(path2.cheapIsDirection(SkPath.kCCW_Direction));

        path2.reset();
        SkASSERT(!path2.cheapComputeDirection(null));
        path2.addCircle(x + SkIntToScalar(360), y + SkIntToScalar(200), SkIntToScalar(50), SkPath.kCW_Direction);
        SkASSERT(path2.cheapIsDirection(SkPath.kCW_Direction));
        canvas.drawPath(path2, paint);
    }

    public static GMRegistry.Factory factory = new GMRegistry.Factory() {
        public GM apply() {
            return new StrokeFillGM();
        }
    };
}
