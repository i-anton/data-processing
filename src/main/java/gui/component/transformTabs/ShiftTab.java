package gui.component.transformTabs;

import data.Line;
import data.model.SimpleTransforms;
import gui.component.inputTabs.ParametersTab;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.NumberStringConverter;

import java.text.NumberFormat;

public class ShiftTab extends ParametersTab {
    @FXML
    private TextField startField;
    @FXML
    private TextField endField;
    @FXML
    private TextField shiftField;
    @FXML
    private TextField scaleField;

    private boolean startInvalid = true, endInvalid = true,
            shiftInvalid = true, scaleInvalid = true;

    public ShiftTab() {
        super();
        this.setText("Cмещение");
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setGroupingUsed(false);
        loadFxml(ShiftTab.class.getResource("ShiftTab.fxml"),this);
        startField.setTextFormatter(new TextFormatter<>(new NumberStringConverter(nf)));
        startField.textProperty().addListener((observable, oldValue, newValue) -> {
            startInvalid = newValue.length() == 0;
            if (!startInvalid) {
                startField.getStyleClass().remove("error");
            } else {
                startField.getStyleClass().add("error");
            }
            setIsInvalid(startInvalid || endInvalid ||shiftInvalid || scaleInvalid);
        });
        endField.setTextFormatter(new TextFormatter<>(new NumberStringConverter(nf)));
        endField.textProperty().addListener((observable, oldValue, newValue) -> {
            endInvalid = newValue.length() == 0;
            if (!endInvalid) {
                endField.getStyleClass().remove("error");
            } else {
                endField.getStyleClass().add("error");
            }
            setIsInvalid(startInvalid || endInvalid ||shiftInvalid || scaleInvalid);
        });
        shiftField.setTextFormatter(new TextFormatter<>(new NumberStringConverter(nf)));
        shiftField.textProperty().addListener((observable, oldValue, newValue) -> {
            shiftInvalid = newValue.length() == 0;
            if (!shiftInvalid) {
                shiftField.getStyleClass().remove("error");
            } else {
                shiftField.getStyleClass().add("error");
            }
            setIsInvalid(startInvalid || endInvalid ||shiftInvalid || scaleInvalid);
        });
        scaleField.setTextFormatter(new TextFormatter<>(new NumberStringConverter(nf)));
        scaleField.textProperty().addListener((observable, oldValue, newValue) -> {
            scaleInvalid = newValue.length() == 0;
            if (!scaleInvalid) {
                scaleField.getStyleClass().remove("error");
            } else {
                scaleField.getStyleClass().add("error");
            }
            setIsInvalid(startInvalid || endInvalid ||shiftInvalid || scaleInvalid);
        });
    }

    @Override
    public Line generateResult() {
        Line line = new Line(getInputLine());
        SimpleTransforms.Shift(line,
                Double.parseDouble(startField.getText()),
                Double.parseDouble(endField.getText()),
                Double.parseDouble(shiftField.getText()),
                Double.parseDouble(scaleField.getText()));
        return line;
    }
}
