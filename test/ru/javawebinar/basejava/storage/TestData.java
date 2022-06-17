package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.model.ContactType;
import ru.javawebinar.basejava.model.ListSection;
import ru.javawebinar.basejava.model.Organization;
import ru.javawebinar.basejava.model.OrganizationSection;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.model.SectionType;
import ru.javawebinar.basejava.model.TextSection;

import java.io.File;
import java.time.Month;

public class TestData {

    protected static final File STORAGE_DIR = Config.getINSTANCE().getStorageDir();
    public static Resume R1;
    public static Resume R2;
    public static Resume R3;
    public static Resume R4;

    static {
        R1 = new Resume("Name1");
        R2 = new Resume("Name2");
        R3 = new Resume("Name3");
        R4 = new Resume("Name4");

        R1.addContact(ContactType.MAIL, "mail1@ya.ru");
        R1.addContact(ContactType.PHONE, "+7 904 900 04 00");
        R1.addSection(SectionType.OBJECTIVE, new TextSection("Позиция"));
        R1.addSection(SectionType.PERSONAL, new TextSection("Личные качества"));
        R1.addSection(SectionType.ACHIEVEMENT, new ListSection("Достижение 1", "Достижение 2", "Достижение 3"));
        R1.addSection(SectionType.QUALIFICATIONS, new ListSection("Java", "SQL", "JavaScript"));
        R1.addSection(SectionType.EXPERIENCE,
                new OrganizationSection(
                        new Organization("Работа 1", "https://hh.ru",
                                new Organization.Position(2006, Month.JANUARY, "сисадмин 1", "описание 1"),
                                new Organization.Position(2001, Month.MARCH, 2005, Month.JANUARY, "сисадмин 2", "описание 2")),
                        new Organization("Работа2", "https://rabota.ru",
                                new Organization.Position(2015, Month.JANUARY, "директор", "пинаю балду"))));
        R1.addSection(SectionType.EDUCATION,
                new OrganizationSection(
                        new Organization("Институт 1", "https://bmstu.ru",
                                new Organization.Position(1996, Month.JANUARY, 2000, Month.DECEMBER, "aspirant", "IT 1"),
                                new Organization.Position(2001, Month.MARCH, 2005, Month.JANUARY, "student", "IT facultet")),
                        new Organization("Институт 2", "https://mipt.ru/")));

        R2.addContact(ContactType.SKYPE, "skype2");
        R2.addContact(ContactType.PHONE, "22222");
    }

}
