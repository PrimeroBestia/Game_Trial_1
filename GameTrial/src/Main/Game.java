package Main;

import javax.swing.JFrame;

public class Game {
	public static void main(String[] arg) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame window = new JFrame("Game");
		window.setLocation(0,0);
		window.setOpacity(1f);
		window.setContentPane(new GamePanel());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack();
		window.setVisible(true);
	}
}
