/**
 * Lab 7 - Character frequency counter backed by java.util.HashMap<Character, Integer>.
 *
 * BUILD-STAGE: all methods are implemented per the approved plan.
 *
 * Design decisions (documented):
 *   - Case-sensitive: 'A' and 'a' are distinct keys.
 *   - Every character is counted, including spaces and punctuation.
 *   - Nothing is silently filtered or normalized.
 *   - An empty string produces an empty map.
 *   - Null input throws NullPointerException.
 *   - The returned frequency map is a defensive copy; internal state is not exposed.
 *   - Public API is minimal: only count(String). No getCount() or mostFrequentChar()
 *     are required by the approved plan or tests.
 */
import java.util.HashMap;
import java.util.Map;

public class CharFrequencyCounter {

    /** Counts every character in text; returns a defensive copy of the frequency map. */
    public Map<Character, Integer> count(String text) {
        if (text == null) {
            throw new NullPointerException("text must not be null");
        }
        Map<Character, Integer> freq = new HashMap<>();
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            Integer current = freq.get(ch);
            freq.put(ch, current == null ? 1 : current + 1);
        }
        return new HashMap<>(freq);
    }
}
