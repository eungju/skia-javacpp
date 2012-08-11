package skia.javacpp;

import com.googlecode.javacpp.BytePointer;
import com.googlecode.javacpp.annotation.Platform;
import com.googlecode.javacpp.annotation.Properties;

import java.nio.charset.Charset;

import static skia.javacpp.Skia.*;

@Properties({
        @Platform(define={"SK_SCALAR_IS_FLOAT", "SK_CAN_USE_FLOAT", "SK_RELEASE", "GR_RELEASE 1", "NDEBUG"}),
        @Platform(value="windows", includepath=windowsIncludepath, linkpath=windowsLinkpath, link=windowsLink),
        @Platform(value="linux", includepath=linuxIncludepath, linkpath=linuxLinkpath, link=linuxLink),
        @Platform(value="macosx", includepath=macosxIncludepath, linkpath=macosxLinkpath, link=macosxLink, framework="Foundation:ApplicationServices:OpenGL")
})
public class Skia {
	public static final String windowsIncludepath = "../skia/include/core;../skia/include/config;../skia/include/effects;../skia/include/images;../skia/include/pipe;../skia/include/utils";
	public static final String windowsLinkpath = "../skia/out/gyp/Release/lib;C:/Program Files/Microsoft SDKs/Windows/v7.0A/Lib";
	public static final String windowsLink = "core;effects;gr:images;opts;opts_ssse3;ports;skgr:utils;gdi32;Usp10;User32;ole32;oleaut32";
	public static final String linuxIncludepath = "../skia/include/core:../skia/include/config:../skia/include/effects:../skia/include/images:../skia/include/pipe:../skia/include/utils";
	public static final String linuxLinkpath = "../skia/out/Release/obj.target/gyp";
	public static final String linuxLink = "core:effects:gr:images:opts:opts_ssse3:ports:skgr:utils:zlib:freetype:png";
	public static final String macosxIncludepath = "../skia/include/core:../skia/include/config:../skia/include/effects:../skia/include/images:../skia/include/pipe:../skia/include/utils";
	public static final String macosxLinkpath = "../skia/out/Release";
	public static final String macosxLink = "core:effects:gr:images:jpeg:opts:opts_ssse3:ports:skgr:utils:zlib";

    public static final Charset UTF_8 = Charset.forName("UTF-8");
    public static final Charset UTF_16 = Charset.forName("UTF-16");
    public static final Charset UTF_32 = Charset.forName("UTF-32");

    public static BytePointer toPointer(String text, Charset charset) {
        byte[] raw = text.getBytes(charset);
        return new BytePointer(raw);
    }
}
