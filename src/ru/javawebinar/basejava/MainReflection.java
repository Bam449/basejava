package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.Resume;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainReflection {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Resume r = new Resume("fullName1");
        Method method = r.getClass().getMethod("toString");
        System.out.println(method.invoke(r));
        int[] i = {7, 4, 8, 3, 1, 7, 5, 6, 6, 2, 4, 3};
        System.out.println(minValue(i));
        System.out.println(oddOrEven(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 10)));
    }

    public static int minValue(int[] values) {
        return Integer.parseInt(Arrays.stream(values)
                .distinct()
                .sorted()
                .mapToObj(Integer::toString)
                .collect(Collectors.joining()));
    }

    public static List<Integer> oddOrEven(List<Integer> integers) {
        int sum = integers.stream()
                .mapToInt(Integer::intValue)
                .sum() % 2;
        return integers.stream().filter(integer -> integer % 2 == sum).collect(Collectors.toList());
    }

}
