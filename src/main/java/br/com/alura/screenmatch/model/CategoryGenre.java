package br.com.alura.screenmatch.model;

public enum CategoryGenre {
    ACTION("Action", "Ação"),
    ROMANCE("Romance", "Romance"),
    COMEDY("Comedy", "Comédia"),
    DRAMA("Drama", "Drama"),
    CRIME("Crime", "Crime"),
    ANIMATION("Animation", "Animação"),;

    private String categoryOMDB;
    private String categoryPortuguese;

    CategoryGenre(String categoryOMDB, String categoryPortuguese){
        this.categoryOMDB = categoryOMDB;
        this.categoryPortuguese = categoryPortuguese;
    }

    public static CategoryGenre fromString(String text){
        for(CategoryGenre categoryGenre: CategoryGenre.values()){
            if(categoryGenre.categoryOMDB.equalsIgnoreCase(text)){
                return categoryGenre;
            }
        }
        throw new IllegalArgumentException("No category found for string: " + text);
    }

    public static CategoryGenre fromPortuguese(String text){
        for(CategoryGenre categoryGenre: CategoryGenre.values()){
            if(categoryGenre.categoryPortuguese.equalsIgnoreCase(text)){
                return categoryGenre;
            }
        }
        throw new IllegalArgumentException("No category found for string: " + text);
    }
}
