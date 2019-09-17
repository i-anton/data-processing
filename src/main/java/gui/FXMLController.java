package gui;

import data.Line;
import de.gsi.chart.XYChart;
import de.gsi.dataset.spi.DoubleDataSet;
import gui.component.inputTabs.ParametersTab;
import gui.component.inputTabs.*;
import gui.component.transformTabs.NormalizeTab;
import gui.component.transformTabs.ShiftTab;
import gui.component.transformTabs.SpikesTab;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class FXMLController implements Initializable {
    @FXML
    private XYChart firstChart;
    @FXML
    private XYChart secondChart;
    @FXML
    private XYChart thirdChart;
    @FXML
    private XYChart fourthChart;
    @FXML
    private TabPane parameterTabs;
    @FXML
    private TabPane transformTabs;
    @FXML
    private ComboBox<Integer> generateToCombo;
    @FXML
    private ComboBox<Integer> transformFromCombo;
    @FXML
    private ComboBox<Integer> transformToCombo;

    private Line[] lines;
    private XYChart[] charts;

    private ArrayList<ParametersTab> inputParameters;
    private ArrayList<ParametersTab> transformParameters;

    private void addPlugins(XYChart... charts) {
        for (XYChart c: charts) {
            c.getXAxis().setLabel("x");
            c.getYAxis().setLabel("y");
            //TODO: Fix broken plugins
        }
    }
    private void addToInputParameters(final ParametersTab comp) {
        inputParameters.add(comp);
        comp.setOnApplyClicked((e)->{
            final var dataSet = new DoubleDataSet("Вход");
            final Integer selectedIdx = generateToCombo.getSelectionModel()
                    .getSelectedItem();
            var chart = charts[selectedIdx];
            chart.getDatasets().clear();
            chart.getDatasets().add(dataSet);
            lines[selectedIdx] = comp.generateResult();
            lines[selectedIdx].addToDataset(dataSet);

            if (!transformFromCombo.getItems().contains(selectedIdx)){
                transformFromCombo.getItems().add(selectedIdx);
            }
        });
    }
    private void addToTransformParameters(final ParametersTab comp) {
        transformParameters.add(comp);
        comp.setOnApplyClicked((e)->{
            final var dataSet = new DoubleDataSet("Преобразовано");
            final Integer selectedFromIdx = transformFromCombo.getSelectionModel()
                    .getSelectedItem();
            final Integer selectedToIdx = transformToCombo.getSelectionModel()
                    .getSelectedItem();
            var chart = charts[selectedToIdx];
            chart.getDatasets().clear();
            chart.getDatasets().add(dataSet);
            comp.setInputLine(lines[selectedFromIdx]);
            lines[selectedToIdx] = comp.generateResult();
            lines[selectedToIdx].addToDataset(dataSet);
        });
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lines = new Line[4];
        charts = new XYChart[4];
        charts[0] = firstChart;
        charts[1] = secondChart;
        charts[2] = thirdChart;
        charts[3] = fourthChart;
        inputParameters = new ArrayList<>();
        addToInputParameters(new LinearParametersTab());
        addToInputParameters(new ExponentialParametersTab());
        addToInputParameters(new PiecewiseParametersTab());
        addToInputParameters(new RandomParametersTab());
        addToInputParameters(new MyRandomParametersTab());
        parameterTabs.getTabs().addAll(inputParameters);
        generateToCombo.getItems().addAll(0,1);
        transformToCombo.valueProperty().addListener((e, oldVal, newVal)->{
            if (newVal != null && newVal.equals(oldVal)) return;
            transformTabs.setDisable(transformFromCombo.getValue() == null);
        });
        transformToCombo.getItems().addAll(2,3);
        transformFromCombo.valueProperty().addListener((e, oldVal, newVal)->{
            if (newVal != null && newVal.equals(oldVal)) return;
            transformTabs.setDisable(transformToCombo.getValue() == null);
        });

        transformParameters = new ArrayList<>();
        addToTransformParameters(new NormalizeTab());
        addToTransformParameters(new ShiftTab());
        addToTransformParameters(new SpikesTab());
        transformTabs.getTabs().addAll(transformParameters);

        addPlugins(firstChart,secondChart,thirdChart,fourthChart);
    }
}