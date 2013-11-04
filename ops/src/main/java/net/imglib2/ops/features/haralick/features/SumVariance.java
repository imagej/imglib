package net.imglib2.ops.features.haralick.features;

import net.imglib2.ops.features.AbstractFeature;
import net.imglib2.ops.features.ModuleInput;
import net.imglib2.ops.features.haralick.HaralickCoocMatrix;
import net.imglib2.ops.features.haralick.helpers.CoocPXPlusY;
import net.imglib2.type.numeric.real.DoubleType;

public class SumVariance extends AbstractFeature
{

	@ModuleInput
	private SumAverage sumAverage;

	@ModuleInput
	private CoocPXPlusY coocPXPlusY;

	@ModuleInput
	private HaralickCoocMatrix cooc;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String name()
	{
		return "Sum Variance";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SumVariance copy()
	{
		return new SumVariance();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DoubleType compute()
	{
		final double[] pxplusy = coocPXPlusY.get();
		final int numGrayLevels = cooc.getNrGrayLevels();
		final double average = this.sumAverage.get().get();

		double res = 0;
		for ( int i = 2; i <= 2 * numGrayLevels; i++ )
		{
			res += ( i - average ) * ( i - average ) * pxplusy[ i ];
		}

		return new DoubleType( res );
	}

}
