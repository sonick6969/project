package app;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import misc.Misc;
import misc.Vector2d;

import java.util.Objects;

/**
 * Класс точки
 */
public class Point {
    /**
     * Множества
     */

    /**
     * Множество, которому принадлежит точка
     */
    /**
     * Координаты точки
     */
    public final Vector2d pos;

    @JsonCreator
    public Point(@JsonProperty("pos") Vector2d pos) {
        this.pos = pos;
    }


    /**
     * Получить цвет точки по её множеству
     *
     * @return цвет точки
     */
    @JsonIgnore
    public int getColor() {
        return Misc.getColor(0xCC, 0x00, 0x00, 0xFF);
    }

    /**
     * Получить положение
     * (нужен для json)
     *
     * @return положение
     */
    public Vector2d getPos() {
        return pos;
    }

    /**
     * Получить множество
     *
     * @return множество
     */


    /**
     * Получить название множества
     *
     * @return название множества
     */


    /**
     * Строковое представление объекта
     *
     * @return строковое представление объекта
     */
    @Override
    public String toString() {
        return "Point{" +
                "pos=" + pos +
                '}';
    }



}
