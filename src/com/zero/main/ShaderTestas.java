package com.zero.main;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class ShaderTestas implements ApplicationListener {

	ShaderProgram shader;
	Texture texture;
	Mesh mesh;

	@Override
	public void create() {
		//Uzkraunam textura is faiko, kuria kabinsim prie shaderio uniformos
		


		String vertexShader = 
				"attribute vec4 a_position;   \n" 
						+ "attribute vec2 a_texCoord;   \n"
						+ "varying vec2 v_texCoord;     \n" 
						+ "void main()                  \n" 
						+ "{                            \n"
						+ "   gl_Position = a_position; \n" 
						+ "   v_texCoord = a_texCoord;  \n" 
						+ "}                            \n";

		String fragmentShader = 
				"#ifdef GL_ES\n" 
						+ "precision mediump float;\n"
						+ "#endif\n"
						+ "varying vec2 v_texCoord;                            \n" 
						+ "uniform sampler2D tex0;                        \n"
						+ "void main()                                         \n"
						+ "{                                                   \n"
						+ "  gl_FragColor = texture2D( tex0, v_texCoord );\n"
						+ "}                                                   \n";


		shader = new ShaderProgram(vertexShader, fragmentShader);
		mesh = new Mesh(true, 3, 3, new VertexAttribute(Usage.Position, 3, "a_position"), new VertexAttribute(
				Usage.TextureCoordinates, 2, "a_texCoord"));
		float[] vertices = {-0.5f, 0.5f, 0, // Position 0
				0.0f, 0.0f, // TexCoord 0
				-0.5f, -0.5f, 0, // Position 1
				0.0f, 1.0f, // TexCoord 1
				0.5f, -0.5f, 0, // Position 2
				1.0f, 1.0f, // TexCoord 2
		};
		short[] indices = {0, 1, 2};
		mesh.setVertices(vertices);
		mesh.setIndices(indices);
		createTexture();


	}

	private void createTexture () {
		FileHandle imageFileHandle = Gdx.files.internal("res/shadertest.png"); 
		texture = new Texture(imageFileHandle, false);
	}

	public void resume () {
		//createTexture();
	}


	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render() {

		 Gdx.gl20.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
         Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

         Gdx.gl20.glActiveTexture(GL20.GL_TEXTURE0);
         texture.bind();

         shader.begin();
         shader.setUniformi("tex0", 0);

         mesh.render(shader, GL20.GL_TRIANGLES);

         shader.end();

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}


	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
