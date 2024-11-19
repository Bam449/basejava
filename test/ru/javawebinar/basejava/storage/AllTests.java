package ru.javawebinar.basejava.storage;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({

        // package1
        ArrayStorageTest.class,
        SortedArrayStorageTest.class,
        })

public class AllTests {
}
