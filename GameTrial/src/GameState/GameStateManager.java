package GameState;

import java.util.ArrayList;

public class GameStateManager {

	private ArrayList<GameState> gameStates;
	private int currentState;

	public static final int MENUSTATE = 0;
	public static final int LEVEL1STATE = 1;

	public GameStateManager() {
		gameStates = new ArrayList<GameState>();

		currentState = MENUSTATE;
		gameStates.add(new MenuState(this));
		gameStates.add(new Level1State(this));
	}

	public void setState(int state) {
		currentState = state;
		gameStates.get(currentState).init();
	}

	public void update() {
		gameStates.get(currentState).update();
	}

	public void draw(java.awt.Graphics2D graphics) {
		gameStates.get(currentState).draw(graphics);
	}

	public void keyPressed(int key) {
		gameStates.get(currentState).keyPressed(key);
	}

	public void keyReleased(int key) {
		gameStates.get(currentState).keyReleased(key);
	}

}
