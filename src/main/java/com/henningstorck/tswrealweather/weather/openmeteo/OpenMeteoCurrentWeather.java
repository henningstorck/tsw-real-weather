package com.henningstorck.tswrealweather.weather.openmeteo;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OpenMeteoCurrentWeather(
	@JsonProperty("temperature_2m") double temperature,
	@JsonProperty("cloud_cover") double cloudCover,
	@JsonProperty("soil_moisture_0_to_1cm") double soilMoisture,
	double precipitation,
	double visibility
) {
}
