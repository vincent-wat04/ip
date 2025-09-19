package vince;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import vince.storage.TaskList;

/**
 * A GUI for Vince AI Assistant using FXML.
 * Follows the MVC pattern with FXML-based view separation.
 */
public class Main extends Application {

    private TaskList tasks = new TaskList();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setTitle("Vince AI Assistant");
            
            // Initialize and inject dependencies
            MainWindow controller = fxmlLoader.<MainWindow>getController();
            controller.setTaskList(tasks);
            
            // Initialize images
            Image userImage = loadImageWithFallback("/images/DaUser.png");
            Image vinceImage = loadImageWithFallback("/images/DaVince.png");
            controller.setImages(userImage, vinceImage);
            
            // Show welcome message
            controller.showWelcomeMessage();
            
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Loads an image with fallback to placeholder if resource not found.
     * 
     * @param resourcePath the path to the image resource
     * @return loaded image or placeholder
     */
    private Image loadImageWithFallback(String resourcePath) {
        try {
            return new Image(this.getClass().getResourceAsStream(resourcePath));
        } catch (Exception e) {
            // Create a simple placeholder image if resource not found
            return new Image(
                    "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg==");
        }
    }
}
