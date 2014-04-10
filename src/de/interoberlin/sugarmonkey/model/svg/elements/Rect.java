package de.interoberlin.sugarmonkey.model.svg.elements;

public class Rect extends AGeometric
{
    private static String name   = "rect";
    private EElement      type   = EElement.RECT;

    private float	 width  = 0;
    private float	 height = 0;
    private float	 x      = 0;
    private float	 y      = 0;

    public static String getName()
    {
	return name;
    }

    public EElement getType()
    {
	return type;
    }

    public void setType(EElement type)
    {
	this.type = type;
    }

    public float getWidth()
    {
	return width;
    }

    public void setWidth(float width)
    {
	if (width >= 0)
	{
	    this.width = width;
	}
    }

    public float getHeight()
    {
	return height;
    }

    public void setHeight(float height)
    {
	if (height >= 0)
	{
	    this.height = height;
	}
    }

    public float getX()
    {
	return x;
    }

    public void setX(float x)
    {
	if (x >= 0)
	{
	    this.x = x;
	}
    }

    public float getY()
    {
	return y;
    }

    public void setY(float y)
    {
	if (y >= 0)
	{
	    this.y = y;
	}
    }
}
