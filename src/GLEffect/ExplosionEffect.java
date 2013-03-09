package GLEffect;

import java.util.Random;

import com.example.customobjectgl.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import beans.Vector3D;
import GLModels.TexturedRectangle;
import GLUtils.TextureHelper;

public class ExplosionEffect {
	
	private int numParticles;
	private int duration;
	private int time;
	private Context c;
	private TexturedRectangle [] glParticles;
	private Vector3D [] velocityVectors;
	
	private int mPositionHandle = 0;
	private int mTextureCoordHandle = 0;
	private int mTextureUniformHandle = 0;
	private int mTextureHandle = 0;
	
	private Random randomGenerator;
	
	private static Vector3D gravity = new Vector3D(0f,-9.8f,0f);//this should update from the phones accelerometer in the future
	
	private Vector3D centerPoint;
	
	public ExplosionEffect(Context c,Vector3D centerPoint, 
			int mPositionHandle,int mTextureCoordinateHandle,
			int mTextureUniformHandle,
			int numParticles, int duration, 
			float viewPortWidth, float viewPortHeight){
		this.numParticles = numParticles;
		this.duration = duration;
		this.c = c;
		this.centerPoint = centerPoint;
		
		this.mPositionHandle = mPositionHandle;
		this.mTextureCoordHandle = mTextureCoordinateHandle;
		this.mTextureUniformHandle = mTextureUniformHandle;
		
		randomGenerator = new Random();
		
		generateStarModels();
		generateStarForces();
	}
	
	private void generateStarModels(){
		glParticles = new TexturedRectangle[numParticles];
		Bitmap b = BitmapFactory.decodeResource(c.getResources(), R.drawable.star);
		mTextureHandle = TextureHelper.loadTexture(c, b);
		
		resetParticles();
	}
	
	private void generateStarForces(){
		velocityVectors = new Vector3D[numParticles];
		
		resetVelocities();
	}
	
	public void explodeStep(float[] mViewMatrix,float [] mProjectionMatrix, float [] mMVPMatrix){
		if(time > duration)
			resetExplosion();
		else
			time++;
			
		for(int i = 0;i<numParticles;i++){
			Vector3D vel = velocityVectors[i];
			glParticles[i].postModelTranslate(vel.getX(), vel.getY(), vel.getZ());
			glParticles[i].updateMVP(mViewMatrix, mProjectionMatrix, mMVPMatrix);
			glParticles[i].render();
		}
	}
	
	//should change center point somehow, need to make sure its still on screen
	private void resetExplosion(){
		time = 0;
		
		resetParticles();
		resetVelocities();
	}
	
	private void resetParticles(){
		for(int i = 0;i<numParticles;i++){
			int scale = randomGenerator.nextInt(5);
			
			Vector3D offset = new Vector3D(randomGenerator.nextFloat()*scale + centerPoint.getX(),randomGenerator.nextFloat()*scale + centerPoint.getX(),-randomGenerator.nextFloat()*50f + centerPoint.getX());
			glParticles[i] = new TexturedRectangle(scale,scale,offset,mPositionHandle,mTextureCoordHandle,mTextureHandle,mTextureUniformHandle);
		}
	}
	
	private void resetVelocities(){
		for(int i=0;i<numParticles;i++){
			float distanceXfromCenter = glParticles[i].getRectangleOrigin().getX() - centerPoint.getX();
			float distanceYfromCenter = glParticles[i].getRectangleOrigin().getY() - centerPoint.getY();
			float distanceZfromCenter = glParticles[i].getRectangleOrigin().getZ() - centerPoint.getZ();
			velocityVectors[i] = new Vector3D(1f/(glParticles[i].getRectangleWidth()*distanceXfromCenter),
					1f/(glParticles[i].getRectangleWidth()*distanceYfromCenter),
					1f/(glParticles[i].getRectangleWidth()*distanceZfromCenter)
					);
		}
	}
	
	public void onDestroy(){
		for(TexturedRectangle t : glParticles){
			t.release();
		}
	}
}
