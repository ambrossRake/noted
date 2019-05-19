package input;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.UnaryOperator;

public class InputDialogue {

	private static Stage stage;
	private static Scene scene;
	private static String userInput;

	public static String promptUser(String title, String header, InputFilter filter, int maxLength) throws IOException {

		stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setMinWidth(240);
		stage.setMinHeight(120);

		stage.setTitle(title);

		Parent layout = FXMLLoader.load(InputDialogue.class.getResource("/fxml/InputDialogue.fxml"));

		scene = new Scene(layout);
		scene.getStylesheets().add(InputDialogue.class.getResource("/css/InputDialogue.css").toExternalForm());

		UnaryOperator<TextFormatter.Change> changeUnaryOperator = c -> {
			if (c.isContentChange()) {
				int newLength = c.getControlNewText().length();
				if (newLength > maxLength) {
					c.setText(c.getControlNewText().substring(0, maxLength));
					c.setRange(0, maxLength);
				}
			}

			return c;
		};

		Label inputHeader = (Label) scene.lookup("#inputHeader");
		inputHeader.setText(header);

		TextField inputField = (TextField) scene.lookup("#inputField");
		inputField.setTextFormatter(new TextFormatter<>(changeUnaryOperator));

		Button confirmationButton = (Button) scene.lookup("#confirmationButton");
		confirmationButton.setOnAction(e -> verifyInputFilter(filter, inputField.getText()));
		Button cancelButton = (Button) scene.lookup("#cancelButton");
		cancelButton.setOnAction(e -> {
			userInput = null;
			stage.close();
		});

		stage.setScene(scene);
		stage.showAndWait();

		return userInput;
	}

	private static void verifyInputFilter(InputFilter filter, String inputText) {
		Label errorMessage = (Label) scene.lookup("#errorMessage");
		if (inputText.isEmpty()) {
			errorMessage.setText("*Name can not be empty");
		} else if (filter.verify(inputText)) {
			errorMessage.setText(filter.getFilterMessage());
		} else {
			userInput = inputText;
			stage.close();
		}
	}
}
