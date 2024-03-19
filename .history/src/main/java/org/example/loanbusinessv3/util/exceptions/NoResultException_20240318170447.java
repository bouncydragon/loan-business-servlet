package org.example.loanbusinessv3.util.exceptions;

public class NoResultException extends RuntimeException {

    private String title;

    public NoResultException() {
    }

    public NoResultException(String title) {
        this.title = title;
    }

    public static NoResultException withPaths(final String... paths) {
        final NoResultException nre = new NoResultException();
        return nre;
    }
    

    

}
