/*
 * #%L
 * ImgLib2: a general-purpose, multidimensional image processing library.
 * %%
 * Copyright (C) 2009 - 2012 Stephan Preibisch, Stephan Saalfeld, Tobias
 * Pietzsch, Albert Cardona, Barry DeZonia, Curtis Rueden, Lee Kamentsky, Larry
 * Lindsey, Johannes Schindelin, Christian Dietz, Grant Harris, Jean-Yves
 * Tinevez, Steffen Jaensch, Mark Longair, Nick Perry, and Jan Funke.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are
 * those of the authors and should not be interpreted as representing official
 * policies, either expressed or implied, of any organization.
 * #L%
 */
package net.imglib2.view.iteration;

import java.util.Iterator;

import net.imglib2.AbstractWrappedInterval;
import net.imglib2.Cursor;
import net.imglib2.FlatIterationOrder;
import net.imglib2.Interval;
import net.imglib2.IterableInterval;
import net.imglib2.IterableRealInterval;
import net.imglib2.RandomAccessible;
import net.imglib2.transform.integer.BoundingBox;
import net.imglib2.transform.integer.BoundingBoxTransform;
import net.imglib2.transform.integer.SlicingTransform;
import net.imglib2.util.Intervals;
import net.imglib2.view.IterableRandomAccessibleInterval;
import net.imglib2.view.TransformBuilder;
import net.imglib2.view.Views;

/**
 * TODO
 * 
 * TODO: {@link TransformBuilder} propagates a BoundingBox through
 * {@link BoundingBoxTransform} transforms. Additionally, for iteration, we need
 * to guarantee that the transforms are bijections (at least within the bounding
 * box).
 * 
 * @author Tobias Pietzsch <tobias.pietzsch@gmail.com>
 */
public class IterableTransformBuilder< T > extends TransformBuilder< T >
{
	/**
	 * TODO
	 * 
	 * @param interval
	 * @param randomAccessible
	 * @return
	 */
	public static < S > IterableInterval< S > getEfficientIterableInterval( final Interval interval, final RandomAccessible< S > randomAccessible )
	{
		return new IterableTransformBuilder< S >( interval, randomAccessible ).buildIterableInterval();
	}

	/**
	 * The interval in which access is needed. This is propagated through the
	 * transforms down the view hierarchy.
	 */
	protected Interval interval;

	public IterableTransformBuilder( final Interval interval, final RandomAccessible< T > randomAccessible )
	{
		super( interval, randomAccessible );
		this.interval = interval;
	}

	class SubInterval extends AbstractWrappedInterval< Interval > implements IterableInterval< T >
	{
		final long numElements;

		final SubIntervalIterable< T > iterableSource;

		public SubInterval( final SubIntervalIterable< T > iterableSource )
		{
			super( interval );
			numElements = Intervals.numElements( interval );
			this.iterableSource = iterableSource;
		}

		@Override
		public long size()
		{
			return numElements;
		}

		@Override
		public T firstElement()
		{
			return cursor().next();
		}

		@Override
		public Object iterationOrder()
		{
			return iterableSource.subIntervalIterationOrder( interval );
		}

		@Override
		public boolean equalIterationOrder( final IterableRealInterval< ? > f )
		{
			return iterationOrder().equals( f.iterationOrder() );
		}

		@Override
		public Iterator< T > iterator()
		{
			return cursor();
		}

		@Override
		public Cursor< T > cursor()
		{
			return iterableSource.cursor( interval );
		}

		@Override
		public Cursor< T > localizingCursor()
		{
			return iterableSource.localizingCursor( interval );
		}
	}

	class Slice extends AbstractWrappedInterval< Interval > implements IterableInterval< T >
	{
		final long numElements;

		final SubIntervalIterable< T > iterableSource;

		final Interval sourceInterval;

		final SlicingTransform transformToSource;

		final boolean hasFlatIterationOrder;

		public Slice( final SubIntervalIterable< T > iterableSource, final Interval sourceInterval, final SlicingTransform transformToSource, final boolean hasFlatIterationOrder )
		{
			super( interval );
			numElements = Intervals.numElements( interval );
			this.iterableSource = iterableSource;
			this.sourceInterval = sourceInterval;
			this.transformToSource = transformToSource;
			this.hasFlatIterationOrder = hasFlatIterationOrder;
		}

		@Override
		public long size()
		{
			return numElements;
		}

		@Override
		public T firstElement()
		{
			return cursor().next();
		}

		@Override
		public Object iterationOrder()
		{
			return hasFlatIterationOrder ? new FlatIterationOrder( interval ) : this;
		}

		@Override
		public boolean equalIterationOrder( final IterableRealInterval< ? > f )
		{
			return iterationOrder().equals( f.iterationOrder() );
		}

		@Override
		public Iterator< T > iterator()
		{
			return cursor();
		}

		@Override
		public Cursor< T > cursor()
		{
			return new SlicingCursor< T >( iterableSource.cursor( sourceInterval ), transformToSource );
		}

		@Override
		public Cursor< T > localizingCursor()
		{
			return new SlicingCursor< T >( iterableSource.localizingCursor( sourceInterval ), transformToSource );
		}
	}

	public IterableInterval< T > buildIterableInterval()
	{
		if ( boundingBox != null && SubIntervalIterable.class.isInstance( source ) )
		{
			@SuppressWarnings( "unchecked" )
			final SubIntervalIterable< T > iterableSource = ( SubIntervalIterable< T > ) source;
			if ( transforms.isEmpty() )
			{
				if ( iterableSource.supportsOptimizedCursor( interval ) )
					return new SubInterval( iterableSource );
			}
			else if ( transforms.size() == 1 && SlicingTransform.class.isInstance( transforms.get( 0 ) ) )
			{
				final SlicingTransform t = ( SlicingTransform ) transforms.get( 0 );
				final int m = t.numTargetDimensions();
				boolean optimizable = true;
				int firstZeroDim = 0;
				for ( ; firstZeroDim < m && !t.getComponentZero( firstZeroDim ); ++firstZeroDim );
				for ( int d = firstZeroDim + 1; d < m; ++d )
					if ( t.getComponentZero( d ) )
					{
						optimizable = false;
						break;
					}
				if ( optimizable )
				{
					// System.out.println( "interval = " + Util.printInterval(
					// interval ) );
					final Interval sliceInterval = t.transform( new BoundingBox( interval ) ).getInterval();
					// System.out.println( "transformed interval = " +
					// Util.printInterval( sliceInterval ) );
					if ( iterableSource.supportsOptimizedCursor( sliceInterval ) )
					{
						// check for FlatIterationOrder
						boolean flat = FlatIterationOrder.class.isInstance( iterableSource.subIntervalIterationOrder( sliceInterval ) );
						for ( int d = 0; d < firstZeroDim && flat; ++d )
							if ( t.getComponentMapping( d ) != d )
								flat = false;
						return new Slice( iterableSource, sliceInterval, t, flat );
					}
				}
			}
		}
		return new IterableRandomAccessibleInterval< T >( Views.interval( build(), interval ) );
	}
}
