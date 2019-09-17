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

public class NormalizeTab extends ParametersTab {
    @FXML
    private TextField scaleField;

    private boolean scaleInvalid = true;

    public NormalizeTab() {
        super();
        this.setText("Нормализация");
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setGroupingUsed(false);
        loadFxml(NormalizeTab.class.getResource("NormalizeTab.fxml"),this);
        scaleField.setTextFormatter(new TextFormatter<>(new NumberStringConverter(nf)));
        scaleField.textProperty().addListener((observable, oldValue, newValue) -> {
            scaleInvalid = newValue.length() == 0;
            if (!scaleInvalid) {
                scaleField.getStyleClass().remove("error");
            } else {
                scaleField.getStyleClass().add("error");
            }
            setIsInvalid(scaleInvalid);
        });
    }

    @Override
    public ArrayList<Line> generateResult() {
        Line line = new Line(getInputLine());
        ArrayList<Line> list = new ArrayList<>();
        SimpleTransforms.normalize(line,
                Double.parseDouble(scaleField.getText()));
        list.add(line);
        return list;
    }
}
