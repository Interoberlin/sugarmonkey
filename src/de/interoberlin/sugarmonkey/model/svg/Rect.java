package de.interoberlin.sugarmonkey.model.svg;

public class Rect extends AElement
{
    private static String name = "rect";

    private float	 width;
    private float	 height;
    private float	 x;
    private float	 y;
    private String style;

    public static String getName()
    {
	return name;
    }

    public float getWidth()
    {
	return width;
    }

    public void setWidth(float width)
    {
	this.width = width;
    }

    public float getHeight()
    {
	return height;
    }

    public void setHeight(float height)
    {
	this.height = height;
    }

    public float getX()
    {
	return x;
    }

    public void setX(float x)
    {
	this.x = x;
    }

    public float getY()
    {
	return y;
    }

    public void setY(float y)
    {
	this.y = y;
    }

    public String getStyle()
    {
	return style;
    }

    public void setStyle(String style)
    {
	this.style = style;
    }
}
