package net.imglib2.ops.features.haralick.helpers;

import net.imglib2.ops.features.annotations.RequiredInput;
import net.imglib2.ops.features.datastructures.CachedAbstractSampler;

public class CoocPY extends CachedAbstractSampler< double[] >
{

	// in the symmetric case px = py
	@RequiredInput
	CoocPX coocPX = new CoocPX();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CoocPY copy()
	{
		return new CoocPY();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double[] recompute()
	{
		return coocPX.get();
	}

}