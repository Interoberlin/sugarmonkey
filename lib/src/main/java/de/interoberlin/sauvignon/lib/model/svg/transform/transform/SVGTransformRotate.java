package de.interoberlin.sauvignon.lib.model.svg.transform.transform;

import de.interoberlin.sauvignon.lib.model.svg.elements.AGeometric;
import de.interoberlin.sauvignon.lib.model.util.Matrix;
import de.interoberlin.sauvignon.lib.model.util.Vector2;

/**
 * http://www.w3.org/TR/SVG/coords.html#TransformAttribute
 */
public class SVGTransformRotate extends ATransformOperator
{
	public final ETransformOperatorType	type	= ETransformOperatorType.ROTATE;
	private Float						angle	= 0f;
	private Float						cx		= 0f;
	private Float						cy		= 0f;

	public SVGTransformRotate()
	{
	}

	public SVGTransformRotate(Float angle)
	{
		this.angle = angle;
		updateMatrix = true;
	}

	public SVGTransformRotate(Float angle, Float cx, Float cy)
	{
		this.angle = angle;
		this.cx = cx;
		this.cy = cy;
		updateMatrix = true;
	}

	public SVGTransformRotate(Float angle, Vector2 c, AGeometric relativeTo)
	{
		this(angle, c.getX(), c.getY());
		setRelativeTo(relativeTo);
	}

	public SVGTransformRotate(Float angle, Vector2 c)
	{
		this.angle = angle;
		this.cx = c.getX();
		this.cy = c.getY();
		updateMatrix = true;
	}

	public SVGTransformRotate(Float[] args)
	{
		if (args.length > 0)
		{
			angle = args[0];
			if (args.length > 2)
			{
				cx = args[1];
				cy = args[2];
			}
			updateMatrix = true;
		}
	}

	public Float getAngle()
	{
		return angle;
	}

	public void setAngle(Float angle)
	{
		this.angle = angle;
		this.updateMatrix = true;
	}

	public Float getCx()
	{
		return cx;
	}

	public void setCx(Float cx)
	{
		this.cx = cx;
		this.updateMatrix = true;
	}

	public Float getCy()
	{
		return cy;
	}

	public void setCy(Float cy)
	{
		this.cy = cy;
		this.updateMatrix = true;
	}

	public Matrix getResultingMatrix()
	{
		if (this.updateMatrix)
		{
			// https://de.wikipedia.org/wiki/Drehmatrix

			float angle = (float) Math.toRadians(this.angle);

			Vector2 v = new Vector2(cx, cy);
			if (getRelativeTo() != null)
			{
				v.applyCTM(getRelativeTo().getCTM());
			}
			float x = v.getX();
			float y = v.getY();

			this.resultingMatrix = new Matrix((float) Math.cos(angle), (float) Math.sin(angle), (float) -Math.sin(angle), (float) Math.cos(angle), (float) (-x * Math.cos(angle)
					+ y * Math.sin(angle) + x), (float) (-x * Math.sin(angle) - y * Math.cos(angle) + y));
			this.updateMatrix = false;
		}
		return this.resultingMatrix;
	}
}
