package br.com.alura.screenmatch;

import br.com.alura.screenmatch.menu.SeriesMenu;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenMatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenMatchApplication.class, args);
	}

	@Override
	public void run(String... args){
		SeriesMenu seriesMenu = new SeriesMenu();
		seriesMenu.showMenu();
	}
}
