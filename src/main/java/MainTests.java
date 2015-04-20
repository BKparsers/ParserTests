import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by retor on 20.04.2015.
 */
public class MainTests extends Application{
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("alpha 2 TESTs");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.onCloseRequestProperty().set(event -> {
            Platform.exit();
            System.exit(0);
            System.gc();
        });
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
