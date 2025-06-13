package br.com.alura.screenmatch;

import br.com.alura.screenmatch.model.EpisodeInfo;
import br.com.alura.screenmatch.model.SeasonInfo;
import br.com.alura.screenmatch.model.SeriesInfo;
import br.com.alura.screenmatch.service.APIConsumption;
import br.com.alura.screenmatch.service.DataConverter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenMatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenMatchApplication.class, args);
	}

	@Override
	public void run(String... args){
		var apiConsumption = new APIConsumption();
		var json = apiConsumption.getData("http://www.omdbapi.com/?apikey=20731567&t=Breaking+Bad");
		System.out.println(json);

		System.out.println("\n-------------\n");

		DataConverter converter = new DataConverter();
		SeriesInfo seriesInfo = converter.getData(json, SeriesInfo.class);
		System.out.println(seriesInfo);

		System.out.println("\n-------------\n");

		json = apiConsumption.getData("http://www.omdbapi.com/?apikey=20731567&t=Breaking+Bad&Season=1&Episode=4");
		EpisodeInfo episodeInfo = converter.getData(json, EpisodeInfo.class);
		System.out.println(episodeInfo);

		System.out.println("\n-------------\n");

		List<SeasonInfo> seasonList = new ArrayList<>();

		for(int i = 1; i <= seriesInfo.totalSeasons(); i++){
			json = apiConsumption.getData("http://www.omdbapi.com/?apikey=20731567&t=Breaking+Bad&Season=" + i);
			SeasonInfo seasonInfo = converter.getData(json, SeasonInfo.class);
			seasonList.add(seasonInfo);
		}

			seasonList.forEach(System.out::println);
	}
}
