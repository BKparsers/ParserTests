/*
 * ******************************************************
 *                  * Copyright (C) 2015 retor  <retor@mail.ru>
 *                  *
 *                  * This file ParserException.java is part of  alfa 2.
 *                  *
 *                  * alfa 2 / parserlib can not be copied and/or distributed without the express
 *                  * permission
 * *****************************************************
 */

package tests.exceptions.parserEx;

/**
 * Created by retor on 29.04.2015.
 */
public class ParserException extends Exception {
    public ParserException(Throwable cause) {
        super(cause);
    }

    public ParserException(String message) {
        super(message);
    }

    public ParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
