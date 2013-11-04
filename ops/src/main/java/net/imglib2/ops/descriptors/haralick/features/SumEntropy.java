package net.imglib2.ops.descriptors.haralick.features;

import net.imglib2.ops.descriptors.AbstractFeature;
import net.imglib2.ops.descriptors.ModuleInput;
import net.imglib2.ops.descriptors.haralick.HaralickCoocMatrix;
import net.imglib2.ops.descriptors.haralick.helpers.CoocPXPlusY;
import net.imglib2.type.numeric.real.DoubleType;

public class SumEntropy extends AbstractFeature
{

	private static final double EPSILON = 0.00000001f;

	@ModuleInput
	private HaralickCoocMatrix cooc;

	@ModuleInput
	private CoocPXPlusY coocPXPlusY;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String name()
	{
		return "Sum Entropy";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SumEntropy copy()
	{
		return new SumEntropy();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DoubleType compute()
	{
		final double[] pxplusy = coocPXPlusY.get();
		final int numGrayLevels = cooc.getDistance();

		double res = 0;

		for ( int i = 2; i <= 2 * numGrayLevels; i++ )
		{
			res += pxplusy[ i ] * Math.log( pxplusy[ i ] + EPSILON );
		}

		res = -res;

		return new DoubleType( res );
	}
}
