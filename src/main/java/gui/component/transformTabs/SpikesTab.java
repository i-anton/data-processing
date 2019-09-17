package gui.component.transformTabs;

import data.Line;
import data.model.SimpleTransforms;
import gui.component.inputTabs.ParametersTab;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.NumberStringConverter;

import java.text.NumberFormat;
import java.util.ArrayList;

public class SpikesTab extends ParametersTab {
    @FXML
    private TextField spikeField;
    @FXML
    private TextField seedField;
    @FXML
    private TextField scaleField;

    private boolean spikeInvalid = true, seedInvalid = true, scaleInvalid = true;

    public SpikesTab() {
        super();
        this.setText("Пики");
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setGroupingUsed(false);
        loadFxml(SpikesTab.class.getResource("SpikesTab.fxml"),this);
        scaleField.setTextFormatter(new TextFormatter<>(new NumberStringConverter(nf)));
        scaleField.textProperty().addListener((observable, oldValue, newValue) -> {
            scaleInvalid = newValue.length() == 0;
            if (!scaleInvalid) {
                scaleField.getStyleClass().remove("error");
            } else {
                scaleField.getStyleClass().add("error");
            }
            setIsInvalid(seedInvalid || scaleInvalid || spikeInvalid);
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
            setIsInvalid(seedInvalid || scaleInvalid || spikeInvalid);
        });
        spikeField.setTextFormatter(new TextFormatter<>(change ->
                (change.getControlNewText().matches("([1-9][0-9]*)?")) ? change : null)); //accept only unsigned integer
        spikeField.textProperty().addListener((observable, oldValue, newValue) -> {
            spikeInvalid = newValue.length() == 0 || newValue.length() > 6;
            if(!spikeInvalid) {
                spikeField.getStyleClass().remove("error");
            } else {
                spikeField.getStyleClass().add("error");
            }
            setIsInvalid(seedInvalid || scaleInvalid || spikeInvalid);
        });
    }

    @Override
    public ArrayList<Line> generateResult() {
        Line line = new Line(getInputLine());
        var list = new ArrayList<Line>();
        SimpleTransforms.spikes(line,
                Integer.parseInt(seedField.getText()),
                Integer.parseInt(spikeField.getText()),
                Double.parseDouble(scaleField.getText()));
        list.add(line);
        return list;
    }
}
