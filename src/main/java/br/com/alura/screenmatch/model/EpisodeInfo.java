package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EpisodeInfo(@JsonAlias("Title") String title, @JsonAlias("Season") Integer season,
                          @JsonAlias Integer episode, @JsonAlias("Runtime") String runtime) {
}
