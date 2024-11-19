
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class Death extends Feature {

    private GameState gameState = GameState.getInstance();

    public Death() {}

    public void doThing() {
        GraphicsContext gc = Main.getCanvas().getGraphicsContext2D();
        Group root = (Group) Main.getCanvas().getParent();

        double canvasWidth = Main.getCanvas().getWidth();
        double canvasHeight = Main.getCanvas().getHeight();

        // Create a black rectangle for background
        Rectangle fadeRect = new Rectangle(0, 0, canvasWidth, canvasHeight);
        fadeRect.setFill(Color.BLACK);
        fadeRect.setOpacity(0);

        // Create text for "GAME OVER" and "YOU DIED"
        Text gameOverText = new Text("GAME OVER");
        gameOverText.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        gameOverText.setFill(Color.RED);
        gameOverText.setTextAlignment(TextAlignment.CENTER);
        gameOverText.setOpacity(0);

        Text youDiedText = new Text("YOU DIED!");
        youDiedText.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        youDiedText.setFill(Color.RED);
        youDiedText.setTextAlignment(TextAlignment.CENTER);
        youDiedText.setOpacity(0);

        // Center the text
        gameOverText.setX((canvasWidth - gameOverText.getBoundsInLocal().getWidth()) / 2);
        gameOverText.setY((canvasHeight - gameOverText.getBoundsInLocal().getHeight()) / 2 - 30);

        youDiedText.setX((canvasWidth - youDiedText.getBoundsInLocal().getWidth()) / 2);
        youDiedText.setY((canvasHeight + youDiedText.getBoundsInLocal().getHeight()) / 2 + 30);

        // Add the rectangle and texts to the root group
        root.getChildren().addAll(fadeRect, gameOverText, youDiedText);

        // Create fade-in transitions
        FadeTransition fadeInRect = new FadeTransition(Duration.seconds(0.7), fadeRect);
        fadeInRect.setFromValue(0);
        fadeInRect.setToValue(1);

        FadeTransition fadeInGameOver = new FadeTransition(Duration.seconds(0.7), gameOverText);
        fadeInGameOver.setFromValue(0);
        fadeInGameOver.setToValue(1);

        FadeTransition fadeInYouDied = new FadeTransition(Duration.seconds(0.7), youDiedText);
        fadeInYouDied.setFromValue(0);
        fadeInYouDied.setToValue(1);

        // Create fade-out transitions
        FadeTransition fadeOutRect = new FadeTransition(Duration.seconds(0.7), fadeRect);
        fadeOutRect.setFromValue(1);
        fadeOutRect.setToValue(0);

        FadeTransition fadeOutGameOver = new FadeTransition(Duration.seconds(0.7), gameOverText);
        fadeOutGameOver.setFromValue(1);
        fadeOutGameOver.setToValue(0);

        FadeTransition fadeOutYouDied = new FadeTransition(Duration.seconds(0.7), youDiedText);
        fadeOutYouDied.setFromValue(1);
        fadeOutYouDied.setToValue(0);

        // Combine fade-in and fade-out transitions
        ParallelTransition fadeIn = new ParallelTransition(fadeInRect, fadeInGameOver, fadeInYouDied);
        ParallelTransition fadeOut = new ParallelTransition(fadeOutRect, fadeOutGameOver, fadeOutYouDied);

        // Create a sequential transition for the entire animation
        SequentialTransition seqTransition = new SequentialTransition(
                fadeIn,
                new javafx.animation.PauseTransition(Duration.seconds(0)), // No pause
                fadeOut
        );

        seqTransition.setOnFinished(e -> {
            // Remove the fade rectangle and texts
            root.getChildren().removeAll(fadeRect, gameOverText, youDiedText);

            // Execute the original code
            Button state = Main.getButtonState();
            Button save = Main.getSaveState();
            save.setVisible(true);
            state.setText("Player");
            gameState.editorMode(false); // Go back to editor mode
        });

        // Start the animation
        seqTransition.play();
    }
}