package skia.javacpp;

import com.googlecode.javacpp.Loader;
import com.googlecode.javacpp.Pointer;
import com.googlecode.javacpp.annotation.ByRef;
import com.googlecode.javacpp.annotation.Cast;
import com.googlecode.javacpp.annotation.Const;
import com.googlecode.javacpp.annotation.Name;
import com.googlecode.javacpp.annotation.NoDeallocator;
import com.googlecode.javacpp.annotation.Platform;
import com.googlecode.javacpp.annotation.Properties;

import static skia.javacpp.core.*;

@Properties({
	@Platform(include={
            "Sk1DPathEffect.h",
            "Sk2DPathEffect.h",
            "SkArithmeticMode.h",
            "SkBlurDrawLooper.h",
            "SkBlurImageFilter.h",
            "SkBlurMaskFilter.h",
            "SkColorMatrix.h",
            "SkColorMatrixFilter.h",
            "SkCornerPathEffect.h",
            "SkDashPathEffect.h",
            "SkDiscretePathEffect.h",
            "SkGradientShader.h",
            "SkMorphologyImageFilter.h",
            "SkTableColorFilter.h",
            "SkTestImageFilters.h",
            "SkUnitMapper.h"})
})
public class effects {
	static { Loader.load(Skia.class); }
	
	/*
	 * Sk1DPathEffect.h
	 */
	
	public static class Sk1DPathEffect extends SkPathEffect {
		static { Loader.load(Skia.class); }
		
		protected Sk1DPathEffect() {};
	}
	
	public static class SkPath1DPathEffect extends Sk1DPathEffect {
		static { Loader.load(Skia.class); }
		
	    //enum Style
	    public static final int kTranslate_Style = 0,
	        kRotate_Style = 1,
	        kMorph_Style = 2;

		public SkPath1DPathEffect(SkPath path, float advance, float phase, int style) {
            allocate(path, advance, phase, style);
            deallocator(new UnrefDeallocator(this));
        }
		@NoDeallocator
		public native void allocate(@Const @ByRef SkPath path, @Cast("SkScalar") float advance, @Cast("SkScalar") float phase, @Cast("SkPath1DPathEffect::Style") int style);
	}

	/*
	 * Sk2DPathEffect.h
	 */
	
	public static class Sk2DPathEffect extends SkPathEffect {
		static { Loader.load(Skia.class); }
		
		protected Sk2DPathEffect() {};
	}
	
	public static class SkPath2DPathEffect extends Sk2DPathEffect {
		static { Loader.load(Skia.class); }
		
	    //enum Style
	    public static final int kTranslate_Style = 0,
	        kRotate_Style = 1,
	        kMorph_Style = 2;

		public SkPath2DPathEffect(SkMatrix matrix, SkPath path) {
            allocate(matrix, path);
            deallocator(new UnrefDeallocator(this));
        }
		@NoDeallocator
		public native void allocate(@Const @ByRef SkMatrix matrix, @Const @ByRef SkPath path);
	}

    /*
     * SkArithmeticMode.h
     */

    public static class SkArithmeticMode extends SkXfermode {
        static { Loader.load(Skia.class); }

        public native static SkXfermode Create(@Cast("SkScalar") float k1, @Cast("SkScalar") float k2, @Cast("SkScalar") float k3, @Cast("SkScalar") float k4);
    };

    /*
      * SkBlurDrawLooper.h
      */
	
	public static class SkBlurDrawLooper extends SkDrawLooper {
		static { Loader.load(Skia.class); }

	    //enum BlurFlags
	    public static final int kNone_BlurFlag = 0x00,
	    		kIgnoreTransform_BlurFlag = 0x01,
	    		kOverrideColor_BlurFlag = 0x02,
	    		kHighQuality_BlurFlag = 0x04,
	    		kAll_BlurFlag = 0x07;

