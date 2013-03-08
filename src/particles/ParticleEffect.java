package particles;

import java.util.ArrayList;

import beans.Vector3D;

public class ParticleEffect {

	private ArrayList<Vertex> vertexBuffer;
	private int m_TextureID;
	private Vector3D m_Force;//force applied to each particle
	private float[] m_LocalToWorldMatrix = new float[16];
	
	private ArrayList<Particle> m_particles;
	
//    typedef Interpolator<glm::vec4> ColorInterpolator;
// 
//    ParticleEffect( unsigned int numParticles = 0 );
//    virtual ~ParticleEffect();
// 
//    void SetCamera( Camera* pCamera );
//    void SetParticleEmitter( ParticleEmitter* pEmitter );
//    void SetColorInterplator( const ColorInterpolator& colors );
// 
//    // Test method to randomize the particles in an interesting way
//    void RandomizeParticles();
//    void EmitParticles();
// 
//    virtual void Update( float fDeltaTime );
//    virtual void Render();
// 
//    bool LoadTexture( const std::string& fileName );
// 
//    // Resize the particle buffer with numParticles
//    void Resize( unsigned int numParticles );
// 
//protected:
//    void RandomizeParticle( Particle& particle );
//    void EmitParticle( Particle& particle );
//public:
//    // Build the vertex buffer from the particle buffer
//    void BuildVertexBuffer();
//private:
//    Camera*             m_pCamera;
//    ParticleEmitter*    m_pParticleEmitter;
//    ColorInterpolator   m_ColorInterpolator;
//    
    private static class Vertex{
    	
    	public Vector3D m_Pos;
    	public float[] m_Diffuse;
    	public float[] m_Tex0;
    	
    	public Vertex(){
    		m_Pos = new Vector3D(0,0,0);
    		m_Diffuse = new float[4];
    		m_Tex0 = new float[2];
    	}
    }
}
