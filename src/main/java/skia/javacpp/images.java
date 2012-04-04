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
	@Platform(include={"SkImageDecoder.h", "SkImageEncoder.h"})
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

        public static class Peeker extends SkRefCnt {
            static { Loader.load(Skia.class); }

            protected Peeker() {}

            public native boolean peek(String tag, @Const Pointer data, @Cast("size_t") int length);
        };

        public native Peeker getPeeker();
        public native Peeker setPeeker(Peeker peeker);

        public static class Chooser extends SkRefCnt {
            public native void begin(int count);
            public native void inspect(int index, @Cast("SkBitmap::Config") int config, int width, int height);
            public native int choose();
        };

        public native Chooser getChooser();
        public native Chooser setChooser(Chooser chooser);

        public native void setPrefConfigTable(@Cast("const SkBitmap::Config*") int[] pref);

        public native SkBitmap.Allocator getAllocator();
        public native SkBitmap.Allocator setAllocator(SkBitmap.Allocator allocator);

        public native int getSampleSize();
        public native void setSampleSize(int size);

        public native void resetSampleSize();

        public native void cancelDecode();

        //enum Mode
	    public static final int kDecodeBounds_Mode = 0,
	    		kDecodePixels_Mode = 1;

        public native boolean decode(SkStream stream, SkBitmap bitmap, @Cast("SkBitmap::Config") int pref, @Cast("SkImageDecoder::Mode") int mode);
        public native boolean decode(SkStream stream, SkBitmap bitmap, @Cast("SkImageDecoder::Mode") int mode);

        public native static SkImageDecoder Factory(SkStream stream);

        public native static boolean DecodeFile(String file, SkBitmap bitmap, @Cast("SkBitmap::Config") int prefConfig, @Cast("SkImageDecoder::Mode") int mode, @Cast("SkImageDecoder::Format*") IntPointer format/* = NULL*/);
	    public native static boolean DecodeFile(String file, SkBitmap bitmap);
        public native static boolean DecodeMemory(@Const Pointer buffer, @Cast("size_t") int size, SkBitmap bitmap,
                                 @Cast("SkBitmap::Config") int prefConfig, @Cast("SkImageDecoder::Mode") int mode,
                                 @Cast("SkImageDecoder::Format*") IntPointer format/* = NULL*/);
        public native static boolean DecodeMemory(@Const Pointer buffer, @Cast("size_t") int size, SkBitmap bitmap);
        public native static boolean DecodeStream(SkStream stream, SkBitmap bitmap,
                                 @Cast("SkBitmap::Config") int prefConfig, @Cast("SkImageDecoder::Mode") int mode,
                                 @Cast("SkImageDecoder::Format*") IntPointer format/* = NULL*/);
        public native static boolean DecodeStream(SkStream stream, SkBitmap bitmap);

        public native static @Cast("SkBitmap::Config") int GetDeviceConfig();
        public native static void SetDeviceConfig(@Cast("SkBitmap::Config") int config);
    }
	
	public static class SkImageEncoder extends Pointer {
		static { Loader.load(Skia.class); }

        //enum Type
        public static final int kJPEG_Type = 0,
                kPNG_Type = 1;

        public native static SkImageEncoder Create(@Cast("SkImageEncoder::Type") int type);

        public static final int kDefaultQuality = 80;

        public native boolean encodeFile(String file, @Const @ByRef SkBitmap bitmap, int quality);
        public native boolean encodeStream(SkWStream stream, @Const @ByRef SkBitmap bitmap, int quality);

        public native static boolean EncodeFile(String file, @Const @ByRef SkBitmap bitmap, @Cast("SkImageEncoder::Type") int type, int quality);
        public native static boolean EncodeStream(SkWStream stream, @Const @ByRef SkBitmap bitmap, @Cast("SkImageEncoder::Type") int type, int quality);
    }
}
