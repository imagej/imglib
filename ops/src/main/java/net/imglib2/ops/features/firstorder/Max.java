package net.imglib2.ops.features.firstorder;

import java.util.Iterator;

import net.imglib2.ops.features.AbstractFeature;
import net.imglib2.ops.features.ModuleInput;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

public class Max extends AbstractFeature
{
	@ModuleInput
	private Iterable< ? extends RealType< ? > > ii;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String name()
	{
		return "Maximum";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Max copy()
	{
		return new Max();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DoubleType compute()
	{
		double max = Double.MIN_VALUE;

		final Iterator< ? extends RealType< ? >> it = ii.iterator();
		while ( it.hasNext() )
		{
			double val = it.next().getRealDouble();
			max = val > max ? val : max;
		}

		return new DoubleType( max );
	}
}
