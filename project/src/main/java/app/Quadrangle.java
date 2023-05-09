package app;


import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Paint;
import misc.Vector2d;

public class Quadrangle{
    public final Vector2d A;
    public final Vector2d B;
    public final Vector2d C;
    public final Vector2d D;

    public Quadrangle(Vector2d A, Vector2d B, Vector2d C, Vector2d D) {
        this.A = A;
        this.B = B;
        this.C = C;
        this.D = D;
    }
    public void paint(Canvas canvas, Paint p)
    {
        canvas.drawLine((float) A.x, (float) A.y, (float) B.x, (float) B.y, p);
        canvas.drawLine((float) B.x, (float) B.y, (float) C.x, (float) C.y, p);
        canvas.drawLine((float) C.x, (float) C.y, (float) D.x, (float) D.y, p);
        canvas.drawLine((float) A.x, (float) A.y, (float) D.x, (float) D.y, p);
    }
}