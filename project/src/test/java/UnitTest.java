import app.Point;
import app.Task;
import misc.CoordinateSystem2d;
import misc.Vector2d;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Класс тестирования
 */
public class UnitTest {


    private static void test(ArrayList<Point> points,  HashSet<Vector2d> Answer) {
        Task task = new Task(new CoordinateSystem2d(10, 10, 20, 20), points);
        task.solve();
        boolean f = false;
        for (Point p : points)
            if (p.pos.x == task.getQ().A.x && p.pos.y == task.getQ().A.y)
                f = true;
        assert f;
        f = false;
        for (Point p : points)
            if (p.pos.x == task.getQ().B.x && p.pos.y == task.getQ().B.y)
                f = true;
        assert f;
        f = false;
        for (Point p : points)
            if (p.pos.x == task.getQ().C.x && p.pos.y == task.getQ().C.y)
                f = true;
        assert f;
        f = false;
        for (Point p : points)
            if (p.pos.x == task.getQ().D.x && p.pos.y == task.getQ().D.y)
                f = true;
        assert f;
        assert Answer.contains(task.getQ().A);
        assert Answer.contains(task.getQ().B);
        assert Answer.contains(task.getQ().C);
        assert Answer.contains(task.getQ().D);

    }


    /**
     * Первый тест
     */
    @Test
    public void test1() {
        ArrayList<Point> points = new ArrayList<>();

        points.add(new Point(new Vector2d(0, 1)));
        points.add(new Point(new Vector2d(1, 0)));
        points.add(new Point(new Vector2d(3, 1)));
        points.add(new Point(new Vector2d(1, -2)));
        points.add(new Point(new Vector2d(-1, -2)));

        HashSet<Vector2d> Answer = new HashSet<>();
        Answer.add(new Vector2d(0, 1));
        Answer.add(new Vector2d(3, 1));
        Answer.add(new Vector2d(1, -2));
        Answer.add(new Vector2d(-1, -2));

        test(points, Answer);
    }

    /**
     * Второй тест
     */
    @Test
    public void test2() {
        ArrayList<Point> points = new ArrayList<>();

        points.add(new Point(new Vector2d(-1, 3)));
        points.add(new Point(new Vector2d(1, 0)));
        points.add(new Point(new Vector2d(-2, 1)));
        points.add(new Point(new Vector2d(2, 2)));
        points.add(new Point(new Vector2d(1, 6)));

        HashSet<Vector2d> Answer = new HashSet<>();
        Answer.add(new Vector2d(1, 0));
        Answer.add(new Vector2d(2, 2));
        Answer.add(new Vector2d(-2, 1));
        Answer.add(new Vector2d(1, 6));

        test(points, Answer);
    }

    /**
     * Третий тест
     */
    @Test
    public void test3() {
        ArrayList<Point> points = new ArrayList<>();

        points.add(new Point(new Vector2d(2, 2)));
        points.add(new Point(new Vector2d(1, 0)));
        points.add(new Point(new Vector2d(3, -1)));
        points.add(new Point(new Vector2d(-2, -1)));
        points.add(new Point(new Vector2d(-2, 1)));

        HashSet<Vector2d> Answer = new HashSet<>();
        Answer.add(new Vector2d(-2, -1));
        Answer.add(new Vector2d(-2, 1));
        Answer.add(new Vector2d(2, 2));
        Answer.add(new Vector2d(3, -1));

        test(points, Answer);
    }
}
