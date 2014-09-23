package de.interoberlin.sauvignon.lib.model.svg.transform.transform;

import de.interoberlin.sauvignon.lib.model.util.Matrix;

public class SVGTransformScale extends ATransformOperator
{
	public final ETransformOperatorType	type	= ETransformOperatorType.SCALE;
	private Float						sx		= 0f;
	private Float						sy		= 0f;

	public SVGTransformScale()
	{
	}

	public SVGTransformScale(Float s)
	{
		sx = s;
		sy = s;
		updateMatrix = true;
	}

	public SVGTransformScale(Float sx, Float sy)
	{
		this.sx = sx;
		this.sy = sy;
		updateMatrix = true;
	}

	public SVGTransformScale(Float[] args)
	{
		if (args.length > 0)
		{
			sx = args[0];
			if (args.length > 1)
				sy = args[1];
			updateMatrix = true;
		}
	}

	public Float getSx()
	{
		return sx;
	}

	public void setSx(Float sx)
	{
		this.sx = sx;
		updateMatrix = true;
	}

	public Float getSy()
	{
		return sy;
	}

	public void setSy(Float sy)
	{
		this.sy = sy;
		updateMatrix = true;
	}

	public Matrix getResultingMatrix()
	{
		if (updateMatrix)
		{
			resultingMatrix = new Matrix(sx, 0f, 0f, sy, 0f, 0f);
			updateMatrix = false;
		}
		return resultingMatrix;
	}
}
