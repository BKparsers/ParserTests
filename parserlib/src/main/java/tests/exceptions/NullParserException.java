/*
 * ******************************************************
 *                  * Copyright (C) 2015 retor  <retor@mail.ru>
 *                  *
 *                  * This file NullParserException.java is part of  alfa 2.
 *                  *
 *                  * alfa 2 / parserlib can not be copied and/or distributed without the express
 *                  * permission
 * *****************************************************
 */

package tests.exceptions;

/**
 * Created by retor on 28.04.2015.
 */
public class NullParserException extends Exception {
    public NullParserException(String message) {
        super(message);
    }

    public NullParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
