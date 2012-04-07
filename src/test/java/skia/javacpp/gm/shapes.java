package skia.javacpp.gm;

import static skia.javacpp.core.*;
import static skia.javacpp.effects.*;

public class shapes {
    static SkRect make_rect(int l, int t, int r, int b) {
        SkRect rect = new SkRect();
        rect.set(SkIntToScalar(l), SkIntToScalar(t),
                SkIntToScalar(r), SkIntToScalar(b));
        return rect;
    }

    static SkShape make_shape0(boolean red) {
        SkRectShape s = new SkRectShape();
        s.setRect(make_rect(10, 10, 90, 90));
        if (red) {
            s.paint().setColor(SK_ColorRED);
        }
        return s;
    }

    static SkShape make_shape1() {
        SkRectShape s = new SkRectShape();
        s.setOval(make_rect(10, 10, 90, 90));
        s.paint().setColor(SK_ColorBLUE);
        return s;
    }

    static SkShape make_shape2() {
        SkRectShape s = new SkRectShape();
        s.setRRect(make_rect(10, 10, 90, 90), SkIntToScalar(20), SkIntToScalar(20));
        s.paint().setColor(SK_ColorGREEN);
        return s;
    }

    public static class ShapesGM extends GM {
        private SkGroupShape fGroup = new SkGroupShape();
        private SkMatrixRef[] fMatrixRefs = new SkMatrixRef[4];

        public ShapesGM() {
            fGroup.autoUnref();

            this.setBGColor(0xFFDDDDDD);

            SkMatrix m = new SkMatrix();
            fGroup.appendShape(make_shape0(false)).unref();
            m.setRotate(SkIntToScalar(30), SkIntToScalar(50), SkIntToScalar(50));
            m.postTranslate(0, SkIntToScalar(120));
            fGroup.appendShape(make_shape0(true), m).unref();

            m.setTranslate(SkIntToScalar(120), 0);
            fGroup.appendShape(make_shape1(), m).unref();
            m.postTranslate(0, SkIntToScalar(120));
            fGroup.appendShape(make_shape2(), m).unref();

            for (int i = 0; i < fMatrixRefs.length; i++) {
                fMatrixRefs[i] = fGroup.getShapeMatrixRef(i);
            }
            float c = SkIntToScalar(50);
            fMatrixRefs[3].preRotate(SkIntToScalar(30), c, c);
        }

        @Override
        protected String onShortName() {
            return "shapes";
        }

        @Override
        protected SkISize onISize() {
            return make_isize(380, 480);
        }

        @Override
        protected void onDraw(SkCanvas canvas) {
            SkMatrix matrix = new SkMatrix();

            SkGroupShape gs = new SkGroupShape();
            new SkAutoUnref(gs);
            gs.appendShape(fGroup);
            matrix.setScale(-SK_Scalar1, SK_Scalar1);
            matrix.postTranslate(SkIntToScalar(220), SkIntToScalar(240));
            gs.appendShape(fGroup, matrix);
            matrix.setTranslate(SkIntToScalar(240), 0);
            matrix.preScale(SK_Scalar1*2, SK_Scalar1*2);
            gs.appendShape(fGroup, matrix);

            if (true) {
                SkPicture pict = new SkPicture();
                SkCanvas cv = pict.beginRecording(1000, 1000);
                cv.scale(SK_ScalarHalf, SK_ScalarHalf);
                gs.draw(cv);
                cv.translate(SkIntToScalar(680), SkIntToScalar(480));
                cv.scale(-SK_Scalar1, SK_Scalar1);
                gs.draw(cv);
                pict.endRecording();
                canvas.drawPicture(pict);
                pict.unref();
            }
        }
    }

    public static GMRegistry.Factory MyFactory = new GMRegistry.Factory() {
        public GM apply() {
            return new ShapesGM();
        }
    };
}
