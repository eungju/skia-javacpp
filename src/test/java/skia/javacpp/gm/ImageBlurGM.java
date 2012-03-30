package skia.javacpp.gm;

import static skia.javacpp.core.*;
import static skia.javacpp.effects.*;

public class ImageBlurGM extends GM {
    static final int WIDTH = 500;
    static final int HEIGHT = 500;

    public ImageBlurGM() {
        this.setBGColor(0xFF000000);
    }

    @Override
    protected String onShortName() {
        return "imageblur";
    }

    @Override
    protected SkISize onISize() {
        return make_isize(WIDTH, HEIGHT);
    }

    @Override
    protected void onDraw(SkCanvas canvas) {
        SkPaint paint = new SkPaint();
        paint.setImageFilter(new SkBlurImageFilter(24.0f, 0.0f)).unref();
        canvas.saveLayer(null, paint);
        paint.setAntiAlias(true);
        String str = "The quick brown fox jumped over the lazy dog.";
        Glitch.srand(1234);
        for (int i = 0; i < 25; ++i) {
            int x = Glitch.rand() % WIDTH;
            int y = Glitch.rand() % HEIGHT;
            paint.setColor(Glitch.rand() % 0x1000000 | 0xFF000000);
            paint.setTextSize(Glitch.rand() % 300);
            canvas.drawText(str, x, y, paint);
        }
        canvas.restore();
    }
}
