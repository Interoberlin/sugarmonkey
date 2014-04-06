package de.interoberlin.sugarmonkey.model.svg;

import java.util.List;

import de.interoberlin.sugarmonkey.model.svg.elements.AElement;
import de.interoberlin.sugarmonkey.model.svg.elements.EElement;
import de.interoberlin.sugarmonkey.model.svg.elements.G;

public class SVG
{
    private static String name = "svg";

    private String	xmlns_dc;
    private String	xmlns_cc;
    private String	xmlns_rdf;
    private String	xmlns_svg;
    private String	xmlns;
    private String	version;
    private int	   width;
    private int	   height;
    private String	id;
    private Defs	  defs;
    private Metadata      metadata;
    private G	     g;

    public static String getName()
    {
	return name;
    }

    public String getXmlns_dc()
    {
	return xmlns_dc;
    }

    public void setXmlns_dc(String xmlns_dc)
    {
	this.xmlns_dc = xmlns_dc;
    }

    public String getXmlns_cc()
    {
	return xmlns_cc;
    }

    public void setXmlns_cc(String xmlns_cc)
    {
	this.xmlns_cc = xmlns_cc;
    }

    public String getXmlns_rdf()
    {
	return xmlns_rdf;
    }

    public void setXmlns_rdf(String xmlns_rdf)
    {
	this.xmlns_rdf = xmlns_rdf;
    }

    public String getXmlns_svg()
    {
	return xmlns_svg;
    }

    public void setXmlns_svg(String xmlns_svg)
    {
	this.xmlns_svg = xmlns_svg;
    }

    public String getXmlns()
    {
	return xmlns;
    }

    public void setXmlns(String xmlns)
    {
	this.xmlns = xmlns;
    }

    public String getVersion()
    {
	return version;
    }

    public void setVersion(String version)
    {
	this.version = version;
    }

    public int getWidth()
    {
	return width;
    }

    public void setWidth(int width)
    {
	this.width = width;
    }

    public int getHeight()
    {
	return height;
    }

    public void setHeight(int height)
    {
	this.height = height;
    }

    public String getId()
    {
	return id;
    }

    public void setId(String id)
    {
	this.id = id;
    }

    public Defs getDefs()
    {
	return defs;
    }

    public void setDefs(Defs defs)
    {
	this.defs = defs;
    }

    public Metadata getMetadata()
    {
	return metadata;
    }

    public void setMetadata(Metadata metadata)
    {
	this.metadata = metadata;
    }

    public G getG()
    {
	return g;
    }

    public void setG(G g)
    {
	this.g = g;
    }

    private List<AElement> getAllElements()
    {
	return g.getSubelements();
    }

    public AElement getElementById(EElement type, String id)
    {
	for (AElement e : getAllElements())
	{
	    if (e.getId().equals(id))
	    {
		return e;
	    }
	}

	return null;
    }

}
