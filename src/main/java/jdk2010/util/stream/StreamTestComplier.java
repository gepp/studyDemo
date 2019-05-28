package jdk2010.util.stream;

import java.util.Arrays;
import java.util.List;

public class StreamTestComplier {

	static String[] atp = new String[] { "Rafael Nadal", "Novak Djokovic", "Stanislas Wawrinka", "David Ferrer",
			"Roger Federer", "Andy Murray", "Tomas Berdych", "Juan Martin Del Potro" };

	public static void main(String[] args) {
	        List<String> players = Arrays.asList(atp);
	        players.forEach((Consumer<String>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)V, lambda$0(java.lang.String ), (Ljava/lang/String;)V)());
	    }

	private static /* synthetic */ void lambda$0(String player) {
		System.out.println(player);
	}

}
