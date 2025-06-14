package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EpisodeInfo(@JsonAlias("Episode") Integer episode,
                          @JsonAlias("Title") String title, @JsonAlias("Released") String released) {
}
