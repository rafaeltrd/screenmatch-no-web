package br.com.alura.screenmatch.menu;

import br.com.alura.screenmatch.model.SeasonInfo;
import br.com.alura.screenmatch.model.SeriesInfo;
import br.com.alura.screenmatch.service.APIConsumption;
import br.com.alura.screenmatch.service.DataConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

        System.out.println("\n-------------\n");

        List<SeasonInfo> seasonList = new ArrayList<>();

        for(int i = 1; i <= seriesInfo.totalSeasons(); i++){
            json = consumption.getData(URL + API_KEY + seriesName.replace
                    (" ", "+") + "&season=" + i);
            SeasonInfo seasonInfo = converter.getData(json, SeasonInfo.class);
            seasonList.add(seasonInfo);
        }

        seasonList.forEach(System.out::println);
    }
}
