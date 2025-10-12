package com.henningstorck.tswrealweather.tsw.exceptions;

public class NoApiKeyException extends Exception {
	public NoApiKeyException(String message) {
		super(message);
	}

	public NoApiKeyException(String message, Throwable cause) {
		super(message, cause);
	}
}
