package skia.javacpp.gm;

import static skia.javacpp.core.*;

public class QuadPathGM extends GM {
    @Override
    protected String onShortName() {
        return "quadpath";
    }

    @Override
    protected SkISize onISize() { return make_isize(1240, 390); }

    void drawPath(SkPath path, SkCanvas canvas, int color,
                  SkRect clip, int cap, int join,
                  int style, int fill,
                  float strokeWidth) {
        path.setFillType(fill);
        SkPaint paint = new SkPaint();
        paint.setStrokeCap(cap);
        paint.setStrokeWidth(strokeWidth);
        paint.setStrokeJoin(join);
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
        }
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
        class CapAndName {
            int  fCap;
            int fJoin;
            String   fName;
            public CapAndName(int cap, int join, String name) {
                fCap = cap;
                fJoin = join;
                fName = name;
            }
        };
        final CapAndName[] gCaps = {
                new CapAndName(SkPaint.kButt_Cap, SkPaint.kBevel_Join, "Butt"),
                new CapAndName(SkPaint.kRound_Cap, SkPaint.kRound_Join, "Round"),
                new CapAndName(SkPaint.kSquare_Cap, SkPaint.kBevel_Join, "Square")
        };
        class PathAndName {
            SkPath      fPath = new SkPath();
            String fName;
        };
        PathAndName path = new PathAndName();
        path.fPath.moveTo(25*SK_Scalar1, 10*SK_Scalar1);
        path.fPath.quadTo(50*SK_Scalar1, 20*SK_Scalar1,
                75*SK_Scalar1, 10*SK_Scalar1);
        path.fName = "moveTo-quad";

        SkPaint titlePaint = new SkPaint();
        titlePaint.setColor(SK_ColorBLACK);
        titlePaint.setAntiAlias(true);
        titlePaint.setLCDRenderText(true);
        titlePaint.setTextSize(15 * SK_Scalar1);
        String title = "Quad Drawn Into Rectangle Clips With " +
        "Indicated Style, Fill and Linecaps, with stroke width 10";
        canvas.drawText(title,
                20 * SK_Scalar1,
                20 * SK_Scalar1,
                titlePaint);

        SkRandom rand = new SkRandom();
        SkRect rect = SkRect.MakeWH(100*SK_Scalar1, 30*SK_Scalar1);
        canvas.save();
        canvas.translate(10 * SK_Scalar1, 30 * SK_Scalar1);
        canvas.save();
        for (int cap = 0; cap < gCaps.length; ++cap) {
            if (0 < cap) {
                canvas.translate((rect.width() + 40 * SK_Scalar1) * gStyles.length, 0);
            }
            canvas.save();
            for (int fill = 0; fill < gFills.length; ++fill) {
                if (0 < fill) {
                    canvas.translate(0, rect.height() + 40 * SK_Scalar1);
                }
                canvas.save();
                for (int style = 0; style < gStyles.length; ++style) {
                    if (0 < style) {
                        canvas.translate(rect.width() + 40 * SK_Scalar1, 0);
                    }

                    int color = 0xff007000;
                    this.drawPath(path.fPath, canvas, color, rect,
                            gCaps[cap].fCap, gCaps[cap].fJoin, gStyles[style].fStyle,
                            gFills[fill].fFill, SK_Scalar1*10);

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
                    labelPaint.setTextSize(10 * SK_Scalar1);
                    canvas.drawText(gStyles[style].fName,
                            0, rect.height() + 12 * SK_Scalar1,
                            labelPaint);
                    canvas.drawText(gFills[fill].fName,
                            0, rect.height() + 24 * SK_Scalar1,
                            labelPaint);
                    canvas.drawText(gCaps[cap].fName,
                            0, rect.height() + 36 * SK_Scalar1,
                            labelPaint);
                }
                canvas.restore();
            }
            canvas.restore();
        }
        canvas.restore();
        canvas.restore();
    }
}
