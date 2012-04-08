package skia.javacpp;

import com.googlecode.javacpp.Loader;
import com.googlecode.javacpp.annotation.NoDeallocator;
import com.googlecode.javacpp.annotation.Platform;
import com.googlecode.javacpp.annotation.Properties;

import static skia.javacpp.core.*;

@Properties({
	@Platform(include={"SkDeferredCanvas.h", "SkUnitMappers.h"})
})
public class utils {
	static { Loader.load(Skia.class); }

    /*
     * SkDeferredCanvas.h
     */

	public static class SkDeferredCanvas extends SkCanvas {
		static { Loader.load(Skia.class); }

        public SkDeferredCanvas() {
            allocate();
            deallocator(new UnrefDeallocator(this));
        }
        @NoDeallocator
        private native void allocate();

        public SkDeferredCanvas(SkDevice device) {
            allocate(device);
            deallocator(new UnrefDeallocator(this));
        };
        @NoDeallocator
        private native void allocate(SkDevice device);

        //TODO: public SkDeferredCanvas(SkDevice device, DeviceContext deviceContext) { allocate(device, deviceContext); };
    }

    /*
     * SkUnitMappers.h
     */

    public static class SkDiscreteMapper extends SkUnitMapper {
        static { Loader.load(Skia.class); }

        public SkDiscreteMapper(int segments) {
            allocate(segments);
            deallocator(new UnrefDeallocator(this));
        }
        @NoDeallocator
        private native void allocate(int segments);
    };

    public static class SkCosineMapper extends SkUnitMapper {
        static { Loader.load(Skia.class); }

        public SkCosineMapper() {
            allocate();
            deallocator(new UnrefDeallocator(this));
        }
        @NoDeallocator
        private native void allocate();
    };
}
