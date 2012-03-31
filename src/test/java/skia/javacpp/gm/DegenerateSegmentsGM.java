package skia.javacpp.gm;

import static skia.javacpp.core.*;

public class DegenerateSegmentsGM extends GM {
    static class PathAndName {
        SkPath      fPath;
        String fName1;
        String fName2;
    }

    @Override
    protected String onShortName() {
        return "degeneratesegments";
    }

    @Override
    protected SkISize onISize() { return make_isize(896, 930); }

    static interface AddSegmentFunc {
        SkPoint apply(SkPath path, SkPoint startPt);
    }

    static AddSegmentFunc AddMove = new AddSegmentFunc() {
    // We need to use explicit commands here, instead of addPath, because we
    // do not want the moveTo that is added at the beginning of a path to
    // appear in the appended path.
    public SkPoint apply(SkPath path, SkPoint startPt) {
        SkPoint moveToPt = plus(startPt, SkPoint.Make(0, 10*SK_Scalar1));
        path.moveTo(moveToPt);
        return moveToPt;
    }
    };

    static AddSegmentFunc AddMoveClose = new AddSegmentFunc() {
    public SkPoint apply(SkPath path, SkPoint startPt) {
        SkPoint moveToPt = plus(startPt, SkPoint.Make(0, 10*SK_Scalar1));
        path.moveTo(moveToPt);
        path.close();
        return moveToPt;
    }
    };

    static AddSegmentFunc AddDegenLine = new AddSegmentFunc() {
    public SkPoint apply(SkPath path, SkPoint startPt) {
        path.lineTo(startPt);
        return startPt;
    }
    };

    static AddSegmentFunc AddMoveDegenLine = new AddSegmentFunc() {
    public SkPoint apply(SkPath path, SkPoint startPt) {
        SkPoint moveToPt = plus(startPt, SkPoint.Make(0, 10*SK_Scalar1));
        path.moveTo(moveToPt);
        path.lineTo(moveToPt);
        return moveToPt;
    }
    };

    static AddSegmentFunc AddMoveDegenLineClose = new AddSegmentFunc() {
    public SkPoint apply(SkPath path, SkPoint startPt) {
        SkPoint moveToPt = plus(startPt, SkPoint.Make(0, 10*SK_Scalar1));
        path.moveTo(moveToPt);
        path.lineTo(moveToPt);
        path.close();
        return moveToPt;
    }
    };

    static AddSegmentFunc AddDegenQuad = new AddSegmentFunc() {
    public SkPoint apply(SkPath path, SkPoint startPt) {
        path.quadTo(startPt, startPt);
        return startPt;
    }
    };

    static AddSegmentFunc AddMoveDegenQuad = new AddSegmentFunc() {
    public SkPoint apply(SkPath path, SkPoint startPt) {
        SkPoint moveToPt = plus(startPt, SkPoint.Make(0, 10*SK_Scalar1));
        path.moveTo(moveToPt);
        path.quadTo(moveToPt, moveToPt);
        return moveToPt;
    }
    };

    static AddSegmentFunc AddMoveDegenQuadClose = new AddSegmentFunc() {
    public SkPoint apply(SkPath path, SkPoint startPt) {
        SkPoint moveToPt = plus(startPt, SkPoint.Make(0, 10*SK_Scalar1));
        path.moveTo(moveToPt);
        path.quadTo(moveToPt, moveToPt);
        path.close();
        return moveToPt;
    }
    };

    static AddSegmentFunc AddDegenCubic = new AddSegmentFunc() {
    public SkPoint apply(SkPath path, SkPoint startPt) {
        path.cubicTo(startPt, startPt, startPt);
        return startPt;
    }
    };

    static AddSegmentFunc AddMoveDegenCubic = new AddSegmentFunc() {
    public SkPoint apply(SkPath path, SkPoint startPt) {
        SkPoint moveToPt = plus(startPt, SkPoint.Make(0, 10*SK_Scalar1));
        path.moveTo(moveToPt);
        path.cubicTo(moveToPt, moveToPt, moveToPt);
        return moveToPt;
    }
    };

    static AddSegmentFunc AddMoveDegenCubicClose = new AddSegmentFunc() {
    public SkPoint apply(SkPath path, SkPoint startPt) {
        SkPoint moveToPt = plus(startPt, SkPoint.Make(0, 10*SK_Scalar1));
        path.moveTo(moveToPt);
        path.cubicTo(moveToPt, moveToPt, moveToPt);
        path.close();
        return moveToPt;
    }
    };

    static AddSegmentFunc AddClose = new AddSegmentFunc() {
    public SkPoint apply(SkPath path, SkPoint startPt) {
        path.close();
        return startPt;
    }
    };

    static AddSegmentFunc AddLine = new AddSegmentFunc() {
    public SkPoint apply(SkPath path, SkPoint startPt) {
        SkPoint endPt = plus(startPt, SkPoint.Make(40*SK_Scalar1, 0));
        path.lineTo(endPt);
        return endPt;
    }
    };

