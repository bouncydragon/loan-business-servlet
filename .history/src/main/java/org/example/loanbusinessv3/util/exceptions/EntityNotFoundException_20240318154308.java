package org.example.loanbusinessv3.util.exceptions;

public class EntityNotFoundException extends RuntimeException {

    private String title;

    public EntityNotFoundException() {
    }

    public EntityNotFoundException(String title) {
        this.title = title;
    }

    public static EntityNotFoundException withPaths(final String... paths) {
        final EntityNotFoundException nfExp = new EntityNotFoundException();
        System.out.println(paths);
        return nfExp;
    }
    

    

}
