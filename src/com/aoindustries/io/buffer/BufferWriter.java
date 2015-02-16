/*
 * ao-io-buffer - Output buffering library.
 * Copyright (C) 2013, 2015  AO Industries, Inc.
 *     support@aoindustries.com
 *     7262 Bull Pen Cir
 *     Mobile, AL 36695
 *
 * This file is part of ao-io-buffer.
 *
 * ao-io-buffer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ao-io-buffer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with ao-io-buffer.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aoindustries.io.buffer;

import java.io.IOException;
import java.io.Writer;

/**
 * A buffered writer with results that may be trimmed, converted to String, and written to another
 * writer.
 *
 * @author  AO Industries, Inc.
 */
abstract public class BufferWriter extends Writer {

	protected BufferWriter() {
    }

    /**
     * Gets the number of characters in this buffer.
	 * Once closed, this length will not be modified.
     */
    abstract public long getLength() throws IOException;

    /**
	 * Gets a short message (like type and length).
	 *
	 * @see  #getResult()  To get access to the buffered content.
     */
    @Override
    abstract public String toString();

	/**
	 * Gets the result from this buffer.
	 * The buffer must be closed.
	 *
	 * @exception  IllegalStateException if not closed
	 */
    abstract public BufferResult getResult() throws IllegalStateException, IOException;
}
