package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.lang.NonNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SeriesInfo(@JsonAlias("Title") String title, @JsonAlias("totalSeasons") Integer totalSeasons,
                         @JsonAlias("imdbRating") String rating, @JsonAlias("Genre") String genre,
                         @JsonAlias("Plot") String plot, @JsonAlias("Poster") String poster,
                         @JsonAlias("Actors") String actors) {

    @Override
    @NonNull
    public String toString(){
        return """
                Title: %s
                Genre: %s
                Plot: %s
                Rating: %s
                Featured actors: %s
                Total seasons: %d
                Poster: %s
                """.formatted(title, genre, plot,
                rating, actors,totalSeasons, poster);
    }
}
