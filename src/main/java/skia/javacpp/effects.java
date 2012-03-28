package skia.javacpp;

import com.googlecode.javacpp.Loader;
import com.googlecode.javacpp.Pointer;
import com.googlecode.javacpp.annotation.ByRef;
import com.googlecode.javacpp.annotation.Cast;
import com.googlecode.javacpp.annotation.Const;
import com.googlecode.javacpp.annotation.NoDeallocator;
import com.googlecode.javacpp.annotation.Platform;
import com.googlecode.javacpp.annotation.Properties;

import static skia.javacpp.core.*;

@Properties({
	@Platform(include={"Sk1DPathEffect.h", "Sk2DPathEffect.h", "SkBlurDrawLooper.h", "SkBlurImageFilter.h", "SkCornerPathEffect.h", "SkDashPathEffect.h", "SkDiscretePathEffect.h", "SkGradientShader.h", "SkUnitMapper.h"})
})
public class effects {
	static { Loader.load(Skia.class); }
	
	/*
	 * Sk1DPathEffect.h
	 */
	
	public static class Sk1DPathEffect extends SkPathEffect {
		static { Loader.load(Skia.class); }
		
		public Sk1DPathEffect(Pointer pointer) { super(pointer); }
		protected Sk1DPathEffect() {};
	}
	
	public static class SkPath1DPathEffect extends Sk1DPathEffect {
		static { Loader.load(Skia.class); }
		
	    //enum Style
	    public static final int kTranslate_Style = 0,
	        kRotate_Style = 1,
	        kMorph_Style = 2;

		public SkPath1DPathEffect(Pointer pointer) { super(pointer); }
		public SkPath1DPathEffect(SkPath path, float advance, float phase, int style) { allocate(path, advance, phase, style); }
		@NoDeallocator
		public native void allocate(@Const @ByRef SkPath path, @Cast("SkScalar") float advance, @Cast("SkScalar") float phase, @Cast("SkPath1DPathEffect::Style") int style);
	}

	/*
	 * Sk2DPathEffect.h
	 */
	
	public static class Sk2DPathEffect extends SkPathEffect {
		static { Loader.load(Skia.class); }
		
		public Sk2DPathEffect(Pointer pointer) { super(pointer); }
		protected Sk2DPathEffect() {};
	}
	
	public static class SkPath2DPathEffect extends Sk2DPathEffect {
		static { Loader.load(Skia.class); }
		
	    //enum Style
	    public static final int kTranslate_Style = 0,
	        kRotate_Style = 1,
	        kMorph_Style = 2;

		public SkPath2DPathEffect(Pointer pointer) { super(pointer); }
		public SkPath2DPathEffect(SkMatrix matrix, SkPath path) { allocate(matrix, path); }
		@NoDeallocator
		public native void allocate(@Const @ByRef SkMatrix matrix, @Const @ByRef SkPath path);
	}
	
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

