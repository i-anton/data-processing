package gui.component.inputTabs;

import data.Line;
import data.model.TrendLine;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.NumberStringConverter;

import java.text.NumberFormat;
import java.util.ArrayList;

public class PiecewiseParametersTab extends ParametersTab {
    @FXML
    private TextField dotsField;
    @FXML
    private TextField stepField;

    private boolean dotsInvalid = true, stepInvalid = true;

    public PiecewiseParametersTab() {
        super();
        this.setText("Кусочный");
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setGroupingUsed(false);
        loadFxml(PiecewiseParametersTab.class.getResource("PiecewiseParametersTab.fxml"),this);
        stepField.setTextFormatter(new TextFormatter<>(new NumberStringConverter(nf)));
        stepField.textProperty().addListener((observable, oldValue, newValue) -> {
            stepInvalid = newValue.length() == 0;
            if (!stepInvalid) {
                stepField.getStyleClass().remove("error");
            } else {
                stepField.getStyleClass().add("error");
            }
            setInvalid(dotsInvalid || stepInvalid);
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
            setInvalid(dotsInvalid || stepInvalid);
        });
    }

    @Override
    public ArrayList<Line> generateResult() {
        ArrayList<Line> list = new ArrayList<>();
        list.add(TrendLine.INSTANCE.piecewise(Integer.parseUnsignedInt(dotsField.getText()),
                Double.parseDouble(stepField.getText())));
        return list;
    }
}
