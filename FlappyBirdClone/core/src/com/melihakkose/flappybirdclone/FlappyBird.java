package com.melihakkose.flappybirdclone;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture bird;

	//BİRD' UN X VE Y EKSENLERİ DEGİSİKLİGİNİ KOLAYLASTİRMAK İCİN TANİMLANAN FLOATLAR
	float birdX=0;
	float birdY=0;

	//OYUN DURUMU
	int gameState=0;

	//Gravity degiskenleri
	float velocity=0;
	float gravity=0.8f;

	//DUSMAN SİNİFİ DEGİSKENLERİ
	Texture bee1;
	Texture bee2;
	Texture bee3;


	//DONGULER
	int numberOfEnemies=4;
	float [] enemyX = new float[numberOfEnemies];
	float enemyVelocity=6;
	float [] enemyOffset1=new float[numberOfEnemies];
	float [] enemyOffset2=new float[numberOfEnemies];
	float [] enemyOffset3=new float[numberOfEnemies];
	Random random;
	//DONGULER ARASİ UZAKLİK
	float distance=0;

	//Circle Collision
	Circle birdCircle;
	Circle[] enemyCircle1;
	Circle[] enemyCircle2;
	Circle[]enemyCircle3;

	ShapeRenderer shapeRenderer;

	//SKOR
	int score=0;
	int scoredEnemy=0;

	//SCORE FONT -GAMESTATE FONT
	BitmapFont font;
	BitmapFont font2;


	@Override
	public void create () {
		//UYGULAMA ACİLİNCA NE OLACAKSA BURAYA YAZİLİR

		batch =new SpriteBatch();
		background=new Texture("background.png");
		bird=new Texture("bird.png");
		bee1=new Texture("bee.png");
		bee2=new Texture("bee.png");
		bee3=new Texture("bee.png");

		distance=Gdx.graphics.getWidth()/2;
		random =new Random();

		birdX=Gdx.graphics.getWidth()/3-bird.getHeight()/2;
		birdY=Gdx.graphics.getHeight()/2;

		shapeRenderer=new ShapeRenderer();

		//Collision
		birdCircle=new Circle();
		enemyCircle1=new Circle[numberOfEnemies];
		enemyCircle2=new Circle[numberOfEnemies];
		enemyCircle3=new Circle[numberOfEnemies];

		font=new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(4);

		font2=new BitmapFont();
		font2.setColor(Color.WHITE);
		font2.getData().setScale(8);


		for(int i=0;i<numberOfEnemies;i++){
			enemyOffset1[i]=(random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);
			enemyOffset2[i]=(random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);
			enemyOffset3[i]=(random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);

			enemyX[i]=Gdx.graphics.getWidth()-bee1.getWidth()/2+i*distance;

			enemyCircle1[i]=new Circle();
			enemyCircle2[i]=new Circle();
			enemyCircle3[i]=new Circle();

		}

	}

	@Override
	public void render () {
		//BACKGROUND CİZME
		batch.begin();
		//BACKGROUND CİZME
		batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());


		//OYUN DEVAM ETTİKCE CAGRİLAN METOT

		if(gameState==1){

			if(enemyX[scoredEnemy]<Gdx.graphics.getWidth()/3-bird.getHeight()/2){
				score++;
				if(scoredEnemy<numberOfEnemies-1){
					scoredEnemy++;
				}else{
					scoredEnemy=0;
				}
			}

			if(Gdx.input.justTouched()){
				velocity=-15;
			}

			for(int i=0;i<numberOfEnemies;i++){

				if(enemyX[i]< 0){
					enemyX[i]=enemyX[i]+numberOfEnemies*distance;

					enemyOffset1[i]=(random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);
					enemyOffset2[i]=(random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);
					enemyOffset3[i]=(random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);

				}else{
					enemyX[i]=enemyX[i]-enemyVelocity;
				}


				batch.draw(bee1,enemyX[i],Gdx.graphics.getHeight()/2+enemyOffset1[i],Gdx.graphics.getWidth()/15,Gdx.graphics.getHeight()/9);
				batch.draw(bee2,enemyX[i],Gdx.graphics.getHeight()/2+enemyOffset2[i],Gdx.graphics.getWidth()/15,Gdx.graphics.getHeight()/9);
				batch.draw(bee3,enemyX[i],Gdx.graphics.getHeight()/2+enemyOffset3[i],Gdx.graphics.getWidth()/15,Gdx.graphics.getHeight()/9);

				enemyCircle1[i]=new Circle(enemyX[i]+Gdx.graphics.getWidth()/30,Gdx.graphics.getHeight()/2+enemyOffset1[i]+Gdx.graphics.getHeight()/18,Gdx.graphics.getWidth()/30);
				enemyCircle2[i]=new Circle(enemyX[i]+Gdx.graphics.getWidth()/30,Gdx.graphics.getHeight()/2+enemyOffset2[i]+Gdx.graphics.getHeight()/18,Gdx.graphics.getWidth()/30);
				enemyCircle3[i]=new Circle(enemyX[i]+Gdx.graphics.getWidth()/30,Gdx.graphics.getHeight()/2+enemyOffset3[i]+Gdx.graphics.getHeight()/18,Gdx.graphics.getWidth()/30);

			}
			if(birdY>0 ){
				velocity=velocity+gravity;
				birdY=birdY-velocity;
			}else{
				gameState=2;
			}


		}else if(gameState==0){
			if(Gdx.input.justTouched()){
				gameState=1;
			}
		}else if(gameState==2){
			font2.draw(batch,"Game Over! Tap To Play Again!",100,Gdx.graphics.getHeight()/2);

			if(Gdx.input.justTouched()){
				gameState=1;

				birdY=Gdx.graphics.getHeight()/2;

				for(int i=0;i<numberOfEnemies;i++){
					enemyOffset1[i]=(random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);
					enemyOffset2[i]=(random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);
					enemyOffset3[i]=(random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);

					enemyX[i]=Gdx.graphics.getWidth()-bee1.getWidth()/2+i*distance;

					enemyCircle1[i]=new Circle();
					enemyCircle2[i]=new Circle();
					enemyCircle3[i]=new Circle();

				}
				velocity=0;
				scoredEnemy=0;
				score=0;

			}
		}



		font.draw(batch,String.valueOf(score),100,200);
		//BİRD CİZME
		batch.draw(bird,birdX,birdY,Gdx.graphics.getWidth()/15,Gdx.graphics.getHeight()/9);
		batch.end();

		//SHAPERENDERER KULLANİMİ
		birdCircle.set(birdX+Gdx.graphics.getWidth()/30,birdY+Gdx.graphics.getHeight()/18,Gdx.graphics.getWidth()/30);
		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.RED);
		//shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);


		for(int i=0;i<numberOfEnemies;i++){
			//shapeRenderer.circle(enemyX[i]+Gdx.graphics.getWidth()/30,Gdx.graphics.getHeight()/2+enemyOffset1[i]+Gdx.graphics.getHeight()/18,Gdx.graphics.getWidth()/30);
			//shapeRenderer.circle(enemyX[i]+Gdx.graphics.getWidth()/30,Gdx.graphics.getHeight()/2+enemyOffset2[i]+Gdx.graphics.getHeight()/18,Gdx.graphics.getWidth()/30);
			//shapeRenderer.circle(enemyX[i]+Gdx.graphics.getWidth()/30,Gdx.graphics.getHeight()/2+enemyOffset3[i]+Gdx.graphics.getHeight()/18,Gdx.graphics.getWidth()/30);

			if(Intersector.overlaps(birdCircle,enemyCircle1[i]) ||Intersector.overlaps(birdCircle,enemyCircle2[i]) ||Intersector.overlaps(birdCircle,enemyCircle3[i])){
				System.out.println("Collision Detection");
				gameState=2;
			}

		}
		shapeRenderer.end();
	}
	
	@Override
	public void dispose () {

	}
}
