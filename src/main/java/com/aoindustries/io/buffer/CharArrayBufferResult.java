/*
 * ao-io-buffer - Output buffering library.
 * Copyright (C) 2013, 2015, 2016, 2020  AO Industries, Inc.
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

import com.aoindustries.io.Encoder;
import com.aoindustries.lang.Strings;
import com.aoindustries.math.SafeMath;
import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A result contained in a single {@code char[]}.
 *
 * @author  AO Industries, Inc.
 */
public class CharArrayBufferResult implements BufferResult {

	/**
	 * @see  CharArrayBufferWriter#buffer
	 */
	private final char[] buffer;

	private final int start;
	private final int end;

	/**
	 * @param buffer  No defensive copy is made and assumes the array will not change
	 */
	public CharArrayBufferResult(
		char[] buffer,
		int start,
		int end
	) {
		this.buffer = buffer;
		this.start = start;
		this.end = end;
	}

	/**
	 * @param buffer  No defensive copy is made and assumes the array will not change
	 */
	public CharArrayBufferResult(char[] buffer) {
		this(buffer, 0, buffer.length);
	}

	@Override
	public long getLength() {
		return end - start;
	}

	private volatile String toStringCache;

	@Override
	public boolean isFastToString() {
		return
			start == end
			|| toStringCache != null;
	}

	@Override
	public String toString() {
		if(toStringCache == null) {
			if(start == end) {
				toStringCache = "";
			} else {
				toStringCache = new String(buffer, start, end - start);
			}
		}
		return toStringCache;
	}

	@Override
	public void writeTo(Writer out) throws IOException {
		out.write(buffer, start, end - start);
	}

	@Override
	public void writeTo(Writer out, long off, long len) throws IOException {
		if((start + off + len) > end) throw new IndexOutOfBoundsException();
		out.write(
			buffer,
			SafeMath.castInt(start + off),
			SafeMath.castInt(len)
		);
	}

	@Override
	public void writeTo(Encoder encoder, Writer out) throws IOException {
		if(encoder == null) {
			writeTo(out);
		} else {
			encoder.write(
				buffer,
				start,
				end - start,
				out
			);
		}
	}

	@Override
	public void writeTo(Encoder encoder, Writer out, long off, long len) throws IOException {
		if(encoder == null) {
			writeTo(out, off, len);
		} else {
			if((start + off + len) > end) throw new IndexOutOfBoundsException();
			encoder.write(
				buffer,
				SafeMath.castInt(start + off),
				SafeMath.castInt(len),
				out
			);
		}
	}

	private final AtomicReference<BufferResult> trimmed = new AtomicReference<>();

	@Override
	public BufferResult trim() {
		BufferResult _trimmed = this.trimmed.get();
		if(_trimmed == null) {
			int newStart = this.start;
			final char[] buf = buffer;
			// Skip past the beginning whitespace characters
			while(newStart < end) {
				// TODO: Support surrogates: there are no whitespace characters outside the BMP as of Unicode 12.1, so this is not a high priority
				char ch = buf[newStart];
				if(!Strings.isWhitespace(ch)) break;
				newStart++;
			}
			// Skip past the ending whitespace characters
			int newEnd = end;
			while(newEnd > newStart) {
				// TODO: Support surrogates: there are no whitespace characters outside the BMP as of Unicode 12.1, so this is not a high priority
				char ch = buf[newEnd - 1];
				if(!Strings.isWhitespace(ch)) break;
				newEnd--;
			}
			// Check if empty
			if(newStart == newEnd) {
				_trimmed = EmptyResult.getInstance();
			}
			// Keep this object if already trimmed
			else if(
				start == newStart
				&& end == newEnd
			) {
				_trimmed = this;
			}
			// Otherwise, return new substring
			else {
				CharArrayBufferResult newTrimmed = new CharArrayBufferResult(
					buffer,
					newStart,
					newEnd
				);
				newTrimmed.trimmed.set(newTrimmed);
				_trimmed = newTrimmed;
			}
			if(!this.trimmed.compareAndSet(null, _trimmed)) {
				_trimmed = this.trimmed.get();
			}
		}
		return _trimmed;
	}
}
