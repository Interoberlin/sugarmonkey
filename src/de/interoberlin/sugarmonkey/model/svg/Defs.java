package de.interoberlin.sugarmonkey.model.svg;

public class Defs
{
    private static String name = "defs";
    private String id;
    
    public static String getName()
    {
	return name;
    }

    public String getId()
    {
	return id;
    }

    public void setId(String id)
    {
	this.id = id;
    }
}
