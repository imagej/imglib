package net.imglib2.ops.features.firstorder.sums;

import java.util.Iterator;

import net.imglib2.ops.features.AbstractFeature;
import net.imglib2.ops.features.ModuleInput;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

public class SumOfInverses extends AbstractFeature
{
	@ModuleInput
	private Iterable< ? extends RealType< ? > > ii;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String name()
	{
		return "Sum of Inverses";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SumOfInverses copy()
	{
		return new SumOfInverses();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DoubleType compute()
	{
		Iterator< ? extends RealType< ? > > it = ii.iterator();
		double result = 0.0;

		while ( it.hasNext() )
		{
			result += ( 1 / it.next().getRealDouble() );
		}
		return new DoubleType( result );
	}
}
