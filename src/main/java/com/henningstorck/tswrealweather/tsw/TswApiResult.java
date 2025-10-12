package com.henningstorck.tswrealweather.tsw;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TswApiResult<T>(
	@JsonProperty("Result") String result,
	@JsonProperty("Values") T values
) {
}
