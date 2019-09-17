package gui.component.transformTabs;

import data.Line;
import data.analysis.Stationary;
import data.model.SimpleTransforms;
import gui.component.inputTabs.ParametersTab;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.NumberStringConverter;
import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.ArrayList;

public class StationarityTab extends ParametersTab {
    @FXML
    private TextField intervalsField;
    @FXML
    private TextField errorField;

    private boolean intervalsInvalid = true, errorInvalid = true;

    public StationarityTab() {
        super();
        this.setText("Анализ стационарности");
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setGroupingUsed(false);
        loadFxml(StationarityTab.class.getResource("StationarityTab.fxml"),this);
        intervalsField.setTextFormatter(new TextFormatter<>(new NumberStringConverter(nf)));
        intervalsField.textProperty().addListener((observable, oldValue, newValue) -> {
            intervalsInvalid = newValue.length() == 0;
            if (!intervalsInvalid) {
                intervalsField.getStyleClass().remove("error");
            } else {
                intervalsField.getStyleClass().add("error");
            }
            setInvalid(intervalsInvalid || errorInvalid);
        });
        errorField.setTextFormatter(new TextFormatter<>(new NumberStringConverter(nf)));
        errorField.textProperty().addListener((observable, oldValue, newValue) -> {
            errorInvalid = newValue.length() == 0;
            if (!errorInvalid) {
                errorField.getStyleClass().remove("error");
            } else {
                errorField.getStyleClass().add("error");
            }
            setInvalid(intervalsInvalid || errorInvalid);
        });
    }

    @NotNull
    @Override
    public ArrayList<Line> generateResult() {
        Line line = new Line(getInputLine());
        return Stationary.dataPerInterval(line,
                Integer.parseInt(intervalsField.getText()),
                Double.parseDouble(errorField.getText()));
    }
}
