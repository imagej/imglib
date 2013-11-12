package net.imglib2.descriptors.moments.central.helper;

import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.descriptors.AbstractFeatureModule;
import net.imglib2.descriptors.ModuleInput;
import net.imglib2.descriptors.geometric.centerofgravity.CenterOfGravity;
import net.imglib2.type.numeric.RealType;

public class NormalizedCentralMoment03 extends AbstractFeatureModule
{
	@ModuleInput
	IterableInterval< ? extends RealType< ? >> ii;

	@ModuleInput
	CenterOfGravity center;

	protected double computeCentralMoment( int p, int q )
	{
		final double centerX = center.get()[ 0 ];
		final double centerY = center.get()[ 1 ];

		double result = 0.0;

		final Cursor< ? extends RealType< ? > > it = ii.localizingCursor();
		while ( it.hasNext() )
		{
			it.fwd();
			final double x = it.getIntPosition( 0 ) - centerX;
			final double y = it.getIntPosition( 1 ) - centerY;

			result += it.get().getRealDouble() * Math.pow( x, p ) * Math.pow( y, q );
		}

		return result;
	}

	@Override
	public String name() {
		return "Normalized central moment p = 0 and q = 3";
	}

	@Override
	protected double calculateFeature() {
		int p = 0; int q = 3;
		double norm = Math.pow(ii.size(), (p + q + 2) / 2);
		return computeCentralMoment(p, q)/norm;
	}
}
