package com.henningstorck.tswrealweather.tsw;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TswTemperature(
	@JsonProperty("Temperature") double temperature,
	@JsonProperty("TemperatureOveridden") int temperatureOverridden
) {
}
