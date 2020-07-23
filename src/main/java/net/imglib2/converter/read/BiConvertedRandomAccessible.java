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

package net.imglib2.converter.read;

import java.util.function.Supplier;

import net.imglib2.Interval;
import net.imglib2.RandomAccessible;
import net.imglib2.converter.AbstractConvertedRandomAccessible;
import net.imglib2.converter.BiConverter;
import net.imglib2.type.Type;

/**
 * TODO
 *
 */
public class BiConvertedRandomAccessible< A, B, C extends Type< C > > extends AbstractConvertedRandomAccessible< A, C >
{
	protected final RandomAccessible< B > sourceB;

	protected final Supplier< BiConverter< ? super A, ? super B, ? super C > > converterSupplier;

	protected final C converted;

	public BiConvertedRandomAccessible(
			final RandomAccessible< A > sourceA,
			final RandomAccessible< B > sourceB,
			final Supplier< BiConverter< ? super A, ? super B, ? super C > > converterSupplier,
			final C c )
	{
		super( sourceA );
		this.sourceB = sourceB;
		this.converterSupplier = converterSupplier;
		this.converted = c.copy();
	}

	public BiConvertedRandomAccessible(
			final RandomAccessible< A > sourceA,
			final RandomAccessible< B > sourceB,
			final BiConverter< ? super A, ? super B, ? super C > converter,
			final C c )
	{
		this( sourceA, sourceB, () -> converter, c );
	}

	@Override
	public BiConvertedRandomAccess< A, B, C > randomAccess()
	{
		return new BiConvertedRandomAccess<>( source.randomAccess(), sourceB.randomAccess(), converterSupplier, converted );
	}

	@Override
	public BiConvertedRandomAccess< A, B, C > randomAccess( final Interval interval )
	{
		return new BiConvertedRandomAccess<>( source.randomAccess( interval ), sourceB.randomAccess( interval ), converterSupplier, converted );
	}

	/**
	 * @return an instance of the destination {@link Type}.
	 */
	public C getDestinationType()
	{
		return converted.copy();
	}

	/**
	 * Returns an instance of the {@link BiConverter}.  If the
	 * {@link BiConvertedIterableInterval} was created with a
	 * {@link BiConverter} instead of a {@link Supplier}, then the returned
	 * {@link BiConverter} will be this instance.
	 *
	 * @return
	 */
	public BiConverter< ? super A, ? super B, ? super C > getConverter()
	{
		return converterSupplier.get();
	}
}
