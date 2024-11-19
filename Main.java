
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.stage.*;
import javafx.scene.control.Button;

import javafx.util.Duration;

public class Main extends Application
{
    private static Game game = new Game();
    private static GameCanvas canvas = new GameCanvas();
    private static int width = 800;
    private static int height = 450;
    private static Button state = new Button("Player"); //Button to Switch state of the Game
    private static Button save = new Button("Save"); //Save the game when pressed

    public static int getWidth()
    {
        return width;
    }

    public static int getHeight()
    {
        return height;
    }

    public static GameCanvas getCanvas()
    {
        return canvas;
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        canvas = new GameCanvas();
        Group root = new Group();

        root.getChildren().add(canvas);

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {

            game.getAnimationHandler().handle();
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        // 60 fps cap to the game
        timeline.setRate(60);

        //Add the Buttons to the Game
        state.setOnAction(new ButtonListener());
        state.setFocusTraversable(false);
        save.setOnAction(new ButtonListener());
        save.setVisible(true);
        save.setLayoutX(750);
        save.setFocusTraversable(false);
        root.getChildren().add(state);
        root.getChildren().add(save);

        Scene scene = new Scene(root, Main.getWidth(), Main.getHeight());
        scene.setOnKeyPressed(new KeyPressed());
        scene.setOnKeyReleased(new KeyReleased());
        scene.setOnMouseClicked(new MouseEvents());
        scene.setOnMouseMoved(new MouseEvents());
        scene.setOnMouseDragged(new MouseEvents());
        stage.setResizable(false);
        stage.setTitle("Bubbles");
        stage.setScene(scene);
        stage.minWidthProperty().bind(scene.widthProperty());
        stage.minHeightProperty().bind(scene.heightProperty());
        stage.getIcons().add(new Image("file:images/bluefloor.png"));
        stage.show();
    }

    public static Game getGameInstance()
    {
        return game;
    }
    public static void main(String[] args)
    {
        launch(args);
    }
    public static Button getButtonState(){return state;}
    public static Button getSaveState(){return save;}
}