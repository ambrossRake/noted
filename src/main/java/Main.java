import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Parent welcomeSceneLayout = FXMLLoader.load(getClass().getResource("/fxml/WelcomeScene.fxml"));
		Scene welcomeScene = new Scene(welcomeSceneLayout);
		welcomeScene.getStylesheets().add(getClass().getResource("/css/WelcomeScene.css").toExternalForm());
		stage.setTitle("noted");
		stage.setWidth(764.0);
		stage.setHeight(573.0);
		stage.setResizable(false);
		stage.setScene(welcomeScene);
		stage.show();
	}

}