    static AddSegmentFunc AddMoveLine = new AddSegmentFunc() {
    public SkPoint apply(SkPath path, SkPoint startPt) {
        SkPoint moveToPt = plus(startPt, SkPoint.Make(0, 10*SK_Scalar1));
        SkPoint endPt = plus(moveToPt, SkPoint.Make(40*SK_Scalar1, 0));
        path.moveTo(moveToPt);
        path.lineTo(endPt);
        return endPt;
    }
    };

    static AddSegmentFunc AddMoveLineClose = new AddSegmentFunc() {
    public SkPoint apply(SkPath path, SkPoint startPt) {
        SkPoint moveToPt = plus(startPt, SkPoint.Make(0, 10*SK_Scalar1));
        SkPoint endPt = plus(moveToPt, SkPoint.Make(40*SK_Scalar1, 0));
        path.moveTo(moveToPt);
        path.lineTo(endPt);
        path.close();
        return endPt;
    }
    };

    static AddSegmentFunc AddQuad = new AddSegmentFunc() {
    public SkPoint apply(SkPath path, SkPoint startPt) {
        SkPoint midPt = plus(startPt, SkPoint.Make(20*SK_Scalar1, 5*SK_Scalar1));
        SkPoint endPt = plus(startPt, SkPoint.Make(40*SK_Scalar1, 0));
        path.quadTo(midPt, endPt);
        return endPt;
    }
    };

    static AddSegmentFunc AddMoveQuad = new AddSegmentFunc() {
    public SkPoint apply(SkPath path, SkPoint startPt) {
        SkPoint moveToPt = plus(startPt, SkPoint.Make(0, 10*SK_Scalar1));
        SkPoint midPt = plus(moveToPt, SkPoint.Make(20*SK_Scalar1, 5*SK_Scalar1));
        SkPoint endPt = plus(moveToPt, SkPoint.Make(40*SK_Scalar1, 0));
        path.moveTo(moveToPt);
        path.quadTo(midPt, endPt);
        return endPt;
    }
    };

    static AddSegmentFunc AddMoveQuadClose = new AddSegmentFunc() {
    public SkPoint apply(SkPath path, SkPoint startPt) {
        SkPoint moveToPt = plus(startPt, SkPoint.Make(0, 10*SK_Scalar1));
        SkPoint midPt = plus(moveToPt, SkPoint.Make(20*SK_Scalar1, 5*SK_Scalar1));
        SkPoint endPt = plus(moveToPt, SkPoint.Make(40*SK_Scalar1, 0));
        path.moveTo(moveToPt);
        path.quadTo(midPt, endPt);
        path.close();
        return endPt;
    }
    };

    static AddSegmentFunc AddCubic = new AddSegmentFunc() {
    public SkPoint apply(SkPath path, SkPoint startPt) {
        SkPoint t1Pt = plus(startPt, SkPoint.Make(15 * SK_Scalar1, 5 * SK_Scalar1));
        SkPoint t2Pt = plus(startPt, SkPoint.Make(25*SK_Scalar1, 5*SK_Scalar1));
        SkPoint endPt = plus(startPt, SkPoint.Make(40*SK_Scalar1, 0));
        path.cubicTo(t1Pt, t2Pt, endPt);
        return endPt;
    }
    };

    static AddSegmentFunc AddMoveCubic = new AddSegmentFunc() {
    public SkPoint apply(SkPath path, SkPoint startPt) {
        SkPoint moveToPt = plus(startPt, SkPoint.Make(0, 10*SK_Scalar1));
        SkPoint t1Pt = plus(moveToPt, SkPoint.Make(15*SK_Scalar1, 5*SK_Scalar1));
        SkPoint t2Pt = plus(moveToPt, SkPoint.Make(25*SK_Scalar1, 5*SK_Scalar1));
        SkPoint endPt = plus(moveToPt, SkPoint.Make(40*SK_Scalar1, 0));
        path.moveTo(moveToPt);
        path.cubicTo(t1Pt, t2Pt, endPt);
        return endPt;
    }
    };

