package skia.javacpp.gm;

import static skia.javacpp.core.*;

public class EmptyPathGM extends GM {

    @Override
    protected String onShortName() {
        return "emptypath";
    }

    @Override
    protected SkISize onISize() { return make_isize(600, 280); }

    void drawEmpty(SkCanvas canvas,
                   int color,
                   SkRect clip,
                   int style,
                   int fill) {
        SkPath path = new SkPath();
        path.setFillType(fill);
        SkPaint paint = new SkPaint();
        paint.setColor(color);
        paint.setStyle(style);
        canvas.save();
        canvas.clipRect(clip);
        canvas.drawPath(path, paint);
        canvas.restore();
    }

    @Override
    protected void onDraw(SkCanvas canvas) {
        class FillAndName {
            int fFill;
            String      fName;

            public FillAndName(int fill, String name) {
                fFill = fill;
                fName = name;
            }
        };
        final FillAndName[] gFills = {
            new FillAndName(SkPath.kWinding_FillType, "Winding"),
            new FillAndName(SkPath.kEvenOdd_FillType, "Even / Odd"),
            new FillAndName(SkPath.kInverseWinding_FillType, "Inverse Winding"),
            new FillAndName(SkPath.kInverseEvenOdd_FillType, "Inverse Even / Odd"),
        };
        class StyleAndName {
            int fStyle;
            String    fName;
            public StyleAndName(int style, String name) {
                fStyle = style;
                fName = name;
            }
        };
        final StyleAndName[] gStyles = {
            new StyleAndName(SkPaint.kFill_Style, "Fill"),
            new StyleAndName(SkPaint.kStroke_Style, "Stroke"),
            new StyleAndName(SkPaint.kStrokeAndFill_Style, "Stroke And Fill"),
        };

        SkPaint titlePaint = new SkPaint();
        titlePaint.setColor(SK_ColorBLACK);
        titlePaint.setAntiAlias(true);
        titlePaint.setLCDRenderText(true);
        titlePaint.setTextSize(15 * SK_Scalar1);
        String title = "Empty Paths Drawn Into Rectangle Clips With " +
        "Indicated Style and Fill";
        canvas.drawText(title,
                20 * SK_Scalar1,
                20 * SK_Scalar1,
                titlePaint);

        SkRandom rand = new SkRandom();
        SkRect rect = SkRect.MakeWH(100*SK_Scalar1, 30*SK_Scalar1);
        int i = 0;
        canvas.save();
        canvas.translate(10 * SK_Scalar1, 0);
        canvas.save();
        for (int style = 0; style < gStyles.length; ++style) {
            for (int fill = 0; fill < gFills.length; ++fill) {
                if (0 == i % 4) {
                    canvas.restore();
                    canvas.translate(0, rect.height() + 40 * SK_Scalar1);
                    canvas.save();
                } else {
                    canvas.translate(rect.width() + 40 * SK_Scalar1, 0);
                }
                ++i;


                int color = rand.nextU();
                color = 0xff000000| color; // force solid
                this.drawEmpty(canvas, color, rect,
                        gStyles[style].fStyle, gFills[fill].fFill);

                SkPaint rectPaint = new SkPaint();
                rectPaint.setColor(SK_ColorBLACK);
                rectPaint.setStyle(SkPaint.kStroke_Style);
                rectPaint.setStrokeWidth(-1);
                rectPaint.setAntiAlias(true);
                canvas.drawRect(rect, rectPaint);

                SkPaint labelPaint = new SkPaint();
                labelPaint.setColor(color);
                labelPaint.setAntiAlias(true);
                labelPaint.setLCDRenderText(true);
                labelPaint.setTextSize(12 * SK_Scalar1);
                canvas.drawText(gStyles[style].fName,
                        0, rect.height() + 15 * SK_Scalar1,
                        labelPaint);
                canvas.drawText(gFills[fill].fName,
                        0, rect.height() + 28 * SK_Scalar1,
                        labelPaint);
            }
        }
        canvas.restore();
        canvas.restore();
    }
}
