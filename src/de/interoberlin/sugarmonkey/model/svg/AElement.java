package de.interoberlin.sugarmonkey.model.svg;

public abstract class AElement
{
    private String id;
    private int    zIndex;

    public String getId()
    {
	return id;
    }

    public void setId(String id)
    {
	this.id = id;
    }

    public int getzIndex()
    {
	return zIndex;
    }

    public void setzIndex(int zIndex)
    {
	this.zIndex = zIndex;
    }
}
