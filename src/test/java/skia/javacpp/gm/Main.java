package skia.javacpp.gm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static skia.javacpp.core.*;
import static skia.javacpp.images.*;
import static skia.javacpp.utils.*;

public class Main {
    static final boolean CAN_IMAGE_PDF = false;

    //typedef int ErrorBitfield;
    static final int ERROR_NONE                    = 0x00;
    static final int ERROR_NO_GPU_CONTEXT          = 0x01;
    static final int ERROR_PIXEL_MISMATCH          = 0x02;
    static final int ERROR_DIMENSION_MISMATCH      = 0x04;
    static final int ERROR_READING_REFERENCE_IMAGE = 0x08;
    static final int ERROR_WRITING_REFERENCE_IMAGE = 0x10;

    static class Iter {
        public static final List<Class> gms = Arrays.<Class>asList(
                PointsGM.class,
                StrokeFillGM.class,
                Poly2PolyGM.class,
                GradTextGM.class,
                StrokeRectGM.class,
                ShadowsGM.class,
                NoColorBleedGM.class,
                TilingGM.class,
                TinyBitmapGM.class,
                VertTextGM.class,
                VertText2GM.class,
                XfermodesGM.class);

        private Iterator<Class> i;
        
        public Iter() {
            reset();
        }

        public void reset() {
            i = gms.iterator();
        }
        
        public GM next() {
            if (i.hasNext()) {
                Class clazz = i.next();
                try {
                    return (GM) clazz.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
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
            invokeGM(gm, canvas);
            canvas.flush();
            canvas.unref();
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
            //TODO: return compare_to_reference_image(readPath, name, bitmap, diffPath, renderModeDescriptor);
            return ERROR_NONE;
        } else if (comparisonBitmap != null) {
            //TODO: return compare_to_reference_image(name, bitmap, comparisonBitmap, diffPath, renderModeDescriptor);
            return ERROR_NONE;
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