    static AddSegmentFunc AddMoveCubicClose = new AddSegmentFunc() {
    public SkPoint apply(SkPath path, SkPoint startPt) {
        SkPoint moveToPt = plus(startPt, SkPoint.Make(0, 10*SK_Scalar1));
        SkPoint t1Pt = plus(moveToPt, SkPoint.Make(15*SK_Scalar1, 5*SK_Scalar1));
        SkPoint t2Pt = plus(moveToPt, SkPoint.Make(25*SK_Scalar1, 5*SK_Scalar1));
        SkPoint endPt = plus(moveToPt, SkPoint.Make(40*SK_Scalar1, 0));
        path.moveTo(moveToPt);
        path.cubicTo(t1Pt, t2Pt, endPt);
        path.close();
        return endPt;
    }
    };

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
        final AddSegmentFunc[] gSegmentFunctions = {
                AddMove,
                AddMoveClose,
                AddDegenLine,
                AddMoveDegenLine,
                AddMoveDegenLineClose,
                AddDegenQuad,
                AddMoveDegenQuad,
                AddMoveDegenQuadClose,
                AddDegenCubic,
                AddMoveDegenCubic,
                AddMoveDegenCubicClose,
                AddClose,
                AddLine,
                AddMoveLine,
                AddMoveLineClose,
                AddQuad,
                AddMoveQuad,
                AddMoveQuadClose,
                AddCubic,
                AddMoveCubic,
                AddMoveCubicClose
        };
       final String[] gSegmentNames = {
                    "Move",
                    "MoveClose",
                    "DegenLine",
                    "MoveDegenLine",
                    "MoveDegenLineClose",
                    "DegenQuad",
                    "MoveDegenQuad",
                    "MoveDegenQuadClose",
                    "DegenCubic",
                    "MoveDegenCubic",
                    "MoveDegenCubicClose",
                    "Close",
                    "Line",
                    "MoveLine",
                    "MoveLineClose",
                    "Quad",
                    "MoveQuad",
                    "MoveQuadClose",
                    "Cubic",
                    "MoveCubic",
                    "MoveCubicClose"
        };

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
            new FillAndName(SkPath.kInverseEvenOdd_FillType, "Inverse Even / Odd")
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
            new StyleAndName(SkPaint.kStroke_Style, "Stroke 10"),
            new StyleAndName(SkPaint.kStrokeAndFill_Style, "Stroke 10 And Fill")
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

        SkPaint titlePaint = new SkPaint();
        titlePaint.setColor(SK_ColorBLACK);
        titlePaint.setAntiAlias(true);
        titlePaint.setLCDRenderText(true);
        titlePaint.setTextSize(15 * SK_Scalar1);
        final String title = "Random Paths Drawn Into Rectangle Clips With " +
        "Indicated Style, Fill and Linecaps, " +
        "with Stroke width 6";
        canvas.drawText(title,
                20 * SK_Scalar1,
                20 * SK_Scalar1,
                titlePaint);

        SkRandom rand = new SkRandom();
        SkRect rect = SkRect.MakeWH(220 * SK_Scalar1, 50 * SK_Scalar1);
        canvas.save();
        canvas.translate(2*SK_Scalar1, 30 * SK_Scalar1); // The title
        canvas.save();
        int numSegments = gSegmentFunctions.length;
        int numCaps = gCaps.length;
        int numStyles = gStyles.length;
        int numFills = gFills.length;
        for (int row = 0; row < 6; ++row) {
            if (0 < row) {
                canvas.translate(0, rect.height() + 100*SK_Scalar1);
            }
            canvas.save();
            for (int column = 0; column < 4; ++column) {
                if (0 < column) {
                    canvas.translate(rect.width() + 4*SK_Scalar1, 0);
                }

                int color = 0xff007000;
                StyleAndName style = gStyles[(rand.nextU() >>> 16) % numStyles];
                CapAndName cap = gCaps[(rand.nextU() >>> 16) % numCaps];
                FillAndName fill = gFills[(rand.nextU() >>> 16) % numFills];
                SkPath path = new SkPath();
                int s1 = (rand.nextU() >>> 16) % numSegments;
                int s2 = (rand.nextU() >>> 16) % numSegments;
                int s3 = (rand.nextU() >>> 16) % numSegments;
                int s4 = (rand.nextU() >>> 16) % numSegments;
                int s5 = (rand.nextU() >>> 16) % numSegments;
                SkPoint pt = SkPoint.Make(10*SK_Scalar1, 0);
                pt = gSegmentFunctions[s1].apply(path, pt);
                pt = gSegmentFunctions[s2].apply(path, pt);
                pt = gSegmentFunctions[s3].apply(path, pt);
                pt = gSegmentFunctions[s4].apply(path, pt);
                pt = gSegmentFunctions[s5].apply(path, pt);

                this.drawPath(path, canvas, color, rect,
                        cap.fCap, cap.fJoin, style.fStyle,
                        fill.fFill, SK_Scalar1*6);

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
                canvas.drawText(style.fName,
                        0, rect.height() + 12 * SK_Scalar1,
                        labelPaint);
                canvas.drawText(fill.fName,
                        0, rect.height() + 24 * SK_Scalar1,
                        labelPaint);
                canvas.drawText(cap.fName,
                        0, rect.height() + 36 * SK_Scalar1,
                        labelPaint);
                canvas.drawText(gSegmentNames[s1],
                        0, rect.height() + 48 * SK_Scalar1,
                        labelPaint);
                canvas.drawText(gSegmentNames[s2],
                        0, rect.height() + 60 * SK_Scalar1,
                        labelPaint);
                canvas.drawText(gSegmentNames[s3],
                        0, rect.height() + 72 * SK_Scalar1,
                        labelPaint);
                canvas.drawText(gSegmentNames[s4],
                        0, rect.height() + 84 * SK_Scalar1,
                        labelPaint);
                canvas.drawText(gSegmentNames[s5],
                        0, rect.height() + 96 * SK_Scalar1,
                        labelPaint);
            }
            canvas.restore();
        }
        canvas.restore();
        canvas.restore();
    }

    public static GMRegistry.Factory factory = new GMRegistry.Factory() {
        public GM apply() {
            return new DegenerateSegmentsGM();
        }
    };
}
