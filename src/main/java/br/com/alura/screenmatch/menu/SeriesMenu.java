package br.com.alura.screenmatch.menu;

import br.com.alura.screenmatch.model.Episode;
import br.com.alura.screenmatch.model.EpisodeInfo;
import br.com.alura.screenmatch.model.SeasonInfo;
import br.com.alura.screenmatch.model.SeriesInfo;
import br.com.alura.screenmatch.service.APIConsumption;
import br.com.alura.screenmatch.service.DataConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class SeriesMenu {
    private Scanner sc = new Scanner(System.in);
    private APIConsumption consumption = new APIConsumption();
    private DataConverter converter = new DataConverter();

    private final String URL = "http://www.omdbapi.com/";
    private final String API_KEY = "?apikey=20731567&t=";

    public void showMenu(){
        System.out.print("Enter the series name to search: ");
        var seriesName = sc.nextLine();
        var json = consumption.getData(URL + API_KEY + seriesName.replace(" ", "+"));

        SeriesInfo seriesInfo = converter.getData(json, SeriesInfo.class);

        System.out.println("\n" + seriesInfo);

        System.out.println();

        List<SeasonInfo> seasonList = new ArrayList<>();

        for(int i = 1; i <= seriesInfo.totalSeasons(); i++){
            json = consumption.getData(URL + API_KEY + seriesName.replace
                    (" ", "+") + "&season=" + i);
            SeasonInfo seasonInfo = converter.getData(json, SeasonInfo.class);
            seasonList.add(seasonInfo);
        }

        seasonList.forEach(System.out::println);

        System.out.println();

        seasonList.forEach(s -> s.episodes().
                forEach(e -> System.out.println(e.title())));

        List<EpisodeInfo> episodeInfo = seasonList.stream()
                .flatMap(s -> s.episodes().stream())
                .collect(Collectors.toList());

        System.out.println("\nTop 5 episodes: ");
        episodeInfo.stream()
                .filter(e -> !e.rating().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(EpisodeInfo::rating).reversed())
                .limit(5)
                .forEach(System.out::println);

        List<Episode> episodes = seasonList.stream()
                .flatMap(s -> s.episodes().stream()
                        .map(e -> new Episode(s.season(), e))
                        ).collect(Collectors.toList());

        System.out.println();

        episodes.forEach(System.out::println);

        System.out.print("\nEnter the episode name: ");
        String titleSnippet = sc.nextLine();

        Optional<Episode> foundEpisode = episodes.stream()
                .filter(e -> e.getTitle().toUpperCase().contains(titleSnippet.toUpperCase()))
                .findFirst();

        if(foundEpisode.isPresent()){
            System.out.println("Episode found! " + foundEpisode.get());
        }else {
            System.out.println("Episode not found!");
        }

        System.out.print("\nStarting from which year do you want to view the episodes? ");
        int year = sc.nextInt();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate searchDate = LocalDate.of(year, 1, 1);

        episodes.stream().filter(e -> e.getReleased() != null && e.getReleased().isAfter(searchDate))
                .forEach(e -> System.out.println(
                        "Season: " + e.getSeason()
                        + ", title: " + e.getTitle()
                        + ", episode: " + e.getEpisodeNumber()
                        + ", release date: " + e.getReleased().format(dtf)
                ));

        System.out.println("Average per season: ");

        Map<Integer, Double> ratingPerSeason = episodes.stream()
                .filter(e -> e.getRating() != null)
                .collect(Collectors.groupingBy(Episode::getSeason,
                        Collectors.averagingDouble(Episode::getRating)));

        ratingPerSeason.forEach((season, avg) ->
                System.out.printf("Season %d: Average = %.2f\n", season, avg));
    }
}
