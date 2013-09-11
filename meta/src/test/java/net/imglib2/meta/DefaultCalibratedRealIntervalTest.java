/*
 * #%L
 * ImgLib2: a general-purpose, multidimensional image processing library.
 * %%
 * Copyright (C) 2009 - 2013 Stephan Preibisch, Tobias Pietzsch, Barry DeZonia,
 * Stephan Saalfeld, Albert Cardona, Curtis Rueden, Christian Dietz, Jean-Yves
 * Tinevez, Johannes Schindelin, Lee Kamentsky, Larry Lindsey, Grant Harris,
 * Mark Hiner, Aivar Grislis, Martin Horn, Nick Perry, Michael Zinsmaier,
 * Steffen Jaensch, Jan Funke, Mark Longair, and Dimiter Prodanov.
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

package net.imglib2.meta;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * @author Barry DeZonia
 */
public class DefaultCalibratedRealIntervalTest {

	private DefaultCalibratedRealInterval interval;

	@Test
	public void test1() {
		double[] extents = new double[]{5,10,20};
		double[] temp = new double[extents.length];
		CalibratedAxis[] axes = new CalibratedAxis[extents.length];
		interval = new DefaultCalibratedRealInterval(extents);
		assertEquals(extents.length, interval.numDimensions());
		interval.realMin(temp);
		assertArrayEquals(new double[3], temp, 0);
		interval.realMax(temp);
		assertArrayEquals(new double[] { 5, 10, 20 }, temp, 0);
		interval.axes(axes);
		// TODO - this is a little surprising
		for (CalibratedAxis axis : axes) {
			assertNull(axis);
		}
		for (int i = 0; i < extents.length; i++) {
			assertEquals(Double.NaN, interval.calibration(i), 0);
			assertNull(interval.unit(i));
		}
		DefaultCalibratedAxis axis = new DefaultCalibratedAxis(Axes.X, "plorps", 4);
		interval.setAxis(axis, 0);
		assertEquals(Axes.X, interval.axis(0).type());
		assertEquals("plorps", interval.axis(0).unit());
		assertEquals(4, interval.axis(0).calibration(), 0);
	}

	@Test
	public void test2() {
		double[] extents = new double[] { 5, 10, 20 };
		double[] temp = new double[extents.length];
		CalibratedAxis axis0 = new DefaultCalibratedAxis(Axes.TIME, "froop", 1);
		CalibratedAxis axis1 = new DefaultCalibratedAxis(Axes.X, "orp", 3);
		CalibratedAxis axis2 =
			new DefaultCalibratedAxis(Axes.CHANNEL, "smump", 5);
		interval = new DefaultCalibratedRealInterval(extents, axis0, axis1, axis2);
		assertEquals(extents.length, interval.numDimensions());
		interval.realMin(temp);
		assertArrayEquals(new double[3], temp, 0);
		interval.realMax(temp);
		assertArrayEquals(new double[] { 5, 10, 20 }, temp, 0);
		CalibratedAxis[] axes = new CalibratedAxis[extents.length];
		interval.axes(axes);
		for (CalibratedAxis axis : axes) {
			assertNotNull(axis);
		}
		assertEquals(Axes.TIME, interval.axis(0).type());
		assertEquals(Axes.X, interval.axis(1).type());
		assertEquals(Axes.CHANNEL, interval.axis(2).type());
		assertEquals("froop", interval.unit(0));
		assertEquals("orp", interval.unit(1));
		assertEquals("smump", interval.unit(2));
		assertEquals(1, interval.calibration(0), 0);
		assertEquals(3, interval.calibration(1), 0);
		assertEquals(5, interval.calibration(2), 0);
	}

}
