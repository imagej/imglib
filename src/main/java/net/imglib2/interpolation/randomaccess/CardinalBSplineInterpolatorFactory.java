/*
 * #%L
 * ImgLib2: a general-purpose, multidimensional image processing library.
 * %%
 * Copyright (C) 2009 - 2018 Tobias Pietzsch, Stephan Preibisch, Stephan Saalfeld,
 * John Bogovic, Albert Cardona, Barry DeZonia, Christian Dietz, Jan Funke,
 * Aivar Grislis, Jonathan Hale, Grant Harris, Stefan Helfrich, Mark Hiner,
 * Martin Horn, Steffen Jaensch, Lee Kamentsky, Larry Lindsey, Melissa Linkert,
 * Mark Longair, Brian Northan, Nick Perry, Curtis Rueden, Johannes Schindelin,
 * Jean-Yves Tinevez and Michael Zinsmaier.
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
 * #L%
 */

package net.imglib2.interpolation.randomaccess;

import net.imglib2.RandomAccessible;
import net.imglib2.RealInterval;
import net.imglib2.interpolation.InterpolatorFactory;
import net.imglib2.type.Type;
import net.imglib2.type.numeric.RealType;

/**
 * TODO
 *
 */
public class CardinalBSplineInterpolatorFactory< T extends RealType< T > > implements InterpolatorFactory< T, RandomAccessible< T > >
{
	final int order;

	final boolean clipping;

	/**
	 * Creates a new {@link CardinalBSplineInterpolatorFactory} using the BSpline
	 * interpolation in a certain window
	 *
	 * @param order
	 *            the order of the bspline
	 * @param clipping
	 *            the bspline-interpolation can create values that are bigger or
	 *            smaller than the original values, so they can be clipped to
	 *            the range of the {@link Type} if wanted
	 */
	public CardinalBSplineInterpolatorFactory( final int order, final boolean clipping )
	{
		this.order = order;
		this.clipping = clipping;
	}

	/**
	 * Creates a new {@link CardinalBSplineInterpolatorFactory} using the BSpline
	 * interpolation in a certain window
	 *
	 * @param order
	 *            the order of the bspline
	 *
	 */
	public CardinalBSplineInterpolatorFactory( final int order )
	{
		this.order = order;
		this.clipping = true;
	}

	/**
	 * Creates a new {@link CardinalBSplineInterpolatorFactory} with standard parameters
	 * (do clipping, alpha=3)
	 */
	public CardinalBSplineInterpolatorFactory()
	{
		this( 3, true );
	}

	@Override
	public CardinalBSplineInterpolator< T > create( final RandomAccessible< T > randomAccessible )
	{
		return new CardinalBSplineInterpolator< T >( randomAccessible, order, order + 1, clipping );
	}

	/**
	 * For now, ignore the {@link RealInterval} and return
	 * {@link #create(RandomAccessible)}.
	 */
	@Override
	public CardinalBSplineInterpolator< T > create( final RandomAccessible< T > randomAccessible, final RealInterval interval )
	{
		return create( randomAccessible );
	}

}
