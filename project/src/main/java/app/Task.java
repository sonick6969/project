package app;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.humbleui.jwm.MouseButton;
import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.skija.Rect;
import misc.*;
import panels.PanelLog;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;



/**
 * Класс задачи
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public class Task {
    /**
     * Текст задачи
     */
    public static final String TASK_TEXT = """
            ПОСТАНОВКА ЗАДАЧИ:
            На плоскости задано множество точек.
            Найти из них такие 4 точки, что построенный по
            ним четырёхугольник не является самопересекающимся
            и имеет при этом максимальную площадь.
            """;

    /**
     * Вещественная система координат задачи
     */
    private final CoordinateSystem2d ownCS;
    /**
     * Список точек
     */
    private final ArrayList<Point> points;
    /**
     * Размер точки
     */
    private static final int POINT_SIZE = 3;
    /**
     * Последняя СК окна
     */
    private CoordinateSystem2i lastWindowCS;
    /**
     * Флаг, решена ли задача
     */
    private boolean solved;
    /**
     * Список точек в пересечении
     */
    private Quadrangle q = new Quadrangle(new Vector2d(0, 0), new Vector2d(0, 0), new Vector2d(0, 0), new Vector2d(0, 0));
    private double S = 0;

    @JsonCreator
    public Task(@JsonProperty("ownCS") CoordinateSystem2d ownCS, @JsonProperty("points") ArrayList<Point> points) {
        this.ownCS = ownCS;
        this.points = points;
    }

    /**
     * Рисование задачи
     *
     * @param canvas   область рисования
     * @param windowCS СК окна
     */
    public void paint(Canvas canvas, CoordinateSystem2i windowCS) {
        // Сохраняем последнюю СК
        lastWindowCS = windowCS;

        canvas.save();
        // создаём перо
        try (var paint = new Paint()) {
            paint.setColor(Misc.getColor(0xFF, 0xFF, 0xFF, 0xFF));
            if (solved){
                paint.setColor(Misc.getColor(0xAA, 0x11, 0xCC, 0x55));
                q.paint(canvas, windowCS, ownCS, paint);
                paint.setColor(Misc.getColor(0xFF, 0xFF, 0xFF, 0xFF));
            }
            for (Point p : points) {
                Vector2i windowPos = windowCS.getCoords(p.pos.x, p.pos.y, ownCS);
                canvas.drawRect(Rect.makeXYWH(windowPos.x - POINT_SIZE, windowPos.y - POINT_SIZE, POINT_SIZE * 2, POINT_SIZE * 2), paint);
            }
        }
        canvas.restore();
    }

    public void addPoint(Vector2d pos) {
        solved = false;
        Point newPoint = new Point(pos);
        points.add(newPoint);
        PanelLog.info("точка " + newPoint + " добавлена");
    }


    /**
     * Клик мыши по пространству задачи
     *
     * @param pos         положение мыши
     * @param mouseButton кнопка мыши
     */
    public void click(Vector2i pos, MouseButton mouseButton) {
        if (lastWindowCS == null) return;
        Vector2d taskPos = ownCS.getCoords(pos, lastWindowCS);
        if (mouseButton.equals(MouseButton.PRIMARY)) {
            addPoint(taskPos);
        } else if (mouseButton.equals(MouseButton.SECONDARY)) {
            addPoint(taskPos);
        }
    }


    /**
     * Добавить случайные точки
     *
     * @param cnt кол-во случайных точек
     */
    public void addRandomPoints(int cnt) {
        CoordinateSystem2i addGrid = new CoordinateSystem2i(30, 30);

        for (int i = 0; i < cnt; i++) {
            Vector2i gridPos = addGrid.getRandomCoords();
            Vector2d pos = ownCS.getCoords(gridPos, addGrid);
            addPoint(pos);
        }
    }

    /**
     * Очистить задачу
     */
    public void clear() {
        points.clear();
        q = new Quadrangle(new Vector2d(0, 0), new Vector2d(0, 0), new Vector2d(0, 0), new Vector2d(0, 0));
        S = 0;
        solved = false;
    }

    /**
     * Решить задачу
     */
    public void solve() {

        for (int i1 = 0; i1 < points.size(); i1++)
            for (int i2 = 0; i2 < points.size(); i2++)
                for (int i3 = 0; i3 < points.size(); i3++)
                    for (int i4 = 0; i4 < points.size(); i4++)
                    {
                        if (i1 == i2 || i1 == i3 || i1 == i4 || i2 == i3 || i3 == i4 || i2 == i4)
                            continue;
                        double x1 = points.get(i1).pos.x;
                        double y1 = points.get(i1).pos.y;
                        double x2 = points.get(i2).pos.x;
                        double y2 = points.get(i2).pos.y;
                        double x3 = points.get(i3).pos.x;
                        double y3 = points.get(i3).pos.y;
                        double x4 = points.get(i4).pos.x;
                        double y4 = points.get(i4).pos.y;
                        double a = Math.sqrt((y2 - y1)*(y2 - y1) + (x2 - x1)*(x2 - x1));
                        double b = Math.sqrt((y3 - y2)*(y3 - y2) + (x2 - x3)*(x2 - x3));
                        double c = Math.sqrt((y3 - y4)*(y3 - y4) + (x3 - x4)*(x3 - x4));
                        double d = Math.sqrt((y4 - y1)*(y4 - y1) + (x4 - x1)*(x4 - x1));
                        double e = Math.sqrt((y1 - y3)*(y1 - y3) + (x3 - x1)*(x3 - x1));
                        double f = Math.sqrt((y2 - y4)*(y2 - y4) + (x2 - x4)*(x2 - x4));
                        double c2 = (a * a + b * b - e * e)/(2 * a * b);
                        double c1 = (a * a + d * d - f * f)/(2 * a * d);
                        double c3 = (c * c + b * b - f * f)/(2 * b * c);
                        double c4 = (d * d + c * c - e * e)/(2 * d * c);
                        if (((float)((x3 - x1) / (x2 - x1)) == (float)((y3 - y1) / (y2 - y1))) || ((float)((x4 - x1) / (x3 - x2)) == (float)((y4 - y2) / (y3 - y2))) || ((float)((x1 - x3) / (x4 - x3)) == (float)((y1 - y3) / (y4 - y3))) || ((float)((x2 - x4) / (x1 - x4)) == (float)((y2 - y4) / (y1 - y4))))
                            continue;
                        double S_ = 0;
                        if (((float)Math.acos(c1) + (float)Math.acos(c2) + (float)Math.acos(c3) + (float)Math.acos(c4)) == (float)(2 * Math.PI)) {
                            double p1 = (a + b + e) / 2;
                            double p2 = (c + d + e) / 2;
                            S_ = Math.sqrt(p1 * (p1 - a) * (p1 - b) * (p1 - e)) + Math.sqrt(p2 * (p2 - c) * (p2 - d) * (p2 - e));
                        }
                        if (((float)Math.acos(c1) + (float)Math.acos(c2) + (float)Math.acos(c3)) == (float)Math.acos(c4))
                        {
                            double p1 = (a + b + f) / 2;
                            double p2 = (c + d + f) / 2;
                            S_ = Math.sqrt(p1 * (p1 - a) * (p1 - b) * (p1 - f)) + Math.sqrt(p2 * (p2 - c) * (p2 - d) * (p2 - f));
                            //PanelLog.success("v4\n");
                        }
                        if (((float)Math.acos(c1) + (float)Math.acos(c3) + (float)Math.acos(c4)) == (float)Math.acos(c2))
                        {
                            double p1 = (a + d + f) / 2;
                            double p2 = (b + c + f) / 2;
                            S_ = Math.sqrt(p1 * (p1 - a) * (p1 - d) * (p1 - f)) + Math.sqrt(p2 * (p2 - b) * (p2 - c) * (p2 - f));
                            //PanelLog.success("v2\n");
                        }
                        if (((float)Math.acos(c1) + (float)Math.acos(c2) + (float)Math.acos(c4)) == (float)Math.acos(c3))
                        {
                            double p1 = (a + b + e) / 2;
                            double p2 = (c + d + e) / 2;
                            S_ = Math.sqrt(p1 * (p1 - a) * (p1 - b) * (p1 - e)) + Math.sqrt(p2 * (p2 - c) * (p2 - d) * (p2 - e));
                            //PanelLog.success("v3\n");
                        }
                        if (((float)Math.acos(c2) + (float)Math.acos(c3) + (float)Math.acos(c4)) == (float)Math.acos(c1))
                        {
                            double p1 = (a + b + e) / 2;
                            double p2 = (c + d + e) / 2;
                            S_ = Math.sqrt(p1 * (p1 - a) * (p1 - b) * (p1 - e)) + Math.sqrt(p2 * (p2 - c) * (p2 - d) * (p2 - e));
                            //PanelLog.success("v1\n");
                        }
                            if (S_ > S)
                            {
                                S = S_;
                                q = new Quadrangle(new Vector2d(x1, y1), new Vector2d(x2, y2), new Vector2d(x3, y3), new Vector2d(x4, y4));
                                //PanelLog.success(String.valueOf(i1) + String.valueOf(i2) + String.valueOf(i3) + String.valueOf(i4) + " " + String.valueOf(S_));

                            }

                        //PanelLog.success(String.valueOf((Math.acos(c1) + Math.acos(c2) + Math.acos(c3) + Math.acos(c4)) == 2 * Math.PI));
                    }
        solved = true;
    }

    /**
     * Получить тип мира
     *
     * @return тип мира
     */
    public CoordinateSystem2d getOwnCS() {
        return ownCS;
    }

    /**
     * Получить название мира
     *
     * @return название мира
     */
    public ArrayList<Point> getPoints() {
        return points;
    }
    public Quadrangle getQ()
    {
        return q;
    }

    /**
     * Получить список пересечений
     *
     * @return список пересечений
     */



    /**
     * Получить список разности
     *
     * @return список разности
     */


    /**
     * Отмена решения задачи
     */
    public void cancel() {
        solved = false;
    }

    /**
     * проверка, решена ли задача
     *
     * @return флаг
     */
    public boolean isSolved() {
        return solved;
    }
    public double Answer()
    {
        return S;
    }
}
