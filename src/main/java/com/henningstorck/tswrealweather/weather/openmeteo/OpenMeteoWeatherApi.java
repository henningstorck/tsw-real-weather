package com.henningstorck.tswrealweather.weather.openmeteo;

import com.henningstorck.tswrealweather.tsw.TswCoordinates;
import com.henningstorck.tswrealweather.weather.Weather;
import com.henningstorck.tswrealweather.weather.WeatherApi;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class OpenMeteoWeatherApi implements WeatherApi {
	@Override
	public Weather getWeather(TswCoordinates tswCoordinates) {
		ObjectMapper objectMapper = new ObjectMapper();

		try (HttpClient httpClient = HttpClient.newHttpClient()) {
			HttpRequest request = HttpRequest.newBuilder(new URI("https://api.open-meteo.com/v1/forecast?latitude=%s&longitude=%s&current=temperature_2m,cloud_cover,soil_moisture_0_to_1cm,precipitation,visibility"
					.formatted(tswCoordinates.lat(), tswCoordinates.lon())))
				.GET()
				.build();

			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			OpenMeteoWeatherApiResult openMeteoWeatherApiResult = objectMapper.readValue(response.body(), OpenMeteoWeatherApiResult.class);

			return new Weather(
				openMeteoWeatherApiResult.current().temperature(),
				openMeteoWeatherApiResult.current().cloudCover() / 100,
				openMeteoWeatherApiResult.current().soilMoisture(),
				openMeteoWeatherApiResult.current().precipitation(),
				clamp(m(0, 1, 5000, 0) * openMeteoWeatherApiResult.current().visibility() + 1, 0, 1)
			);
		} catch (URISyntaxException | InterruptedException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	private double m(double x1, double y1, double x2, double y2) {
		return (y2 - y1) / (x2 - x1);
	}

	private double clamp(double value, double min, double max) {
		return Math.max(min, Math.min(max, value));
	}
}
