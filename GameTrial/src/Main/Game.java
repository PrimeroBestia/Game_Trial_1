package Main;

import javax.swing.JFrame;

public class Game {
	public static void main(String[] arg) {
		JFrame window = new JFrame("Game");
		window.setContentPane(new GamePanel());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);	
		window.pack();
		window.setVisible(true);
	}
}
