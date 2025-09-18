package vince;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;

/**
 * An example of a custom control using FXML.
 * This control represents a dialog box consisting of an ImageView to represent
 * the speaker's face and a label
 * containing text from the speaker.
 */
public class DialogBox extends HBox {

    private Label text;
    private ImageView displayPicture;

    public DialogBox(Label l, ImageView iv) {
        text = l;
        displayPicture = iv;

        // Enable text wrapping and make it responsive
        text.setWrapText(true);
        text.setPrefWidth(Region.USE_COMPUTED_SIZE);
        text.setMaxWidth(300.0); // Set a reasonable maximum width for text
        
        // Make image responsive but with reasonable size constraints
        displayPicture.setFitWidth(80.0);
        displayPicture.setFitHeight(80.0);
        displayPicture.setPreserveRatio(true);
        displayPicture.setSmooth(true);

        // Set up responsive layout - user dialog should align to right
        this.setAlignment(Pos.CENTER_RIGHT);
        this.setSpacing(10.0);
        this.setPadding(new Insets(5.0));
        
        // Add children in order: text first, then image (for user dialog)
        this.getChildren().addAll(text, displayPicture);
        
        // Set preferred width to be responsive but with constraints
        this.setPrefWidth(Region.USE_COMPUTED_SIZE);
        this.setMaxWidth(400.0); // Limit the maximum width of the entire dialog box
        
        // Ensure the dialog box itself is aligned to the right within its container
        this.setStyle("-fx-background-color: transparent;");
    }

    /**
     * Flips the dialog box such that the ImageView is on the left and text on the
     * right. Used for Vince's responses.
     */
    private void flip() {
        this.setAlignment(Pos.CENTER_LEFT);
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        FXCollections.reverse(tmp);
        this.getChildren().setAll(tmp);
    }

    public static DialogBox getUserDialog(Label l, ImageView iv) {
        return new DialogBox(l, iv);
    }

    public static DialogBox getVinceDialog(Label l, ImageView iv) {
        var db = new DialogBox(l, iv);
        db.flip();
        return db;
    }
}