        public SkBlurDrawLooper(float radius, float dx, float dy, int color) {
            allocate(radius, dx, dy, color);
            deallocator(new UnrefDeallocator(this));
        }
        @NoDeallocator
        public native void allocate(@Cast("SkScalar") float radius, @Cast("SkScalar") float dx, @Cast("SkScalar") float dy, @Cast("SkColor") int color);
        
		public SkBlurDrawLooper(float radius, float dx, float dy, int color, int flags) {
            allocate(radius, dx, dy, color, flags);
            deallocator(new UnrefDeallocator(this));
        }
		@NoDeallocator
		public native void allocate(@Cast("SkScalar") float radius, @Cast("SkScalar") float dx, @Cast("SkScalar") float dy, @Cast("SkColor") int color, @Cast("uint32_t") int flags/* = kNone_BlurFlag*/);
	}

	/*
	 * SkBlurImageFilter.h
	 */
	
	public static class SkBlurImageFilter extends SkImageFilter {
		static { Loader.load(Skia.class); }

		public SkBlurImageFilter(float sigmaX, float sigmaY) {
            allocate(sigmaX, sigmaY);
            deallocator(new UnrefDeallocator(this));
        }
		@NoDeallocator
		public native void allocate(@Cast("SkScalar") float sigmaX, @Cast("SkScalar") float sigmaY);
	}

    /*
     * SkBlurMaskFilter.h
     */

    public static class SkBlurMaskFilter extends Pointer {
        static { Loader.load(Skia.class); }

        //enum BlurStyle
        public static final int kNormal_BlurStyle = 0,
            kSolid_BlurStyle = 1,
            kOuter_BlurStyle = 2,
            kInner_BlurStyle = 3;

        //enum BlurFlags
        public static final int kNone_BlurFlag = 0x00,
            kIgnoreTransform_BlurFlag   = 0x01,
            kHighQuality_BlurFlag       = 0x02,
            kAll_BlurFlag = 0x03;

        public native static SkMaskFilter Create(@Cast("SkScalar") float radius, @Cast("SkBlurMaskFilter::BlurStyle") int style,
                                    @Cast("uint32_t") int flags/* = kNone_BlurFlag*/);

        public native static SkMaskFilter CreateEmboss(@Cast("const SkScalar*") float[] direction,
                                                       @Cast("SkScalar") float ambient, @Cast("SkScalar") float specular,
                                                       @Cast("SkScalar") float blurRadius);

        private SkBlurMaskFilter() {}
    };

    /*
     * SkColorMatrix.h
     */

    public static class SkColorMatrix extends Pointer {
        static { Loader.load(Skia.class); }

        public SkColorMatrix() { allocate(); }
        private native void allocate();

        public native void setIdentity();
        public native void setScale(@Cast("SkScalar") float rScale, @Cast("SkScalar") float gScale, @Cast("SkScalar") float bScale,
                                    @Cast("SkScalar") float aScale/* = SK_Scalar1*/);
//        public native void preScale(@Cast("SkScalar") float rScale, @Cast("SkScalar") float gScale, @Cast("SkScalar") float bScale,
//                                    @Cast("SkScalar") float aScale/* = SK_Scalar1*/);
//        public native void postScale(@Cast("SkScalar") float rScale, @Cast("SkScalar") float gScale, @Cast("SkScalar") float bScale,
//                                     @Cast("SkScalar") float aScale/* = SK_Scalar1*/);

        //enum Axis
        public static final int kR_Axis = 0,
            kG_Axis = 1,
            kB_Axis = 2;
        public native void setRotate(@Cast("SkColorMatrix::Axis") int axis, @Cast("SkScalar") float degrees);
        public native void setSinCos(@Cast("SkColorMatrix::Axis") int axis, @Cast("SkScalar") float sine, @Cast("SkScalar") float cosine);
        public native void preRotate(@Cast("SkColorMatrix::Axis") int axis, @Cast("SkScalar") float degrees);
        public native void postRotate(@Cast("SkColorMatrix::Axis") int axis, @Cast("SkScalar") float degrees);

