
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent welcomeSceneLayout = FXMLLoader.load(getClass().getResource("/fxml/WelcomeScene.fxml"));
        Scene welcomeScene = new Scene(welcomeSceneLayout);
        welcomeScene.getStylesheets().add(getClass().getResource("/css/WelcomeScene.css").toExternalForm());
        stage.setTitle("Notebook");
        stage.setWidth(700.0);
        stage.setHeight(550.0);
        stage.setResizable(false);
        stage.setScene(welcomeScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
