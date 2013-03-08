package GLModels;

import com.example.customobjectgl.R;

import GLUtils.ModelImporter;
import android.content.Context;
import android.opengl.GLES20;

public class Balloon {
	
	private int mSquarePositionsBufferIdx;
	private int mSquareTexCoordsBufferIdx;
	
	private int mPositionHandle = 0;
	private int mTextureCoordHandle = 0;
	private int mTextureDataHandle = 0;
	private int mTextureUniformHandle = 0;
	
	private int numTriangles;
	
	public Balloon(Context c, int position, int texCoord,int dataHandle, int uniformHandle){
		mPositionHandle = position;
		mTextureCoordHandle = texCoord;
		mTextureDataHandle = dataHandle;
		mTextureUniformHandle = uniformHandle;
	
		ModelImporter importer = new ModelImporter(R.raw.ballon_obj,c);
		numTriangles = importer.getVertices().capacity();
		final int buffers[] = new int[2];
		
		GLES20.glGenBuffers(2, buffers, 0);						

		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, importer.getVertices().capacity() * ModelImporter.FLOAT_SIZE_BYTES, importer.getVertices(), GLES20.GL_STATIC_DRAW);

		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[1]);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, importer.getTexCoords().capacity() * ModelImporter.FLOAT_SIZE_BYTES, importer.getTexCoords(),
				GLES20.GL_STATIC_DRAW);

		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

		mSquarePositionsBufferIdx = buffers[0];
		mSquareTexCoordsBufferIdx = buffers[1];
		
		//make buffers null since there now saved in opengl context
		importer = null;
	}
	
	public void render(){
	    // Bind the texture to this unit.
	   // GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandle);
	 
	    // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
	 //   GLES20.glUniform1i(mTextureUniformHandle, 0);
	    
		// Pass in the position information
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mSquarePositionsBufferIdx);
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		GLES20.glVertexAttribPointer(mPositionHandle, ModelImporter.POSITION_DATA_SIZE, GLES20.GL_FLOAT, false, 0, 0);
		
		// Pass in the texture information
	//	GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mSquareTexCoordsBufferIdx);
	//	GLES20.glEnableVertexAttribArray(mTextureCoordHandle);
	//	GLES20.glVertexAttribPointer(mTextureCoordHandle, ModelImporter.TEXTURE_COORDINATE_DATA_SIZE, GLES20.GL_FLOAT, false,
	//			0, 0);

		// Clear the currently bound buffer (so future OpenGL calls do not use this buffer).
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

		// Draw the cubes.
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, numTriangles*3);
	}
	
	public void release(){
		final int[] buffersToDelete = new int[] { mSquarePositionsBufferIdx,mSquareTexCoordsBufferIdx };
		GLES20.glDeleteBuffers(buffersToDelete.length, buffersToDelete, 0);
	}
	
}
