package jdk2010.bitset;

import java.util.BitSet;

public class BitSetTest {
    public static void main(String[] args) {
        BitSet bitSet = new BitSet(100);
        bitSet.set(1, true);
        bitSet.set(3, true);
        bitSet.set(6, true);
        bitSet.set(10000000, true);
        for (int i = 0; i < bitSet.size(); i++) {
            boolean b = bitSet.get(i);
            if (b) {
                System.out.println(i);
            }
        }
    }
}
