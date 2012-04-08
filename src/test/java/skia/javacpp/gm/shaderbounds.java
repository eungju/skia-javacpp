package skia.javacpp.gm;

import static skia.javacpp.core.*;
import static skia.javacpp.effects.*;

public class shaderbounds {
    static ShaderGenFunc MakeLinear = new ShaderGenFunc() {
        public SkShader apply(float width, float height, boolean alternate) {
            SkPoint[] pts = { SkPoint.Make(0, 0), SkPoint.Make(width, height)};
            int[] colors = {SK_ColorRED, SK_ColorGREEN};
            if (alternate) {
                pts[1].fY(0);
                colors[0] = SK_ColorBLUE;
                colors[1] = SK_ColorYELLOW;
            }
            return SkGradientShader.CreateLinear(pts, colors, null, SkShader.kClamp_TileMode, null);
        }
    };

    static interface ShaderGenFunc {
        public SkShader apply(float width, float height, boolean alternate);
    }

    public static class ShaderBoundsGM extends GM {
        public ShaderBoundsGM(ShaderGenFunc maker, String name) {
            fShaderMaker = maker;
            fName = name;
        }

        @Override
        protected String onShortName() {
            return fName;
        }

        @Override
        protected SkISize onISize() { return make_isize(320, 240); }

        @Override
        protected SkMatrix onGetInitialTransform() {
            SkMatrix result = new SkMatrix();
            float scale = SkFloatToScalar(0.8f);
            result.setScale(scale, scale);
            result.postTranslate(SkIntToScalar(7), SkIntToScalar(23));
            return result;
        }

        @Override
        protected void onDraw(SkCanvas canvas) {
            // The PDF device has already clipped to the content area, but we
            // do it again here so that the raster and pdf results are consistent.
            canvas.clipRect(SkRect.MakeWH(SkIntToScalar(320),
                    SkIntToScalar(240)));

            SkMatrix canvasScale = new SkMatrix();
            float scale = SkFloatToScalar(0.7f);
            canvasScale.setScale(scale, scale);
            canvas.concat(canvasScale);

            // Background shader.
            SkPaint paint = new SkPaint();
            paint.setShader(MakeShader(559, 387, false));
            SkRect r = SkRect.MakeXYWH(SkIntToScalar(-12), SkIntToScalar(-41),
                    SkIntToScalar(571), SkIntToScalar(428));
            canvas.drawRect(r, paint);

            // Constrained shader.
            paint.setShader(MakeShader(101, 151, true));
            r = SkRect.MakeXYWH(SkIntToScalar(43), SkIntToScalar(71),
                    SkIntToScalar(101), SkIntToScalar(151));
            canvas.clipRect(r);
            canvas.drawRect(r, paint);
        }

        SkShader MakeShader(int width, int height, boolean background) {
            float scale = SkFloatToScalar(0.5f);
            if (background) {
                scale = SkFloatToScalar(0.6f);
            }
            float shaderWidth = SkIntToScalar(width)/scale;
            float shaderHeight = SkIntToScalar(height)/scale;
            SkShader shader = fShaderMaker.apply(shaderWidth, shaderHeight, background);
            SkMatrix shaderScale = new SkMatrix();
            shaderScale.setScale(scale, scale);
            shader.setLocalMatrix(shaderScale);
            return shader;
        }

        private ShaderGenFunc fShaderMaker;
        private String fName;
        //private SkShader MakeShader(boolean background);
    }

    public static GMRegistry.Factory MyFactory = new GMRegistry.Factory() {
        public GM apply() {
            return new ShaderBoundsGM(MakeLinear, "shaderbounds_linear");
        }
    };
}
