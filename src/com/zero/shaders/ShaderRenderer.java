package com.zero.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

public abstract class ShaderRenderer {

	protected String vertexShaderFile, fragmentShaderFile;
	protected String vertexShader, fragmentShader;
	protected FileHandle vertexF, fragmentF;
	
	public ShaderRenderer() {
		if (!Gdx.app.getGraphics().isGL20Available()) {
			throw new GdxRuntimeException("GLES2 Not Available!");
		}
	}

	public void setVertexShaderFromFile(String file) {
		this.vertexShaderFile = file;
		this.vertexF = Gdx.files.internal("res/shaders/"+ this.vertexShaderFile);
		String tmp = this.vertexF.readString();
		this.setVertexShader(tmp);
	}
	
	public void setFragmentShaderFromFile(String file) {
		this.fragmentShaderFile = file;
		this.fragmentF = Gdx.files.internal("res/shaders/"+ this.fragmentShaderFile); 
		String tmp = this.fragmentF.readString();
		this.setFragmentShader(tmp);
	}
	
	public void setVertexShader(String vertexShader) {
		this.vertexShader = vertexShader;
	}
	
	public void setFragmentShader(String fragmentShader) {
		this.fragmentShader = fragmentShader;
	}
	
	public FileHandle getVertexFile() {
		return this.vertexF;
	}
	
	public FileHandle getFragmentFile() {
		return this.fragmentF;
	}
	
	public abstract void create();
	public abstract void render(float delta);
	/*{
		//Uzkraunam textura is faiko, kuria kabinsim prie shaderio uniformos
		texture = new Texture(Gdx.files.internal("res/shader_test.jpg"),true);
    	
		//check we can use GLES2
		if (!Gdx.app.getGraphics().isGL20Available()) {
			throw new GdxRuntimeException("GLES2 Not Available!");
		}
				
		//Sukuriam vertex shaderi kuriame pasakome kad musu parametras vPosition yra gl_position (instukcija?)
		String vertexShader = 
			"attribute vec4 vPosition; 		\n" + 
			"void main()					\n" +
			"{								\n" +
			"	gl_Position = vPosition;	\n" +
			"}	" +
			"							\n";
		
		//Sukuriam fragment(pixel?) shaderi kuriame yra 3 uniformos (parametrai perduodami is isores). Vectorius is 2 coordinaciu
		//resolution, float time, sample2D (aka textura) tex0
		//nu ir visa logika kurios as nesuprantu, ji apskaicioja pixelio spalva reikemoj pozicijoj :D
		String fragmentShader = 
				"#ifdef GL_ES\n"+
						"precision highp float;\n"+
						"#endif\n"+
						"uniform vec2 resolution;\n"+
						"uniform float time;\n"+
						"uniform sampler2D tex0;\n"+
						"vec3 deform( in vec2 p )\n"+
						"{\n"+
						"    vec2 uv;\n"+
						"    vec2 q = vec2( sin(1.1*time+p.x),sin(1.2*time+p.y) );\n"+
						"    float a = atan(q.y,q.x);\n"+
						"    float r = sqrt(dot(q,q));\n"+
						"    uv.x = sin(0.0+1.0*time)+p.x*sqrt(r*r+1.0);\n"+
						"    uv.y = sin(0.6+1.1*time)+p.y*sqrt(r*r+1.0);\n"+
						"\n"+
						"    return texture2D(tex0,uv*.5).xyz;\n"+
						"}\n"+
						"\n"+
						"void main(void)\n"+
						"{\n"+
						"    vec2 p = -1.0 + 2.0 * gl_FragCoord.xy / resolution.xy;\n"+
						"    vec2 s = p;\n"+
						"\n"+
						"    vec3 total = vec3(0.0);\n"+
						"    vec2 d = (vec2(0.0,0.0)-p)/40.0;\n"+
						"    float w = 1.0;\n"+
						"    for( int i=0; i<40; i++ )\n"+
						"    {\n"+
						"        vec3 res = deform(s);\n"+
						"        res = smoothstep(0.1,1.0,res*res);\n"+
						"        total += w*res;\n"+
						"        w *= .99;\n"+
						"        s += d;\n"+
						"    }\n"+
						"    total /= 40.0;\n"+
						"    float r = 1.5/(1.0+dot(p,p));\n"+
						"    gl_FragColor = vec4( total*r,1.0);\n"+
						"}\n";
		
		
		//Sukuriam shaderProgram ir nurodom savo shaderius, bei patikrinam ar jie susicompilino be klaidu
		shaderProgram = new ShaderProgram(vertexShader, fragmentShader);
		 if (shaderProgram.isCompiled() == false) {
	         Gdx.app.log("ShaderError", shaderProgram.getLog());
	         System.exit(0);
	      }
		 
		 
		//Mesh, kaip ir poligonas kruva tasku sujunktu tarp pusavi. Keli idomus faktai:
	    //pirmas parametras nusako ar meshas statinis tai yra ar nesikeis/nedauk keisis tarp frame renderinimu
	    // 4,4 nurodo kiek mesas tures verticiu (tasku/liniju?) ir indiciu (tasku/liniju?)
		//Toliau kuriamas sakykim shaderio atributas (pameni ta pavadinima vertex shaderi vPosition?)
		 //jame nusakoma, kad jis tures po 3 kintamuosius
		 
		mesh = new Mesh(true, 4, 4, 
                new VertexAttribute(VertexAttributes.Usage.Position, 3, "vPosition"));          
 
		//vat cia ir kuriamos pacios vertices, naudojant po 3 kintamuosius, kaip aprasyta atribute, jei pridetume dar ir spalvos
		//atributa su 4 kintamaisiai viena vertive turetu isviso 7 (3 pozicijos 4 spalvos)
        mesh.setVertices(new float[] { -0.5f, -0.5f, 0,
                                        -0.5f, 0.5f, 0,
                                        0.5f, 0.5f, 0,
                                        0.5f, -0.5f, 0});   
        
        //Nusakoma kokia tvarka paisyt vertices is arejaus
        mesh.setIndices(new short[] { 0, 1, 2, 3});       
        
        //Vienas idomus dalykas kad mesha paisant centras yra 0.0 o ekrano puse = 1
	}
	*/
	
	/*
	{
		time += delta;

		shaderProgram.begin();
		
		//Pribidinama textura, kad butu galima panaudot kaip tex0
		texture.bind();
		//priskiriamos shaderio uniformos, textura, resolucija (vectorius is 2) ir laikas floatas (taip kaip buvo aprasyta shaderi)
		shaderProgram.setUniformf("tex0", 0);
		shaderProgram.setUniformf("resolution", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		shaderProgram.setUniformf("time", time);
		
		//Paisoma, nurodant kokias primityvas naudoti (GL_QUAD neradau, quadai butu kaip tik mesham po 4 vertices, tai teko naudot triangle_fan)
		mesh.render(shaderProgram, GL20.GL_TRIANGLE_FAN);
		shaderProgram.end();		
	}
	*/
	
}
