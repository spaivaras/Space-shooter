package com.zero.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class ShaderRenderer {

	protected String vertexShaderFile, fragmentShaderFile;
	protected String vertexShader, fragmentShader;
	protected FileHandle vertexF, fragmentF;
	
	public ShaderRenderer() {
		if (!Gdx.app.getGraphics().isGL20Available()) {
			throw new GdxRuntimeException("GLES 2.0 Not Available!");
		}
	}
	
	public ShaderRenderer(String vertexShaderName, String fragmentShaderName) {
		this();
		this.setVertexShaderFromFile(vertexShaderName);
		this.setFragmentShaderFromFile(fragmentShaderName);
	}
	
	public ShaderProgram getShaderProgram() {
		ShaderProgram temp = new ShaderProgram(this.vertexShader, this.fragmentShader);
		
		if (temp.isCompiled() == false) {
			Gdx.app.log("ShaderError", temp.getLog());
			System.exit(0);
		}
		
		return temp;
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
}
