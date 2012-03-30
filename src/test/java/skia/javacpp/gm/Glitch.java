package skia.javacpp.gm;

import com.googlecode.javacpp.Loader;
import com.googlecode.javacpp.annotation.Cast;
import com.googlecode.javacpp.annotation.Platform;
import com.googlecode.javacpp.annotation.Properties;

@Properties({
        @Platform(include={"stdlib.h"})
})
public class Glitch {
    static { Loader.load(); }

    public native static int rand();

    public native static void srand(@Cast("unsigned") int seed);
}
