package it.uniroma1.touchrecorder.data;

/**
 * Created by luca on 29/12/17.
 */

public enum Handwriting {
    ITALIC("ITALIC"), BLOCK_LETTERS("BLOCK LETTERS");

    private String value;

    Handwriting(String s) {
        value = s;
    }

    /**
     * @return the Enum representation for the given string.
     * @throws IllegalArgumentException if unknown string.
     */
    public static Handwriting toEnum(String s) throws IllegalArgumentException {

        for (Handwriting h : Handwriting.values()) {
            if (h.value.equalsIgnoreCase(s)) {
                return h;
            }
        }
        throw new IllegalArgumentException("unknown value:" + s);
    }

    @Override
    public String toString() {
        return value;
    }
}
