package skia.javacpp.gm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static skia.javacpp.core.*;
import static skia.javacpp.images.*;
import static skia.javacpp.utils.*;

public class gmmain {
    static final boolean CAN_IMAGE_PDF = false;

    //typedef int ErrorBitfield;
    static final int ERROR_NONE                    = 0x00;
    static final int ERROR_NO_GPU_CONTEXT          = 0x01;
    static final int ERROR_PIXEL_MISMATCH          = 0x02;
    static final int ERROR_DIMENSION_MISMATCH      = 0x04;
    static final int ERROR_READING_REFERENCE_IMAGE = 0x08;
    static final int ERROR_WRITING_REFERENCE_IMAGE = 0x10;

    static class Iter {
        public static final List<GMRegistry.Factory> gms = Arrays.<GMRegistry.Factory>asList(
                BitmapCopyGM.MyFactory,
                FilterGM.MyFactory,
                BitmapScrollGM.MyFactory,
                BlursGM.MyFactory,
                ColorMatrixGM.MyFactory,
                ComplexClipGM.gFact0,
                ComplexClipGM.gFact1,
                ComplexClip2GM.factory,
                ConvexPathsGM.factory,
                CubicPathGM.factory,
                CubicClosePathGM.factory,
                DegenerateSegmentsGM.factory,
//                DrawBitmapRectGM.factory,
                EmptyPathGM.factory,
                FillTypeGM.factory,
                FillTypePerspGM.factory,
                FontScalerGM.factory,
                GradientsGM.factory,
                GradTextGM.factory,
                HairModesGM.factory,
                ImageBlurGM.factory,
                LcdTextGM.factory,
                LinePathGM.factory,
                LineClosePathGM.factory,
                MorphologyGM.factory,
                NoColorBleedGM.factory,
                PathEffectGM.factory,
                PathFillGM.factory,
                PathReverseGM.factory,
                PointsGM.factory,
                Poly2PolyGM.factory,
                QuadPathGM.factory,
                QuadClosePathGM.factory,
                ShaderBoundsGM.factory,
                ShaderTextGM.factory,
                ShadowsGM.factory,
                ShapesGM.factory,
                StrokeFillGM.factory,
                StrokeRectGM.factory,
                StrokesGM.factory,
                Strokes2GM.factory,
                TableColorFilterGM.factory,
                TestImageFiltersGM.factory,
                TilingGM.factory,
                TinyBitmapGM.factory,
                VertTextGM.factory,
                VertText2GM.factory,
                XfermodesGM.factory
        );

        private Iterator<GMRegistry.Factory> i;
        
        public Iter() {
            reset();
        }

        public void reset() {
            i = gms.iterator();
        }
        
        public GM next() {
            if (i.hasNext()) {
                GMRegistry.Factory fact = i.next();
                return fact.apply();
            }
            return null;
        }
    }

    static String make_name(String shortName, String configName) {
        return shortName + "_" + configName;
    }

    static String make_filename(String path, String pathSuffix, String name, String suffix) {
        StringBuilder filename = new StringBuilder(path);
        if (path.endsWith("/")) {
            filename.delete(path.length() - 1, 1);
        }
        filename.append(pathSuffix);
        filename.append("/");
        filename.append(String.format("%s.%s", name, suffix));
        return filename.toString();
    }

    static void force_all_opaque(SkBitmap bitmap) {
        bitmap.lockPixels();
        try {
            for (int y = 0; y < bitmap.height(); y++) {
                for (int x = 0; x < bitmap.width(); x++) {
                    int v = bitmap.getAddr32(x, y).get();
                    bitmap.getAddr32(x, y).put(v | (SK_A32_MASK << SK_A32_SHIFT));
                }
            }
        } finally {
            bitmap.unlockPixels();
        }
    }

    static boolean write_bitmap(String path, SkBitmap bitmap) {
        SkBitmap copy = new SkBitmap();
        bitmap.copyTo(copy, SkBitmap.kARGB_8888_Config);
        force_all_opaque(copy);
        return SkImageEncoder.EncodeFile(path, copy, SkImageEncoder.kPNG_Type, 100);
    }

