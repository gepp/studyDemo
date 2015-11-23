package jdk2010.io.nio.reactor1;

import java.io.IOException;

public class Main {
		public static void main(String[] args) throws IOException {
			Reactor reactor = new Reactor(9098);
	        reactor.run();
		}
}
