package app;


import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Paint;
import misc.CoordinateSystem2d;
import misc.CoordinateSystem2i;
import misc.Vector2d;
import misc.Vector2i;

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
    public void paint(Canvas canvas, CoordinateSystem2i winCS, CoordinateSystem2d trueCS, Paint p)
    {
        Vector2i winPosA = winCS.getCoords(A, trueCS);
        Vector2i winPosB = winCS.getCoords(B, trueCS);
        Vector2i winPosC = winCS.getCoords(C, trueCS);
        Vector2i winPosD = winCS.getCoords(D, trueCS);
        canvas.drawLine((float) winPosA.x, (float) winPosA.y, (float) winPosB.x, (float) winPosB.y, p);
        canvas.drawLine((float) winPosB.x, (float) winPosB.y, (float) winPosC.x, (float) winPosC.y, p);
        canvas.drawLine((float) winPosC.x, (float) winPosC.y, (float) winPosD.x, (float) winPosD.y, p);
        canvas.drawLine((float) winPosA.x, (float) winPosA.y, (float) winPosD.x, (float) winPosD.y, p);
    }
}