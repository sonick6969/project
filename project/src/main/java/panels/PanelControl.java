package panels;

import app.Button;
import app.Task;
import controls.*;
import io.github.humbleui.jwm.*;
import io.github.humbleui.skija.Canvas;
import misc.CoordinateSystem2i;
import misc.Vector2d;
import misc.Vector2i;

import java.util.ArrayList;
import java.util.List;

import static app.Application.PANEL_PADDING;
import static app.Colors.FIELD_BACKGROUND_COLOR;
import static app.Colors.FIELD_TEXT_COLOR;

public class PanelControl extends GridPanel {
    MultiLineLabel task;
    public List<Label> labels;
    public List<Input> inputs;
    public List<Button> buttons;
    private final Button solve;

    public PanelControl(
            Window window, boolean drawBG, int color, int padding, int gridWidth, int gridHeight,
            int gridX, int gridY, int colspan, int rowspan
    ) {
        super(window, drawBG, color, padding, gridWidth, gridHeight, gridX, gridY, colspan, rowspan);

        inputs = new ArrayList<>();
        labels = new ArrayList<>();
        buttons = new ArrayList<>();

        task = new MultiLineLabel(
                window, false, backgroundColor, PANEL_PADDING,
                6, 7, 0, 0, 6, 2, Task.TASK_TEXT,
                false, true);
        // добавление вручную
        Label xLabel = new Label(window, false, backgroundColor, PANEL_PADDING,
                6, 7, 0, 2, 1, 1, "X", true, true);
        labels.add(xLabel);
        Input xField = InputFactory.getInput(window, false, FIELD_BACKGROUND_COLOR, PANEL_PADDING,
                6, 7, 1, 2, 2, 1, "0.0", true,
                FIELD_TEXT_COLOR, true);
        inputs.add(xField);
        Label yLabel = new Label(window, false, backgroundColor, PANEL_PADDING,
                6, 7, 3, 2, 1, 1, "Y", true, true);
        labels.add(yLabel);
        Input yField = InputFactory.getInput(window, false, FIELD_BACKGROUND_COLOR, PANEL_PADDING,
                6, 7, 4, 2, 2, 1, "0.0", true,
                FIELD_TEXT_COLOR, true);
        inputs.add(yField);

        Button addPoint = new Button(
                window, false, backgroundColor, PANEL_PADDING,
                6, 7, 0, 3, 3, 1, "Добавить новую\nточку",
                true, true);
        addPoint.setOnClick(() -> {
            if (!xField.hasValidDoubleValue()) {
                PanelLog.warning("X координата введена неверно");
            } else if (!yField.hasValidDoubleValue())
                PanelLog.warning("Y координата введена неверно");
            else
                PanelRendering.task.addPoint(
                        new Vector2d(xField.doubleValue(), yField.doubleValue())
                );
        });
        buttons.add(addPoint);


        // случайное добавление
        Label cntLabel = new Label(window, false, backgroundColor, PANEL_PADDING,
                6, 7, 0, 4, 1, 1, "Кол-во", true, true);
        labels.add(cntLabel);

        Input cntField = InputFactory.getInput(window, false, FIELD_BACKGROUND_COLOR, PANEL_PADDING,
                6, 7, 1, 4, 2, 1, "5", true,
                FIELD_TEXT_COLOR, true);
        inputs.add(cntField);

        Button addRandomPoints = new Button(
                window, false, backgroundColor, PANEL_PADDING,
                6, 7, 3, 4, 3, 1, "Добавить\nслучайные точки",
                true, true);
        addRandomPoints.setOnClick(() -> {
            if (!cntField.hasValidIntValue()) {
                PanelLog.warning("кол-во точек указано неверно");
            } else
                PanelRendering.task.addRandomPoints(cntField.intValue());
        });
        buttons.add(addRandomPoints);

        // управление
        Button load = new Button(
                window, false, backgroundColor, PANEL_PADDING,
                6, 7, 0, 5, 3, 1, "Загрузить",
                true, true);
        /*load.setOnClick(() -> {
            PanelRendering.load();
            cancelTask();
        });*/
        buttons.add(load);

        Button save = new Button(
                window, false, backgroundColor, PANEL_PADDING,
                6, 7, 3, 5, 3, 1, "Сохранить",
                true, true);
        //save.setOnClick(PanelRendering::save);
        buttons.add(save);

        Button clear = new Button(
                window, false, backgroundColor, PANEL_PADDING,
                6, 7, 0, 6, 3, 1, "Очистить",
                true, true);
        clear.setOnClick(() -> PanelRendering.task.clear());
        buttons.add(clear);

        solve = new Button(
                window, false, backgroundColor, PANEL_PADDING,
                6, 7, 3, 6, 3, 1, "Решить",
                true, true);
        solve.setOnClick(() -> {
            if (!PanelRendering.task.isSolved()) {
                PanelRendering.task.solve();
                String s;
                if (PanelRendering.task.Answer() == 0)
                    s = "Искомого четырехугольника не существует.\n";
                else
                    s = "Задача решена\n" + "Площадь искомого четырехугольника: " + PanelRendering.task.Answer()  + "\n";
                PanelLog.success(s);
                solve.text = "Сбросить";
            } else {
                cancelTask();
            }
            window.requestFrame();
        });
        buttons.add(solve);
    }

    @Override
    public void accept(Event e) {
        super.accept(e);
        if (e instanceof EventMouseMove ee) {
            for (Input input : inputs)
                input.accept(ee);

            for (Button button : buttons) {
                if (lastWindowCS != null)
                    button.checkOver(lastWindowCS.getRelativePos(new Vector2i(ee)));
            }
        } else if (e instanceof EventMouseButton) {
            if (!lastInside)
                return;

            Vector2i relPos = lastWindowCS.getRelativePos(lastMove);

            for (Button button : buttons) {
                button.click(relPos);
            }

            for (Input input : inputs) {
                if (input.contains(relPos)) {
                    input.setFocus();
                }
            }
            window.requestFrame();
        } else if (e instanceof EventTextInput ee) {
            for (Input input : inputs) {
                if (input.isFocused()) {
                    input.accept(ee);
                }
            }
            window.requestFrame();
        } else if (e instanceof EventKey ee) {
            for (Input input : inputs) {
                if (input.isFocused()) {
                    input.accept(ee);
                }
            }
            window.requestFrame();
        }
    }

    @Override
    public void paintImpl(Canvas canvas, CoordinateSystem2i windowCS) {
        task.paint(canvas, windowCS);

        for (Button button : buttons) {
            button.paint(canvas, windowCS);
        }
        for (Input input : inputs) {
            input.paint(canvas, windowCS);
        }
        for (Label label : labels) {
            label.paint(canvas, windowCS);
        }
    }


    private void cancelTask() {
        PanelRendering.task.cancel();
        solve.text = "Решить";
    }
}
