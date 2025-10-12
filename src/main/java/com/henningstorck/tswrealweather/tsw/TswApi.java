package com.henningstorck.tswrealweather.tsw;

import com.henningstorck.tswrealweather.tsw.exceptions.NoApiKeyException;
import com.henningstorck.tswrealweather.tsw.exceptions.NoGameException;
import com.henningstorck.tswrealweather.tsw.exceptions.NoSessionException;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TswApi {
	public static final int MAX_ATTEMPTS = 3;

	private String apiKey;

	public TswTemperature getTemperature() throws NoApiKeyException, NoGameException, NoSessionException {
		return getValue("WeatherManager.Temperature", TswTemperature.class, 1);
	}

	public TswCoordinates getCoordinates() throws NoApiKeyException, NoGameException, NoSessionException {
		return getValue("CurrentFormation/0.LatLon", TswCoordinates.class, 1);
	}

	public void setTemperature(double temperature) throws NoApiKeyException, NoGameException {
		setValue("WeatherManager.Temperature", temperature, 1);
	}

	public void setCloudiness(double cloudiness) throws NoApiKeyException, NoGameException {
		setValue("WeatherManager.Cloudiness", cloudiness, 1);
	}

	public void setWetness(double wetness) throws NoApiKeyException, NoGameException {
		setValue("WeatherManager.Wetness", wetness, 1);
	}

	public void setPrecipitation(double precipitation) throws NoApiKeyException, NoGameException {
		setValue("WeatherManager.Precipitation", precipitation, 1);
	}

	public void setFogDensity(double fogDensity) throws NoApiKeyException, NoGameException {
		setValue("WeatherManager.FogDensity", fogDensity, 1);
	}

	private <T> T getValue(String key, Class<T> clazz, int attempt) throws NoApiKeyException, NoGameException, NoSessionException {
		ObjectMapper objectMapper = new ObjectMapper();

		try (HttpClient httpClient = HttpClient.newHttpClient()) {
			HttpRequest request = HttpRequest.newBuilder(new URI("http://localhost:31270/get/%s".formatted(key)))
				.header("DTGCommKey", getApiKey())
				.GET()
				.build();

			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

			JavaType javaType = objectMapper.getTypeFactory().constructParametricType(TswApiResult.class, clazz);
			TswApiResult<T> tswApiResult = objectMapper.readValue(response.body(), javaType);

			T values = tswApiResult.values();

			if (values == null) {
				throw new NoSessionException();
			}

			return values;
		} catch (ConnectException e) {
			throw new NoGameException();
		} catch (URISyntaxException | InterruptedException | IOException e) {
			if (attempt < MAX_ATTEMPTS) {
				return getValue(key, clazz, attempt + 1);
			}

			throw new RuntimeException(e);
		}
	}

	private void setValue(String key, Object value, int attempt) throws NoApiKeyException, NoGameException {
		try (HttpClient httpClient = HttpClient.newHttpClient()) {
			HttpRequest request = HttpRequest.newBuilder(new URI("http://localhost:31270/set/%s?value=%s".formatted(key, value)))
				.header("DTGCommKey", getApiKey())
				.method("PATCH", HttpRequest.BodyPublishers.noBody())
				.build();

			httpClient.send(request, HttpResponse.BodyHandlers.discarding());
		} catch (ConnectException e) {
			throw new NoGameException();
		} catch (URISyntaxException | InterruptedException | IOException e) {
			if (attempt < MAX_ATTEMPTS) {
				setValue(key, value, attempt + 1);
				return;
			}

			throw new RuntimeException(e);
		}
	}

	private String getApiKey() throws NoApiKeyException {
		if (this.apiKey == null) {
			this.apiKey = new TswApiKeyLoader().getApiKey();
		}

		return this.apiKey;
	}
}
