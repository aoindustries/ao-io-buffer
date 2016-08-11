/*
 * ao-io-buffer - Output buffering library.
 * Copyright (C) 2013, 2015, 2016  AO Industries, Inc.
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

/**
 * Discards everything.  Not a proper buffer but useful to time the overhead of calling the buffers.
 *
 * @author  AO Industries, Inc.
 */
final public class NullBufferWriter extends BufferWriter {

	private static final NullBufferWriter instance = new NullBufferWriter();

	public static NullBufferWriter getInstance() {
		return instance;
	}

	private NullBufferWriter() {
	}

	@Override
	public void write(int c) {
	}

	@Override
	public void write(char cbuf[]) {
	}

	@Override
	public void write(char cbuf[], int off, int len) {
	}

	@Override
	public void write(String str) {
	}

	@Override
	public void write(String str, int off, int len) {
	}

	@Override
	public NullBufferWriter append(CharSequence csq) {
		return this;
	}

	@Override
	public NullBufferWriter append(CharSequence csq, int start, int end) {
		return this;
	}

	@Override
	public NullBufferWriter append(char c) {
		return this;
	}

	@Override
	public void flush() {
	}

	@Override
	public void close()  {
	}

	@Override
	public long getLength() {
		return 0;
	}

	@Override
	public String toString() {
		return "NullBufferWriter()";
	}

	@Override
	public EmptyResult getResult() {
		return EmptyResult.getInstance();
	}
}
