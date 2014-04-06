package de.interoberlin.sugarmonkey.model.svg.elements;

import java.util.ArrayList;
import java.util.List;


public class G extends AElement
{
    private static String  name	= "g";

    private String	 transform;

    private List<AElement> subelements = new ArrayList<AElement>();

    public static String getName()
    {
	return name;
    }

    public String getTransform()
    {
	return transform;
    }

    public void setTransform(String transform)
    {
	this.transform = transform;
    }

    public List<AElement> getSubelements()
    {
	return subelements;
    }

    public void setSubelements(List<AElement> subelements)
    {
	this.subelements = subelements;
    }
}
