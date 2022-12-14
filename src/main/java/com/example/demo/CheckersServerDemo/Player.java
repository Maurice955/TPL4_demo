package com.example.demo.CheckersServerDemo;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Player implements Runnable {
    private final Game game;
    private Player opponent;
    public PlayerRole playerRole;
    public Socket socket;
    public Scanner input;
    public PrintWriter output;


    public Player(Socket socket, PlayerRole playerRole, Game game) {
        this.socket = socket;
        this.playerRole = playerRole;
        this.game = game;
    }

    public Player getOpponent(){
        return this.opponent;
    }


    @Override
    public void run() {
        try{
            setup();
            processCommands();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (opponent != null && opponent.output != null) {
                opponent.output.println("OTHER_PLAYER_LEFT");
            }
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void setup() throws IOException {
        input = new Scanner(socket.getInputStream());
        output = new PrintWriter(socket.getOutputStream(), true);
        output.println("WELCOME " + playerRole.side);
        if (playerRole == PlayerRole.WHITE) {
            game.currentPlayer = this;
            output.println("MESSAGE Waiting for opponent to connect");
        } else {
            this.opponent = game.currentPlayer;
//            this.opponent.opponent = this;
            opponent.output.println("MESSAGE Waiting for opponent move");
        }
    }

    private void processCommands() {
        while (input.hasNextLine()) {
            var command = input.nextLine();
            if (command.startsWith("QUIT")) {
                return;
            } else if (command.startsWith("MOVE")) {
                processMoveCommand(
                        Integer.parseInt( command.substring(5).split(":") [0] ),
                        Integer.parseInt( command.substring(5).split(":") [1] )
                );
            }
        }
    }

    private void processMoveCommand(int xLocation, int yLocation) {
        try {
            game.move(xLocation, yLocation, this);
            output.println("VALID_MOVE");
            opponent.output.println("OPPONENT_MOVED " + xLocation + ":" + yLocation);
            if (game.hasWinner()) {
                output.println("VICTORY");
                opponent.output.println("DEFEAT");
            } else if (1==2 /*DRAW CONDITION*/) {
                output.println("DRAW");
                opponent.output.println("DRAW");
            }
        } catch (IllegalStateException e) {
            output.println("MESSAGE " + e.getMessage());
        }
    }

}
