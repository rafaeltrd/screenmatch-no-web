package br.com.alura.screenmatch.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

@Entity
@Table(name = "series")
public class Series {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String title;
    private Integer totalSeasons;
    private Double rating;

    @Enumerated(EnumType.STRING)
    private CategoryGenre genre;
    private String plot;
    private String poster;
    private String actors;

    @Transient
    private List<Episode> episodes = new ArrayList<>();

    public Series(SeriesInfo seriesInfo){
        this.title = seriesInfo.title();
        this.totalSeasons = seriesInfo.totalSeasons();
        this.rating = OptionalDouble.of(Double.parseDouble(seriesInfo.rating())).orElse(0.0);
        this.genre = CategoryGenre.fromString(seriesInfo.genre().split(",")[0].trim());
        this.plot = seriesInfo.plot();
        this.poster = seriesInfo.poster();
        this.actors = seriesInfo.actors();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTotalSeasons() {
        return totalSeasons;
    }

    public void setTotalSeasons(Integer totalSeasons) {
        this.totalSeasons = totalSeasons;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public CategoryGenre getGenre() {
        return genre;
    }

    public void setGenre(CategoryGenre genre) {
        this.genre = genre;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    @Override
    public String toString() {
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
