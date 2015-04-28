/*
 * ******************************************************
 *                  * Copyright (C) 2015 retor  <retor@mail.ru>
 *                  *
 *                  * This file ParserNotFoundException.java is part of  alfa 2.
 *                  *
 *                  * alfa 2 / parserlib can not be copied and/or distributed without the express
 *                  * permission
 * *****************************************************
 */

package tests.exceptions;

/**
 * Created by retor on 28.04.2015.
 */
public class ParserNotFoundException extends Exception {
    public ParserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParserNotFoundException(String message) {
        super(message);
    }

    public ParserNotFoundException(Throwable cause) {
        super(cause);
    }
}
