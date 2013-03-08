package com.example.customobjectgl;

import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import beans.Vector3D;

import GLModels.Balloon;
import GLModels.Circle;
import GLModels.GLModelInterface;
import GLModels.TexturedRectangle;
import GLUtils.ShaderHelper;
import GLUtils.TextureHelper;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.GestureDetector.OnGestureListener;

public class Scene extends GLSurfaceView{

	private GestureDetector gDetector;
	private SceneRenderer renderer;
	private Context context;
	private int translationFactor;
	
	public Scene(Context context) {
		super(context);
		this.context = context;
		
		setEGLContextClientVersion(2);
		setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		getHolder().setFormat(PixelFormat.TRANSLUCENT);
		setZOrderOnTop(true);
		
		if(renderer == null)
			renderer = new SceneRenderer(context);
		
		final DisplayMetrics displayMetrics = new DisplayMetrics();
		((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
		translationFactor = displayMetrics.densityDpi;
		
		setRenderer(renderer);
		setRenderMode(RENDERMODE_WHEN_DIRTY);
		//setRenderMode(RENDERMODE_CONTINUOUSLY);
		
		gDetector = new GestureDetector(context,new OnGestureListener(){
			@Override
			public boolean onDown(MotionEvent arg0) {
				return false;
			}

			@Override
			public boolean onFling(MotionEvent arg0, MotionEvent arg1,
					float arg2, float arg3) {
				return false;
			}

			@Override
			public void onLongPress(MotionEvent arg0) {
			}

			@Override
			public boolean onScroll(MotionEvent arg0, MotionEvent arg1,
					float disX, float disY) {
				final float x = -disX;
				final float y = disY;
//				queueEvent(new Runnable() {
//					public void run() {
//						renderer.postModelTranslate(x/translationFactor, y/translationFactor);
//						//renderer.postModelTranslate(x, y);
//					}
//				});
//				requestRender();
				return true;
			}

			@Override
			public void onShowPress(MotionEvent arg0) {
			}

			@Override
			public boolean onSingleTapUp(MotionEvent arg0) {
				return false;
			}
		});
	}
	
	public void onDestroy(){
		renderer.onDestroy();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		//return gDetector.onTouchEvent(event);
		return false;
	}

	private class SceneRenderer implements Renderer{

		private int mProgramHandle = 0;
		private int mVertexHandle = 0;
		private int mFragHandle = 0;
		private int mTextureUniformHandle = 0;
		
		private float[] mViewMatrix = new float[16];
		private float[] mProjectionMatrix = new float[16];
		private float[] mMVPMatrix = new float[16];
		
		private float[] mEyeMatrix = new float[3];
		private float[] mLookMatrix = new float[3];
		private float[] mUpMatrix = new float[3];
		
		private int mMVPMatrixHandle = 0;
		private int mPositionHandle = 0;
		private int mTextureCoordinateHandle = 0;
		
		//private Balloon balloonModel;
		//private Circle circleModel;
		
		private TexturedRectangle[] stars;
		
		//private Bitmap balloonImage;
		
		public SceneRenderer(Context c){
			//Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
			//balloonImage = Bitmap.createScaledBitmap(b, TextureHelper.findNextPowerOfTwo(b.getWidth()), TextureHelper.findNextPowerOfTwo(b.getHeight()), true);
		}
		
		@Override
		public void onDrawFrame(GL10 gl) {
			GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);			                                    
		    
			// Set the active texture unit to texture unit 0.
		    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		    
//		    if (balloonModel != null) {
//		    	balloonModel.render();
//			}
//		    
//		    if(circleModel != null)
//		    	circleModel.render();
		    
		    for(int i=0;i<stars.length;i++){
		    	//stars[i].postModelTranslate(0, 0, -0.5f);
		    	stars[i].updateMVP(mViewMatrix, mProjectionMatrix, mMVPMatrix);
		    	stars[i].render();
		    }
		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			// Set the OpenGL viewport to the same size as the surface.
			GLES20.glViewport(0, 0, width, height);

			// Create a new perspective projection matrix. The height will stay the same
			// while the width will vary as per aspect ratio.
			final float ratio = (float) width / height;
			final float left = -ratio;
			final float right = ratio;
			final float bottom = -1.0f;
			final float top = 1.0f;
			final float near = 1.0f;
			final float far = 1000.0f;
			
			Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
		}

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			// Set the background clear color to black.
			GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
			
			// Use culling to remove back faces.
			GLES20.glEnable(GLES20.GL_CULL_FACE);
			
			// Enable depth testing
			//GLES20.glEnable(GLES20.GL_DEPTH_TEST);	
			
			// Enable blending ( trying to get rid of the transparent part of the texture on model )
			GLES20.glEnable(GLES20.GL_BLEND);
			GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
			
			// Position the eye in front of the origin.
			mEyeMatrix[0] = 2.5f;
			mEyeMatrix[1] = 0.0f;
			mEyeMatrix[2] = -0.5f;// <= -0.5f seems to work

			// We are looking toward the distance
			mLookMatrix[0] = 2.5f;
			mLookMatrix[1] = 0.0f;
			mLookMatrix[2] = -5.0f;

			// Set our up vector. This is where our head would be pointing were we holding the camera.
			mUpMatrix[0] = 0.0f;
			mUpMatrix[1] = 1.0f;
			mUpMatrix[2] = 0.0f;

			// Set the view matrix. This matrix can be said to represent the camera position.
			// NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
			// view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
			Matrix.setLookAtM(mViewMatrix, 0, mEyeMatrix[0], mEyeMatrix[1], mEyeMatrix[2], mLookMatrix[0], mLookMatrix[1], mLookMatrix[2], mUpMatrix[0], mUpMatrix[1], mUpMatrix[2]);
			
			String shaderString = ShaderHelper.readTextFileFromRawResource(context, R.raw.vertexshader);
			mVertexHandle = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, shaderString);
			shaderString = ShaderHelper.readTextFileFromRawResource(context, R.raw.fragmentshader);
			mFragHandle = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, shaderString);
			mProgramHandle = ShaderHelper.createAndLinkProgram(mVertexHandle, mFragHandle,  new String[] {"a_TexCoordinate"});
			mTextureUniformHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_Texture");
		    mTextureCoordinateHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_TexCoordinate");
		    mPositionHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_Position");
		    mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_MVPMatrix");
		    
		    GLES20.glUseProgram(mProgramHandle);
		    
		    //balloonModel = new Balloon(context,mPositionHandle,mTextureCoordinateHandle,TextureHelper.loadTexture(context, balloonImage),mTextureUniformHandle);
		    //circleModel = new Circle(mPositionHandle);
		    GLModelInterface.setMVPMatrixHandle(mMVPMatrixHandle);
		    generateStarModels();
		    System.gc();
		}
		
		private void generateStarModels(){
			int numStars = 20;
			stars = new TexturedRectangle[numStars]; 
			Random randomGenerator = new Random();
			
			Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.star);
			int textureHandle = TextureHelper.loadTexture(context, b);
			
			for(int i = 0;i<numStars;i++){
				int scale = randomGenerator.nextInt(5);
				
				Vector3D offset = new Vector3D(randomGenerator.nextFloat()*scale,randomGenerator.nextFloat()*scale,-randomGenerator.nextFloat()*50f);
				stars[i] = new TexturedRectangle(scale,scale,offset,mPositionHandle,mTextureCoordinateHandle,textureHandle,mTextureUniformHandle);
			}
		}
		
		public void onDestroy(){
			for(TexturedRectangle t : stars){
				t.release();
			}
		}
	}
	
}
