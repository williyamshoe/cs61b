public class OffByN implements CharacterComparator {
    int offset;

    public OffByN(int N) {
        offset = N;
    }

    @Override
    public boolean equalChars(char x, char y) {
        return x - y == offset || y - x == offset;
    }

}
