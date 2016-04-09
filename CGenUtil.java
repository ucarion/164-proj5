import java.io.PrintStream;
import java.util.List;

public class CGenUtil {
    private List<AbstractSymbol> classIds;
    private PrintStream out;
    private int labelCount;

    public CGenUtil(PrintStream out, List<AbstractSymbol> classIds) {
        this.out = out;
        this.classIds = classIds;
        labelCount = 0;
    }

    public String emitCoolString(String value) {
        String lengthLabel = emitCoolInt(value.length());

        String label = emitNewLabel();
        out.println(".word " + getClassId(TreeConstants.Str));
        int length = 4 + value.length() / 4 + 1;

        out.println(".word " + length);
        out.println(".word String_dispTab");
        out.println(".word " + lengthLabel);
        out.println(".ascii \"" + value + "\"");
        out.println(".byte 0");
        out.println(".align 2");
        out.println(".word -1");

        return label;
    }

    public String emitCoolInt(int value) {
        String label = emitNewLabel();

        out.println(".word " + getClassId(TreeConstants.Int));
        out.println(".word 4");
        out.println(".word Int_dispTab");
        out.println(".word " + value);
        out.println(".word -1");

        return label;
    }

    private String emitNewLabel() {
        String label = "label_" + labelCount;
        out.println(label + ":");
        labelCount++;
        return label;
    }

    private int getClassId(AbstractSymbol klass) {
        return classIds.indexOf(klass);
    }
}
