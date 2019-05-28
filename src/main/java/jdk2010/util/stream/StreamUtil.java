package jdk2010.util.stream;

import java.util.Arrays;
import java.util.List;

public class StreamUtil {
	static String[] atp = { "Rafael Nadal", "Novak Djokovic", "Stanislas Wawrinka", "David Ferrer", "Roger Federer",
			"Andy Murray", "Tomas Berdych", "Juan Martin Del Potro" };

	public static void main(String[] args) {
		List<String> players = Arrays.asList(atp);
		// ��ǰ��ѭ����ʽ
		// for (String player : players) {
		// System.out.print(player + "; ");
		// }

		// ʹ�� lambda ���ʽ�Լ���������(functional operation)
		players.forEach((player) -> 
				System.out.println(player)

		);

	}
}
