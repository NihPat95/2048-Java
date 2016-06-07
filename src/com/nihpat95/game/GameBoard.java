package com.nihpat95.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Random;

public class GameBoard {

	public static final int ROWS = 4;
	public static final int COLS = 4;
	private final int startinTile = 2;
	private Tile[][]board;
	private boolean dead;
	private boolean won;
	private BufferedImage gameBoard;
	private BufferedImage finalImage;
	private int X;
	private int score = 0;
	private int highscore;
	private Font scoreFont;
	private int Y;
	private static int SPACING = 10;
	public static int Board_WIDTH = (COLS + 1)*SPACING + COLS * Tile.WIDTH;
	public static int Board_HEIGHT = (ROWS + 1)*SPACING + ROWS * Tile.HEIGHT;
	private String saveDataPath;
	private String gameover;
	private String filename= "metadata";

	private boolean hasStarted;

	public GameBoard(int x, int y){
		try{
			
			saveDataPath = GameBoard.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		gameover = "";
		scoreFont = Game.main.deriveFont(24f);
		dead = false;
		X = x;
		Y = y;
		board = new Tile[ROWS][COLS];
		gameBoard = new BufferedImage(Board_WIDTH,Board_HEIGHT,BufferedImage.TYPE_INT_RGB);
		finalImage = new BufferedImage(Board_WIDTH,Board_HEIGHT,BufferedImage.TYPE_INT_RGB);
		loadhighscore();
		createBoardImage();
		start();
	}

private void createSaveData(){
	try{
		System.out.println("Creating File");
		File file = new File(saveDataPath,filename);
		FileWriter output = new FileWriter(file);
		BufferedWriter writer  = new BufferedWriter(output);
		writer.write(""+0);
		writer.flush();
		writer.close();
	}catch(Exception e){
		e.printStackTrace();
	}
	
}
private void storeHighscore(){
	
	

}
private void sethighScore(){
	
	FileWriter out = null;
	try{
		File f = new File(saveDataPath,filename);
		out = new FileWriter(f);
		BufferedWriter writer = new BufferedWriter(out);
		System.out.println("Writing High Score");
			writer.write(""+highscore);
			writer.flush();
			writer.close();
		
		
	}catch(Exception e){
		e.printStackTrace();
	}
	
	
}
	
	private void loadhighscore() {
		// TODO Auto-generated method stub
		
		try{
		File f = new File(saveDataPath,filename);
		if(!f.isFile()){
			System.out.println("File Not Found");
			createSaveData();
		}
		
		BufferedReader reader = new BufferedReader (new InputStreamReader(new FileInputStream(f)));
		highscore = Integer.parseInt(reader.readLine());
		reader.close();
			
		}
		catch(Exception e){
			e.printStackTrace();
			highscore = 0 ;
		}
		
		}
		
	


	private void createBoardImage() {
		// TODO Auto-generated method stub

		Graphics2D g = (Graphics2D) gameBoard.getGraphics();
		g.setColor(Color.gray);
		g.fillRect(0, 0, Board_WIDTH, Board_HEIGHT);
		g.setColor(Color.lightGray);

		for(int row =0 ; row < ROWS; row++)
		{
			for(int col=0; col < COLS; col++){
				int x = SPACING + SPACING * col + Tile.WIDTH * col;
				int y = SPACING + SPACING * row + Tile.HEIGHT * row; 
				g.fillRoundRect(x, y, Tile.WIDTH, Tile.HEIGHT, Tile.ARC_WIDTH, Tile.ARC_HEIGHT);
			}
		}


	}
	public void render(Graphics2D g){
		Graphics2D g2d = (Graphics2D)finalImage.getGraphics();
		g2d.drawImage(gameBoard, 0, 0, null);

		for(int i=0; i<ROWS; i++)
		{
			for(int j=0; j<COLS; j++)
			{
				Tile current = board[i][j];
				if(current==null){
					 
				}
				else{
					current.render(g2d);
				}
			}
		}

		g.drawImage(finalImage, X, Y, null);
		g2d.dispose();
		g.setColor(Color.LIGHT_GRAY);
		g.setFont(scoreFont);
		g.drawString("Current: "+score, 20, 40);
		g.setColor(Color.red);
		g.drawString("Best: "+highscore, Game.WIDTH - DrawUtils.getMessageWidth("Best: "+highscore, scoreFont, g) - 20, 40);
		g.drawString(gameover, 20, 80);
	}

	public void update(){
		checkKeys();
		
		if(score >= highscore){
			highscore = score;
		}
		
		
		for(int i=0; i<ROWS; i++){
			for(int j=0; j<COLS; j++)
			{
				Tile current = board[i][j];
				if(current == null){
					 
				}
				else
				{
					current.update();
					resetPosition(current,i,j);
					if(current.getValue() == 2048)
					{
						won = true;
					}
				}
			}
		}

	}


	private void resetPosition(Tile current, int i, int j) {
		// TODO Auto-generated method stub
		if(current == null)
			return;
		int x = getTileX(j);
		int y = getTileY(i);
		int distx = current.getX() - x;
		int disty = current.getY() - y;

		if(Math.abs(distx)<Tile.SLIDE_SPEED){
			current.setX(current.getX() - distx);
		}
		if(Math.abs(disty)<Tile.SLIDE_SPEED){
			current.setY(current.getY() - disty);
		}
		if(distx < 0 ){
			current.setX(current.getX() + Tile.SLIDE_SPEED);
		}
		if(disty < 0 ){
			current.setY(current.getY() + Tile.SLIDE_SPEED);
		}
		if(distx > 0){
			current.setX(current.getX() - Tile.SLIDE_SPEED);
		}
		if(disty > 0){
			current.setY(current.getY() - Tile.SLIDE_SPEED);
		}

	}


	private void checkKeys() {
		// TODO Auto-generated method stub
		if(Keyboard.typed(KeyEvent.VK_LEFT)){
			moveTile(Direction.LEFT);
			if(!hasStarted)
				hasStarted = true;
		}
		if(Keyboard.typed(KeyEvent.VK_RIGHT)){
			moveTile(Direction.RIGHT);
			if(!hasStarted)
				hasStarted = true;
		}

		if(Keyboard.typed(KeyEvent.VK_DOWN)){
			moveTile(Direction.DOWN);
			if(!hasStarted)
				hasStarted = true;
		}

		if(Keyboard.typed(KeyEvent.VK_UP)){
			moveTile(Direction.UP);
			if(!hasStarted)
				hasStarted = true;
		}

	}

	private void moveTile(Direction d) {
		// TODO Auto-generated method stub
		
		System.out.println("Inside Move Tile");
		boolean canMove = false;
		int horizontalDirection = 0;
		int verticalDirection = 0;

		if(d == Direction.LEFT){
			horizontalDirection = -1;
			for(int i=0; i<ROWS; i++)
			{
				for(int j=0; j<COLS; j++){
					if(!canMove){
						canMove = move(i,j,horizontalDirection,verticalDirection,d);
					}
					else
						move(i,j,horizontalDirection,verticalDirection,d);
				} 
			}
		}
		else if(d == Direction.RIGHT){
			horizontalDirection = 1;
			for(int i=0; i<ROWS; i++)
			{
				for(int j=COLS-1; j>=0; j--){
					if(!canMove){
						canMove = move(i,j,horizontalDirection,verticalDirection,d);
					}
					else
						move(i,j,horizontalDirection,verticalDirection,d);
				} 
			}
		}
		else if(d == Direction.UP){
			verticalDirection = -1;
			for(int j=0; j<COLS; j++)
			{
				for(int i=0; i<ROWS; i++){
					if(!canMove){
						canMove = move(i,j,horizontalDirection,verticalDirection,d);
					}
					else
						move(i,j,horizontalDirection,verticalDirection,d);
				} 
			}
		}
		else if(d == Direction.DOWN){
			verticalDirection = 1;
			for(int j=0; j<COLS; j++)
			{
				for(int i=ROWS-1; i>=0; i--){
					if(!canMove){
						canMove = move(i,j,horizontalDirection,verticalDirection,d);
					}
					else
						move(i,j,horizontalDirection,verticalDirection,d);
				} 
			}
		}
		else
			System.out.println("Not Valid Direction");

		
		
		
		for(int i=0; i<ROWS; i++)
		{
			for(int j=0; j<COLS; j++)
			{
				Tile current = board[i][j];
				if(current == null)
				{
					 
				}
				else
				{
					current.setCanCombine(true);
				}
			}

			
		}
		
		System.out.println("Value Of Can Move: "+canMove);
		if(canMove){
			spawnRandom();
			//My change
			//checkDead();
		}

		checkDead();

	}


	private void checkDead() {
		// TODO Auto-generated method stub
		for(int i=0; i<ROWS; i++)
		{
			for(int j=0; j<COLS; j++){
				//System.out.println("checkSurroundingTile : "+(checkSurroundingTiles(i,j,board[i][j])));
				if(board[i][j] == null)
					{
					return;
					}
				if(checkSurroundingTiles(i,j,board[i][j]))
				{
					
					return;
				}
				
			}
		}
		gameover = "Game Over";
		dead = true;
	    System.out.println("Game Over");
		sethighScore();
	}


	private boolean checkSurroundingTiles(int i, int j, Tile tile) {
		// TODO Auto-generated method stub

		if(i > 0){
			Tile check = board [i-1][j];
			if(check==null) 
				return true;
			if(tile.getValue() == check.getValue())
				return true;
		}
		else if(i < ROWS - 1){
			Tile check = board [i+1][j];
			if(check==null) 
				return true;
			if(tile.getValue() == check.getValue())
				return true;
		}
		else if(j > 0){
			Tile check = board [i][j-1];
			if(check==null) 
				return true;
			if(tile.getValue() == check.getValue())
				return true;
		}
		else if(j < COLS - 1){
			Tile check = board [i][j+1];
			if(check==null) 
				return true;
			if(tile.getValue() == check.getValue())
				return true;
		}
		return false;
	}


	private boolean move(int i, int j, int horizontalDirection, int verticalDirection, Direction d) {
		// TODO Auto-generated method stub
		
		boolean canMove = false;

		Tile current = board[i][j];
		if(current == null)
			return false;
		boolean move = true;
		int newcol = j;
		int newrow = i;
		while(move){

			newcol+=horizontalDirection;
			newrow+=verticalDirection;
			if(checkOutOfBounds(d,newrow,newcol))
				{
				System.out.println("Out of Bounds Case");
				break;
				}
			if(board[newrow][newcol] == null){
				board[newrow][newcol] = current;
				board[newrow - verticalDirection][newcol - horizontalDirection] = null;
				board[newrow][newcol].setSlideTo(new Point(newrow,newcol));
				canMove = true;
				System.out.println("Case 1");
			}
			else if(board[newrow][newcol].getValue() == current.getValue() && board[newrow][newcol].CanCombine())
			{
				board[newrow][newcol].setCanCombine(false);
				board[newrow][newcol].setValue(board[newrow][newcol].getValue() * 2);
				canMove = true; 
				board[newrow - verticalDirection][newcol - horizontalDirection]=null;
				board[newrow][newcol].setSlideTo(new Point(newrow, newcol));
				board[newrow][newcol].setCombineAnimation(true);
				score += board[newrow][newcol].getValue();
				System.out.println("Case 2");
			}
			else
			{
				move = false;
				System.out.println("Case 3");
			}


		}


		return canMove;
	}


	private boolean checkOutOfBounds(Direction d, int newrow, int newcol) {
		// TODO Auto-generated method stub
//		if(d == Direction.LEFT){
//			return newcol < 0;
//		}
//		else if(d == Direction.RIGHT){
//			return newcol > COLS - 1;
//		}
//		else if(d == Direction.UP){
//			return newrow < 0;
//		}
//		else if(d == Direction.DOWN){
//			return newrow > ROWS - 1;
//		}
		
		if( newrow < ROWS && newrow >=0 && newcol < COLS && newcol >= 0)
			return false;
		
		return true;
	}


	private void start(){
		for(int i=0; i<startinTile; i++)
		{
			spawnRandom();
		} 
	}


	private void spawnRandom() {
		// TODO Auto-generated method stub
		
		System.out.println("Inside Spawn Random");
		
		if(dead)
		{
			System.out.println("Random Game Over");
			
		}
			
		
		Random random = new Random();
		boolean notValid = true;
		while(notValid){
			int location = random.nextInt(ROWS * COLS);
			int row = location / ROWS;
			int col = location % COLS;
			Tile current = board[row][col];
			if(current == null)
			{
				int value = random.nextInt(10) < 9 ? 2 : 4;
				Tile tile = new Tile(value, getTileX(col), getTileY(row));
				board[row][col] = tile;
				notValid = false;
			}
		}
	}


	private int getTileX(int col) {
		// TODO Auto-generated method stub

		return SPACING + col * Tile.WIDTH + col * SPACING;
	}


	private int getTileY(int row) {
		// TODO Auto-generated method stub
		return SPACING + row * Tile.HEIGHT + row * SPACING;
	}
}
