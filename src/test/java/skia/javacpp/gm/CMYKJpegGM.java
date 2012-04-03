package skia.javacpp.gm;

import static skia.javacpp.core.*;
import static skia.javacpp.images.*;

public class CMYKJpegGM extends GM {
    public CMYKJpegGM() {

        // parameters to the "decode" call
        boolean dither = false;
        int prefConfig = SkBitmap.kARGB_8888_Config;

        String filename = gResourcePath;
        if (!filename.endsWith("/") && !filename.endsWith("\\")) {
            filename += "/";
        }

        filename += "CMYK.jpg";

        SkFILEStream stream = new SkFILEStream(filename);
        SkImageDecoder codec = SkImageDecoder.Factory(stream);
        if (codec != null) {
            stream.rewind();
            codec.setDitherImage(dither);
            codec.decode(stream, fBitmap, prefConfig,
                    SkImageDecoder.kDecodePixels_Mode);
        }
    }

    @Override
    protected String onShortName() {
        return "cmykjpeg";
    }

    @Override
    protected SkISize onISize() {
        return make_isize(640, 480);
    }

    @Override
    protected void onDraw(SkCanvas canvas) {
        canvas.translate(20*SK_Scalar1, 20*SK_Scalar1);
        canvas.drawBitmap(fBitmap, 0, 0);
    }

    private SkBitmap fBitmap = new SkBitmap();

    public static GMRegistry.Factory MyFactory = new GMRegistry.Factory() {
        public GM apply() {
            return new CMYKJpegGM();
        }
    };
}
