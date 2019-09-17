package gui.component.inputTabs;

import data.Line;
import data.model.TrendLine;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.text.NumberFormat;
import java.util.ArrayList;

public class MyRandomParametersTab extends ParametersTab {
    @FXML
    private TextField dotsField;
    @FXML
    private TextField seedField;

    private boolean dotsInvalid = true, seedInvalid = true;

    public MyRandomParametersTab() {
        super();
        this.setText("Cлучайный+");
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setGroupingUsed(false);
        loadFxml(MyRandomParametersTab.class.getResource("MyRandomParametersTab.fxml"),this);
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
    public ArrayList<Line> generateResult() {
        ArrayList<Line> list = new ArrayList<>();
        list.add(TrendLine.MyRandom(Integer.parseUnsignedInt(dotsField.getText()),
                Integer.parseInt(seedField.getText())));
        return list;
    }
}
