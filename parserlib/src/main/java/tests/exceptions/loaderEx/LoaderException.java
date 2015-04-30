/*
 * ******************************************************
 *                  * Copyright (C) 2015 retor  <retor@mail.ru>
 *                  *
 *                  * This file LoaderException.java is part of  alfa 2.
 *                  *
 *                  * alfa 2 / parserlib can not be copied and/or distributed without the express
 *                  * permission
 * *****************************************************
 */

package tests.exceptions.loaderEx;

/**
 * Created by retor on 29.04.2015.
 */
public class LoaderException extends Exception {
    public LoaderException(Throwable cause) {
        super(cause);
    }

    public LoaderException(String message) {
        super(message);
    }

    public LoaderException(String message, Throwable cause) {
        super(message, cause);
    }
}
