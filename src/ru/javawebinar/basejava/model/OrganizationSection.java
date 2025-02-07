package ru.javawebinar.basejava.model;

import java.util.Arrays;
import java.util.List;

public class OrganizationSection extends Section{

    private List<Organization> organizations;

    public OrganizationSection(List<Organization> organizations) {
        this.organizations = organizations;
    }

    public OrganizationSection(Organization ... organizations) {
        this(Arrays.asList(organizations));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        OrganizationSection that = (OrganizationSection) o;
        return organizations.equals(that.organizations);
    }

    @Override
    public int hashCode() {
        return organizations.hashCode();
    }
}
