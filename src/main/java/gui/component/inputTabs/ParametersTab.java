package gui.component.inputTabs;

import data.Line;
import javafx.beans.DefaultProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

@DefaultProperty("extension")
abstract public class ParametersTab extends Tab {

    @FXML
    private VBox extension;
    public ObservableList<Node> getExtension() {
        return extension.getChildren();
    }

    @FXML
    private Button applyButton;
    private EventHandler<ActionEvent> onApplyClicked;
    public void setOnApplyClicked(final EventHandler<ActionEvent> onApplyClicked) {
        this.onApplyClicked = onApplyClicked;
        applyButton.setOnAction(onApplyClicked);
    }

    private Line inputLine;
    public void setInputLine(final Line line){inputLine = line;}
    protected Line getInputLine(){return inputLine;}

    private ReadOnlyBooleanWrapper isInvalid = new ReadOnlyBooleanWrapper(this, "isInvalid",true);
    public final boolean getIsInvalid() { return isInvalid.get(); }
    public final void setIsInvalid(boolean value) { isInvalid.set(value);}
    public final ReadOnlyBooleanProperty isInvalidProperty() { return isInvalid.getReadOnlyProperty(); }

    public abstract ArrayList<Line> generateResult();

    public ParametersTab()
    {
        super();
        loadFxml(ParametersTab.class.getResource("ParametersTab.fxml"),this);
        applyButton.disableProperty().bind(isInvalid);
    }

    protected void loadFxml(URL fxmlFile, Object rootController) {
        var loader = new FXMLLoader(fxmlFile);
        loader.setController(rootController);
        loader.setRoot(rootController);
        try {
            loader.load();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}
