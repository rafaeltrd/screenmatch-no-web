package br.com.alura.screenmatch.model;

public enum CategoryGenre {
    ACTION("Action"),
    ROMANCE("Romance"),
    COMEDY("Comedy"),
    DRAMA("Drama"),
    CRIME("Crime"),
    ANIMATION("Animation"),;

    private String categoryOMDB;

    CategoryGenre(String categoryOMDB){
        this.categoryOMDB = categoryOMDB;
    }

    public static CategoryGenre fromString(String text){
        for(CategoryGenre categoryGenre: CategoryGenre.values()){
            if(categoryGenre.categoryOMDB.equalsIgnoreCase(text)){
                return categoryGenre;
            }
        }
        throw new IllegalArgumentException("No category found for string: " + text);
    }
}
