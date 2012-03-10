package com.zero.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class MapShader {
	
	protected ShaderProgram shaderProgram;
	protected Mesh mesh;
	protected ShaderRenderer renderer;
	
	float time;
	Texture texture;
	
	public void create() {
		renderer = new ShaderRenderer("map_vs", "map_fs");
		texture = new Texture(Gdx.files.internal("res/shaders/textures/shader_test.jpg"),true);
		
		shaderProgram = renderer.getShaderProgram();
		this.createMesh();
	}

	public void render(float delta) {
		time += delta;

		shaderProgram.begin();
		
		texture.bind();
		shaderProgram.setUniformf("tex0", 0);
		shaderProgram.setUniformf("resolution", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		shaderProgram.setUniformf("time", time);
		
		this.mesh.render(shaderProgram, GL20.GL_TRIANGLE_FAN);
		shaderProgram.end();	
		
	}
	
	protected void createMesh() {
		this.mesh = new Mesh(true, 4, 4, 
                new VertexAttribute(VertexAttributes.Usage.Position, 3, "vPosition"));          
 
        this.mesh.setVertices(new float[] { -1f, -1f, 0,
                                        -1f, 1f, 0,
                                        1f, 1f, 0,
                                        1f, -1f, 0});   
        
        this.mesh.setIndices(new short[] { 0, 1, 2, 3});       
		
	}
	
}