        public native void setConcat(@Const @ByRef SkColorMatrix a, @Const @ByRef SkColorMatrix b);
        public native void preConcat(@Const @ByRef SkColorMatrix mat);
        public native void postConcat(@Const @ByRef SkColorMatrix mat);

        public native void setSaturation(@Cast("SkScalar") float sat);
        public native void setRGB2YUV();
        public native void setYUV2RGB();
    };

    /*
     * SkColorMatrixFilter.h
     */

    public static class SkColorMatrixFilter extends SkColorFilter {
        static { Loader.load(Skia.class); }

        public  SkColorMatrixFilter(SkColorMatrix mat) {
            allocate(mat);
            deallocator(new UnrefDeallocator(this));
        };
        @NoDeallocator
        private native void allocate(@Const @ByRef SkColorMatrix mat);

        public SkColorMatrixFilter(float[] array) {
            allocate(array);
            deallocator(new UnrefDeallocator(this));
        };
        @NoDeallocator
        private native void allocate(@Cast("const SkScalar*") float[] array20);

//        virtual void filterSpan(const SkPMColor src[], int count, SkPMColor[]) SK_OVERRIDE;
//        virtual void filterSpan16(const uint16_t src[], int count, uint16_t[]) SK_OVERRIDE;
//        virtual uint32_t getFlags() SK_OVERRIDE;
//        virtual bool asColorMatrix(SkScalar matrix[20]) SK_OVERRIDE;
    }

    /*
    * SkCornerPathEffect.h
    */
	
	public static class SkCornerPathEffect extends SkPathEffect {
		static { Loader.load(Skia.class); }

		public SkCornerPathEffect(float radius) {
            allocate(radius);
            deallocator(new UnrefDeallocator(this));
        }
		@NoDeallocator
		public native void allocate(@Cast("SkScalar") float radius);
	}

	/*
	 * SkDashPathEffect.h
	 */

	public static class SkDashPathEffect extends SkPathEffect {
		static { Loader.load(Skia.class); }

        public SkDashPathEffect(float[] intervals, float phase) {
            allocate(intervals, intervals.length, phase);
            deallocator(new UnrefDeallocator(this));
        }
        @NoDeallocator
        public native void allocate(@Cast("const SkScalar*") float[] intervals, int count, @Cast("SkScalar") float phase);
        public SkDashPathEffect(float[] intervals, float phase, boolean scaleToFit) {
            allocate(intervals, intervals.length, phase, scaleToFit);
            deallocator(new UnrefDeallocator(this));
        }
		@NoDeallocator
		public native void allocate(@Cast("const SkScalar*") float[] intervals, int count, @Cast("SkScalar") float phase, boolean scaleToFit/* = false*/);
	}

	/*
	 * SkDiscretePathEffect.h
	 */

	public static class SkDiscretePathEffect extends SkPathEffect {
		static { Loader.load(Skia.class); }

		public SkDiscretePathEffect(@Cast("SkScalar") float segLength, @Cast("SkScalar") float deviation) {
            allocate(segLength, deviation);
            deallocator(new UnrefDeallocator(this));
        }
		@NoDeallocator
		public native void allocate(@Cast("SkScalar") float segLength, @Cast("SkScalar") float deviation);
	}

	/*
	 * SkGradientShader.h
	 */
	
	public static class SkGradientShader extends Pointer {
		static { Loader.load(Skia.class); }

        public static SkShader CreateLinear(SkPoint[] pts, int[] colors, float[] pos, int mode) {
            return CreateLinear(SkPoint.constArray(pts), colors, pos, colors.length, mode);
        }
        public native static SkShader CreateLinear(@Const SkPoint pts, @Cast("const SkColor*") int[] colors, @Cast("const SkScalar*") float[] pos, int count, @Cast("SkShader::TileMode") int mode);
        public static SkShader CreateLinear(SkPoint[] pts, int[] colors, float[] pos, int mode, SkUnitMapper mapper) {
			return CreateLinear(SkPoint.constArray(pts), colors, pos, colors.length, mode, mapper);
		}
		public native static SkShader CreateLinear(@Const SkPoint pts, @Cast("const SkColor*") int[] colors, @Cast("const SkScalar*") float[] pos, int count, @Cast("SkShader::TileMode") int mode, SkUnitMapper mapper/* = NULL*/);

