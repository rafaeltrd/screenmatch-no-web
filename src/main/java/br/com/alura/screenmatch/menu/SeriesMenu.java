package br.com.alura.screenmatch.menu;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.repository.SeriesRepository;
import br.com.alura.screenmatch.service.APIConsumption;
import br.com.alura.screenmatch.service.DataConverter;

import java.util.*;
import java.util.stream.Collectors;

public class SeriesMenu {
    private Scanner sc = new Scanner(System.in);
    private APIConsumption consumption = new APIConsumption();
    private DataConverter converter = new DataConverter();

    private final String ADDRESS = "http://www.omdbapi.com/";
    private final String API_KEY = System.getenv("OMDB_API_KEY");
    private List<SeriesInfo> seriesInfos = new ArrayList<>();

    private SeriesRepository repository;

    private List<Series> series = new ArrayList<>();

    public SeriesMenu(SeriesRepository repository) {
        this.repository = repository;
    }

    public void showMenu(){
        int choice = -1;
        while (choice != 0) {
            System.out.println("""
                    \n1 - Search series
                    2 - Search episodes
                    3 - List searched series
                    4 - Find series by title
                    5 - Find series by actor
                    6 - Top 5 series
                    7 - Find series by genre
                    
                    0 - Quit""");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    searchSeriesWeb();
                    break;

                case 2:
                    getEpisodesBySeries();
                    break;

                case 3:
                    listSearchedSeries();
                    break;

                case 4:
                    findSeriesByTitle();
                    break;

                case 5:
                    findSeriesByActor();
                    break;

                case 6:
                    findTop5Series();
                    break;

                case 7:
                    findSeriesByGenre();
                    break;

                case 0:
                    break;

                default:
                    System.out.println("\nInvalid option!");
                    break;
            }
        }
    }

    private void searchSeriesWeb(){
        SeriesInfo info = getSeriesInfo();
        //seriesInfos.add(info);
        Series series = new Series(info);
        repository.save(series);
        System.out.println(info);
    }

    private SeriesInfo getSeriesInfo(){
        System.out.print("Enter the series name to search: ");
        var seriesName = sc.nextLine();
        var json = consumption.getData(ADDRESS + API_KEY + seriesName.replace(" ", "+"));
        return converter.getData(json, SeriesInfo.class);
    }

    private void getEpisodesBySeries(){
        listSearchedSeries();
        //SeriesInfo seriesInfo = getSeriesInfo();
        System.out.print("Choose a series by name: ");
        String seriesName = sc.nextLine();

        Optional<Series> seriesOptional = repository.findByTitleContainingIgnoreCase(seriesName);

        if(seriesOptional.isPresent()){
            var seriesFound = seriesOptional.get();
            List<SeasonInfo> seasons = new ArrayList<>();

            for(int i = 1; i <= seriesFound.getTotalSeasons(); i++){
                var json = consumption.getData(ADDRESS + API_KEY + seriesFound.getTitle().replace
                        (" ", "+") + "&season=" + i);
                SeasonInfo seasonInfo = converter.getData(json, SeasonInfo.class);
                seasons.add(seasonInfo);
            }

            seasons.forEach(System.out::println);
            List<Episode> episodes = seasons.stream()
                    .flatMap(s -> s.episodes().stream()
                            .map(e -> new Episode(s.season(), e)))
                    .collect(Collectors.toList());

            seriesFound.setEpisodes(episodes);
            repository.save(seriesFound);
        }else {
            System.out.println("Series not found!");
        }
    }

    private void listSearchedSeries(){
        series = repository.findAll();
        series.stream().sorted(Comparator.comparing(Series::getGenre))
                .forEach(System.out::println);
    }

    private void findSeriesByTitle() {
        System.out.print("Enter series title: ");
        var seriesName = sc.nextLine();
        Optional<Series> seriesFound = repository.findByTitleContainingIgnoreCase(seriesName);

        if(seriesFound.isPresent()){
            System.out.println("Series data: " + seriesFound.get());
        } else {
            System.out.println("Series not found!");
        }
    }

    private void findSeriesByActor() {
        System.out.print("Enter the actor's name: ");
        var actorName = sc.nextLine();

        System.out.print("Ratings starting from what value?");
        var rating = sc.nextDouble();

        List<Series> seriesList = repository.findByActorsContainingIgnoreCaseAndRatingGreaterThanEqual(actorName, rating);

        if(seriesList.isEmpty()){
            System.out.println("Actor not found!");

        } else {
            System.out.println("\nSeries that " + actorName + " worked on: ");
            seriesList.forEach(s -> System.out.println(s.getTitle() + " | " + s.getGenre() + " | rating: " + s.getRating()));
        }
    }

    private void findTop5Series() {
        List<Series> topSeries = repository.findTop5ByOrderByRatingDesc();

        topSeries.forEach(s -> System.out.println(s.getTitle() + " | " + s.getGenre() + " | rating: " + s.getRating()));
    }

    private void findSeriesByGenre() {
        System.out.println("""
            Which language would you like to search for:
            1 - English
            2 - Portuguese""");

        var choice = sc.nextInt();

        System.out.print("Enter genre: ");
        sc.nextLine();
        var genre = sc.nextLine();

        switch (choice) {
            case 1:
                CategoryGenre categoryGenre = CategoryGenre.fromString(genre);
                List<Series> seriesByGenre = repository.findSeriesByGenre(categoryGenre);
                seriesByGenre.forEach(System.out::println);

                break;

            case 2:
                CategoryGenre categoryGenrePortuguese = CategoryGenre.fromPortuguese(genre);
                List<Series> seriesByGenrePortuguese = repository.findSeriesByGenre(categoryGenrePortuguese);
                seriesByGenrePortuguese.forEach(System.out::println);

                break;

            default:
                System.out.println("Invalid option!");
        }
    }
}
