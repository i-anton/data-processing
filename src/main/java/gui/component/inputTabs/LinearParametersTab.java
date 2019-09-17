package gui.component.inputTabs;

import data.Line;
import data.model.TrendLine;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.NumberStringConverter;

import java.text.NumberFormat;
import java.util.ArrayList;

public class LinearParametersTab extends ParametersTab {
    @FXML
    private TextField dotsField;
    @FXML
    private TextField angleField;
    @FXML
    private TextField offsetField;

    private boolean dotsInvalid = true, angleInvalid = true, offsetInvalid = true;

    public LinearParametersTab() {
        super();
        this.setText("Линейный");
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setGroupingUsed(false);
        loadFxml(LinearParametersTab.class.getResource("LinearParametersTab.fxml"),this);
        angleField.setTextFormatter(new TextFormatter<>(new NumberStringConverter(nf)));
        angleField.textProperty().addListener((observable, oldValue, newValue) -> {
            angleInvalid = newValue.length() == 0;
            if (!angleInvalid) {
                angleField.getStyleClass().remove("error");
            } else {
                angleField.getStyleClass().add("error");
            }
            setIsInvalid(dotsInvalid || offsetInvalid || angleInvalid);
        });
        offsetField.setTextFormatter(new TextFormatter<>(new NumberStringConverter(nf)));
        offsetField.textProperty().addListener((observable, oldValue, newValue) -> {
            offsetInvalid = newValue.length() == 0;
            if(!offsetInvalid) {
                offsetField.getStyleClass().remove("error");
            } else {
                offsetField.getStyleClass().add("error");
            }
            setIsInvalid(dotsInvalid || offsetInvalid || angleInvalid);
        });
        dotsField.setTextFormatter(new TextFormatter<>(change ->
                (change.getControlNewText().matches("([1-9][0-9]*)?")) ? change : null)); //accept only unsigned integer
        dotsField.textProperty().addListener((observable, oldValue, newValue) -> {
            dotsInvalid = newValue.length() == 0 || newValue.length() > 6;
            if(!dotsInvalid) {
                dotsField.getStyleClass().remove("error");
            } else {
                dotsField.getStyleClass().add("error");
            }
            setIsInvalid(dotsInvalid || offsetInvalid || angleInvalid);
        });
    }

    @Override
    public ArrayList<Line> generateResult() {
        ArrayList<Line> list = new ArrayList<>();
        list.add(TrendLine.Linear(Integer.parseUnsignedInt(dotsField.getText()),
                Double.parseDouble(angleField.getText()),Double.parseDouble(offsetField.getText())));
        return list;
    }
}
