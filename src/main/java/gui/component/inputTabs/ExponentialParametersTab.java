package gui.component.inputTabs;

import data.Line;
import data.model.TrendLine;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.NumberStringConverter;

import java.text.NumberFormat;
import java.util.ArrayList;

public class ExponentialParametersTab extends ParametersTab {
    @FXML
    private TextField dotsField;
    @FXML
    private TextField koefField;
    @FXML
    private TextField degreeField;

    private boolean dotsInvalid = true, koefInvalid = true, degreeInvalid = true;

    public ExponentialParametersTab() {
        super();
        this.setText("Экспонента");
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setGroupingUsed(false);
        loadFxml(ExponentialParametersTab.class.getResource("ExponentialParametersTab.fxml"),this);
        koefField.setTextFormatter(new TextFormatter<>(new NumberStringConverter(nf)));
        koefField.textProperty().addListener((observable, oldValue, newValue) -> {
            koefInvalid = newValue.length() == 0;
            if (!koefInvalid) {
                koefField.getStyleClass().remove("error");
            } else {
                koefField.getStyleClass().add("error");
            }
            setIsInvalid(dotsInvalid || degreeInvalid || koefInvalid);
        });
        degreeField.setTextFormatter(new TextFormatter<>(new NumberStringConverter(nf)));
        degreeField.textProperty().addListener((observable, oldValue, newValue) -> {
            degreeInvalid = newValue.length() == 0;
            if(!degreeInvalid) {
                degreeField.getStyleClass().remove("error");
            } else {
                degreeField.getStyleClass().add("error");
            }
            setIsInvalid(dotsInvalid || degreeInvalid || koefInvalid);
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
            setIsInvalid(dotsInvalid || degreeInvalid || koefInvalid);
        });
    }

    @Override
    public ArrayList<Line> generateResult() {
        ArrayList<Line> list = new ArrayList<>();
        list.add(TrendLine.Exponential(Integer.parseUnsignedInt(dotsField.getText()),
                Double.parseDouble(koefField.getText()),Double.parseDouble(degreeField.getText())));
        return list;
    }
}
