package skia.javacpp.gm;

import static skia.javacpp.core.*;

public abstract class GM {
	public GM() {
		fBGColor = SK_ColorWHITE;
	}
		
	//enum Flags
    public static final int kSkipPDF_Flag = 1 << 0,
    		kSkipPicture_Flag = 1 << 1;

    public void draw(SkCanvas canvas) {
    	drawBackground(canvas);
    	drawContent(canvas);
    }
    
    public void drawBackground(SkCanvas canvas) {
    	onDrawBackground(canvas);
    }
    
    public void drawContent(SkCanvas canvas) {
    	onDraw(canvas);
    }
        
	public SkISize getISize() {
		return onISize();
	}
	
    public String shortName() {
    	if (fShortName == null) {
    		fShortName = onShortName();
    	}
    	return fShortName;
    }

    public int getFlags() {
            return onGetFlags();
    }
        
    public int getBGColor() {
    	return fBGColor;
    }
    
    public void setBGColor(int color) {
    	fBGColor = color;
    }

    public void drawSizeBounds(SkCanvas canvas, int color) {
    	SkISize size = getISize();
    	SkRect r = SkRect.MakeWH(size.width(), size.height());
    	SkPaint paint = new SkPaint();
    	paint.setColor(color);
    	canvas.drawRect(r, paint);
    }

	protected abstract void onDraw(SkCanvas canvas);
	
	protected void onDrawBackground(SkCanvas canvas) {
		canvas.drawColor(fBGColor);
	}
	
	protected abstract SkISize onISize();
	protected abstract String onShortName();
	protected int onGetFlags() { return 0; }
        
    private String fShortName;
   	private int fBGColor;
}
