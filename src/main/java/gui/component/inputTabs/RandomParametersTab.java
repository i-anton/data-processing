package gui.component.inputTabs;

import data.Line;
import data.model.TrendLine;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.text.NumberFormat;

public class RandomParametersTab extends ParametersTab {
    @FXML
    private TextField dotsField;
    @FXML
    private TextField seedField;

    private boolean dotsInvalid = true, seedInvalid = true;

    public RandomParametersTab() {
        super();
        this.setText("Cлучайный");
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setGroupingUsed(false);
        loadFxml(getClass().getResource("RandomParametersTab.fxml"),this);
        dotsField.setTextFormatter(new TextFormatter<>(change ->
                (change.getControlNewText().matches("([1-9][0-9]*)?")) ? change : null)); //accept only unsigned integer
        dotsField.textProperty().addListener((observable, oldValue, newValue) -> {
            dotsInvalid = newValue.length() == 0 || newValue.length() > 6;
            if(!dotsInvalid) {
                dotsField.getStyleClass().remove("error");
            } else {
                dotsField.getStyleClass().add("error");
            }
            setIsInvalid(dotsInvalid || seedInvalid);
        });
        seedField.setTextFormatter(new TextFormatter<>(change ->
                (change.getControlNewText().matches("([1-9][0-9]*)?")) ? change : null)); //accept only unsigned integer
        seedField.textProperty().addListener((observable, oldValue, newValue) -> {
            seedInvalid = newValue.length() == 0 || newValue.length() > 6;
            if(!seedInvalid) {
                seedField.getStyleClass().remove("error");
            } else {
                seedField.getStyleClass().add("error");
            }
            setIsInvalid(seedInvalid || dotsInvalid);
        });
    }

    @Override
    public Line generateResult() {
        return TrendLine.Random(Integer.parseUnsignedInt(dotsField.getText()),
                Integer.parseInt(seedField.getText()));
    }
}
