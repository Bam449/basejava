package ru.javawebinar.basejava.storage.junit4;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({

        ArrayStorageTest.class,
        SortedArrayStorageTest.class,
        ListStorageTest.class
        })
public class AllTest {
}
