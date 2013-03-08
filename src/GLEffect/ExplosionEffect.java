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
	private Context c;
	private TexturedRectangle [] glParticles;
	private Vector3D [] velocityVectors;
	
	private int mPositionHandle = 0;
	private int mTextureCoordHandle = 0;
	private int mTextureUniformHandle = 0;
	
	private Random randomGenerator;
	
	private static Vector3D gravity = new Vector3D(0f,-9.8f,0f);//this should update from the phones accelerometer in the future
	
	public ExplosionEffect(Context c,int mPositionHandle,int mTextureCoordinateHandle,int mTextureUniformHandle,
			int numParticles, int duration, float viewPortWidth, float viewPortHeight){
		this.numParticles = numParticles;
		this.c = c;
		
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
		int textureHandle = TextureHelper.loadTexture(c, b);
		
		for(int i = 0;i<numParticles;i++){
			int scale = randomGenerator.nextInt(5);
			
			Vector3D offset = new Vector3D(randomGenerator.nextFloat()*scale,randomGenerator.nextFloat()*scale,-randomGenerator.nextFloat()*50f);
			glParticles[i] = new TexturedRectangle(scale,scale,offset,mPositionHandle,mTextureCoordHandle,textureHandle,mTextureUniformHandle);
		}
	}
	
	private void generateStarForces(){
		velocityVectors = new Vector3D[numParticles];
		
		
	}
}
