package com.henningstorck.tswrealweather.tsw;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TswCoordinates(
	@JsonProperty("Lat") double lat,
	@JsonProperty("Lon") double lon
) {
}
