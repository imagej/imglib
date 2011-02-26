/**
 * Copyright (c) 2011, Tobias Pietzsch & Stephan Preibisch & Stephan Saalfeld
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.  Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials
 * provided with the distribution.  Neither the name of the Fiji project nor
 * the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package mpicbg.imglib;



/**
 * An element that can be positioned in n-dimensional real space.
 *
 * @author Stephan Saalfeld <saalfeld@mpi-cbg.de> and Stephan Preibisch
 */
public interface RealPositionable extends Positionable
{
	/**
	 * Move the element in one dimension for some distance.
	 *  
	 * @param distance
	 * @param d
	 */
	public void move( float distance, int d );

	/**
	 * Move the element in one dimension for some distance.
	 *  
	 * @param distance
	 * @param d
	 */
	public void move( double distance, int d );

	/**
	 * Move the element relative to its current location using a
	 * {@link RealLocalizable} as distance vector.
	 * 
	 * @param localizable
	 */
	public void move( RealLocalizable localizable );
	
	/**
	 * Move the element relative to its current location using a float[] as
	 * distance vector.
	 * 
	 * @param position
	 */
	public void move( float[] position );
	
	/**
	 * Move the element relative to its current location using a float[] as
	 * distance vector.
	 * 
	 * @param position
	 */
	public void move( double[] position );
	
	/**
	 * Place the element at the same location as a given {@link RealLocalizable}
	 * 
	 * @param localizable
	 */
	public void setPosition( RealLocalizable localizable );
	
	/**
	 * Set the position of the element.
	 * 
	 * @param position
	 */
	public void setPosition( float position[] );
	
	/**
	 * Set the position of the element.
	 * 
	 * @param position
	 */
	public void setPosition( double position[] );
	
	/**
	 * Set the position of the element for one dimension.
	 * 
	 * @param position
	 * @param d
	 */
	public void setPosition( float position, int d );		
	
	/**
	 * Set the position of the element for one dimension.
	 * 
	 * @param position
	 * @param d
	 */
	public void setPosition( double position, int d );
}
