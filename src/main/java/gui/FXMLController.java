package gui;

import data.Line;
import de.gsi.chart.XYChart;
import de.gsi.dataset.spi.DoubleDataSet;
import gui.component.inputTabs.*;
import gui.component.transformTabs.NormalizeTab;
import gui.component.transformTabs.ShiftTab;
import gui.component.transformTabs.SpikesTab;
import gui.component.transformTabs.StationarityTab;
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

    private ArrayList[] lines;
    private XYChart[] charts;

    private ArrayList<ParametersTab> inputParameters;
    private ArrayList<ParametersTab> transformParameters;

    private void addPlugins(XYChart... charts) {
        for (XYChart c: charts) {
            c.getXAxis().setLabel("x");
            c.getYAxis().setLabel("y");
        }
    }
    private void addToInputParameters(final ParametersTab comp) {
        inputParameters.add(comp);
        comp.setOnApplyClicked((e)->{
            final Integer selectedIdx = generateToCombo.getSelectionModel()
                    .getSelectedItem();
            var chart = charts[selectedIdx];
            lines[selectedIdx] = comp.generateResult();
            addToDataset(lines[selectedIdx], chart);

            if (!transformFromCombo.getItems().contains(selectedIdx)){
                transformFromCombo.getItems().add(selectedIdx);
            }
        });
    }
    private void addToDataset(ArrayList<Line> lines, XYChart chart){
        chart.getDatasets().clear();
        int i = 0;
        for (Line l : lines){
            DoubleDataSet set = new DoubleDataSet(Integer.toString(i));
            l.addToDataset(set);
            chart.getDatasets().add(set);
        }
    }
    private void addToTransformParameters(final ParametersTab comp) {
        transformParameters.add(comp);
        comp.setOnApplyClicked((e)->{
            final var dataSet = new DoubleDataSet("Преобразовано");
            final Integer selectedFromIdx = transformFromCombo.getSelectionModel()
                    .getSelectedItem();
            final Integer selectedToIdx = transformToCombo.getSelectionModel()
                    .getSelectedItem();
            comp.setInputLine((Line) lines[selectedFromIdx].get(0));
            var chart = charts[selectedToIdx];

            lines[selectedToIdx] = comp.generateResult();
            addToDataset(lines[selectedToIdx], chart);
        });
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lines = new ArrayList[4];
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
        generateToCombo.getSelectionModel().select(0);
        transformToCombo.valueProperty().addListener((e, oldVal, newVal)->{
            if (newVal != null && newVal.equals(oldVal)) return;
            transformTabs.setDisable(transformFromCombo.getValue() == null);
        });
        transformToCombo.getItems().addAll(2,3);
        transformToCombo.getSelectionModel().select(0);
        transformFromCombo.valueProperty().addListener((e, oldVal, newVal)->{
            if (newVal != null && newVal.equals(oldVal)) return;
            transformTabs.setDisable(false);
        });

        transformParameters = new ArrayList<>();
        addToTransformParameters(new NormalizeTab());
        addToTransformParameters(new ShiftTab());
        addToTransformParameters(new SpikesTab());
        addToTransformParameters(new StationarityTab());
        transformTabs.getTabs().addAll(transformParameters);

        addPlugins(firstChart,secondChart,thirdChart,fourthChart);
    }
}