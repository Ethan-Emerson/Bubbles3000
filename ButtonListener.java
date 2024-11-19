
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class ButtonListener implements EventHandler<ActionEvent> {

    private static GameState state = GameState.getInstance();
    private static Level theLevel = Main.getGameInstance().getTheLevel();

    @Override
    public void handle(ActionEvent event) {
        if(event.getSource() == Main.getButtonState()){
            if (Goal.getInstance().getIfDeployed() && Start.getInstance().getIfDeployed()) {
                //theLevel.loadLevel();
                state.editorMode(!state.getState()); //Switch Mode's back and forth
                Button gameState = Main.getButtonState();
                Button saveState = Main.getSaveState();

                //Change the Button Text
                if (state.getState()) {
                    gameState.setText("Stop");
                    saveState.setVisible(false);
                    theLevel.saveLevel(); //auto save
                    theLevel.resetTimerHandlers();
                } else {
                    gameState.setText("Player");
                    saveState.setVisible(true);
                }
            } else {
                // Logic to handle missing goal or start
            }
        }
        else if(event.getSource() == Main.getSaveState()){
            theLevel.saveLevel();
        }
    }
}