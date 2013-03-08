package GLModels;

import java.nio.FloatBuffer;

import beans.Vector3D;

import GLUtils.ModelImporter;
import android.opengl.GLES20;

public class TexturedRectangle extends GLModelInterface {

	private int mSquarePositionsBufferIdx;
	private int mSquareTexCoordsBufferIdx;
	
	public TexturedRectangle(int bitmapWidth, int bitmapHeight,Vector3D offset,int positionHandle,int texCoord,int dataHandle, int uniformHandle){
		super(2);
		mPositionHandle = positionHandle;
		mTextureCoordHandle = texCoord;
		mTextureDataHandle = dataHandle;
		mTextureUniformHandle = uniformHandle;
		
		FloatBuffer data = createObject(offset,new float[]{bitmapWidth,bitmapHeight});
		FloatBuffer texCoords = createTexCoords();
		
		final int buffers[] = new int[2];
		GLES20.glGenBuffers(2, buffers, 0);
		
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, data.capacity() * ModelImporter.FLOAT_SIZE_BYTES, data, GLES20.GL_STATIC_DRAW);
		
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[1]);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, texCoords.capacity() * ModelImporter.FLOAT_SIZE_BYTES, texCoords, GLES20.GL_STATIC_DRAW);
		
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

		mSquarePositionsBufferIdx = buffers[0];
		mSquareTexCoordsBufferIdx = buffers[1];
	}
	
	@Override
	public void render() {
		 // Bind the texture to this unit.
	    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandle);
	 
	    // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
	    GLES20.glUniform1i(mTextureUniformHandle, 0);
	    
		// Pass in the position information
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mSquarePositionsBufferIdx);
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		GLES20.glVertexAttribPointer(mPositionHandle, ModelImporter.POSITION_DATA_SIZE, GLES20.GL_FLOAT, false, 0, 0);
		
		// Pass in the texture information
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mSquareTexCoordsBufferIdx);
		GLES20.glEnableVertexAttribArray(mTextureCoordHandle);
		GLES20.glVertexAttribPointer(mTextureCoordHandle, ModelImporter.TEXTURE_COORDINATE_DATA_SIZE, GLES20.GL_FLOAT, false,
				0, 0);

		// Clear the currently bound buffer (so future OpenGL calls do not use this buffer).
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

		// Draw the cubes.
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, numTriangles*3);
	}

	@Override
	public void release() {
		final int[] buffersToDelete = new int[] { mSquarePositionsBufferIdx,mSquareTexCoordsBufferIdx};
		GLES20.glDeleteBuffers(buffersToDelete.length, buffersToDelete, 0);
	}

	@Override
	public FloatBuffer createObject(Vector3D offset,float[] args) {
		float [] mPositionData = new float[]{
				
			args[0] + offset.getX(), offset.getY(), offset.getZ(),
			offset.getX(),offset.getY(),offset.getZ(),
			offset.getX(),offset.getY() + args[1],offset.getZ(),
			
			args[0] + offset.getX(), offset.getY(), offset.getZ(),
			offset.getX(),offset.getY() + args[1],offset.getZ(),
			args[0] + offset.getX(),args[1] + offset.getY(),offset.getZ()
			
		};
		return getBufferFromArray(mPositionData);
	}

	@Override
	public FloatBuffer createTexCoords() {
		float [] mTextureCoords = new float[]{
				
			1f,1f,
			0f,1f,
			0f,0f,
			
			1f,1f,
			0f,0f,
			1f,0f
			
		};
		return getBufferFromArray(mTextureCoords);
	}

}
