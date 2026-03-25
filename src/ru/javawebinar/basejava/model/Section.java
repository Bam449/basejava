package ru.javawebinar.basejava.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import java.io.Serializable;

@XmlSeeAlso({TextSection.class, ListSection.class, OrganizationSection.class})
@XmlAccessorType(XmlAccessType.FIELD)
abstract public class Section implements Serializable {
}
