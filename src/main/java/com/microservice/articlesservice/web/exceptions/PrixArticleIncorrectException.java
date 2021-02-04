package com.microservice.articlesservice.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class PrixArticleIncorrectException extends RuntimeException {

	public PrixArticleIncorrectException(String s) {
		super(s);
	}
}
