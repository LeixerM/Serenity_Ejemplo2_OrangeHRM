package com.orangehrm.demo.models;

import java.security.SecureRandom;

/**
 * An employee created by the suite. Names and ids are unique per run, so scenarios never
 * collide with each other or with data other people create on the shared public demo.
 */
public record Employee(String firstName, String middleName, String lastName, String employeeId) {

    private static final String LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static Employee unique() {
        return new Employee("Qa", "Auto", "Employee" + random(LETTERS, 8), random(DIGITS, 9));
    }

    public String fullName() {
        return firstName + " " + middleName + " " + lastName;
    }

    /** How the employee list prints the "First (& Middle) Name" column. */
    public String firstAndMiddleName() {
        return firstName + " " + middleName;
    }

    private static String random(String alphabet, int length) {
        StringBuilder value = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            value.append(alphabet.charAt(RANDOM.nextInt(alphabet.length())));
        }
        return value.toString();
    }
}