        public SkBlurDrawLooper(float radius, float dx, float dy, int color) { allocate(radius, dx, dy, color); }
        @NoDeallocator
        public native void allocate(@Cast("SkScalar") float radius, @Cast("SkScalar") float dx, @Cast("SkScalar") float dy, @Cast("SkColor") int color);
		public SkBlurDrawLooper(float radius, float dx, float dy, int color, int flags) { allocate(radius, dx, dy, color, flags); }
		@NoDeallocator
		public native void allocate(@Cast("SkScalar") float radius, @Cast("SkScalar") float dx, @Cast("SkScalar") float dy, @Cast("SkColor") int color, @Cast("uint32_t") int flags/* = kNone_BlurFlag*/);
	}

	/*
	 * SkBlurImageFilter.h
	 */
	
	public static class SkBlurImageFilter extends SkImageFilter {
		static { Loader.load(Skia.class); }

		public SkBlurImageFilter(Pointer pointer) { super(pointer); }
		public SkBlurImageFilter(float sigmaX, float sigmaY) { allocate(sigmaX, sigmaY); }
		@NoDeallocator
		public native void allocate(@Cast("SkScalar") float sigmaX, @Cast("SkScalar") float sigmaY);
	}

	/*
	 * SkCornerPathEffect.h
	 */
	
	public static class SkCornerPathEffect extends SkPathEffect {
		static { Loader.load(Skia.class); }

		public SkCornerPathEffect(Pointer pointer) { super(pointer); }
		public SkCornerPathEffect(float radius) { allocate(radius); }
		@NoDeallocator
		public native void allocate(@Cast("SkScalar") float radius);
	}

	/*
	 * SkDashPathEffect.h
	 */

	public static class SkDashPathEffect extends SkPathEffect {
		static { Loader.load(Skia.class); }

		public SkDashPathEffect(Pointer pointer) { super(pointer); }
		public SkDashPathEffect(float[] intervals, float phase, boolean scaleToFit) { allocate(intervals, intervals.length, phase, scaleToFit); }
		@NoDeallocator
		public native void allocate(@Cast("const SkScalar*") float[] intervals, int count, @Cast("SkScalar") float phase, boolean scaleToFit/* = false*/);
	}

	/*
	 * SkDiscretePathEffect.h
	 */

	public static class SkDiscretePathEffect extends SkPathEffect {
		static { Loader.load(Skia.class); }

		public SkDiscretePathEffect(Pointer pointer) { super(pointer); }
		public SkDiscretePathEffect(@Cast("SkScalar") float segLength, @Cast("SkScalar") float deviation) { allocate(segLength, deviation); }
		@NoDeallocator
		public native void allocate(@Cast("SkScalar") float segLength, @Cast("SkScalar") float deviation);
	}

	/*
	 * SkGradientShader.h
	 */
	
	public static class SkGradientShader extends Pointer {
		static { Loader.load(Skia.class); }

        public static SkShader CreateLinear(SkPoint[] pts, int[] colors, float[] pos, int mode) {
            return CreateLinear(pts, colors, pos, mode, null);
        }
		public static SkShader CreateLinear(SkPoint[] pts, int[] colors, float[] pos, int mode, SkUnitMapper mapper) {
			SkPoint ptsPtr = new SkPoint(pts.length);
			for (int i = 0; i < pts.length; i++) {
				ptsPtr.position(i).copy(pts[i]);
			}
			return CreateLinear(ptsPtr, colors, pos, colors.length, mode, mapper);
		}
		private static native SkShader CreateLinear(@Const SkPoint pts, @Cast("const SkColor *") int[] colors, @Cast("const SkScalar *") float[] pos, int count, @Cast("SkShader::TileMode") int mode, SkUnitMapper mapper/* = NULL*/);
		public static SkShader CreateRadial(SkPoint center, float radius, int[] colors, float[] pos, int mode, SkUnitMapper mapper/* = NULL*/) {
			return CreateRadial(center, radius, colors, pos, colors.length, mode, mapper);
		}
		private static native SkShader CreateRadial(@Const @ByRef SkPoint center, @Cast("SkScalar") float radius, @Cast("const SkColor *") int[] colors, @Cast("const SkScalar *") float[] pos, int count, @Cast("SkShader::TileMode") int mode, SkUnitMapper mapper/* = NULL*/);
		public static SkShader CreateTwoPointRadial(SkPoint start, float startRadius, SkPoint end, float endRadius, int[] colors, float[] pos, int mode, SkUnitMapper mapper/* = NULL*/) {
			return CreateTwoPointRadial(start, startRadius, end, endRadius, colors, pos, colors.length, mode, mapper);
		}
		private static native SkShader CreateTwoPointRadial(@Const @ByRef SkPoint start, @Cast("SkScalar") float startRadius, @Const @ByRef SkPoint end, @Cast("SkScalar") float endRadius, @Cast("const SkColor *") int[] colors, @Cast("const SkScalar *") float[] pos, int count, @Cast("SkShader::TileMode") int mode, SkUnitMapper mapper/* = NULL*/);

		public static SkShader CreateSweep(float cx, float cy, int[] colors, float[] pos, SkUnitMapper mapper/* = NULL*/) {
			return CreateSweep(cx, cy, colors, pos, colors.length, mapper);
		}
		private static native SkShader CreateSweep(@Cast("SkScalar") float cx, @Cast("SkScalar") float cy, @Cast("const SkColor *") int[] colors, @Cast("const SkScalar *") float[] pos, int count, SkUnitMapper mapper/* = NULL*/);
	}
}
