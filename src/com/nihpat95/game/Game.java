package com.nihpat95.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Game extends JPanel implements KeyListener,Runnable   {

	public static final int WIDTH = 400;
	private static final int HEIGHT = 630;
	public static final Font main = new Font("Nexa",Font.PLAIN,28);
	private Thread game;
	private boolean running;
	private BufferedImage Image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);

	private long startTime;
	private long elapsed;
	private boolean set;
	private GameBoard board;
	
	public Game(){
		setFocusable(true);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		addKeyListener(this);
		board = new GameBoard(WIDTH/2 - GameBoard.Board_WIDTH/2, HEIGHT - GameBoard.Board_HEIGHT - 10);
	}

	private static final long serialVersionUID = 1L;

	private void update(){
		
		board.update();
		Keyboard.update();

	}
	private void render(){

		Graphics2D g = (Graphics2D) Image.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		board.render(g);

		//Render Board

		g.dispose();
		Graphics2D g2d = (Graphics2D) getGraphics();
		g2d.drawImage(Image, 0, 0, null);
		g2d.dispose();

	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		int fps = 0;
		int updates = 0;
		long fpsTimer = System.currentTimeMillis();
		double nsPerUpdate = 1000000000.0 / 60;

		double then = System.nanoTime();
		double unprocessed = 0;

		//update queue
		while(running){
			boolean shouldRender = false;
			double now = System.nanoTime();
			unprocessed += (now - then) / nsPerUpdate;
			then = now;

			while(unprocessed >=1){
				updates++;
				update();
				unprocessed--;
				shouldRender = true;
			}
			if(shouldRender){
				fps++;
				render();
				shouldRender = false;
			}
			else{
				try{
					Thread.sleep(1);
				}catch(Exception e){
					e.printStackTrace();
				}
			}

			if(System.currentTimeMillis() - fpsTimer > 1000){
				//System.out.printf("%d fps %d updates",fps, updates);
				//System.out.println();
				fps = 0;
				updates = 0; 
				fpsTimer += 1000;
			}
		}
	}

	public synchronized void start(){
		if(running) return;
		running = true; 
		game = new Thread(this,"game");
		game.start();
	}

	public synchronized void stop(){

		if(!running) return;
		running = false; 

		System.exit(0);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		// System.out.println("KeyPressed");
		Keyboard.keyPressed(e);

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		// System.out.println("KeyReleased");
		Keyboard.keyReleased(e);
	}

}
