package jdk2010.util.stream;

import java.util.Arrays;
import java.util.List;

public class StreamUtil {
	static String[] atp = { "Rafael Nadal", "Novak Djokovic", "Stanislas Wawrinka", "David Ferrer", "Roger Federer",
			"Andy Murray", "Tomas Berdych", "Juan Martin Del Potro" };

	public static void main(String[] args) {
		List<String> players = Arrays.asList(atp);
		// 以前的循环方式
		// for (String player : players) {
		// System.out.print(player + "; ");
		// }

		// 使用 lambda 表达式以及函数操作(functional operation)
		players.forEach((player) -> 
				System.out.println(player)

		);

	}
}
