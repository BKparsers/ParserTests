/*
 * ******************************************************
 *                  * Copyright (C) 2015 retor  <retor@mail.ru>
 *                  *
 *                  * This file LoaderNotFoundException.java is part of  alfa 2.
 *                  *
 *                  * alfa 2 / parserlib can not be copied and/or distributed without the express
 *                  * permission
 * *****************************************************
 */

package tests.exceptions.loaderEx;

/**
 * Created by retor on 28.04.2015.
 */
public class LoaderNotFoundException extends LoaderException{
    public LoaderNotFoundException(Throwable cause) {
        super(cause);
    }

    public LoaderNotFoundException(String message) {
        super(message);
    }

    public LoaderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
