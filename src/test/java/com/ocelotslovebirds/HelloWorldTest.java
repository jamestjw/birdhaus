package com.ocelotslovebirds;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.ocelotslovebirds.HelloWorld;

public class HelloWorldTest {
	@Test
	public void testGetFourOneTwo() {
		HelloWorld helloWorld = new HelloWorld();
		assertEquals(412, helloWorld.getFourOneTwo());
	}
}
