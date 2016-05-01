import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Collection;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Stack;

public class CGenUtil {
    private Map<AbstractSymbol, class_c> classesByName;
    public List<AbstractSymbol> classIds;
    public PrintStream out;

    private PrintStream dataOut;
    private ByteArrayOutputStream dataOutHack;
    private int labelCount;
    private class_c currClass;
    private Stack<Integer> numPushes;
    private Map<AbstractSymbol, Stack<Integer>> localVariables;

    public CGenUtil(PrintStream out, List<class_c> classes) {
        this.out = out;
        this.dataOutHack = new ByteArrayOutputStream();
        this.dataOut = new PrintStream(this.dataOutHack);
        this.labelCount = 0;
        this.numPushes = new Stack<>();
        this.numPushes.push(0);
        this.localVariables = new HashMap<>();

        this.classesByName = new HashMap<>();
        this.classIds = new ArrayList<>();

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


        classes.add(Object_class);
        classes.add(IO_class);
        classes.add(Str_class);
        classes.add(Bool_class);
        classes.add(Int_class);

        for (class_c klass : classes) {
            classesByName.put(klass.name, klass);
            classIds.add(klass.name);
        }
    }

    public int push(String register) {
        pushNoCount(register);
        numPushes.push(numPushes.pop() + 1);

        return getFpOffset();
    }

    public void pushNoCount(String register) {
        out.println("\tsw " + register + " 0($sp)\t#push " + register);
        out.println("\taddiu $sp $sp -4");
    }

    public void getTop(String register) {
        out.println("\tlw " + register + " 4($sp)\t#peek " + register);
    }

    public void pop() {
        popNoCount();
        numPushes.push(numPushes.pop() - 1);
    }

    public void popNoCount() {
        out.println("\taddiu $sp $sp 4\t#pop");
    }

    public void enterScope() {
        numPushes.push(0);
    }

    public void exitScope() {
        numPushes.pop();
    }

    private int getFpOffset() {
        int n = numPushes.peek() - 1;
        return n * -4;
    }

    public void pushLocalVariable(AbstractSymbol name, int offset) {
        if (!this.localVariables.containsKey(name)) {
            this.localVariables.put(name, new Stack<Integer>());
        }

        this.localVariables.get(name).push(offset);
    }

    public void popLocalVariable(AbstractSymbol name) {
        this.localVariables.get(name).pop();
    }

    public int getLocalVariableOffset(AbstractSymbol name) {
        if (!this.localVariables.containsKey(name)) {
            return -1;
        }

        if (this.localVariables.get(name).empty()) {
            return -1;
        }

        return this.localVariables.get(name).peek();
    }

    public String emitCoolDataString(String value) {
        String lengthLabel = emitCoolDataInt(value.length());

        String label = emitNewDataLabel();
        dataOut.println("\t.word " + getClassId(TreeConstants.Str));
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

    public String emitCoolDataBool(boolean value) {
        String label = emitNewDataLabel();

        dataOut.println("\t.word " + getClassId(TreeConstants.Bool));
        dataOut.println("\t.word 4");
        dataOut.println("\t.word Bool_dispTab");

        int intValue = value ? 1 : 0;
        dataOut.println("\t.word " + intValue);
        dataOut.println("\t.word -1");

        return label;
    }

    public void emitPrototype(class_c klass) {
        List<String> attrLabels = new ArrayList<>();

        for (attr a : klass.getAttrs(this)) {
            if (a.type_decl.equals(TreeConstants.Bool)) {
                attrLabels.add(emitCoolDataBool(false));
            } else if (a.type_decl.equals(TreeConstants.Int)) {
                attrLabels.add(emitCoolDataInt(0));
            } else if (a.type_decl.equals(TreeConstants.Str)) {
                attrLabels.add(emitCoolDataString(""));
            } else {
                attrLabels.add("0");
            }
        }

        dataOut.println(klass.name + "_protObj:");
        dataOut.println("\t.word " + getClassId(klass.name));
        int length = 3 + attrLabels.size();
        dataOut.println("\t.word " + length);
        dataOut.println("\t.word " + klass.name + "_dispTab");

        for (String label : attrLabels) {
            dataOut.println("\t.word " + label);
        }

        dataOut.println("\t.word -1");
    }

    public void emitDispatchTable(class_c klass) {
        dataOut.println(klass.name + "_dispTab:");
        for (method m : klass.getCallableMethods(this)) {
            class_c definer = klass.getDefiningClass(this, m);
            dataOut.println("\t.word " + definer.name + "." + m.name);
        }
    }

    public void emitDataTables() {
        dataOut.println("_int_tag: " + getClassId(TreeConstants.Int));
        dataOut.println("_bool_tag: " + getClassId(TreeConstants.Bool));
        dataOut.println("_string_tag: " + getClassId(TreeConstants.Str));

        dataOut.println("bool_const0:");
        dataOut.println("\t.word " + getClassId(TreeConstants.Bool));
        dataOut.println("\t.word 4");
        dataOut.println("\t.word Bool_dispTab");
        dataOut.println("\t.word 0");
        dataOut.println("\t.word -1");

        dataOut.println("bool_const1:");
        dataOut.println("\t.word " + getClassId(TreeConstants.Bool));
        dataOut.println("\t.word 4");
        dataOut.println("\t.word Bool_dispTab");
        dataOut.println("\t.word 1");
        dataOut.println("\t.word -1");

        List<String> labels = new ArrayList<>();
        for (class_c klass : getClasses()) {
            labels.add(emitCoolDataString(klass.name.toString()));
        }

        dataOut.println("class_nameTab:");
        for (String label : labels) {
            dataOut.println("\t.word " + label);
        }

        dataOut.println("class_objTab:");
        for (class_c klass : getClasses()) {
            dataOut.println("\t.word " + klass.name + "_protObj");
            dataOut.println("\t.word " + klass.name + "_init");
        }
    }

    private String filenameLabel;

    public void emitFilename(AbstractSymbol filename) {
        this.filenameLabel = emitCoolDataString(filename.toString());
    }

    public String getFilenameLabel() {
        return filenameLabel;
    }

    public void emitInit(class_c klass) {
        out.println(klass.name + "_init:");
        enterScope();

        pushNoCount("$fp");
        out.println("\tmove $fp $sp");
        push("$ra");
        push("$s0");
        out.println("\tmove $s0 $a0");

        out.println("\tjal " + getClassByName(klass.parent).name + "_init");

        for (int i = 0; i < klass.features.getLength(); i++) {
            if (klass.features.getNth(i) instanceof attr) {
                attr a = (attr) klass.features.getNth(i);

                a.init.cgen(this);
                int offset = 12 + 4 * klass.getAttrIndex(this, a.name);
                out.println("\tsw $a0 " + offset + "($s0)");
            }
        }

        out.println("\tmove $a0 $s0");
        getTop("$s0");
        pop();
        getTop("$ra");
        pop();
        getTop("$fp");
        popNoCount();
        out.println("\tjr $ra");

        exitScope();
    }

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

    public List<class_c> getClasses() {
        List<class_c> result = new ArrayList<>();

        for (int i = 0; i < numClasses(); i++) {
            result.add(getClassByName(getClassById(i)));
        }

        return result;
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

    public class_c getCurrentClass() {
        return this.currClass;
    }

    public class_c resolveIfSelfType(AbstractSymbol className) {
        if (className.equals(TreeConstants.SELF_TYPE)) {
            return currClass;
        } else {
            return getClassByName(className);
        }
    }
}
