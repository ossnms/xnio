package org.xnio.ssl;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;
import org.xnio.Pooled;
import org.xnio._private.Messages;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class JsseSslConduitEngineTest {

	private final Mockery context = new Mockery();

	@Test(expected = IOException.class)
	public void freedPooledBuffer_throwsIoException() throws Exception {
		final Pooled<Integer> socketBuffer = context.mock(Pooled.class);

		context.checking(new Expectations() {{
			oneOf(socketBuffer).getResource(); will(throwException(Messages.msg.bufferFreed()));
		}});

		JsseSslConduitEngine.getResource(socketBuffer);
	}

	@Test
	public void allocatedPooledBuffer_doesNotThrowIoException() throws Exception {
		final Pooled<Integer> socketBuffer = context.mock(Pooled.class);
		final Integer buffer = 125;

		context.checking(new Expectations() {{
			oneOf(socketBuffer).getResource(); will(returnValue(buffer));
		}});

		final Integer returned = JsseSslConduitEngine.getResource(socketBuffer);
		assertThat(returned, is(buffer));
	}
}