package com.henningstorck.tswrealweather.weather;

import com.henningstorck.tswrealweather.tsw.TswCoordinates;

public interface WeatherApi {
	Weather getWeather(TswCoordinates tswCoordinates);
}