        public static SkShader CreateRadial(SkPoint center, float radius, int[] colors, float[] pos, int mode) {
            return CreateRadial(center, radius, colors, pos, colors.length, mode);
        }
        public native static SkShader CreateRadial(@Const @ByRef SkPoint center, @Cast("SkScalar") float radius, @Cast("const SkColor *") int[] colors, @Cast("const SkScalar *") float[] pos, int count, @Cast("SkShader::TileMode") int mode);

		public static SkShader CreateRadial(SkPoint center, float radius, int[] colors, float[] pos, int mode, SkUnitMapper mapper/* = NULL*/) {
			return CreateRadial(center, radius, colors, pos, colors.length, mode, mapper);
		}
        public native static SkShader CreateRadial(@Const @ByRef SkPoint center, @Cast("SkScalar") float radius, @Cast("const SkColor *") int[] colors, @Cast("const SkScalar *") float[] pos, int count, @Cast("SkShader::TileMode") int mode, SkUnitMapper mapper/* = NULL*/);

        public static SkShader CreateTwoPointRadial(SkPoint start, float startRadius, SkPoint end, float endRadius, int[] colors, float[] pos, int mode) {
            return CreateTwoPointRadial(start, startRadius, end, endRadius, colors, pos, colors.length, mode);
        }
        public native static SkShader CreateTwoPointRadial(@Const @ByRef SkPoint start, @Cast("SkScalar") float startRadius, @Const @ByRef SkPoint end, @Cast("SkScalar") float endRadius, @Cast("const SkColor *") int[] colors, @Cast("const SkScalar *") float[] pos, int count, @Cast("SkShader::TileMode") int mode);
        public static SkShader CreateTwoPointRadial(SkPoint start, float startRadius, SkPoint end, float endRadius, int[] colors, float[] pos, int mode, SkUnitMapper mapper/* = NULL*/) {
			return CreateTwoPointRadial(start, startRadius, end, endRadius, colors, pos, colors.length, mode, mapper);
		}
        public native static SkShader CreateTwoPointRadial(@Const @ByRef SkPoint start, @Cast("SkScalar") float startRadius, @Const @ByRef SkPoint end, @Cast("SkScalar") float endRadius, @Cast("const SkColor *") int[] colors, @Cast("const SkScalar *") float[] pos, int count, @Cast("SkShader::TileMode") int mode, SkUnitMapper mapper/* = NULL*/);

		public static SkShader CreateSweep(float cx, float cy, int[] colors, float[] pos, SkUnitMapper mapper/* = NULL*/) {
			return CreateSweep(cx, cy, colors, pos, colors.length, mapper);
		}
        public native static SkShader CreateSweep(@Cast("SkScalar") float cx, @Cast("SkScalar") float cy, @Cast("const SkColor *") int[] colors, @Cast("const SkScalar *") float[] pos, int count, SkUnitMapper mapper/* = NULL*/);
	}

    /*
     * SkMorphologyImageFilter.h
     */

    public static class SkMorphologyImageFilter extends SkImageFilter {
        static { Loader.load(Skia.class); }
        
        protected SkMorphologyImageFilter() {}
    }

    public static class SkDilateImageFilter extends SkMorphologyImageFilter {
        static { Loader.load(Skia.class); }

