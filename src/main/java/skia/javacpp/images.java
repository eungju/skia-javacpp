package skia.javacpp;

import com.googlecode.javacpp.IntPointer;
import com.googlecode.javacpp.Loader;
import com.googlecode.javacpp.Pointer;
import com.googlecode.javacpp.annotation.ByRef;
import com.googlecode.javacpp.annotation.Cast;
import com.googlecode.javacpp.annotation.Const;
import com.googlecode.javacpp.annotation.Platform;
import com.googlecode.javacpp.annotation.Properties;

import static skia.javacpp.core.*;

@Properties({
	@Platform(include={"SkImageDecoder.h", "SkImageEncoder.h", "SkBitmap.h"})
})
public class images {
	static { Loader.load(Skia.class); }

	public static class SkImageDecoder  extends Pointer {
		static { Loader.load(Skia.class); }

	    //enum Format
		public static final int kUnknown_Format = 0,
				kBMP_Format = 1,
				kGIF_Format = 2,
				kICO_Format = 3,
				kJPEG_Format = 4,
				kPNG_Format = 5,
				kWBMP_Format = 6;
		
	    public native @Cast("SkImageDecoder::Format") int getFormat();
	    public native boolean getDitherImage();
	    public native void setDitherImage(boolean dither);
	    
	    //enum Mode 
	    public static final int kDecodeBounds_Mode = 0,
	    		kDecodePixels_Mode = 1;

	    public static native boolean DecodeFile(String file, SkBitmap bitmap, @Cast("SkBitmap::Config") int prefConfig, @Cast("SkImageDecoder::Mode") int mode, @Cast("SkImageDecoder::Format*") IntPointer format/* = NULL*/);
	    public static native boolean DecodeFile(String file, SkBitmap bitmap);
	}
	
	public static class SkImageEncoder extends Pointer {
		static { Loader.load(Skia.class); }

		public static native boolean EncodeFile(String file, @Const @ByRef SkBitmap bitmap, @Cast("SkImageEncoder::Type") int type, int quality);
		
		//enum Type
		public static final int kJPEG_Type = 0,
				kPNG_Type = 1;
		
	    public static final int kDefaultQuality = 80;
	}
}
