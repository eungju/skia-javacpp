package skia.javacpp.gm;

import static skia.javacpp.core.*;

public class complexclip {
    static final int gPathColor = SK_ColorBLACK;
    static final int gClipAColor = SK_ColorBLUE;
    static final int gClipBColor = SK_ColorRED;

    public static class ComplexClipGM extends GM {
        private boolean fDoAAClip;
        public ComplexClipGM(boolean aaclip) {
            fDoAAClip = aaclip;
            this.setBGColor(0xFFDDDDDD);
    //        this->setBGColor(SkColorSetRGB(0xB0,0xDD,0xB0));
        }

        @Override
        protected String onShortName() {
            return String.format("complexclip_%s", fDoAAClip ? "aa" : "bw");
        }

        @Override
        protected SkISize onISize() { return make_isize(970, 780); }

        @Override
        protected void onDraw(SkCanvas canvas) {
            SkPath path = new SkPath();
            path.moveTo(SkIntToScalar(0),   SkIntToScalar(50));
            path.quadTo(SkIntToScalar(0),   SkIntToScalar(0),   SkIntToScalar(50),  SkIntToScalar(0));
            path.lineTo(SkIntToScalar(175), SkIntToScalar(0));
            path.quadTo(SkIntToScalar(200), SkIntToScalar(0),   SkIntToScalar(200), SkIntToScalar(25));
            path.lineTo(SkIntToScalar(200), SkIntToScalar(150));
            path.quadTo(SkIntToScalar(200), SkIntToScalar(200), SkIntToScalar(150), SkIntToScalar(200));
            path.lineTo(SkIntToScalar(0),   SkIntToScalar(200));
            path.close();
            path.moveTo(SkIntToScalar(50),  SkIntToScalar(50));
            path.lineTo(SkIntToScalar(150), SkIntToScalar(50));
            path.lineTo(SkIntToScalar(150), SkIntToScalar(125));
            path.quadTo(SkIntToScalar(150), SkIntToScalar(150), SkIntToScalar(125), SkIntToScalar(150));
            path.lineTo(SkIntToScalar(50),  SkIntToScalar(150));
            path.close();
            path.setFillType(SkPath.kEvenOdd_FillType);
            SkPaint pathPaint = new SkPaint();
            pathPaint.setAntiAlias(true);
            pathPaint.setColor(gPathColor);

            SkPath clipA = new SkPath();
            clipA.moveTo(SkIntToScalar(10),  SkIntToScalar(20));
            clipA.lineTo(SkIntToScalar(165), SkIntToScalar(22));
            clipA.lineTo(SkIntToScalar(70),  SkIntToScalar(105));
            clipA.lineTo(SkIntToScalar(165), SkIntToScalar(177));
            clipA.lineTo(SkIntToScalar(-5),  SkIntToScalar(180));
            clipA.close();

            SkPath clipB = new SkPath();
            clipB.moveTo(SkIntToScalar(40),  SkIntToScalar(10));
            clipB.lineTo(SkIntToScalar(190), SkIntToScalar(15));
            clipB.lineTo(SkIntToScalar(195), SkIntToScalar(190));
            clipB.lineTo(SkIntToScalar(40),  SkIntToScalar(185));
            clipB.lineTo(SkIntToScalar(155), SkIntToScalar(100));
            clipB.close();

            SkPaint paint = new SkPaint();
            paint.setAntiAlias(true);
            paint.setTextSize(SkIntToScalar(20));

            class Op {
                int fOp;
                String  fName;
                public Op(int op, String name) {
                    fOp = op;
                    fName = name;
                }
            };
            final Op[] gOps = { //extra spaces in names for measureText
                new Op(SkRegion.kIntersect_Op,         "Isect "),
                new Op(SkRegion.kDifference_Op,        "Diff "),
                new Op(SkRegion.kUnion_Op,             "Union "),
                new Op(SkRegion.kXOR_Op,               "Xor "),
                new Op(SkRegion.kReverseDifference_Op, "RDiff ")
            };

            canvas.translate(SkIntToScalar(20), SkIntToScalar(20));
            canvas.scale(3 * SK_Scalar1 / 4, 3 * SK_Scalar1 / 4);

            for (int invBits = 0; invBits < 4; ++invBits) {
                canvas.save();
                for (int op = 0; op < gOps.length; ++op) {
                    this.drawHairlines(canvas, path, clipA, clipB);

                    boolean doInvA = SkToBool(invBits & 1);
                    boolean doInvB = SkToBool(invBits & 2);
                    canvas.save();
                    // set clip
                    clipA.setFillType(doInvA ? SkPath.kInverseEvenOdd_FillType :
                    SkPath.kEvenOdd_FillType);
                    clipB.setFillType(doInvB ? SkPath.kInverseEvenOdd_FillType :
                    SkPath.kEvenOdd_FillType);
                    canvas.clipPath(clipA, SkRegion.kIntersect_Op, fDoAAClip);
                    canvas.clipPath(clipB, gOps[op].fOp, fDoAAClip);

                    // draw path clipped
                    canvas.drawPath(path, pathPaint);
                    canvas.restore();


                    float txtX = SkIntToScalar(45);
                    paint.setColor(gClipAColor);
                    final String aTxt = doInvA ? "InvA " : "A ";
                    canvas.drawText(aTxt, txtX, SkIntToScalar(220), paint);
                    txtX += paint.measureText(aTxt);
                    paint.setColor(SK_ColorBLACK);
                    canvas.drawText(gOps[op].fName,
                            txtX, SkIntToScalar(220), paint);
                    txtX += paint.measureText(gOps[op].fName);
                    paint.setColor(gClipBColor);
                    final String bTxt = doInvB ? "InvB " : "B ";
                    canvas.drawText(bTxt, txtX, SkIntToScalar(220), paint);

                    canvas.translate(SkIntToScalar(250),0);
                }
                canvas.restore();
                canvas.translate(0, SkIntToScalar(250));
            }
        }
        private void drawHairlines(SkCanvas canvas, SkPath path,
                           SkPath clipA, SkPath clipB) {
            SkPaint paint = new SkPaint();
            paint.setAntiAlias(true);
            paint.setStyle(SkPaint.kStroke_Style);
            final int fade = 0x33;

            // draw path in hairline
            paint.setColor(gPathColor); paint.setAlpha(fade);
            canvas.drawPath(path, paint);

            // draw clips in hair line
            paint.setColor(gClipAColor); paint.setAlpha(fade);
            canvas.drawPath(clipA, paint);
            paint.setColor(gClipBColor); paint.setAlpha(fade);
            canvas.drawPath(clipB, paint);
        }
    }

    public static GMRegistry.Factory gFact0 = new GMRegistry.Factory() {
        public GM apply() {
            return new ComplexClipGM(false);
        }
    };

    public static GMRegistry.Factory gFact1 = new GMRegistry.Factory() {
        public GM apply() {
            return new ComplexClipGM(true);
        }
    };
}