        public SkDilateImageFilter(int radiusX, int radiusY) {
            allocate(radiusX, radiusY);
            deallocator(new UnrefDeallocator(this));
        };
        @NoDeallocator
        private native void allocate(int radiusX, int radiusY);

//        public native boolean onFilterImage(Proxy proxy, @Const @ByRef SkBitmap src, @Const @ByRef SkMatrix matrix,
//                                   SkBitmap result, SkIPoint offset);
    }

    public static class SkErodeImageFilter extends SkMorphologyImageFilter {
        static { Loader.load(Skia.class); }

        public SkErodeImageFilter(int radiusX, int radiusY) {
            allocate(radiusX, radiusY);
            deallocator(new UnrefDeallocator(this));
        };
        @NoDeallocator
        private native void allocate(int radiusX, int radiusY);

//        public native boolean onFilterImage(Proxy proxy, @Const @ByRef SkBitmap src, @Const @ByRef SkMatrix,
//                SkBitmap result, SkIPoint offset);
    }

    /*
     * SkTableColorFilter.h
     */
    
    public static class SkTableColorFilter extends Pointer {
        static { Loader.load(Skia.class); }

        public native static SkColorFilter Create(@Cast("const uint8_t*") byte[] table);
        public native static SkColorFilter CreateARGB(@Cast("const uint8_t*") byte[] tableA,
                                                      @Cast("const uint8_t*") byte[] tableR,
                                                      @Cast("const uint8_t*") byte[] tableG,
                                                      @Cast("const uint8_t*") byte[] tableB);
    }

    /*
     * SkTestImageFilters.h
     */
    
    public static class SkComposeImageFilter extends SkImageFilter {
        static { Loader.load(Skia.class); }

        public SkComposeImageFilter(SkImageFilter outer, SkImageFilter inner) {
            allocate(outer, inner);
            deallocator(new UnrefDeallocator(this));
        }
        @NoDeallocator
        private native void allocate(SkImageFilter outer, SkImageFilter inner);
    };

    public static class SkOffsetImageFilter extends SkImageFilter {
        static { Loader.load(Skia.class); }

        public SkOffsetImageFilter(float dx, float dy) {
            allocate(dx, dy);
            deallocator(new UnrefDeallocator(this));
        }
        @NoDeallocator
        private native void allocate(@Cast("SkScalar") float dx, @Cast("SkScalar") float dy);
    };

    public static class SkMergeImageFilter extends SkImageFilter {
        static { Loader.load(Skia.class); }

        public SkMergeImageFilter(SkImageFilter first, SkImageFilter second) {
            allocate(first, second);
            deallocator(new UnrefDeallocator(this));
        }
        @NoDeallocator
        private native void allocate(SkImageFilter first, SkImageFilter second);
        public SkMergeImageFilter(SkImageFilter first, SkImageFilter second, int mode) {
            allocate(first, second, mode);
            deallocator(new UnrefDeallocator(this));
        }
        @NoDeallocator
        private native void allocate(SkImageFilter first, SkImageFilter second, @Cast("SkXfermode::Mode") int mode/* = SkXfermode::kSrcOver_Mode*/);

//        public SkMergeImageFilter(SkImageFilter[] filters, int count, int[] modes) { allocate(filters, count, modes); }
//        @NoDeallocator
//        private native void allocate(@Const SkImageFilter[] filters, int count, @Cast("const SkXfermode::Mode modes*") int[] modes/* = NULL*/);
    };

    public static class SkColorFilterImageFilter extends SkImageFilter {
        static { Loader.load(Skia.class); }

        public SkColorFilterImageFilter(SkColorFilter cf) {
            allocate(cf);
            deallocator(new UnrefDeallocator(this));
        }
        @NoDeallocator
        private native void allocate(SkColorFilter cf);
    };

    public static class SkDownSampleImageFilter extends SkImageFilter {
        static { Loader.load(Skia.class); }

        public SkDownSampleImageFilter(float scale) {
            allocate(scale);
            deallocator(new UnrefDeallocator(this));
        }
        @NoDeallocator
        private native void allocate(@Cast("SkScalar") float scale);
    };
}
