/*
 * ******************************************************
 *                  * Copyright (C) 2015 retor  <retor@mail.ru>
 *                  *
 *                  * This file SimpleItem.java is part of  alfa 2.
 *                  *
 *                  * alfa 2 / parserlib can not be copied and/or distributed without the express
 *                  * permission
 * *****************************************************
 */

package contentclasses.test;

/**
 * Created by retor on 23.04.2015.
 */
public class SimpleItem {
    private String name;
    private float value;

    public SimpleItem() {
    }

    public SimpleItem(String name, float value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimpleItem)) return false;

        SimpleItem that = (SimpleItem) o;

        if (Float.compare(that.getValue(), getValue()) != 0) return false;
        return !(getName() != null ? !getName().equals(that.getName()) : that.getName() != null);

    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getValue() != +0.0f ? Float.floatToIntBits(getValue()) : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SimpleItem{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
