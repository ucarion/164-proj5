import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Collection;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CGenUtil {
    private Map<AbstractSymbol, class_c> classesByName;
    public List<AbstractSymbol> classIds;
    public PrintStream out;

    private PrintStream dataOut;
    private ByteArrayOutputStream dataOutHack;
    private int labelCount;
    private class_c currClass;

    public CGenUtil(PrintStream out, Classes klasses) {
        this.out = out;
        this.dataOutHack = new ByteArrayOutputStream();
        this.dataOut = new PrintStream(this.dataOutHack);
        labelCount = 0;

        this.classesByName = new HashMap<>();
        this.classIds = new ArrayList<>();

        for (int i = 0; i < klasses.getLength(); i++) {
            class_c klass = (class_c) klasses.getNth(i);
            classesByName.put(klass.name, klass);
            classIds.add(klass.name);
        }

        AbstractSymbol filename = AbstractTable.stringtable.addString("<basic class>");

        class_c Object_class =
            new class_c(0,
                   TreeConstants.Object_,
                   TreeConstants.No_class,
                   new Features(0)
                   .appendElement(new method(0,
                              TreeConstants.cool_abort,
                              new Formals(0),
                              TreeConstants.Object_,
                              new no_expr(0)))
                   .appendElement(new method(0,
                              TreeConstants.type_name,
                              new Formals(0),
                              TreeConstants.Str,
                              new no_expr(0)))
                   .appendElement(new method(0,
                              TreeConstants.copy,
                              new Formals(0),
                              TreeConstants.SELF_TYPE,
                              new no_expr(0))),
                   filename);

        class_c IO_class =
            new class_c(0,
                   TreeConstants.IO,
                   TreeConstants.Object_,
                   new Features(0)
                   .appendElement(new method(0,
                              TreeConstants.out_string,
                              new Formals(0)
                              .appendElement(new formalc(0,
                                         TreeConstants.arg,
                                         TreeConstants.Str)),
                              TreeConstants.SELF_TYPE,
                              new no_expr(0)))
                   .appendElement(new method(0,
                              TreeConstants.out_int,
                              new Formals(0)
                              .appendElement(new formalc(0,
                                         TreeConstants.arg,
                                         TreeConstants.Int)),
                              TreeConstants.SELF_TYPE,
                              new no_expr(0)))
                   .appendElement(new method(0,
                              TreeConstants.in_string,
                              new Formals(0),
                              TreeConstants.Str,
                              new no_expr(0)))
                   .appendElement(new method(0,
                              TreeConstants.in_int,
                              new Formals(0),
                              TreeConstants.Int,
                              new no_expr(0))),
                   filename);

        class_c Int_class =
            new class_c(0,
                   TreeConstants.Int,
                   TreeConstants.Object_,
                   new Features(0)
                   .appendElement(new attr(0,
                            TreeConstants.val,
                            TreeConstants.prim_slot,
                            new no_expr(0))),
                   filename);

        class_c Bool_class =
            new class_c(0,
                   TreeConstants.Bool,
                   TreeConstants.Object_,
                   new Features(0)
                   .appendElement(new attr(0,
                            TreeConstants.val,
                            TreeConstants.prim_slot,
                            new no_expr(0))),
                   filename);

        class_c Str_class =
            new class_c(0,
                   TreeConstants.Str,
                   TreeConstants.Object_,
                   new Features(0)
                   .appendElement(new attr(0,
                            TreeConstants.val,
                            TreeConstants.Int,
                            new no_expr(0)))
                   .appendElement(new attr(0,
                            TreeConstants.str_field,
                            TreeConstants.prim_slot,
                            new no_expr(0)))
                   .appendElement(new method(0,
                              TreeConstants.length,
                              new Formals(0),
                              TreeConstants.Int,
                              new no_expr(0)))
                   .appendElement(new method(0,
                              TreeConstants.concat,
                              new Formals(0)
                              .appendElement(new formalc(0,
                                         TreeConstants.arg,
                                         TreeConstants.Str)),
                              TreeConstants.Str,
                              new no_expr(0)))
                   .appendElement(new method(0,
                              TreeConstants.substr,
                              new Formals(0)
                              .appendElement(new formalc(0,
                                         TreeConstants.arg,
                                         TreeConstants.Int))
                              .appendElement(new formalc(0,
                                         TreeConstants.arg2,
                                         TreeConstants.Int)),
                              TreeConstants.Str,
                              new no_expr(0))),
                              filename);

        classesByName.put(TreeConstants.Object_, Object_class);
        classesByName.put(TreeConstants.IO, IO_class);
        classesByName.put(TreeConstants.Int, Int_class);
        classesByName.put(TreeConstants.Bool, Bool_class);
        classesByName.put(TreeConstants.Str, Str_class);

        classIds.add(TreeConstants.Object_);
        classIds.add(TreeConstants.IO);
        classIds.add(TreeConstants.Int);
        classIds.add(TreeConstants.Bool);
        classIds.add(TreeConstants.Str);
    }

    public void push(String register) {
        out.println("\tsw " + register + " 0($sp)");
        out.println("\taddiu $sp $sp -4");
    }

    public void getTop(String register) {
        out.println("\tlw " + register + " 4($sp)");
    }

    public void pop() {
        out.println("\taddiu $sp $sp 4");
    }

    public String emitCoolDataString(String value) {
        String lengthLabel = emitCoolDataInt(value.length());

        String label = emitNewDataLabel();
        dataOut.println("\t.word 5");
        int length = 4 + value.length() / 4 + 1;

        dataOut.println("\t.word " + length);
        dataOut.println("\t.word String_dispTab");
        dataOut.println("\t.word " + lengthLabel);

        String asciiValue = value.replace("\n", "\\n");
        dataOut.println("\t.ascii \"" + asciiValue + "\"");
        dataOut.println("\t.byte 0");
        dataOut.println("\t.align 2");
        dataOut.println("\t.word -1");

        return label;
    }

    public String emitCoolDataInt(int value) {
        String label = emitNewDataLabel();

        dataOut.println("\t.word " + getClassId(TreeConstants.Int));
        dataOut.println("\t.word 4");
        dataOut.println("\t.word Int_dispTab");
        dataOut.println("\t.word " + value);
        dataOut.println("\t.word -1");

        return label;
    }

    // public String emitCoolBool(boolean value) {
    //     String label = emitNewLabel();
    //
    //     out.println("\t.word " + getClassId(TreeConstants.Bool));
    //     out.println("\t.word 4");
    //     out.println("\t.word Bool_dispTab");
    //
    //     int intValue = value ? 1 : 0;
    //     out.println("\t.word " + intValue);
    //     out.println("\t.word -1");
    //
    //     return label;
    // }
    //
    // public void outputPrototype(class_c klass) {
    //     List<String> attrLabels = new ArrayList<>();
    //
    //     for (attr a : klass.getAttrs(this)) {
    //         if (a.type_decl.equals(TreeConstants.Bool)) {
    //             attrLabels.add(emitCoolBool(false));
    //         } else if (a.type_decl.equals(TreeConstants.Int)) {
    //             attrLabels.add(emitCoolInt(0));
    //         } else if (a.type_decl.equals(TreeConstants.Str)) {
    //             attrLabels.add(emitCoolString(""));
    //         } else {
    //             attrLabels.add("0");
    //         }
    //     }
    //
    //     out.println(klass.name + "_protObj:");
    //     out.println("\t.word " + getClassId(klass.name));
    //     int length = 3 + attrLabels.size();
    //     out.println("\t.word " + length);
    //     out.println("\t.word " + klass.name + "_dispTab");
    //
    //     for (String label : attrLabels) {
    //         out.println("\t.word " + label);
    //     }
    //
    //     out.println("\t.word -1");
    // }

    private String emitNewDataLabel() {
        String label = getNewLabel();
        dataOut.println(label + ":");
        return label;
    }

    public String getNewLabel() {
        String label = "label_" + labelCount;
        labelCount++;
        return label;
    }

    public void dumpDataSegment(PrintStream out) {
        try {
            out.write(dataOutHack.toByteArray());
        } catch (IOException e) {

        }
    }

    public int getClassId(AbstractSymbol klass) {
        return classIds.indexOf(klass);
    }

    public int numClasses() {
        return classIds.size();
    }

    public AbstractSymbol getClassById(int classId) {
        return classIds.get(classId);
    }

    public class_c getClassByName(AbstractSymbol klass) {
        return classesByName.get(klass);
    }

    public Collection<class_c> getClasses() {
        return classesByName.values();
    }

    public boolean isBasicClass(class_c klass) {
        AbstractSymbol name = klass.name;

        return name.equals(TreeConstants.Object_) ||
            name.equals(TreeConstants.IO) ||
            name.equals(TreeConstants.Int) ||
            name.equals(TreeConstants.Bool) ||
            name.equals(TreeConstants.Str);
    }

    public void setCurrentClass(class_c klass) {
        this.currClass = klass;
    }

    public class_c resolveIfSelfType(AbstractSymbol className) {
        if (className.equals(TreeConstants.SELF_TYPE)) {
            return currClass;
        } else {
            return getClassByName(className);
        }
    }
}
