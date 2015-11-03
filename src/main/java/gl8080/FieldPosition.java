package gl8080;

public class FieldPosition {

    public final int left;
    public final int length;

    public FieldPosition(int left) {
        this.left = left;
        this.length = -1;
    }

    public FieldPosition(int left, int length) {
        this.left = left;
        this.length = length;
    }
}
