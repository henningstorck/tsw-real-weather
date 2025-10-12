package com.henningstorck.tswrealweather;

import com.henningstorck.tswrealweather.tsw.TswApi;
import com.henningstorck.tswrealweather.tsw.TswCoordinates;
import com.henningstorck.tswrealweather.tsw.TswTemperature;
import com.henningstorck.tswrealweather.tsw.exceptions.NoApiKeyException;
import com.henningstorck.tswrealweather.tsw.exceptions.NoGameException;
import com.henningstorck.tswrealweather.tsw.exceptions.NoSessionException;
import com.henningstorck.tswrealweather.weather.Weather;
import com.henningstorck.tswrealweather.weather.WeatherApi;
import com.henningstorck.tswrealweather.weather.openmeteo.OpenMeteoWeatherApi;

import java.time.Duration;
import java.time.Instant;

public class TswRealWeather {
	public static final Duration POLLING_RATE = Duration.ofSeconds(5);
	public static final Duration WEATHER_UPDATE_RATE = Duration.ofMinutes(5);

	private final TswApi tswApi;
	private final WeatherApi weatherApi;

	private Instant lastWeatherUpdate = null;

	static void main() {
		new TswRealWeather().loop();
	}

	public TswRealWeather() {
		this.tswApi = new TswApi();
		this.weatherApi = new OpenMeteoWeatherApi();
	}

	private void loop() {
		try {
			while (true) {
				tick();
				Thread.sleep(POLLING_RATE);
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	private void tick() {
		try {
			TswTemperature tswTemperature = tswApi.getTemperature();
			TswCoordinates tswCoordinates = tswApi.getCoordinates();

			if (tswTemperature.temperatureOverridden() == 1 &&
				lastWeatherUpdate != null && lastWeatherUpdate.plus(WEATHER_UPDATE_RATE).isAfter(Instant.now())) {
				return;
			}

			Weather weather = weatherApi.getWeather(tswCoordinates);

			System.out.printf("Updating weather. Temperature is %.1f°C, cloudiness is %.0f%%, wetness is %.0f%%, precipitation is %.0f%%, fog density is %.0f%%.%n",
				weather.temperature(),
				weather.cloudiness() * 100,
				weather.wetness() * 100,
				weather.precipitation() * 100,
				weather.fogDensity() * 100);

			tswApi.setTemperature(weather.temperature());
			tswApi.setCloudiness(weather.cloudiness());
			tswApi.setWetness(weather.wetness());
			tswApi.setPrecipitation(weather.precipitation());
			tswApi.setFogDensity(weather.fogDensity());

			lastWeatherUpdate = Instant.now();
		} catch (NoApiKeyException e) {
			System.out.println(e.getMessage());
		} catch (NoGameException e) {
			System.out.println("Waiting for game...");
		} catch (NoSessionException e) {
			System.out.println("Waiting for session...");
		}
	}
}
