package skia.javacpp;

import com.googlecode.javacpp.IntPointer;
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
	@Platform(include={"SkDeferredCanvas.h"})
})
public class utils {
	static { Loader.load(Skia.class); }

	public static class SkDeferredCanvas extends SkCanvas {
		static { Loader.load(Skia.class); }

        public SkDeferredCanvas() { allocate(); }
        @NoDeallocator
        private native void allocate();
        public SkDeferredCanvas(SkDevice device) { allocate(device); };
        @NoDeallocator
        private native void allocate(SkDevice device);
        //TODO: public SkDeferredCanvas(SkDevice device, DeviceContext deviceContext) { allocate(device, deviceContext); };
    }
}
