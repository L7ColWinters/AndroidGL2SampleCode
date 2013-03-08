package GLModels;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import beans.Vector3D;

import GLUtils.ModelImporter;
import android.opengl.GLES20;
import android.opengl.Matrix;

public abstract class GLModelInterface {
	
	private float[] mTemporaryMatrix = new float[16];
	private float[] mModelMatrix = new float[16];
	
	protected int numTriangles;
	
	protected int mPositionHandle = 0;
	protected int mTextureCoordHandle = 0;
	protected int mTextureDataHandle = 0;
	protected int mTextureUniformHandle = 0;
	
	private static int mMVPMatrixHandle;
	
	public GLModelInterface(int numTriangles){
		this.numTriangles = numTriangles;
		Matrix.setIdentityM(mModelMatrix, 0);
	}
	
	public abstract void render();
	public abstract void release();
	public abstract FloatBuffer createObject(Vector3D offset,float [] args);
	public abstract FloatBuffer createTexCoords();
	
	public void updateMVP(float[] mViewMatrix, float[] mProjectionMatrix, float[] mMVPMatrix){
		Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        Matrix.multiplyMM(mTemporaryMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
		System.arraycopy(mTemporaryMatrix, 0, mMVPMatrix, 0, 16);

		// Pass in the combined matrix.
		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
	}
	
	protected FloatBuffer getBufferFromArray(float[] arr){
		FloatBuffer data = ByteBuffer.allocateDirect(arr.length
                * ModelImporter.FLOAT_SIZE_BYTES).order(ByteOrder.nativeOrder()).asFloatBuffer();
        data.put(arr);
        data.position(0);
        return data;
	}
	
	public static void setMVPMatrixHandle(int handle){
		mMVPMatrixHandle = handle;
	}
	
	public void postModelScale(float dx, float dy, float dz){
		Matrix.scaleM(mModelMatrix, 0, dx, dy, dz);
	}
	
	public void postModelTranslate(float dx, float dy, float dz){
		Matrix.translateM(mModelMatrix, 0, dx, dy, dz);
	}
	
	public void postModelRotate(float angle, float pivotX, float pivotY, float pivotZ){
		Matrix.rotateM(mModelMatrix, 0, angle, pivotX, pivotY, pivotZ);
	}
}
