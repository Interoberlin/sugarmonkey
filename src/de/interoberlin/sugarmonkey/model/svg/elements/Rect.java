package de.interoberlin.sugarmonkey.model.svg.elements;

public class Rect extends AElement
{
    private static String name = "rect";
    private EElement      type = EElement.RECT;

    private float	 width;
    private float	 height;
    private float	 x;
    private float	 y;
    private String	style;

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
	if (width > 0)
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
