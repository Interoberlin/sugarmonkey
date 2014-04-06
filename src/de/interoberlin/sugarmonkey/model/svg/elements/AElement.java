package de.interoberlin.sugarmonkey.model.svg.elements;

public abstract class AElement
{
    private EElement type;
    private String   id;
    private int      zIndex;

    public EElement getType()
    {
	return type;
    }

    public void setType(EElement type)
    {
	this.type = type;
    }

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
