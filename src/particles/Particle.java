package particles;

import beans.Vector3D;

public class Particle {
	public Particle() {
		m_Position = new Vector3D(0, 0, 0);
		m_Velocity = new Vector3D(0, 0, 0);
		m_Color = new float[4];
		m_fRotate = 0;
		m_fAge = 0;
		m_fLifeTime = 0;
	}

	public Vector3D m_Position; // Center point of particle
	public Vector3D m_Velocity; // Current particle velocity
	public float[] m_Color; // Particle color
	public float m_fRotate; // Rotate the particle the center
	public float m_fSize; // Size of the particle
	public float m_fAge;
	public float m_fLifeTime;
}
