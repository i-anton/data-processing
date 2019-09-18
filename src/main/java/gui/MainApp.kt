package gui

import javafx.application.Application
import javafx.application.Application.launch
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

class MainApp : Application() {

    @Throws(Exception::class)
    override fun start(stage: Stage) {
        val root = FXMLLoader.load<Parent>(javaClass.getResource("scene.fxml"))
        val scene = Scene(root)
        scene.stylesheets.add(javaClass.getResource("styles.css").toExternalForm())
        stage.title = "Data Processing"
        stage.scene = scene
        stage.show()
    }
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(MainApp::class.java, *args)
        }
    }
}
