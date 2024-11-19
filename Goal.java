
import javafx.scene.control.Button;

public class Goal extends Feature
{
    private static GameState gameState = GameState.getInstance();
    public static Goal instance;
    private static boolean deployed = false;

    private Goal()
    {

    }
    public static Goal getInstance() {
        if (instance == null) {
            synchronized (Goal.class) {
                if (instance == null) {
                    instance = new Goal();
                }
            }
        }
        return instance;
    }

    public void doThing()
    {
        Button state = Main.getButtonState();
        Button save = Main.getSaveState();
        save.setVisible(true);
        state.setText("Player");
        gameState.editorMode(!gameState.getState()); //go back to editor mode
    }

    public boolean getIfDeployed(){return deployed;}

    public void setDeployedAs(boolean deployed){
        this.deployed = deployed;
    }

}
