package thedrake;

import java.io.PrintWriter;

public enum PlayingSide implements JSONSerializable {
    ORANGE, BLUE;

    @Override
    public void toJSON(PrintWriter writer) {
        writer.printf("\"");
        writer.printf(this.toString());
        writer.printf("\"");
    }
}
