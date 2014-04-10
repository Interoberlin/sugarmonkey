package de.interoberlin.sugarmonkey.controller.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.graphics.Paint;
import android.util.Xml;
import de.interoberlin.sugarmonkey.model.svg.CC_Work;
import de.interoberlin.sugarmonkey.model.svg.DC_Type;
import de.interoberlin.sugarmonkey.model.svg.Defs;
import de.interoberlin.sugarmonkey.model.svg.Metadata;
import de.interoberlin.sugarmonkey.model.svg.RDF_RDF;
import de.interoberlin.sugarmonkey.model.svg.SVG;
import de.interoberlin.sugarmonkey.model.svg.elements.AElement;
import de.interoberlin.sugarmonkey.model.svg.elements.Circle;
import de.interoberlin.sugarmonkey.model.svg.elements.G;
import de.interoberlin.sugarmonkey.model.svg.elements.Rect;

public class SVGParser
{
    private static SVGParser instance;

    private SVGParser()
    {

    }

    public static SVGParser getInstance()
    {
	if (instance == null)
	{
	    instance = new SVGParser();
	}

	return instance;
    }

    /**
     * Parses svg file an returns an SVG element
     * 
     * @param in
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    public SVG parse(InputStream in) throws XmlPullParserException, IOException
    {
	try
	{
	    XmlPullParser parser = Xml.newPullParser();
	    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
	    parser.setInput(in, null);
	    parser.nextTag();
	    return readSVG(parser);
	} finally
	{
	    in.close();
	}
    }

    /**
     * Returns a SVG element
     * 
     * @param parser
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    private SVG readSVG(XmlPullParser parser) throws XmlPullParserException, IOException
    {
	String name = "";
	parser.require(XmlPullParser.START_TAG, null, SVG.getName());

	// Initialize attributes and subelements
	String xmlns_dc = "";
	String xmlns_cc = "";
	String xmlns_rdf = "";
	String xmlns_svg = "";
	String xmlns = "";
	String version = "";
	String width = "0";
	String height = "0";
	String id = "";
	Defs defs = null;
	Metadata metadata = null;
	List<AElement> subelements = new ArrayList<AElement>();

	// Read attributes
	xmlns_dc = parser.getAttributeValue(null, "dc");
	xmlns_cc = parser.getAttributeValue(null, "cc");
	xmlns_rdf = parser.getAttributeValue(null, "rdf");
	xmlns_svg = parser.getAttributeValue(null, "svg");
	xmlns = parser.getAttributeValue(null, "xmlns");
	version = parser.getAttributeValue(null, "version");
	width = parser.getAttributeValue(null, "width");
	height = parser.getAttributeValue(null, "height");
	id = parser.getAttributeValue(null, "id");

	// Read subelements
	while (parser.next() != XmlPullParser.END_TAG)
	{
	    if (parser.getEventType() != XmlPullParser.START_TAG)
	    {
		continue;
	    }

	    name = parser.getName();

	    // Starts by looking for the entry tag
	    if (name.equals("defs"))
	    {
		defs = (readDefs(parser));
	    } else if (name.equals("metadata"))
	    {
		metadata = (readMetadata(parser));
	    } else if (name.equals("g"))
	    {
		subelements.add(readG(parser));
	    } else if (name.equals("rect"))
	    {
		subelements.add(readRect(parser));
	    } else if (name.equals("circle"))
	    {
		subelements.add(readCircle(parser));
	    } else
	    {
		skip(parser);
	    }
	}

	SVG svg = new SVG();
	svg.setXmlns_dc(xmlns_dc);
	svg.setXmlns_cc(xmlns_cc);
	svg.setXmlns_rdf(xmlns_rdf);
	svg.setXmlns_svg(xmlns_svg);
	svg.setXmlns(xmlns);
	svg.setVersion(version);
	svg.setWidth(Integer.parseInt(width));
	svg.setHeight(Integer.parseInt(height));
	svg.setId(id);
	svg.setDefs(defs);
	svg.setMetadata(metadata);
	svg.setSubelements(subelements);

	return svg;
    }

    /**
     * Returns a Defs element
     * 
     * @param parser
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    private Defs readDefs(XmlPullParser parser) throws XmlPullParserException, IOException
    {
	String name = null;
	parser.require(XmlPullParser.START_TAG, null, Defs.getName());

	// Initialize attributes and subelements
	String id = "";

	// Read attributes
	id = parser.getAttributeValue(null, "id");

	// Read subelements
	while (parser.next() != XmlPullParser.END_TAG)
	{
	    if (parser.getEventType() != XmlPullParser.START_TAG)
	    {
		continue;
	    }

	    name = parser.getName();
	}

	Defs defs = new Defs();
	defs.setId(id);

	return defs;
    }

    /**
     * Returns a Metadata element
     * 
     * @param parser
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    private Metadata readMetadata(XmlPullParser parser) throws XmlPullParserException, IOException
    {
	String name = null;
	parser.require(XmlPullParser.START_TAG, null, Metadata.getName());

	// Initialize attributes and subelements
	String id = "";
	RDF_RDF rdf_RDF = null;

	// Read attributes
	id = parser.getAttributeValue(null, "id");

	// Read subelements
	while (parser.next() != XmlPullParser.END_TAG)
	{
	    if (parser.getEventType() != XmlPullParser.START_TAG)
	    {
		continue;
	    }

	    name = parser.getName();

	    // Starts by looking for the entry tag
	    if (name.equals("rdf:RDF"))
	    {
		rdf_RDF = (readRdf_RDF(parser));
	    } else
	    {
		skip(parser);
	    }
	}

	Metadata metadata = new Metadata();
	metadata.setId(id);
	metadata.setRdf_RDF(rdf_RDF);

	return metadata;
    }

    /**
     * Returns a RDF_RDF element
     * 
     * @param parser
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    private RDF_RDF readRdf_RDF(XmlPullParser parser) throws XmlPullParserException, IOException
    {
	String name = null;
	parser.require(XmlPullParser.START_TAG, null, RDF_RDF.getName());

	// Initialize attributes and subelements
	CC_Work cc_work = null;

	// Read subelements
	while (parser.next() != XmlPullParser.END_TAG)
	{
	    if (parser.getEventType() != XmlPullParser.START_TAG)
	    {
		continue;
	    }

	    name = parser.getName();

	    // Starts by looking for the entry tag
	    if (name.equals("cc:Work"))
	    {
		cc_work = (readCC_Work(parser));
	    } else
	    {
		skip(parser);
	    }
	}

	RDF_RDF rdf_RDF = new RDF_RDF();
	rdf_RDF.setCc_work(cc_work);

	return rdf_RDF;
    }

    /**
     * Returns a CC_Work element
     * 
     * @param parser
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    private CC_Work readCC_Work(XmlPullParser parser) throws XmlPullParserException, IOException
    {
	String name = null;
	parser.require(XmlPullParser.START_TAG, null, CC_Work.getName());

	// Initialize attributes and subelements
	String rdf_about = "";
	String dc_format = "";
	DC_Type dc_type = null;
	String dc_title = "";

	// Read attributes
	rdf_about = parser.getAttributeValue("rdf", "about");

	// Read subelements
	while (parser.next() != XmlPullParser.END_TAG)
	{
	    if (parser.getEventType() != XmlPullParser.START_TAG)
	    {
		continue;
	    }

	    name = parser.getName();

	    // Starts by looking for the entry tag
	    if (name.equals("dc:format"))
	    {
		dc_format = readString(parser, "dc:format");
	    } else if (name.equals("dc:type"))
	    {
		dc_type = readDC_Type(parser);
	    } else if (name.equals("dc:title"))
	    {
		dc_title = readString(parser, "dc:title");
	    } else
	    {
		skip(parser);
	    }
	}

	CC_Work cc_work = new CC_Work();
	cc_work.setRdf_about(rdf_about);
	cc_work.setDc_Format(dc_format);
	cc_work.setDc_type(dc_type);
	cc_work.setDc_title(dc_title);

	return cc_work;
    }

    /**
     * Returns a CC_Work element
     * 
     * @param parser
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    private DC_Type readDC_Type(XmlPullParser parser) throws XmlPullParserException, IOException
    {
	String name = null;
	parser.require(XmlPullParser.START_TAG, null, DC_Type.getName());

	// Initialize attributes and subelements
	String rdf_resource = null;

	// Read attributes
	rdf_resource = parser.getAttributeValue("rdf", "resource");

	// Read subelements
	while (parser.next() != XmlPullParser.END_TAG)
	{
	    if (parser.getEventType() != XmlPullParser.START_TAG)
	    {
		continue;
	    }

	    name = parser.getName();
	}

	DC_Type dc_type = new DC_Type();
	dc_type.setRdf_resource(rdf_resource);

	return dc_type;
    }

    /**
     * Returns a G element
     * 
     * @param parser
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    private G readG(XmlPullParser parser) throws XmlPullParserException, IOException
    {
	String name = null;
	parser.require(XmlPullParser.START_TAG, null, G.getName());

	// Initialize attributes and subelements
	String transform = "";
	List<AElement> subelements = new ArrayList<AElement>();

	// Read attributes
	transform = parser.getAttributeValue(null, "transform");

	// Read subelements
	while (parser.next() != XmlPullParser.END_TAG)
	{
	    if (parser.getEventType() != XmlPullParser.START_TAG)
	    {
		continue;
	    }

	    name = parser.getName();

	    // Starts by looking for the entry tag
	    if (name.equals("g"))
	    {
		subelements.add(readG(parser));
	    } else if (name.equals("rect"))
	    {
		subelements.add(readRect(parser));
	    } else if (name.equals("circle"))
	    {
		subelements.add(readCircle(parser));
	    } else
	    {
		skip(parser);
	    }
	}

	G g = new G();
	g.setTransform(transform);
	g.setSubelements(subelements);

	return g;
    }

    /**
     * Returns a Rect element
     * 
     * @param parser
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    private Rect readRect(XmlPullParser parser) throws XmlPullParserException, IOException
    {
	String name = null;
	parser.require(XmlPullParser.START_TAG, null, Rect.getName());

	// Initialize attributes and subelements
	String width = "";
	String height = "";
	String x = "";
	String y = "";
	String id = "";
	String fill = "";
	String opacity = "";
	String style = "";

	// Read attributes
	width = parser.getAttributeValue(null, "width");
	height = parser.getAttributeValue(null, "height");
	x = parser.getAttributeValue(null, "x");
	y = parser.getAttributeValue(null, "y");
	id = parser.getAttributeValue(null, "id");
	fill = parser.getAttributeValue(null, "fill");
	opacity = parser.getAttributeValue(null, "opacity");
	style = parser.getAttributeValue(null, "style");

	// Read subelements
	while (parser.next() != XmlPullParser.END_TAG)
	{
	    if (parser.getEventType() != XmlPullParser.START_TAG)
	    {
		continue;
	    }

	    name = parser.getName();
	}

	Rect rect = new Rect();
	rect.setWidth(Float.parseFloat(width));
	rect.setHeight(Float.parseFloat(height));
	rect.setX(Float.parseFloat(x));
	rect.setY(Float.parseFloat(y));
	rect.setId(id);

	// Remove invalid characters
	if (fill != null)
	{
	    fill = fill.replaceAll("#", "");
	}

	// Evaluate style
	if (style != null)
	{
	    if (style.contains("opacity"))
	    {
		opacity = style.replaceAll(".*opacity:", "").replaceAll(";.*", "");
	    }

	    if (style.contains("fill"))
	    {
		fill = style.replaceAll(".*fill:#", "").replaceAll(";.*", "");
	    }
	}

	// Set defaults
	if (fill == null)
	{
	    fill = "FFFFFF";
	}
	if (opacity == null)
	{
	    opacity = "1";
	}

	int colorA = (int) Float.parseFloat(opacity) * 255;
	int colorR = Integer.parseInt(fill.substring(0, 2), 16);
	int colorG = Integer.parseInt(fill.substring(2, 4), 16);
	int colorB = Integer.parseInt(fill.substring(4, 6), 16);

	Paint paintFill = new Paint();
	paintFill.setARGB(colorA, colorR, colorG, colorB);
	rect.setFill(paintFill);

	return rect;
    }

    /**
     * Returns a Circle element
     * 
     * @param parser
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    private Circle readCircle(XmlPullParser parser) throws XmlPullParserException, IOException
    {
	String name = null;
	parser.require(XmlPullParser.START_TAG, null, Circle.getName());

	// Initialize attributes and subelements
	String cx = "";
	String cy = "";
	String r = "";
	String id = "";
	String fill = "";
	String opacity = "";
	String style = "";

	// Read attributes
	cx = parser.getAttributeValue(null, "cx");
	cy = parser.getAttributeValue(null, "cy");
	r = parser.getAttributeValue(null, "r");
	id = parser.getAttributeValue(null, "id");
	fill = parser.getAttributeValue(null, "fill");
	opacity = parser.getAttributeValue(null, "opacity");
	style = parser.getAttributeValue(null, "style");

	// Read subelements
	while (parser.next() != XmlPullParser.END_TAG)
	{
	    if (parser.getEventType() != XmlPullParser.START_TAG)
	    {
		continue;
	    }

	    name = parser.getName();
	}

	Circle circle = new Circle();
	circle.setCx(Integer.parseInt(cx));
	circle.setCy(Integer.parseInt(cy));
	circle.setR(Integer.parseInt(r));
	circle.setId(id);

	// Remove invalid characters
	if (fill != null)
	{
	    fill = fill.replaceAll("#", "");
	}

	// Evaluate style
	if (style != null)
	{
	    if (style.contains("opacity"))
	    {
		opacity = style.replaceAll(".*opacity:", "").replaceAll(";.*", "");
	    }

	    if (style.contains("fill"))
	    {
		fill = style.replaceAll(".*fill:#", "").replaceAll(";.*", "");
	    }
	}

	// Set defaults
	if (fill == null)
	{
	    fill = "FFFFFF";
	}
	if (opacity == null)
	{
	    opacity = "1";
	}

	int colorA = (int) Float.parseFloat(opacity) * 255;
	int colorR = Integer.parseInt(fill.substring(0, 2), 16);
	int colorG = Integer.parseInt(fill.substring(2, 4), 16);
	int colorB = Integer.parseInt(fill.substring(4, 6), 16);

	Paint paintFill = new Paint();
	paintFill.setARGB(colorA, colorR, colorG, colorB);
	circle.setFill(paintFill);

	return circle;
    }

    /**
     * Returns the title of a card
     * 
     * @param parser
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readString(XmlPullParser parser, String tag) throws IOException, XmlPullParserException
    {
	parser.require(XmlPullParser.START_TAG, null, tag);
	String title = readText(parser);
	parser.require(XmlPullParser.END_TAG, null, tag);
	return title;
    }

    /**
     * Reads the value of a cell
     * 
     * @param parser
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException
    {
	String result = "";
	if (parser.next() == XmlPullParser.TEXT)
	{
	    result = parser.getText();
	    parser.nextTag();
	}
	return result;
    }

    /**
     * Skips a tag that does not fit
     * 
     * @param parser
     * @throws XmlPullParserException
     * @throws IOException
     */
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException
    {
	if (parser.getEventType() != XmlPullParser.START_TAG)
	{
	    throw new IllegalStateException();
	}
	int depth = 1;
	while (depth != 0)
	{
	    switch (parser.next())
	    {
		case XmlPullParser.END_TAG:
		    depth--;
		    break;
		case XmlPullParser.START_TAG:
		    depth++;
		    break;
	    }
	}
    }
}
