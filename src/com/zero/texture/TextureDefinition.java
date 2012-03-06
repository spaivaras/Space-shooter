package com.zero.texture;

/**
 * Base texture definition.
 * 
 */
public class TextureDefinition
{
	private String path;
	private String name;
	private int rows;
	private int cols;
	private boolean animated;
	
	/**
	 * Construct definition.
	 * 
	 * @param name
	 *            The texture name.
	 * @param path
	 *            The file path to texture.
	 */
	public TextureDefinition(String name, String path)
	{
		this.name = name;
		this.path = path;
		this.rows = 1;
		this.cols = 1;
		this.animated = false;
	}
	
	/**
	 * Construct definition for animation sheet texture.
	 * 
	 * @param name
	 *            The texture name.
	 * @param path
	 *            The file path to texture.
	 * @param rows
	 *            The number of rows in texture sheet.
	 * @param cols
	 *            The number of columns in texture sheet.
	 */
	public TextureDefinition(String name, String path, int rows, int cols)
	{
		this.name = name;
		this.path = path;
		this.rows = rows;
		this.cols = cols;
		this.animated = true;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public int getRows()
	{
		return rows;
	}
	
	public void setRows(int rows)
	{
		this.rows = rows;
	}
	
	public int getCols()
	{
		return cols;
	}
	
	public void setCols(int cols)
	{
		this.cols = cols;
	}
	
	public boolean isAnimated()
	{
		return animated;
	}
	
	public void setAnimated(boolean animated)
	{
		this.animated = animated;
	}
	
	public String getPath()
	{
		return path;
	}
	
	public void setPath(String path)
	{
		this.path = path;
	}
	
}
