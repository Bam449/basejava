import java.util.Arrays;

public class ArrayStorage {
    public static final int STORAGE_SIZE = 10000;
    private final Resume[] storage = new Resume[STORAGE_SIZE];
    private int size;

    public void save(Resume r) {
        if (size == STORAGE_SIZE) {
            System.out.println("Error");
            return;
        }
        int searchKey = getSearchKey(r.getUuid());
        if (searchKey >= 0) {
            System.out.println("Error");
            return;
        }
        storage[size] = r;
        size++;
    }

    public Resume get(String uuid) {
        int searchKey = getSearchKey(uuid);
        if (searchKey < 0) {
            System.out.println("Error");
            return null;
        }
        return storage[searchKey];
    }

    public void update(Resume resume) {
        int searchKey = getSearchKey(resume.getUuid());
        if (searchKey < 0) {
            System.out.println("Error");
            return;
        }
        storage[searchKey] = resume;
    }

    public void delete(String uuid) {
        int searchKey = getSearchKey(uuid);
        if (searchKey < 0) {
            System.out.println("Error");
            return;
        }
        storage[searchKey] = storage[size - 1];
        storage[size - 1] = null;
        size--;
    }

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    public int size() {
        return size;
    }

    private int getSearchKey(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}
