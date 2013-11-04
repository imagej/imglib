package net.imglib2.ops.descriptors.haralick;

import java.util.Arrays;

import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.ops.data.CooccurrenceMatrix;
import net.imglib2.ops.data.CooccurrenceMatrix.MatrixOrientation;
import net.imglib2.ops.descriptors.AbstractModule;
import net.imglib2.ops.descriptors.ModuleInput;
import net.imglib2.ops.descriptors.firstorder.Max;
import net.imglib2.ops.descriptors.firstorder.Min;
import net.imglib2.type.numeric.RealType;

public class HaralickCoocMatrix extends AbstractModule< CooccurrenceMatrix >
{

	@ModuleInput
	IterableInterval< ? extends RealType< ? >> ii;

	@ModuleInput
	Min min;

	@ModuleInput
	Max max;

	private final int nrGrayLevels;

	private final int distance;

	private final MatrixOrientation orientation;

	public HaralickCoocMatrix( final int nrGrayLevels, final int distance, final MatrixOrientation orientation )
	{
		this.nrGrayLevels = nrGrayLevels;
		this.distance = distance;
		this.orientation = orientation;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected CooccurrenceMatrix recompute()
	{

		final Cursor< ? extends RealType< ? > > cursor = ii.cursor();

		final double localMin = this.min.get().get();

		final double localMax = this.max.get().get();

		int[][] pixels = new int[ ( int ) ii.dimension( 0 ) ][ ( int ) ii.dimension( 1 ) ];

		for ( int i = 0; i < pixels.length; i++ )
		{
			Arrays.fill( pixels[ i ], Integer.MAX_VALUE );
		}

		CooccurrenceMatrix matrix = new CooccurrenceMatrix( nrGrayLevels );

		while ( cursor.hasNext() )
		{
			cursor.fwd();
			pixels[ cursor.getIntPosition( 1 ) - ( int ) ii.min( 1 ) ][ cursor.getIntPosition( 0 ) - ( int ) ii.min( 0 ) ] = ( int ) ( ( ( cursor.get().getRealDouble() - localMin ) / ( localMax - localMin + 1 ) ) * nrGrayLevels );

		}

		int nrPairs = 0;

		for ( int y = 0; y < pixels.length; y++ )
		{
			for ( int x = 0; x < pixels[ y ].length; x++ )
			{
				// ignore pixels not in mask
				if ( pixels[ y ][ x ] == Integer.MAX_VALUE )
				{
					continue;
				}

				// get second pixel
				int sx = x + getOrientation().dx * distance;
				int sy = y + getOrientation().dy * distance;
				// get third pixel
				int tx = x - getOrientation().dx * distance;
				int ty = y - getOrientation().dy * distance;

				// second pixel in interval and mask
				if ( sx >= 0 && sy >= 0 && sy < pixels.length && sx < pixels[ sy ].length && pixels[ sy ][ sx ] != Integer.MAX_VALUE )
				{
					matrix.incValueAt( pixels[ y ][ x ], pixels[ sy ][ sx ] );
					nrPairs++;
				}
				// third pixel in interval
				if ( tx >= 0 && ty >= 0 && ty < pixels.length && tx < pixels[ ty ].length && pixels[ ty ][ tx ] != Integer.MAX_VALUE )
				{
					matrix.incValueAt( pixels[ y ][ x ], pixels[ ty ][ tx ] );
					nrPairs++;
				}
			}
		}

		if ( nrPairs > 0 )
		{
			matrix.divideBy( nrPairs );
		}

		return matrix;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HaralickCoocMatrix copy()
	{
		return new HaralickCoocMatrix( nrGrayLevels, distance, orientation );
	}

	public int getNrGrayLevels()
	{
		return nrGrayLevels;
	}

	public int getDistance()
	{
		return distance;
	}

	public MatrixOrientation getOrientation()
	{
		return orientation;
	}
}
