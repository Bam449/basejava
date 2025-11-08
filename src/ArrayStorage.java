/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private final Resume[] storage = new Resume[10000];

    public void clear() {
    }

    public void save(Resume r) {
    }

    public Resume get(String uuid) {
        return null;
    }

    public void delete(String uuid) {
    }

    public Resume[] getAll() {
        return new Resume[0];
    }

    public int size() {
        return 0;
    }
}
