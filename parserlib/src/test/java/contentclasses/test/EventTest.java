/*
 * ******************************************************
 *                  * Copyright (C) 2015 retor  <retor@mail.ru>
 *                  *
 *                  * This file EventTest.java is part of  alfa 2.
 *                  *
 *                  * alfa 2 / parserlib can not be copied and/or distributed without the express
 *                  * permission
 * *****************************************************
 */

package contentclasses.test;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by retor on 23.04.2015.
 */
public class EventTest {
    Event event = new Event();
    long timer = System.currentTimeMillis();
    SimpleItem[] arrayc1 = {new SimpleItem("t1", 1), new SimpleItem("t1.2", 2)};
    SimpleItem[] arrayc2 = {new SimpleItem("t2", 5), new SimpleItem("t2.2", 7)};
    SimpleItem[] arraye = {new SimpleItem("te", 3), new SimpleItem("te.2", 23)};
    SimpleItem[] test = {new SimpleItem("test", 1), new SimpleItem("test2", 35)};

    @Before
    public void setUp() throws Exception {
        this.event.setName("test");
        this.event.setId(1);
        this.event.setState("good");
        this.event.setStartTime(timer);
        this.event.setCatId(54);
        this.event.setCom1("m1");
        this.event.setCom2("m2");
        this.event.setC1Miscs(new ArrayList<>(Arrays.asList(arrayc1)));
        this.event.setC2Miscs(new ArrayList<>(Arrays.asList(arrayc2)));
        this.event.setEventMiscs(new ArrayList<>(Arrays.asList(arraye)));
    }

    @Test
    public void testGetC1Miscs() throws Exception {
        assertNotNull(event.getC1Miscs());
        assertArrayEquals(event.getC1Miscs().toArray(), arrayc1);
    }

    @Test
    public void testSetC1Miscs() throws Exception {
        event.setC1Miscs(new ArrayList<>(Arrays.asList(test)));
        assertArrayEquals(event.getC1Miscs().toArray(), test);
        assertEquals(event.getC1Miscs().get(0), test[0]);
    }

    @Test
    public void testGetC2Miscs() throws Exception {
        assertNotNull(event.getC2Miscs());
        assertArrayEquals(event.getC2Miscs().toArray(), arrayc2);
    }

    @Test
    public void testSetC2Miscs() throws Exception {
        event.setC2Miscs(new ArrayList<>(Arrays.asList(test)));
        assertArrayEquals(event.getC2Miscs().toArray(), test);
        assertEquals(event.getC2Miscs().get(0), test[0]);
    }

    @Test
    public void testGetCatId() throws Exception {
        assertNotNull(event.getCatId());
        assertTrue(event.getCatId()>0);
    }

    @Test
    public void testSetCatId() throws Exception {
        event.setCatId(5);
        assertEquals(event.getCatId(), 5);
    }

    @Test
    public void testGetCom1() throws Exception {
        assertNotNull(event.getCom1());
    }

    @Test
    public void testSetCom1() throws Exception {
        event.setCom1("com1");
        assertNotNull(event.getCom1());
        assertEquals(event.getCom1(), "com1");
    }

    @Test
    public void testGetCom2() throws Exception {
        assertNotNull(event.getCom2());
    }

    @Test
    public void testSetCom2() throws Exception {
        event.setCom1("com2");
        assertNotNull(event.getCom1());
        assertEquals(event.getCom1(), "com2");
    }

}