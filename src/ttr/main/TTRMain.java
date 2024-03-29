package ttr.main;

import ttr.model.player.HumanPlayer;
import ttr.model.player.*;
import ttr.model.player.Player;
import ttr.model.player.StupidPlayer;
import ttr.view.scenes.TTRGamePlayScene;

public class TTRMain {

	public static void main(String[] args) {
		
		/* This is the game object required by the engine (essentially just the game window) */
		TicketToRide myGame = new TicketToRide();
		myGame.setFramesPerSecond(60);
		
		/* Initialize two players. This can be any combination of human players or AI players */
		//Player player1 = new StupidPlayer("Stupid  Player", 0.0f);
		Player player1 = new StupidPlayer("stupid_playeer", 0.0f);
		Player player2 = new Guwop("gieiow2");
		
		/* Setup the scene, and get the game started */
		TTRGamePlayScene scene = new TTRGamePlayScene("Ticket To Ride", "woodBacking.jpg", myGame, player1, player2);
		myGame.setCurrentScene(scene);
		
		scene.setScaleX(0.8);
		scene.setScaleY(0.8);
		
		player1.setScene(scene);
		player2.setScene(scene);
		myGame.start();
		scene.playGame();
	}
}