    static int compute_diff_pmcolor(int c0, int c1) {
        int dr = SkGetPackedR32(c0) - SkGetPackedR32(c1);
        int dg = SkGetPackedG32(c0) - SkGetPackedG32(c1);
        int db = SkGetPackedB32(c0) - SkGetPackedB32(c1);
        return SkPackARGB32(0xFF, SkAbs32(dr), SkAbs32(dg), SkAbs32(db));
    }

    static void compute_diff(SkBitmap target, SkBitmap base,
                             SkBitmap diff) {
        diff.lockPixels();
        try {
            final int w = target.width();
            final int h = target.height();
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    int c0 = base.getAddr32(x, y).get();
                    int c1 = target.getAddr32(x, y).get();
                    int d = 0;
                    if (c0 != c1) {
                        d = compute_diff_pmcolor(c0, c1);
                    }
                    diff.getAddr32(x, y).put(d);
                }
            }
        } finally {
            diff.unlockPixels();
        }
    }

    static int compare(final SkBitmap target, final SkBitmap base,
                                 String name,
                                 String renderModeDescriptor,
                                 SkBitmap diff) {
        SkBitmap copy = new SkBitmap();
        SkBitmap bm = target;
        if (target.config() != SkBitmap.kARGB_8888_Config) {
            target.copyTo(copy, SkBitmap.kARGB_8888_Config);
            bm = copy;
        }
        SkBitmap baseCopy = new SkBitmap();
        SkBitmap bp = base;
        if (base.config() != SkBitmap.kARGB_8888_Config) {
            base.copyTo(baseCopy, SkBitmap.kARGB_8888_Config);
            bp = baseCopy;
        }

        force_all_opaque(bm);
        force_all_opaque(bp);

        final int w = bm.width();
        final int h = bm.height();
        if (w != bp.width() || h != bp.height()) {
            SkDebugf(
                    "---- %s dimensions mismatch for %s base [%d %d] current [%d %d]\n",
                    renderModeDescriptor, name,
                    bp.width(), bp.height(), w, h);
            return ERROR_DIMENSION_MISMATCH;
        }

        bm.lockPixels();
        try {
            bp.lockPixels();
            try {
                for (int y = 0; y < h; y++) {
                    for (int x = 0; x < w; x++) {
                        int c0 = bp.getAddr32(x, y).get();
                        int c1 = bm.getAddr32(x, y).get();
                        if (c0 != c1) {
                            SkDebugf(
                                    "----- %s pixel mismatch for %s at [%d %d] base 0x%08X current 0x%08X\n",
                                    renderModeDescriptor, name, x, y, c0, c1);

                            if (diff != null) {
                                diff.setConfig(SkBitmap.kARGB_8888_Config, w, h);
                                diff.allocPixels();
                                compute_diff(bm, bp, diff);
                            }
                            return ERROR_PIXEL_MISMATCH;
                        }
                    }
                }

                // they're equal
                return ERROR_NONE;
            } finally {
                bp.unlockPixels();
            }
        } finally {
            bm.unlockPixels();
        }
    }

    //enum Backend
    public static final int kRaster_Backend = 0,
        kGPU_Backend = 1,
        kPDF_Backend = 2,
        kXPS_Backend = 3;

    static class ConfigData {
        int fConfig;
        int fBackend;
        String fName;

        public ConfigData(int config, int backend, String name) {
            fConfig = config;
            fBackend = backend;
            fName = name;
        }
    };

    static void setup_bitmap(ConfigData gRec, SkISize size, SkBitmap bitmap) {
        bitmap.setConfig(gRec.fConfig, size.width(), size.height());
        bitmap.allocPixels();
        bitmap.eraseColor(0);
    }

    static void invokeGM(GM gm, SkCanvas canvas) {
        invokeGM(gm, canvas, false);
    }
    
    static void invokeGM(GM gm, SkCanvas canvas, boolean isPDF/* = false*/) {
        if (!isPDF) {
            canvas.setMatrix(gm.getInitialTransform());
        }
        //TODO: installFilter(canvas);
        gm.draw(canvas);
        canvas.setDrawFilter(null);
    }

    static int generate_image(GM gm, ConfigData gRec, SkBitmap bitmap, boolean deferred) {
        SkISize size = gm.getISize();
        setup_bitmap(gRec, size, bitmap);

        if (gRec.fBackend == kRaster_Backend) {
            SkCanvas canvas;
            if (deferred) {
                canvas = new SkDeferredCanvas();
                canvas.setDevice(new SkDevice(bitmap)).unref();
            } else {
                canvas = new SkCanvas(bitmap);
            }
            new SkAutoUnref(canvas);
            invokeGM(gm, canvas);
            canvas.flush();
        } else {  // GPU
//            if (NULL == context) {
//                return ERROR_NO_GPU_CONTEXT;
//            }
//            SkCanvas gc;
//            if (deferred) {
//                gc = new SkDeferredCanvas();
//            } else {
//                gc = new SkGpuCanvas(context, rt);
//            }
//        SkAutoUnref gcUnref(gc);
//        gc.setDevice(new SkGpuDevice(context, rt)).unref();
//        invokeGM(gm, gc);
//        bitmap.setConfig(SkBitmap.kARGB_8888_Config, size.fWidth, size.fHeight);
//        gc.readPixels(bitmap, 0, 0);
        }
        return ERROR_NONE;
    }

    static int write_reference_image(ConfigData gRec,
                                               String writePath,
                                               String renderModeDescriptor,
                                               String name,
                                               SkBitmap bitmap,
                                               SkDynamicMemoryWStream document) {
        String path = null;
        boolean success = false;
        if (gRec.fBackend == kRaster_Backend ||
                gRec.fBackend == kGPU_Backend ||
                (gRec.fBackend == kPDF_Backend && CAN_IMAGE_PDF)) {

            path = make_filename(writePath, renderModeDescriptor, name, "png");
            success = write_bitmap(path, bitmap);
        }
        if (kPDF_Backend == gRec.fBackend) {
            path = make_filename(writePath, renderModeDescriptor, name, "pdf");
            //TODO: success = write_document(path, document);
        }
        if (kXPS_Backend == gRec.fBackend) {
            path = make_filename(writePath, renderModeDescriptor, name, "xps");
            //TODO: success = write_document(path, document);
        }
        if (success) {
            return ERROR_NONE;
        } else {
            System.err.print(String.format("FAILED to write %s\n", path));
            return ERROR_WRITING_REFERENCE_IMAGE;
        }
    }

    static int compare_to_reference_image(final String name,
                                                    SkBitmap bitmap,
                                                    final SkBitmap comparisonBitmap,
                                                    final String diffPath,
                                                    final String renderModeDescriptor) {
        int errors;
        SkBitmap diffBitmap = new SkBitmap();
        errors = compare(bitmap, comparisonBitmap, name, renderModeDescriptor,
                diffPath != null ? diffBitmap : null);
        if ((ERROR_NONE == errors) && diffPath != null) {
            String diffName = make_filename(diffPath, "", name, ".diff.png");
            if (!write_bitmap(diffName, diffBitmap)) {
                errors |= ERROR_WRITING_REFERENCE_IMAGE;
            }
        }
        return errors;
    }

    static int compare_to_reference_image(final String readPath,
                                                    final String name,
                                                    SkBitmap bitmap,
                                                    final String diffPath,
                                                    final String renderModeDescriptor) {
        String path = make_filename(readPath, "", name, "png");
        SkBitmap orig = new SkBitmap();
        if (SkImageDecoder.DecodeFile(path, orig,
                SkBitmap.kARGB_8888_Config,
                SkImageDecoder.kDecodePixels_Mode, null)) {
            return compare_to_reference_image(name, bitmap,
                    orig, diffPath,
                    renderModeDescriptor);
        } else {
            System.err.print(String.format("FAILED to read %s\n", path));
            return ERROR_READING_REFERENCE_IMAGE;
        }
    }

    static int handle_test_results(GM gm, ConfigData gRec,
                                             String writePath,
                                             String readPath,
                                             String diffPath,
                                             String renderModeDescriptor,
                                             SkBitmap bitmap,
                                             SkDynamicMemoryWStream pdf,
                                             SkBitmap comparisonBitmap) {
        String name = make_name(gm.shortName(), gRec.fName);

        if (writePath != null) {
            return write_reference_image(gRec, writePath, renderModeDescriptor, name, bitmap, pdf);
        } else if (readPath != null && (
                gRec.fBackend == kRaster_Backend ||
                        gRec.fBackend == kGPU_Backend ||
                        (gRec.fBackend == kPDF_Backend && CAN_IMAGE_PDF))) {
            return compare_to_reference_image(readPath, name, bitmap, diffPath, renderModeDescriptor);
        } else if (comparisonBitmap != null) {
            return compare_to_reference_image(name, bitmap, comparisonBitmap, diffPath, renderModeDescriptor);
        } else {
            return ERROR_NONE;
        }
    }

    static int test_drawing(GM gm, ConfigData gRec, String writePath,
                                      String readPath,
                                      String diffPath,
                                      SkBitmap bitmap) {
        SkDynamicMemoryWStream document = null;

        if (gRec.fBackend == kRaster_Backend || gRec.fBackend == kGPU_Backend) {
            int errors = generate_image(gm, gRec, bitmap, false);
            if (ERROR_NONE != errors) {
                return errors;
            }
        }
        return handle_test_results(gm, gRec, writePath, readPath, diffPath, "", bitmap, document, null);
    }

    static final ConfigData gRec[] = new ConfigData[] {
            new ConfigData(SkBitmap.kARGB_8888_Config, kRaster_Backend, "8888"),
            new ConfigData(SkBitmap.kARGB_4444_Config, kRaster_Backend, "4444"),
            new ConfigData(SkBitmap.kRGB_565_Config, kRaster_Backend, "565")
    };

    static boolean skip_name(List<String> array, String name) {
        if (array.isEmpty()) {
            return false;
        }
        for (int i = 0; i < array.size(); ++i) {
            if (array.indexOf(name) >= 0) {
                return false;
            }
        }
        return true;
    }

	public static void main(String[] args) {
        String writePath = null;
        String readPath = null;
        String diffPath = null;
        String resourcePath = null;

        List<String> fMatches = new ArrayList<String>();

        boolean doPDF = true;
        boolean doReplay = true;
        boolean doSerialize = false;
        boolean useMesa = false;
        boolean useDebugGL = false;
        boolean doDeferred = true;
        boolean disableTextureCache = false;

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.equals("-w")) {
                writePath = args[++i];
            } else if (arg.equals("-r")) {
                readPath = args[++i];
            } else if (arg.equals("-d")) {
                diffPath = args[++i];
            } else if (arg.equals("-i")) {
                resourcePath = args[++i];
            }
        }
        
        GM.SetResourcePath(resourcePath);

        int maxW = -1;
        int maxH = -1;
        Iter iter = new Iter();
        GM gm;
        while ((gm = iter.next()) != null) {
            SkISize size = gm.getISize();
            maxW = SkMax32(size.width(), maxW);
            maxH = SkMax32(size.height(), maxH);
        }

        if (readPath != null) {
            System.err.print(String.format("reading from %s\n", readPath));
        } else if (writePath != null) {
            System.err.print(String.format("writing to %s\n", writePath));
        }

        if (resourcePath != null) {
            System.err.print(String.format("reading resources from %s\n", resourcePath));
        }

        int testsRun = 0;
        int testsPassed = 0;
        int testsFailed = 0;
        int testsMissingReferenceImages = 0;

        iter.reset();
        while ((gm = iter.next()) != null) {
            String shortName = gm.shortName();
            if (skip_name(fMatches, shortName)) {
                continue;
            }

            SkISize size = gm.getISize();
            SkDebugf("drawing... %s [%d %d]\n", shortName, size.width(), size.height());
            SkBitmap forwardRenderedBitmap = new SkBitmap();

            for (int i = 0; i < gRec.length; i++) {
                // Skip any tests that we don't even need to try.
                int gmFlags = gm.getFlags();
                if ((kPDF_Backend == gRec[i].fBackend) &&
                        (!doPDF || (gmFlags & GM.kSkipPDF_Flag) != 0))
                {
                    continue;
                }

                int testErrors = ERROR_NONE;

                if (ERROR_NONE == testErrors) {
                    testErrors |= test_drawing(gm, gRec[i], writePath, readPath, diffPath, forwardRenderedBitmap);
                }

                testsRun++;
                if (ERROR_NONE == testErrors) {
                    testsPassed++;
                } else if ((ERROR_READING_REFERENCE_IMAGE & testErrors) != 0) {
                    testsMissingReferenceImages++;
                } else {
                    testsFailed++;
                }
            }
        }

        System.out.print(String.format("Ran %d tests: %d passed, %d failed, %d missing reference images\n",
                testsRun, testsPassed, testsFailed, testsMissingReferenceImages));

        System.exit((0 == testsFailed) ? 0 : -1);
	}
}