package br.com.alura.screenmatch.menu;

import br.com.alura.screenmatch.model.SeasonInfo;
import br.com.alura.screenmatch.model.Series;
import br.com.alura.screenmatch.model.SeriesInfo;
import br.com.alura.screenmatch.repository.SeriesRepository;
import br.com.alura.screenmatch.service.APIConsumption;
import br.com.alura.screenmatch.service.DataConverter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class SeriesMenu {
    private Scanner sc = new Scanner(System.in);
    private APIConsumption consumption = new APIConsumption();
    private DataConverter converter = new DataConverter();

    private final String ADDRESS = "http://www.omdbapi.com/";
    private final String API_KEY = "?apikey=20731567&t=";
    private List<SeriesInfo> seriesInfos = new ArrayList<>();

    private SeriesRepository repository;

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

                case 0:
                    break;

                default:
                    System.out.println("Invalid option!");
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
        System.out.println("Enter the series name to search: ");
        var seriesName = sc.nextLine();
        var json = consumption.getData(ADDRESS + API_KEY + seriesName.replace(" ", "+"));
        return converter.getData(json, SeriesInfo.class);
    }

    private void getEpisodesBySeries(){
        SeriesInfo seriesInfo = getSeriesInfo();
        List<SeasonInfo> seasons = new ArrayList<>();

        for(int i = 1; i <= seriesInfo.totalSeasons(); i++){
            var json = consumption.getData(ADDRESS + API_KEY + seriesInfo.title().replace
                    (" ", "+") + "&season=" + i);
            SeasonInfo seasonInfo = converter.getData(json, SeasonInfo.class);
            seasons.add(seasonInfo);
        }
        seasons.forEach(System.out::println);
    }

    private void listSearchedSeries(){
        List<Series> series = repository.findAll();
        series.stream().sorted(Comparator.comparing(Series::getGenre))
                .forEach(System.out::println);
    }
}
