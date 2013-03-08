package beans;

import java.util.ArrayList;

//Face class holds an ArrayList of UVWs, Vertices, and Vertex Normals
public class Face {
	
	private ArrayList<Vector3D> verts;
	private ArrayList<Vector3D> uvws;
	private ArrayList<Vector3D> normals;
	
	public Face(){
		verts = new ArrayList<Vector3D>();
		uvws = new ArrayList<Vector3D>();
		normals = new ArrayList<Vector3D>();
	}
	
	public ArrayList<Vector3D> getVertices(){
		return verts;
	}
	
	public ArrayList<Vector3D> getUvws(){
		return uvws;
	}
	
	public ArrayList<Vector3D> getNormals(){
		return normals;
	}
}
