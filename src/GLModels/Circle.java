package GLModels;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import GLUtils.ModelImporter;
import android.opengl.GLES20;

public class Circle {

	private int mSquarePositionsBufferIdx;
	
	private int mPositionHandle = 0;
	
	private static final int numTriangles = 16;
	
	public Circle(int positionHandle){
		this.mPositionHandle = positionHandle;
		
		final float[] mPositionData = createCircle(1.5f,0f,0f);
		
		FloatBuffer data = ByteBuffer.allocateDirect(mPositionData.length
                * ModelImporter.FLOAT_SIZE_BYTES).order(ByteOrder.nativeOrder()).asFloatBuffer();
        data.put(mPositionData);
        data.position(0);
        
		final int buffers[] = new int[1];
		GLES20.glGenBuffers(1, buffers, 0);
		
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, data.capacity() * ModelImporter.FLOAT_SIZE_BYTES, data, GLES20.GL_STATIC_DRAW);
		
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

		mSquarePositionsBufferIdx = buffers[0];
	}
	
	public void render(){
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mSquarePositionsBufferIdx);
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		GLES20.glVertexAttribPointer(mPositionHandle, ModelImporter.POSITION_DATA_SIZE, GLES20.GL_FLOAT, false, 0, 0);
		
		// Clear the currently bound buffer (so future OpenGL calls do not use this buffer).
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

		// Draw the cubes.
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, numTriangles*3);
	}
	
	public void release(){
		final int[] buffersToDelete = new int[] { mSquarePositionsBufferIdx};
		GLES20.glDeleteBuffers(buffersToDelete.length, buffersToDelete, 0);
	}
	
	private float[] createCircle(float radius, float centerX, float centerY){
		float[] tmp = new float[numTriangles*9];
		int j = 0;
		for(int i = 0;i<numTriangles*9;i+=9){
			tmp[i] = centerX;
			tmp[i+1] = centerY;
			tmp[i+2] = 0f;
			
			tmp[i+3] = (float) (radius*Math.cos(j*Math.PI*2/numTriangles)) + centerX;
			tmp[i+4] = (float) (radius*Math.sin(j*Math.PI*2/numTriangles)) + centerY;
			tmp[i+5] = 0f;
					
			tmp[i+6] = (float) (radius*Math.cos((j+1)*Math.PI*2/numTriangles)) + centerX;
			tmp[i+7] = (float) (radius*Math.sin((j+1)*Math.PI*2/numTriangles)) + centerY;
			tmp[i+8] = 0f;
					
			j++;
		}
		
		return tmp;
	}
}
