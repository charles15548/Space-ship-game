package state;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

import gameObject.Constants;
import gameObject.Meteor;
import gameObject.MovingObject;
import gameObject.Player;
import gameObject.Size;
import gameObject.Ufo;
import graphics.Animation;
import graphics.Assets;
import math.Vector2d;

public class Game_state {
	private Player player;
	private ArrayList<MovingObject> movingObjects = new ArrayList<MovingObject>();
	private ArrayList<Animation> explosion = new ArrayList<Animation>();
	private int score =0;
	private int meteors;
	private int lives = 3;
	
	public Game_state() {
		player = new Player(new Vector2d(Constants.WIDTH/2 - Assets.player.getWidth()/2,
				Constants.HEIGHT/2 - Assets.player.getHeight()/2),
				new Vector2d(),Constants.PLAYER_MAX_VEL, Assets.player, this);
		movingObjects.add(player);
		meteors = 1;
		
		starWave();
	}
	public void addScore(int value) {
		score += value;
	}
	
	public void divideMeteor(Meteor meteor) {
		Size size = meteor.getSize();
		
		BufferedImage[] textures = size.textures;
		
		Size newSize = null;
		
		switch(size) {
		case BIG:
			newSize = Size.MED;
			break;
		case MED:
			newSize = Size.SMALL;
			break;
		case SMALL:
			newSize = Size.TINY;
			break;
		default:
			return;
		}
		
		for (int i = 0; i <size.quantity; i++) {
			
			 movingObjects.add(new Meteor(
					 meteor.getPosition(),
					 new Vector2d(0,1).setDireccion(Math.random()*Math.PI*2),
					 Constants.METEOR_VEL*Math.random()+1,
					 textures[(int)(Math.random()*textures.length)],
					 this,
					 newSize
					 ));
		}
	}
	
	private void starWave() {
		double x, y;
		for (int i = 0; i < meteors; i++) {
			x = i % 2 == 0 ? Math.random()*Constants.WIDTH : 0;
			y = i % 2 == 0 ? 0 : Math.random()*Constants.HEIGHT;
			 BufferedImage texture =  Assets.bigs[(int)(Math.random()*Assets.bigs.length)];
			
			 movingObjects.add(new Meteor(
					 new Vector2d(x,y),
					 new Vector2d(0,1).setDireccion(Math.random()*Math.PI*2),
					 Constants.METEOR_VEL*Math.random()+1,
					 texture,
					 this,
					 Size.BIG
					 ));
		}
		meteors ++;
		spawnUfo();
		
	}
	
	public void playExplosion(Vector2d position) {
		explosion.add(new Animation(
				Assets.exp,
				50,
				position.subtrac(new Vector2d(Assets.exp[0].getWidth()/2,
						Assets.exp[0].getHeight()/2))
				));
	}
	
	
	private void spawnUfo() {
		int rand = (int) (Math.random()*2);
		double x = rand  == 0 ? (Math.random()*Constants.WIDTH) : 0;
		double y = rand  == 0 ? 0 : (Math.random()*Constants.HEIGHT);
		
		ArrayList<Vector2d> path = new ArrayList<Vector2d>();
		
		double posX, posY;
		
		posX = Math.random()*Constants.WIDTH/2;
		posY = Math.random()*Constants.HEIGHT/2;
		path.add(new Vector2d(posX, posY));
		
		posX = Math.random()*(Constants.WIDTH/2) + Constants.WIDTH/2;
		posY = Math.random()*Constants.HEIGHT/2;
		path.add(new Vector2d(posX, posY));

		posX = Math.random()*Constants.WIDTH/2;
		posY = Math.random()*(Constants.HEIGHT/2)+ Constants.HEIGHT/2;
		path.add(new Vector2d(posX, posY));
		
		posX = Math.random()*(Constants.WIDTH/2)+ Constants.WIDTH/2;
		posY = Math.random()*(Constants.HEIGHT/2) +Constants.HEIGHT/2;
		path.add(new Vector2d(posX, posY));
		
		movingObjects.add(new Ufo(
				new Vector2d(x,y),
				new Vector2d(),
				Constants.UFO_MAX_VEL,
				Assets.ufo,
				path,
				this
				));
		
	}
	
	public void update()
	{
		for (int i = 0; i < movingObjects.size(); i++) {
			movingObjects.get(i).update();
		}
		for(int i = 0;i < explosion.size(); i++) {
			Animation anim = explosion.get(i);
			anim.update();
			if(!anim.isRunning()) {
				explosion.remove(i);
			}
		}
		for (int i = 0; i < movingObjects.size(); i++) {
			if(movingObjects.get(i) instanceof Meteor)
				return;
		}
		
		starWave();
		

	}
	public void draw(Graphics g) {
		
		Graphics2D g2d = (Graphics2D)g;
		
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		
		for (int i = 0; i < movingObjects.size(); i++) {
			movingObjects.get(i).draw(g);
		}
		
		
		for (int i = 0; i < explosion.size(); i++) {
			Animation anim = explosion.get(i);
			g2d.drawImage(anim.getCurrentFrame(), (int)anim.getPosition().getX(), (int)anim.getPosition().getY(),  null);
		}
		drawScore(g);
		drawLives(g);;
	}
	
	private void drawScore(Graphics g) {
		Vector2d pos = new Vector2d(850,25);
		
		String scoreToString = Integer.toString(score);
		
		for (int i = 0; i < scoreToString.length(); i++) {
			
			g.drawImage(Assets.numbers[Integer.parseInt(scoreToString.substring(i,i+1))], (int)pos.getX(),(int)pos.getY(),null);
			
			pos.setX(pos.getX()+20);
		}
	}
	
	private void drawLives( Graphics g) {
		
		Vector2d livePosition = new Vector2d(25,25);
		
		g.drawImage( Assets.life, (int)livePosition.getX(),(int)livePosition.getY(),null);
		
		g.drawImage(Assets.numbers[10],(int)livePosition.getX()+40,
				(int)livePosition.getY()+5,null);
		String livesToString = Integer.toString(lives);
		
		Vector2d pos = new Vector2d(livePosition.getX(),livePosition.getY());
		
		for(int i =0; i < livesToString.length(); i ++)
			
		{
			int number = Integer.parseInt(livesToString.substring(i, i+1));
			
			if(number <= 0)
				break;
			g.drawImage(Assets.numbers[number],
				 (int)pos.getX()+60,(int)pos.getY()+5,null	);
			pos.setX(pos.getX()+20);
		}
	}
	
	public ArrayList<MovingObject> getMovingObjects() {
		return movingObjects;
	}
	public Player getPlayer() {
		return player;
	}
	
	public void subtracLife() {
		lives --;
	}
	
	
}
