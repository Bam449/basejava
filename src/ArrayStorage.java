import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];
    private int size;

    public void save(Resume r) {
        int key = getKey(r.getUuid());
        if (key > 0) return;
        storage[size] = r;
        size++;
    }

    public Resume get(String uuid) {
        int key = getKey(uuid);
        if (key < 0) return null;
        return storage[key];
    }

    public void delete(String uuid) {
        int key = getKey(uuid);
        if (key < 0) return;
        storage[key] = storage[size - 1];
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

    int size() {
        return size;
    }

    private int getKey(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}
