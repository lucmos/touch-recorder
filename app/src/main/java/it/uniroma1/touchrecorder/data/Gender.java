package it.uniroma1.touchrecorder.data;

import com.google.gson.GsonBuilder;

/**
 * Created by luca on 29/12/17.
 */

public enum Gender {
    MALE("MALE"), FEMALE("FEMALE");

    private String value;

    Gender(String s) {
        value = s;
    }

    /**
     * @return the Enum representation for the given string.
     * @throws IllegalArgumentException if unknown string.
     */
    public static Gender toEnum(String s) throws IllegalArgumentException {

        for (Gender h : Gender.values()) {
            if (h.value.equalsIgnoreCase(s)) {
                return h;
            }
        }
        throw new IllegalArgumentException("unknown value:" + s);
    }
    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }

    }
