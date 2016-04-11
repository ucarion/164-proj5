// -*- mode: java -*-
//
// file: cool-tree.m4
//
// This file defines the AST
//
//////////////////////////////////////////////////////////



import java.util.Enumeration;
import java.io.PrintStream;
import java.util.Vector;

import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

/** Defines simple phylum Program */
abstract class Program extends TreeNode {
    protected Program(int lineNumber) {
        super(lineNumber);
    }
    public abstract void dump_with_types(PrintStream out, int n);
    public abstract void semant();
    public abstract void cgen(PrintStream s);

}


/** Defines simple phylum Class_ */
abstract class Class_ extends TreeNode {
    protected Class_(int lineNumber) {
        super(lineNumber);
    }
    public abstract void dump_with_types(PrintStream out, int n);
    public abstract AbstractSymbol getName();
    public abstract AbstractSymbol getParent();
    public abstract AbstractSymbol getFilename();
    public abstract Features getFeatures();

}


/** Defines list phylum Classes
    <p>
    See <a href="ListNode.html">ListNode</a> for full documentation. */
class Classes extends ListNode {
    public final static Class elementClass = Class_.class;
    /** Returns class of this lists's elements */
    public Class getElementClass() {
        return elementClass;
    }
    protected Classes(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }
    /** Creates an empty "Classes" list */
    public Classes(int lineNumber) {
        super(lineNumber);
    }
    /** Appends "Class_" element to this list */
    public Classes appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }
    public TreeNode copy() {
        return new Classes(lineNumber, copyElements());
    }
}


/** Defines simple phylum Feature */
abstract class Feature extends TreeNode {
    protected Feature(int lineNumber) {
        super(lineNumber);
    }
    public abstract void dump_with_types(PrintStream out, int n);
    
    protected abstract void assignTypes(ClassTable classTable, SymbolTable symbolTable);
    protected abstract AbstractSymbol getName();
}


/** Defines list phylum Features
    <p>
    See <a href="ListNode.html">ListNode</a> for full documentation. */
class Features extends ListNode {
    public final static Class elementClass = Feature.class;
    /** Returns class of this lists's elements */
    public Class getElementClass() {
        return elementClass;
    }
    protected Features(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }
    /** Creates an empty "Features" list */
    public Features(int lineNumber) {
        super(lineNumber);
    }
    /** Appends "Feature" element to this list */
    public Features appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }
    public TreeNode copy() {
        return new Features(lineNumber, copyElements());
    }
}


/** Defines simple phylum Formal */
abstract class Formal extends TreeNode {
    protected Formal(int lineNumber) {
        super(lineNumber);
    }
    public abstract void dump_with_types(PrintStream out, int n);

}


/** Defines list phylum Formals
    <p>
    See <a href="ListNode.html">ListNode</a> for full documentation. */
class Formals extends ListNode {
    public final static Class elementClass = Formal.class;
    /** Returns class of this lists's elements */
    public Class getElementClass() {
        return elementClass;
    }
    protected Formals(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }
    /** Creates an empty "Formals" list */
    public Formals(int lineNumber) {
        super(lineNumber);
    }
    /** Appends "Formal" element to this list */
    public Formals appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }
    public TreeNode copy() {
        return new Formals(lineNumber, copyElements());
    }
}


/** Defines simple phylum Expression */
abstract class Expression extends TreeNode {
    protected Expression(int lineNumber) {
        super(lineNumber);
    }
    private AbstractSymbol type = null;
    public AbstractSymbol get_type() { return type; }
    public Expression set_type(AbstractSymbol s) { type = s; return this; }
    public abstract void dump_with_types(PrintStream out, int n);
    public void dump_type(PrintStream out, int n) {
        if (type != null)
            { out.println(Utilities.pad(n) + ": " + type.getString()); }
        else
            { out.println(Utilities.pad(n) + ": _no_type"); }
    }
     protected abstract void assignTypes(ClassTable classTable, SymbolTable symbolTable);
    public abstract void cgen(CGenUtil util);

}


/** Defines list phylum Expressions
    <p>
    See <a href="ListNode.html">ListNode</a> for full documentation. */
class Expressions extends ListNode {
    public final static Class elementClass = Expression.class;
    /** Returns class of this lists's elements */
    public Class getElementClass() {
        return elementClass;
    }
    protected Expressions(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }
    /** Creates an empty "Expressions" list */
    public Expressions(int lineNumber) {
        super(lineNumber);
    }
    /** Appends "Expression" element to this list */
    public Expressions appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }
    public TreeNode copy() {
        return new Expressions(lineNumber, copyElements());
    }
}


/** Defines simple phylum Case */
abstract class Case extends TreeNode {
    protected Case(int lineNumber) {
        super(lineNumber);
    }
    public abstract void dump_with_types(PrintStream out, int n);

}


/** Defines list phylum Cases
    <p>
    See <a href="ListNode.html">ListNode</a> for full documentation. */
class Cases extends ListNode {
    public final static Class elementClass = Case.class;
    /** Returns class of this lists's elements */
    public Class getElementClass() {
        return elementClass;
    }
    protected Cases(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }
    /** Creates an empty "Cases" list */
    public Cases(int lineNumber) {
        super(lineNumber);
    }
    /** Appends "Case" element to this list */
    public Cases appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }
    public TreeNode copy() {
        return new Cases(lineNumber, copyElements());
    }
}


/** Defines AST constructor 'programc'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class programc extends Program {
    protected Classes classes;
    /** Creates "programc" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for classes
      */
    public programc(int lineNumber, Classes a1) {
        super(lineNumber);
        classes = a1;
    }
    public TreeNode copy() {
        return new programc(lineNumber, (Classes)classes.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "programc\n");
        classes.dump(out, n+2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_program");
        for (Enumeration e = classes.getElements(); e.hasMoreElements(); ) {
	    ((Class_)e.nextElement()).dump_with_types(out, n + 2);
        }
    }
    /** This method is the entry point to the semantic checker.  You will
        need to complete it in programming assignment 4.
	<p>
        Your checker should do the following two things:
	<ol>
	<li>Check that the program is semantically correct
	<li>Decorate the abstract syntax tree with type information
        by setting the type field in each Expression node.
        (see tree.h)
	</ol>
	<p>
	You are free to first do (1) and make sure you catch all semantic
    	errors. Part (2) can be done in a second stage when you want
	to test the complete compiler.
    */
    public void semant() {
        /* ClassTable constructor may do some semantic analysis */
        ClassTable classTable = new ClassTable(classes);

        //First Pass: Check that there are no cycles in inheritance graph
        Map<AbstractSymbol, AbstractSymbol> inheritance = checkInheritanceCycles();
        if (inheritance == null) {
            classTable.semantError().println("Cyclic inheritance graph");
            abort();
        }

        //Second Pass: Fill out the class table
        for (int i = 0; i < classes.getLength(); i++) {
            class_c klass = (class_c) classes.getNth(i);
			classTable.addClassName(klass.name, klass);
        }

        //Final Pass: assign types and enforce semantic rules
        for (int i = 0; i < classes.getLength(); i++) {
            class_c klass = (class_c) classes.getNth(i);
            klass.assignTypes(classTable);
        }

        // Make sure that class Main exists and contains method main()
        if (!checkForMain(classTable)) {
            classTable.semantError().println("No method main taking no arguments of class Main was found.");
        }

        if (classTable.errors()) {
            abort();
        }
    }

    private void abort() {
        System.err.println("Compilation halted due to static semantic errors.");
        System.exit(1);
    }
    
    // Build a graph out of classes; make sure that all classes inherit from Object and 
    // that there are no cycles in the graph (i.e. B inherit from A, A inherits from B )
    private Map<AbstractSymbol, AbstractSymbol> checkInheritanceCycles() {
        Map<AbstractSymbol, AbstractSymbol> graph = new HashMap<>();
        graph.put(TreeConstants.IO, TreeConstants.Object_);
        graph.put(TreeConstants.Int, TreeConstants.Object_);
        graph.put(TreeConstants.Str, TreeConstants.Object_);
        graph.put(TreeConstants.Bool, TreeConstants.Object_);

        for (int i = 0; i < classes.getLength(); i++) {
            class_c klass = (class_c) classes.getNth(i);
            graph.put(klass.name, klass.parent);
        }

        for (AbstractSymbol klass : graph.keySet()) {
            if (!inheritsObject(klass, graph)) {
                return null;
            }
        }

        return graph;
    }

    // Helper class for checkInheritanceCycles
    private boolean inheritsObject(AbstractSymbol klass,
            Map<AbstractSymbol, AbstractSymbol> graph) {
        Set<AbstractSymbol> visited = new HashSet<>();

        AbstractSymbol curr = klass;

        while (true) {
            if (visited.contains(curr)) {
                return false;
            }

            if (curr == TreeConstants.Object_) {
                return true;
            }

            visited.add(curr);
            curr = graph.get(curr);
        }
    }    

    //Return true if Main.main() exists, false otherwise
    private boolean checkForMain(ClassTable classTable) {
        class_c mainClass = classTable.getClassByName(TreeConstants.Main);
        if (mainClass == null) {
            return false;
        }

        for (int i = 0; i < mainClass.features.getLength(); i++) {
            Feature feature = (Feature) mainClass.features.getNth(i);
            if (feature instanceof method) {
                method m = (method) feature;

                if (m.name.equals(TreeConstants.main_meth)) {
                    return m.formals.getLength() == 0;
                }
            }
        }

        return false;
    }
    
    public void cgen(PrintStream out) {
        CGenUtil util = new CGenUtil(out, classes);

        List<String> classNames = new ArrayList<>();
        for (AbstractSymbol klass : util.classIds) {
            String label = util.emitCoolString(klass.toString());
            classNames.add(label);
        }

        out.println("class_nameTab:");
        for (int i = 0; i < classNames.size(); i++) {
            out.println("\t.word " + classNames.get(i));
        }

        Map<AbstractSymbol, AbstractSymbol> inheritance = inheritanceGraph();

        out.println("class_objTab:");
        for (int i = 0; i < util.numClasses(); i++) {
            String className = util.getClassById(i).toString();
            out.println("\t.word " + className + "_protObj");
            out.println("\t.word " + className + "_init");
        }

        for (class_c klass : util.getClasses()) {
            util.outputPrototype(klass);
        }

        for (class_c klass : util.getClasses()) {
            out.println(klass.name + "_dispTab:");
            for (method m : klass.getMethods(util)) {
                class_c definer = klass.getDefiningClass(util, m);
                out.println("\t.word " + definer.name + "." + m.name);
            }
        }

        for (class_c klass : util.getClasses()) {
            for (int i = 0; i < klass.features.getLength(); i++) {
                Feature feature = (Feature) klass.features.getNth(i);

                if (feature instanceof method) {
                    method m = (method) feature;
                    m.cgen(klass, util);
                }
            }
        }
    }

    private Map<AbstractSymbol, AbstractSymbol> inheritanceGraph() {
        Map<AbstractSymbol, AbstractSymbol> graph = new HashMap<>();
        graph.put(TreeConstants.IO, TreeConstants.Object_);
        graph.put(TreeConstants.Int, TreeConstants.Object_);
        graph.put(TreeConstants.Str, TreeConstants.Object_);
        graph.put(TreeConstants.Bool, TreeConstants.Object_);

        for (int i = 0; i < classes.getLength(); i++) {
            class_c klass = (class_c) classes.getNth(i);
            graph.put(klass.name, klass.parent);
        }

        return graph;
    }
}


/** Defines AST constructor 'class_c'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class class_c extends Class_ {
    protected AbstractSymbol name;
    protected AbstractSymbol parent;
    protected Features features;
    protected AbstractSymbol filename;
    /** Creates "class_c" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for name
      * @param a1 initial value for parent
      * @param a2 initial value for features
      * @param a3 initial value for filename
      */
    public class_c(int lineNumber, AbstractSymbol a1, AbstractSymbol a2, Features a3, AbstractSymbol a4) {
        super(lineNumber);
        name = a1;
        parent = a2;
        features = a3;
        filename = a4;
    }
    public TreeNode copy() {
        return new class_c(lineNumber, copy_AbstractSymbol(name), copy_AbstractSymbol(parent), (Features)features.copy(), copy_AbstractSymbol(filename));
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "class_c\n");
        dump_AbstractSymbol(out, n+2, name);
        dump_AbstractSymbol(out, n+2, parent);
        features.dump(out, n+2);
        dump_AbstractSymbol(out, n+2, filename);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_class");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, parent);
        out.print(Utilities.pad(n + 2) + "\"");
        Utilities.printEscapedString(out, filename.getString());
        out.println("\"\n" + Utilities.pad(n + 2) + "(");
        for (Enumeration e = features.getElements(); e.hasMoreElements();) {
	    ((Feature)e.nextElement()).dump_with_types(out, n + 2);
        }
        out.println(Utilities.pad(n + 2) + ")");
    }
    public AbstractSymbol getName()     { return name; }
    public AbstractSymbol getParent()   { return parent; }
    public AbstractSymbol getFilename() { return filename; }
    public Features getFeatures()       { return features; }

    //assign types to class, make sure no two classes share a name, and that class
    // doesn't inherit from basic classes (Int, Bool, String)
    protected void assignTypes(ClassTable classTable) {
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.enterScope();
        symbolTable.addId(TreeConstants.self, name);

        if (name.equals(TreeConstants.SELF_TYPE)) {
            classTable.semantError().println("Classes cannot be called SELF_TYPE");
        }

        Set<AbstractSymbol> featureNames = new HashSet<>();

        for (int i = 0; i < features.getLength(); i++) {
            Feature feature = (Feature) features.getNth(i);
            feature.assignTypes(classTable, symbolTable);

            if (featureNames.contains(feature.getName())) {
                classTable.semantError().println("Feature " + feature.getName() + " cannot be multiply defined.");
            } else {
                featureNames.add(feature.getName());
            }
        }

        symbolTable.exitScope();

        if (parent.equals(TreeConstants.Int) ||
                parent.equals(TreeConstants.Bool) ||
                parent.equals(TreeConstants.Str)) {
            classTable.semantError().println("Class " + name + " cannot inherit from basic classes.");
        }
    }

    //get attribute of class using attribute's name
	protected method getMethod(AbstractSymbol name, ClassTable classTable) {
		for (int i = 0; i < features.getLength(); i++) {
			Feature feature = (Feature) features.getNth(i);
			if (feature instanceof method) {
				method m = (method) feature;

				if (m.name.equals(name)) {
					return m;
				}
			}
		}

		if (this.name == TreeConstants.Object_) {
			return null;
		} else {
			class_c parentClass = classTable.getClassByName(parent);
			return parentClass.getMethod(name, classTable);
		}
	}

    //get attribute of class using attribute's name 
	protected attr getAttr(AbstractSymbol name, ClassTable classTable) {
		for (int i = 0; i < features.getLength(); i++) {
			Feature feature = (Feature) features.getNth(i);
			if (feature instanceof attr) {
				attr m = (attr) feature;

				if (m.name.equals(name)) {
					return m;
				}
			}
		}

		if (this.name == TreeConstants.Object_) {
			return null;
		} else {
			class_c parentClass = classTable.getClassByName(parent);
			return parentClass.getAttr(name, classTable);
		}
	}
    // method to check that current is either klass or a descendent thereof 
    protected boolean conformsTo(ClassTable classTable, AbstractSymbol klass) {
        if (name.equals(klass)) {
            return true;
        }

        if (name.equals(TreeConstants.Object_)) {
            return false;
        } else {
            return classTable.getClassByName(parent).conformsTo(classTable, klass);
        }
    }
    
    public List<attr> getAttrs(CGenUtil util) {
        if (name.equals(TreeConstants.Object_)) {
            return new ArrayList<>();
        } else {
            class_c parentClass = util.getClassByName(parent);
            List<attr> result = parentClass.getAttrs(util);

            for (int i = 0; i < features.getLength(); i++) {
                Feature feature = (Feature) features.getNth(i);

                if (feature instanceof attr) {
                    result.add((attr) feature);
                }
            }

            return result;
        }
    }

    public List<method> getMethods(CGenUtil util) {
        List<method> result;

        if (name.equals(TreeConstants.Object_)) {
            result = new ArrayList<>();
        } else {
            class_c parentClass = util.getClassByName(parent);
            result = parentClass.getMethods(util);
        }

        for (int i = 0; i < features.getLength(); i++) {
            Feature feature = (Feature) features.getNth(i);

            if (feature instanceof method) {
                result.add((method) feature);
            }
        }

        return result;
    }

    public class_c getDefiningClass(CGenUtil util, method meth) {
        for (int i = 0; i < features.getLength(); i++) {
            Feature feature = (Feature) features.getNth(i);

            if (feature instanceof method) {
                method m = (method) feature;

                if (m.equals(meth)) {
                    return this;
                }
            }
        }

        return util.getClassByName(parent).getDefiningClass(util, meth);
    }

    public int getAttrIndex(CGenUtil util, AbstractSymbol attrName) {
        List<attr> attrs = getAttrs(util);
        for (int i = 0; i < attrs.size(); i++) {
            attr a = attrs.get(i);

            if (a.name.equals(attrName)) {
                return i;
            }
        }

        throw new RuntimeException();
    }
}


/** Defines AST constructor 'method'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class method extends Feature {
    protected AbstractSymbol name;
    protected Formals formals;
    protected AbstractSymbol return_type;
    protected Expression expr;
    /** Creates "method" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for name
      * @param a1 initial value for formals
      * @param a2 initial value for return_type
      * @param a3 initial value for expr
      */
    public method(int lineNumber, AbstractSymbol a1, Formals a2, AbstractSymbol a3, Expression a4) {
        super(lineNumber);
        name = a1;
        formals = a2;
        return_type = a3;
        expr = a4;
    }
    public TreeNode copy() {
        return new method(lineNumber, copy_AbstractSymbol(name), (Formals)formals.copy(), copy_AbstractSymbol(return_type), (Expression)expr.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "method\n");
        dump_AbstractSymbol(out, n+2, name);
        formals.dump(out, n+2);
        dump_AbstractSymbol(out, n+2, return_type);
        expr.dump(out, n+2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_method");
        dump_AbstractSymbol(out, n + 2, name);
        for (Enumeration e = formals.getElements(); e.hasMoreElements();) {
	    ((Formal)e.nextElement()).dump_with_types(out, n + 2);
        }
        dump_AbstractSymbol(out, n + 2, return_type);
	expr.dump_with_types(out, n + 2);
    }

    //assign types to method, make sure no two methods in the same class share a name, that 
    // parameters have different names and existing types, and that inheritence rules for 
    // overriding methods are followed
    protected void assignTypes(ClassTable classTable, SymbolTable symbolTable) {
        symbolTable.enterScope();

        if (!return_type.equals(TreeConstants.SELF_TYPE) && classTable.getClassByName(return_type) == null) {
            classTable.semantError().println("Undefined return type " + return_type);
        }

        Set<AbstractSymbol> names = new HashSet<>();

        for (int i = 0; i < formals.getLength(); i++) {
            formalc formal = (formalc) formals.getNth(i);
            symbolTable.addId(formal.name, formal.type_decl);

            if (names.contains(formal.name)) {
                classTable.semantError().println("Formal parameter " + formal.name + " is multiply defined.");
            }

            if (formal.type_decl.equals(TreeConstants.SELF_TYPE)) {
                classTable.semantError().println("Formal parameter " + formal.name + " cannot have type SELF_TYPE.");
            }

            if (classTable.getClassByName(formal.type_decl) == null) {
                classTable.semantError().println("Formal parameter " + formal.name + " has unknown type " + formal.type_decl);
            }

            names.add(formal.name);
        }

        expr.assignTypes(classTable, symbolTable);
        symbolTable.exitScope();

        AbstractSymbol exprType = classTable.resolveIfSelfType(expr.get_type(), symbolTable);
        AbstractSymbol conformType = classTable.resolveIfSelfType(return_type, symbolTable);

        if (!classTable.getClassByName(exprType).conformsTo(classTable, conformType)) {
            classTable.semantError().println("Method " + name + " has a body that does not conform to " + return_type);
        }

        class_c selfClass = classTable.getClassByName((AbstractSymbol) symbolTable.lookup(TreeConstants.self));
        class_c parentClass = classTable.getClassByName(selfClass.parent);
        method overridden = parentClass.getMethod(name, classTable);

        if (overridden != null) {
            if (formals.getLength() != overridden.formals.getLength()) {
                classTable.semantError().println("Method " + name + " is overriden, but has different number of arguments.");
            } else {
                for (int i = 0; i < formals.getLength(); i++) {
                    formalc formal = (formalc) formals.getNth(i);
                    formalc overridenFormal = (formalc) overridden.formals.getNth(i);

                    if (!formal.type_decl.equals(overridenFormal.type_decl)) {
                        classTable.semantError().println("Method " + name + " arg #" + i + " is overriden, but has different types: "
                                + formal.type_decl + ", " + overridenFormal.type_decl);
                    }
                }
            }

            AbstractSymbol returnType = classTable.resolveIfSelfType(return_type, symbolTable);
            AbstractSymbol overriddenReturnType = classTable.resolveIfSelfType(overridden.return_type, symbolTable);

            if (!returnType.equals(overriddenReturnType)) {
                classTable.semantError().println("Overriden method " + name + " has different return types: " + returnType + ", " + overriddenReturnType);
            }
        }
    }

    protected AbstractSymbol getName() {
        return name;
    }
    
    public void cgen(class_c klass, CGenUtil util) {
        if (util.isBasicClass(klass)) {
            return;
        }

        util.out.println(klass.name + "." + name + ":");
        util.out.println("\tmove $fp $sp");
        util.push("$ra");
        expr.cgen(util);
        util.getTop("$ra");

        int z = 8 + 4 * formals.getLength();
        util.out.println("\taddiu $sp $sp " + z);
        util.out.println("\tlw $fp 0($sp)");
        util.out.println("\tjr $ra");
    }
}


/** Defines AST constructor 'attr'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class attr extends Feature {
    protected AbstractSymbol name;
    protected AbstractSymbol type_decl;
    protected Expression init;
    /** Creates "attr" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for name
      * @param a1 initial value for type_decl
      * @param a2 initial value for init
      */
    public attr(int lineNumber, AbstractSymbol a1, AbstractSymbol a2, Expression a3) {
        super(lineNumber);
        name = a1;
        type_decl = a2;
        init = a3;
    }
    public TreeNode copy() {
        return new attr(lineNumber, copy_AbstractSymbol(name), copy_AbstractSymbol(type_decl), (Expression)init.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "attr\n");
        dump_AbstractSymbol(out, n+2, name);
        dump_AbstractSymbol(out, n+2, type_decl);
        init.dump(out, n+2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_attr");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
	init.dump_with_types(out, n + 2);
    }

    // /assign types to attributes, make sure that <Class>.<attribute> has an existing <Class>,
    // and that initialized attribute has intial value of type conforming to its declaration
    protected void assignTypes(ClassTable classTable, SymbolTable symbolTable) {
        init.assignTypes(classTable, symbolTable);

        if (classTable.getClassByName(type_decl) == null) {
            if (!TreeConstants.SELF_TYPE.equals(type_decl)) {
                classTable.semantError().println("Attr " + name + ": No such class: " + type_decl);
            }
        }

        if (init.get_type() != null) {
            AbstractSymbol initType = classTable.resolveIfSelfType(init.get_type(), symbolTable);
            AbstractSymbol conformType = classTable.resolveIfSelfType(type_decl, symbolTable);

            if (!classTable.getClassByName(initType).conformsTo(classTable, conformType)) {
                classTable.semantError().println("Attr " + name + " has an init that does not conform to " + type_decl);
            }
        }
    }

    protected AbstractSymbol getName() {
        return name;
    }
}


/** Defines AST constructor 'formalc'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class formalc extends Formal {
    protected AbstractSymbol name;
    protected AbstractSymbol type_decl;
    /** Creates "formalc" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for name
      * @param a1 initial value for type_decl
      */
    public formalc(int lineNumber, AbstractSymbol a1, AbstractSymbol a2) {
        super(lineNumber);
        name = a1;
        type_decl = a2;
    }
    public TreeNode copy() {
        return new formalc(lineNumber, copy_AbstractSymbol(name), copy_AbstractSymbol(type_decl));
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "formalc\n");
        dump_AbstractSymbol(out, n+2, name);
        dump_AbstractSymbol(out, n+2, type_decl);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_formal");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
    }
}


/** Defines AST constructor 'branch'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class branch extends Case {
    protected AbstractSymbol name;
    protected AbstractSymbol type_decl;
    protected Expression expr;
    /** Creates "branch" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for name
      * @param a1 initial value for type_decl
      * @param a2 initial value for expr
      */
    public branch(int lineNumber, AbstractSymbol a1, AbstractSymbol a2, Expression a3) {
        super(lineNumber);
        name = a1;
        type_decl = a2;
        expr = a3;
    }
    public TreeNode copy() {
        return new branch(lineNumber, copy_AbstractSymbol(name), copy_AbstractSymbol(type_decl), (Expression)expr.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "branch\n");
        dump_AbstractSymbol(out, n+2, name);
        dump_AbstractSymbol(out, n+2, type_decl);
        expr.dump(out, n+2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_branch");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
	expr.dump_with_types(out, n + 2);
    }

}


/** Defines AST constructor 'assign'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class assign extends Expression {
    protected AbstractSymbol name;
    protected Expression expr;
    /** Creates "assign" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for name
      * @param a1 initial value for expr
      */
    public assign(int lineNumber, AbstractSymbol a1, Expression a2) {
        super(lineNumber);
        name = a1;
        expr = a2;
    }
    public TreeNode copy() {
        return new assign(lineNumber, copy_AbstractSymbol(name), (Expression)expr.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "assign\n");
        dump_AbstractSymbol(out, n+2, name);
        expr.dump(out, n+2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_assign");
        dump_AbstractSymbol(out, n + 2, name);
	expr.dump_with_types(out, n + 2);
	dump_type(out, n);
    }

    //assign types to assignment statements and make sure expression being assigned 
    // conforms to type of variable
    protected void assignTypes(ClassTable classTable, SymbolTable symbolTable) {
		expr.assignTypes(classTable, symbolTable);

        class_c selfClass = classTable.getClassByName((AbstractSymbol) symbolTable.lookup(TreeConstants.self));
        attr assigned = selfClass.getAttr(name, classTable);

        if (assigned == null) {
            classTable.semantError().println("No such attr: " + name);
        } else {
            AbstractSymbol assignedType = classTable.resolveIfSelfType(assigned.type_decl, symbolTable);
            AbstractSymbol exprType = classTable.resolveIfSelfType(expr.get_type(), symbolTable);
            if (!classTable.getClassByName(exprType).conformsTo(classTable, assignedType)) {
                classTable.semantError().println("Attr " + name + " has type " + assigned.type_decl + " but is assigned a " + exprType);
            }
        }

		set_type(expr.get_type());
    }
    
    public void cgen(CGenUtil util) {
    }
}


/** Defines AST constructor 'static_dispatch'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class static_dispatch extends Expression {
    protected Expression expr;
    protected AbstractSymbol type_name;
    protected AbstractSymbol name;
    protected Expressions actual;
    /** Creates "static_dispatch" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for expr
      * @param a1 initial value for type_name
      * @param a2 initial value for name
      * @param a3 initial value for actual
      */
    public static_dispatch(int lineNumber, Expression a1, AbstractSymbol a2, AbstractSymbol a3, Expressions a4) {
        super(lineNumber);
        expr = a1;
        type_name = a2;
        name = a3;
        actual = a4;
    }
    public TreeNode copy() {
        return new static_dispatch(lineNumber, (Expression)expr.copy(), copy_AbstractSymbol(type_name), copy_AbstractSymbol(name), (Expressions)actual.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "static_dispatch\n");
        expr.dump(out, n+2);
        dump_AbstractSymbol(out, n+2, type_name);
        dump_AbstractSymbol(out, n+2, name);
        actual.dump(out, n+2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_static_dispatch");
	expr.dump_with_types(out, n + 2);
        dump_AbstractSymbol(out, n + 2, type_name);
        dump_AbstractSymbol(out, n + 2, name);
        out.println(Utilities.pad(n + 2) + "(");
        for (Enumeration e = actual.getElements(); e.hasMoreElements();) {
	    ((Expression)e.nextElement()).dump_with_types(out, n + 2);
        }
        out.println(Utilities.pad(n + 2) + ")");
	dump_type(out, n);
    }

    //assign types to static dispatch (<expr>@<type>.id(<expr>,...,<expr>)), and make
    // sure that the method is called correctly
    protected void assignTypes(ClassTable classTable, SymbolTable symbolTable) {
        expr.assignTypes(classTable, symbolTable);

        for (int i = 0; i < actual.getLength(); i++) {
            Expression arg = (Expression) actual.getNth(i);
            arg.assignTypes(classTable, symbolTable);
        }

        class_c staticClass = classTable.getClassByName(type_name);
        if (staticClass == null) {
            classTable.semantError().println("Unknown type " + type_name);
            return;
        }

        AbstractSymbol exprClass = expr.get_type();

        if (exprClass == null) {
            return;
        }

        method calledMethod = staticClass.getMethod(name, classTable);
        if (calledMethod == null) {
            classTable.semantError().println("No method " + name + " exists for type " + type_name);
            return;
        }

        AbstractSymbol returnType = calledMethod.return_type;

        if (actual.getLength() != calledMethod.formals.getLength()) {
            classTable.semantError().println("Statically-invoked method " + name + " invoked with wrong number of arguments.");
        } else {
            for (int i = 0; i < actual.getLength(); i++) {
                AbstractSymbol expectedType = ((formalc) calledMethod.formals.getNth(i)).type_decl;
                AbstractSymbol actualType = ((Expression) actual.getNth(i)).get_type();
                actualType = classTable.resolveIfSelfType(actualType, symbolTable);

                if (!classTable.getClassByName(actualType).conformsTo(classTable, expectedType)) {
                    classTable.semantError().println("Statically-invoked method " + name + " arg #" + i + ": was expecting " + expectedType + ", got " + actualType);
                }
            }

    		if (returnType == TreeConstants.SELF_TYPE) {
    		    set_type(type_name);
    		} else {
    		    set_type(calledMethod.return_type);
    		}
        }

    }
    
    public void cgen(CGenUtil util) {
    }
}


/** Defines AST constructor 'dispatch'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class dispatch extends Expression {
    protected Expression expr;
    protected AbstractSymbol name;
    protected Expressions actual;
    /** Creates "dispatch" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for expr
      * @param a1 initial value for name
      * @param a2 initial value for actual
      */
    public dispatch(int lineNumber, Expression a1, AbstractSymbol a2, Expressions a3) {
        super(lineNumber);
        expr = a1;
        name = a2;
        actual = a3;
    }
    public TreeNode copy() {
        return new dispatch(lineNumber, (Expression)expr.copy(), copy_AbstractSymbol(name), (Expressions)actual.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "dispatch\n");
        expr.dump(out, n+2);
        dump_AbstractSymbol(out, n+2, name);
        actual.dump(out, n+2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_dispatch");
	expr.dump_with_types(out, n + 2);
        dump_AbstractSymbol(out, n + 2, name);
        out.println(Utilities.pad(n + 2) + "(");
        for (Enumeration e = actual.getElements(); e.hasMoreElements();) {
	    ((Expression)e.nextElement()).dump_with_types(out, n + 2);
        }
        out.println(Utilities.pad(n + 2) + ")");
	dump_type(out, n);
    }

    //assign types to non-static dispatch (<type>.id(<expr>,...,<expr>) or id(<expr>,...,<expr>)), and make
    // sure that the method is called correctly
    protected void assignTypes(ClassTable classTable, SymbolTable symbolTable) {
        expr.assignTypes(classTable, symbolTable);

        for (int i = 0; i < actual.getLength(); i++) {
            Expression arg = (Expression) actual.getNth(i);
            arg.assignTypes(classTable, symbolTable);
        }

        AbstractSymbol dispatchType = expr.get_type();
        boolean dispatchOnSelf = false;
        if (dispatchType == TreeConstants.SELF_TYPE) {
            dispatchType = (AbstractSymbol) symbolTable.lookup(TreeConstants.self);
		    dispatchOnSelf = true;
        }

        class_c dispatchClass = classTable.getClassByName(dispatchType);

        if (dispatchClass == null) {
            return;
        }

        method calledMethod = dispatchClass.getMethod(name, classTable);
        if (calledMethod == null) {
            classTable.semantError().println("No method " + name + " exists for type " + dispatchType);
            return;
        }

        AbstractSymbol returnType = calledMethod.return_type;

        if (actual.getLength() != calledMethod.formals.getLength()) {
            classTable.semantError().println("Method " + name + " invoked with wrong number of arguments.");
        } else {
            for (int i = 0; i < actual.getLength(); i++) {
                AbstractSymbol expectedType = ((formalc) calledMethod.formals.getNth(i)).type_decl;
                AbstractSymbol actualType = ((Expression) actual.getNth(i)).get_type();
                actualType = classTable.resolveIfSelfType(actualType, symbolTable);

                if (!classTable.getClassByName(actualType).conformsTo(classTable, expectedType)) {
                    classTable.semantError().println("Method " + name + " arg #" + i + ": was expecting " + expectedType + ", got " + actualType);
                }
            }

    		if (returnType == TreeConstants.SELF_TYPE && !dispatchOnSelf) {
    		    set_type(dispatchType);
    		} else {
    		    set_type(calledMethod.return_type);
    		}
        }
    }
    
    public void cgen(CGenUtil util) {
        /*util.push("$fp");

        expr.cgen(util);
        util.out.println("lw $t0 8($a0)");
        int functionOffset = util.getClassByName
        util.out.println("lw $t0 ")

        for (int i = 0; i < actual.getLength(); i++) {
            Expression e = (Expression) actual.getNth(i);
            e.cgen(util);
            util.push("$a0");
        }*/
    }
}


/** Defines AST constructor 'cond'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class cond extends Expression {
    protected Expression pred;
    protected Expression then_exp;
    protected Expression else_exp;
    /** Creates "cond" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for pred
      * @param a1 initial value for then_exp
      * @param a2 initial value for else_exp
      */
    public cond(int lineNumber, Expression a1, Expression a2, Expression a3) {
        super(lineNumber);
        pred = a1;
        then_exp = a2;
        else_exp = a3;
    }
    public TreeNode copy() {
        return new cond(lineNumber, (Expression)pred.copy(), (Expression)then_exp.copy(), (Expression)else_exp.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "cond\n");
        pred.dump(out, n+2);
        then_exp.dump(out, n+2);
        else_exp.dump(out, n+2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_cond");
	pred.dump_with_types(out, n + 2);
	then_exp.dump_with_types(out, n + 2);
	else_exp.dump_with_types(out, n + 2);
	dump_type(out, n);
    }

    //assign type to an if-then-else statement and make sure the bit between if and then 
    // is a Bool
    protected void assignTypes(ClassTable classTable, SymbolTable symbolTable) {
        pred.assignTypes(classTable, symbolTable);
        then_exp.assignTypes(classTable, symbolTable);
        else_exp.assignTypes(classTable, symbolTable);

        if (!TreeConstants.Bool.equals(pred.get_type())) {
            classTable.semantError().println("The predicate of a conditional must be a Bool. Instead, was a: " + pred.get_type());
        }

        set_type(classTable.joinClasses(then_exp.get_type(), else_exp.get_type(), symbolTable));
    }
    
    public void cgen(CGenUtil util) {
    }
}


/** Defines AST constructor 'loop'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class loop extends Expression {
    protected Expression pred;
    protected Expression body;
    /** Creates "loop" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for pred
      * @param a1 initial value for body
      */
    public loop(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        pred = a1;
        body = a2;
    }
    public TreeNode copy() {
        return new loop(lineNumber, (Expression)pred.copy(), (Expression)body.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "loop\n");
        pred.dump(out, n+2);
        body.dump(out, n+2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_loop");
	pred.dump_with_types(out, n + 2);
	body.dump_with_types(out, n + 2);
	dump_type(out, n);
    }

    //assign type to while loop
    protected void assignTypes(ClassTable classTable, SymbolTable symbolTable) {
        pred.assignTypes(classTable, symbolTable);
        body.assignTypes(classTable, symbolTable);

        if (!TreeConstants.Bool.equals(pred.get_type())) {
            classTable.semantError().println("The predicate of a loop must be a Bool. Instead, was a: " + pred.get_type());
        }

        set_type(TreeConstants.Object_);
    }
    
    public void cgen(CGenUtil util) {
    }
}


/** Defines AST constructor 'typcase'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class typcase extends Expression {
    protected Expression expr;
    protected Cases cases;
    /** Creates "typcase" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for expr
      * @param a1 initial value for cases
      */
    public typcase(int lineNumber, Expression a1, Cases a2) {
        super(lineNumber);
        expr = a1;
        cases = a2;
    }
    public TreeNode copy() {
        return new typcase(lineNumber, (Expression)expr.copy(), (Cases)cases.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "typcase\n");
        expr.dump(out, n+2);
        cases.dump(out, n+2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_typcase");
	expr.dump_with_types(out, n + 2);
        for (Enumeration e = cases.getElements(); e.hasMoreElements();) {
	    ((Case)e.nextElement()).dump_with_types(out, n + 2);
        }
	dump_type(out, n);
    }

    /*
     * Assign type to case statement, which is the least type of all the branches
     * The only possible error for type cases is not having any branches at all,
     * which causes a parser, not semantic, error.
     */
    protected void assignTypes(ClassTable classTable, SymbolTable symbolTable) {
        expr.assignTypes(classTable, symbolTable);

        for (int i = 0; i < cases.getLength(); i++) {
            branch b = (branch) cases.getNth(i);

            symbolTable.enterScope();
            symbolTable.addId(b.name, b.type_decl);
            b.expr.assignTypes(classTable, symbolTable);
            symbolTable.exitScope();
        }

        AbstractSymbol returnType = null;
        for (int i = 0; i < cases.getLength(); i++) {
            branch b = (branch) cases.getNth(i);

            if (returnType == null) {
                returnType = b.expr.get_type();
            } else {
                returnType = classTable.joinClasses(returnType, b.expr.get_type(), symbolTable);
            }
        }

        set_type(returnType);
    }    
    
    public void cgen(CGenUtil util) {
    }
}


/** Defines AST constructor 'block'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class block extends Expression {
    protected Expressions body;
    /** Creates "block" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for body
      */
    public block(int lineNumber, Expressions a1) {
        super(lineNumber);
        body = a1;
    }
    public TreeNode copy() {
        return new block(lineNumber, (Expressions)body.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "block\n");
        body.dump(out, n+2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_block");
        for (Enumeration e = body.getElements(); e.hasMoreElements();) {
	    ((Expression)e.nextElement()).dump_with_types(out, n + 2);
        }
	dump_type(out, n);
    }

    //type of last expression in block is type of block
    protected void assignTypes(ClassTable classTable, SymbolTable symbolTable) {
        for (int i = 0; i < body.getLength(); i++) {
            Expression expr = (Expression) body.getNth(i);
            expr.assignTypes(classTable, symbolTable);
        }

        Expression last = (Expression) body.getNth(body.getLength() - 1);
        set_type(last.get_type());
    }
    
    public void cgen(CGenUtil util) {
    }
}


/** Defines AST constructor 'let'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class let extends Expression {
    protected AbstractSymbol identifier;
    protected AbstractSymbol type_decl;
    protected Expression init;
    protected Expression body;
    /** Creates "let" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for identifier
      * @param a1 initial value for type_decl
      * @param a2 initial value for init
      * @param a3 initial value for body
      */
    public let(int lineNumber, AbstractSymbol a1, AbstractSymbol a2, Expression a3, Expression a4) {
        super(lineNumber);
        identifier = a1;
        type_decl = a2;
        init = a3;
        body = a4;
    }
    
    public TreeNode copy() {
        return new let(lineNumber, copy_AbstractSymbol(identifier), copy_AbstractSymbol(type_decl), (Expression)init.copy(), (Expression)body.copy());
    }
    
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "let\n");
        dump_AbstractSymbol(out, n+2, identifier);
        dump_AbstractSymbol(out, n+2, type_decl);
        init.dump(out, n+2);
        body.dump(out, n+2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_let");
        dump_AbstractSymbol(out, n + 2, identifier);
        dump_AbstractSymbol(out, n + 2, type_decl);
        init.dump_with_types(out, n + 2);
        body.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    //assign type to let expression, make sure that assignments of variables in let
    // have types comforming to declared types
    protected void assignTypes(ClassTable classTable, SymbolTable symbolTable) {
        init.assignTypes(classTable, symbolTable);

        symbolTable.enterScope();
        symbolTable.addId(identifier, type_decl);
        body.assignTypes(classTable, symbolTable);
        symbolTable.exitScope();

        if (!classTable.getClassByName(init.get_type()).conformsTo(classTable, type_decl)) {
            classTable.semantError().println("Let variable " + identifier + " has type " + type_decl + " but is assigned to a " + init.get_type());
        }

        set_type(body.get_type());
    }

    
    public void cgen(CGenUtil util) {
    }
}


/** Defines AST constructor 'plus'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class plus extends Expression {
    protected Expression e1;
    protected Expression e2;
    /** Creates "plus" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      * @param a1 initial value for e2
      */
    public plus(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }
    public TreeNode copy() {
        return new plus(lineNumber, (Expression)e1.copy(), (Expression)e2.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "plus\n");
        e1.dump(out, n+2);
        e2.dump(out, n+2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_plus");
	e1.dump_with_types(out, n + 2);
	e2.dump_with_types(out, n + 2);
	dump_type(out, n);
    }

    //assign type to addition expression
    protected void assignTypes(ClassTable classTable, SymbolTable symbolTable) {
		e1.assignTypes(classTable, symbolTable);
		e2.assignTypes(classTable, symbolTable);

        if (!e1.get_type().equals(TreeConstants.Int) || !e2.get_type().equals(TreeConstants.Int)) {
            classTable.semantError().println("Cannot add " + e1.get_type() + " and " + e2.get_type());
        }

		set_type(TreeConstants.Int);
    }    
    
    public void cgen(CGenUtil util) {
        e1.cgen(util);
        util.push("$a0");
        e2.cgen(util);
        util.push("$a0");

        util.out.println("\tjal Object.copy");
        util.getTop("$t2");
        util.pop();
        util.getTop("$t1");
        util.pop();

        util.out.println("\tlw $t1 12($t1)");
        util.out.println("\tlw $t2 12($t2)");
        util.out.println("\tadd $t3 $t1 $t2");
        util.out.println("\tsw $t3 12($a0)");
    }
}


/** Defines AST constructor 'sub'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class sub extends Expression {
    protected Expression e1;
    protected Expression e2;
    /** Creates "sub" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      * @param a1 initial value for e2
      */
    public sub(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }
    public TreeNode copy() {
        return new sub(lineNumber, (Expression)e1.copy(), (Expression)e2.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "sub\n");
        e1.dump(out, n+2);
        e2.dump(out, n+2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_sub");
	e1.dump_with_types(out, n + 2);
	e2.dump_with_types(out, n + 2);
	dump_type(out, n);
    }

    protected void assignTypes(ClassTable classTable, SymbolTable symbolTable) {
        e1.assignTypes(classTable, symbolTable);
		e2.assignTypes(classTable, symbolTable);

        if (!e1.get_type().equals(TreeConstants.Int) || !e2.get_type().equals(TreeConstants.Int)) {
            classTable.semantError().println("Cannot subtract " + e1.get_type() + " from " + e2.get_type());
        }

		set_type(TreeConstants.Int);
    }
    
    public void cgen(CGenUtil util) {
    }
}


/** Defines AST constructor 'mul'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class mul extends Expression {
    protected Expression e1;
    protected Expression e2;
    /** Creates "mul" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      * @param a1 initial value for e2
      */
    public mul(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }
    public TreeNode copy() {
        return new mul(lineNumber, (Expression)e1.copy(), (Expression)e2.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "mul\n");
        e1.dump(out, n+2);
        e2.dump(out, n+2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_mul");
	e1.dump_with_types(out, n + 2);
	e2.dump_with_types(out, n + 2);
	dump_type(out, n);
    }

    protected void assignTypes(ClassTable classTable, SymbolTable symbolTable) {
        e1.assignTypes(classTable, symbolTable);
		e2.assignTypes(classTable, symbolTable);

        if (!e1.get_type().equals(TreeConstants.Int) || !e2.get_type().equals(TreeConstants.Int)) {
            classTable.semantError().println("Cannot multiply " + e1.get_type() + " and " + e2.get_type());
        }

		set_type(TreeConstants.Int);
    }    
    
    public void cgen(CGenUtil util) {
    }
}


/** Defines AST constructor 'divide'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class divide extends Expression {
    protected Expression e1;
    protected Expression e2;
    /** Creates "divide" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      * @param a1 initial value for e2
      */
    public divide(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }
    public TreeNode copy() {
        return new divide(lineNumber, (Expression)e1.copy(), (Expression)e2.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "divide\n");
        e1.dump(out, n+2);
        e2.dump(out, n+2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_divide");
	e1.dump_with_types(out, n + 2);
	e2.dump_with_types(out, n + 2);
	dump_type(out, n);
    }

    protected void assignTypes(ClassTable classTable, SymbolTable symbolTable) {
        e1.assignTypes(classTable, symbolTable);
		e2.assignTypes(classTable, symbolTable);

        if (!e1.get_type().equals(TreeConstants.Int) || !e2.get_type().equals(TreeConstants.Int)) {
            classTable.semantError().println("Cannot divide " + e1.get_type() + " by " + e2.get_type());
        }

		set_type(TreeConstants.Int);
    }    
    
    public void cgen(CGenUtil util) {
    }
}


/** Defines AST constructor 'neg'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class neg extends Expression {
    protected Expression e1;
    /** Creates "neg" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      */
    public neg(int lineNumber, Expression a1) {
        super(lineNumber);
        e1 = a1;
    }
    public TreeNode copy() {
        return new neg(lineNumber, (Expression)e1.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "neg\n");
        e1.dump(out, n+2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_neg");
	e1.dump_with_types(out, n + 2);
	dump_type(out, n);
    }

    protected void assignTypes(ClassTable classTable, SymbolTable symbolTable) {
        e1.assignTypes(classTable, symbolTable);

        if (!e1.get_type().equals(TreeConstants.Int)) {
            classTable.semantError().println("Cannot negate a " + e1.get_type());
        }

        set_type(TreeConstants.Int);
    }    
    
    public void cgen(CGenUtil util) {
    }
}


/** Defines AST constructor 'lt'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class lt extends Expression {
    protected Expression e1;
    protected Expression e2;
    /** Creates "lt" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      * @param a1 initial value for e2
      */
    public lt(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }
    public TreeNode copy() {
        return new lt(lineNumber, (Expression)e1.copy(), (Expression)e2.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "lt\n");
        e1.dump(out, n+2);
        e2.dump(out, n+2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_lt");
	e1.dump_with_types(out, n + 2);
	e2.dump_with_types(out, n + 2);
	dump_type(out, n);
    }

    //assign type to comparison expression. Make sure expressions being compared are Ints
    protected void assignTypes(ClassTable classTable, SymbolTable symbolTable) {
        e1.assignTypes(classTable, symbolTable);
        e2.assignTypes(classTable, symbolTable);

        if (!e1.get_type().equals(TreeConstants.Int) || !e2.get_type().equals(TreeConstants.Int)) {
            classTable.semantError().println("Cannot compare " + e1.get_type() + " < " + e2.get_type());
        }

        set_type(TreeConstants.Bool);
    }    
    
    public void cgen(CGenUtil util) {
    }
}


/** Defines AST constructor 'eq'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class eq extends Expression {
    protected Expression e1;
    protected Expression e2;
    /** Creates "eq" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      * @param a1 initial value for e2
      */
    public eq(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }
    public TreeNode copy() {
        return new eq(lineNumber, (Expression)e1.copy(), (Expression)e2.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "eq\n");
        e1.dump(out, n+2);
        e2.dump(out, n+2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_eq");
	e1.dump_with_types(out, n + 2);
	e2.dump_with_types(out, n + 2);
	dump_type(out, n);
    }

    //assign type to comparison expression. Make sure expressions being compared can be compared
    protected void assignTypes(ClassTable classTable, SymbolTable symbolTable) {
        e1.assignTypes(classTable, symbolTable);
        e2.assignTypes(classTable, symbolTable);

        AbstractSymbol t1 = e1.get_type();
        AbstractSymbol t2 = e2.get_type();

        if (t1.equals(TreeConstants.Int) || t2.equals(TreeConstants.Int)
                || t1.equals(TreeConstants.Bool) || t2.equals(TreeConstants.Bool)
                || t1.equals(TreeConstants.Str) || t2.equals(TreeConstants.Str)) {
            if (!t1.equals(t2)) {
                classTable.semantError().println("Cannot compare " + t1 + " and " + t2);
            }
        }

        set_type(TreeConstants.Bool);
    }
    
    public void cgen(CGenUtil util) {
    }
}


/** Defines AST constructor 'leq'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class leq extends Expression {
    protected Expression e1;
    protected Expression e2;
    /** Creates "leq" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      * @param a1 initial value for e2
      */
    public leq(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }
    public TreeNode copy() {
        return new leq(lineNumber, (Expression)e1.copy(), (Expression)e2.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "leq\n");
        e1.dump(out, n+2);
        e2.dump(out, n+2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_leq");
	e1.dump_with_types(out, n + 2);
	e2.dump_with_types(out, n + 2);
	dump_type(out, n);
    }

    //assign type to comparison expression. Make sure expressions being compared are Ints
    protected void assignTypes(ClassTable classTable, SymbolTable symbolTable) {
        e1.assignTypes(classTable, symbolTable);
        e2.assignTypes(classTable, symbolTable);

        if (!e1.get_type().equals(TreeConstants.Int) || !e2.get_type().equals(TreeConstants.Int)) {
            classTable.semantError().println("Cannot compare " + e1.get_type() + " <= " + e2.get_type());
        }

        set_type(TreeConstants.Bool);
    }
    
    public void cgen(CGenUtil util) {
    }
}


/** Defines AST constructor 'comp'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class comp extends Expression {
    protected Expression e1;
    /** Creates "comp" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      */
    public comp(int lineNumber, Expression a1) {
        super(lineNumber);
        e1 = a1;
    }
    public TreeNode copy() {
        return new comp(lineNumber, (Expression)e1.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "comp\n");
        e1.dump(out, n+2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_comp");
	e1.dump_with_types(out, n + 2);
	dump_type(out, n);
    }

    //assign Bool type to boolean inversion. Make sure expr being inverted is Bool
    protected void assignTypes(ClassTable classTable, SymbolTable symbolTable) {
        e1.assignTypes(classTable, symbolTable);

        if (!e1.get_type().equals(TreeConstants.Bool)) {
            classTable.semantError().println("Cannot negate a " + e1.get_type());
        }

        set_type(TreeConstants.Bool);
    }
    
    public void cgen(CGenUtil util) {
    }
}


/** Defines AST constructor 'int_const'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class int_const extends Expression {
    protected AbstractSymbol token;
    /** Creates "int_const" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for token
      */
    public int_const(int lineNumber, AbstractSymbol a1) {
        super(lineNumber);
        token = a1;
    }
    public TreeNode copy() {
        return new int_const(lineNumber, copy_AbstractSymbol(token));
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "int_const\n");
        dump_AbstractSymbol(out, n+2, token);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_int");
	dump_AbstractSymbol(out, n + 2, token);
	dump_type(out, n);
    }

    protected void assignTypes(ClassTable classTable, SymbolTable symbolTable) {
        set_type(TreeConstants.Int);
    }
    
    public void cgen(CGenUtil util) {
        util.out.println("\tLoad " + token + " into $a0");
    }
}


/** Defines AST constructor 'bool_const'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class bool_const extends Expression {
    protected Boolean val;
    /** Creates "bool_const" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for val
      */
    public bool_const(int lineNumber, Boolean a1) {
        super(lineNumber);
        val = a1;
    }
    public TreeNode copy() {
        return new bool_const(lineNumber, copy_Boolean(val));
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "bool_const\n");
        dump_Boolean(out, n+2, val);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_bool");
	dump_Boolean(out, n + 2, val);
	dump_type(out, n);
    }

    protected void assignTypes(ClassTable classTable, SymbolTable symbolTable) {
        set_type(TreeConstants.Bool);
    }
    
    public void cgen(CGenUtil util) {
        CgenSupport.emitLoadBool(CgenSupport.ACC, new BoolConst(val), util.out);
    }
}


/** Defines AST constructor 'string_const'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class string_const extends Expression {
    protected AbstractSymbol token;
    /** Creates "string_const" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for token
      */
    public string_const(int lineNumber, AbstractSymbol a1) {
        super(lineNumber);
        token = a1;
    }
    public TreeNode copy() {
        return new string_const(lineNumber, copy_AbstractSymbol(token));
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "string_const\n");
        dump_AbstractSymbol(out, n+2, token);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_string");
	out.print(Utilities.pad(n + 2) + "\"");
	Utilities.printEscapedString(out, token.getString());
	out.println("\"");
	dump_type(out, n);
    }

    protected void assignTypes(ClassTable classTable, SymbolTable symbolTable) {
		set_type(TreeConstants.Str);
    }
    
    public void cgen(CGenUtil util) {
        CgenSupport.emitLoadString(CgenSupport.ACC,
                                  (StringSymbol)AbstractTable.stringtable.lookup(token.getString()), util.out);
    }

}


/** Defines AST constructor 'new_'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class new_ extends Expression {
    protected AbstractSymbol type_name;
    /** Creates "new_" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for type_name
      */
    public new_(int lineNumber, AbstractSymbol a1) {
        super(lineNumber);
        type_name = a1;
    }
    public TreeNode copy() {
        return new new_(lineNumber, copy_AbstractSymbol(type_name));
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "new_\n");
        dump_AbstractSymbol(out, n+2, type_name);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_new");
	dump_AbstractSymbol(out, n + 2, type_name);
	dump_type(out, n);
    }

    protected void assignTypes(ClassTable classTable, SymbolTable symbolTable) {
        if (classTable.getClassByName(type_name) == null) {
            classTable.semantError().println("Cannot create new instance of unknown class: " + type_name);
        } else {
            set_type(type_name);
        }
    }
    
    public void cgen(CGenUtil util) {
    }

}


/** Defines AST constructor 'isvoid'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class isvoid extends Expression {
    protected Expression e1;
    /** Creates "isvoid" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      */
    public isvoid(int lineNumber, Expression a1) {
        super(lineNumber);
        e1 = a1;
    }
    public TreeNode copy() {
        return new isvoid(lineNumber, (Expression)e1.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "isvoid\n");
        e1.dump(out, n+2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_isvoid");
	e1.dump_with_types(out, n + 2);
	dump_type(out, n);
    }

    protected void assignTypes(ClassTable classTable, SymbolTable symbolTable) {
        e1.assignTypes(classTable, symbolTable);
        set_type(TreeConstants.Bool);
    }
    
    public void cgen(CGenUtil util) {
    }
}


/** Defines AST constructor 'no_expr'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class no_expr extends Expression {
    /** Creates "no_expr" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      */
    public no_expr(int lineNumber) {
        super(lineNumber);
    }
    public TreeNode copy() {
        return new no_expr(lineNumber);
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "no_expr\n");
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_no_expr");
	dump_type(out, n);
    }

    protected void assignTypes(ClassTable classTable, SymbolTable symbolTable) {

    }    
    
    public void cgen(CGenUtil util) {
    }

}


/** Defines AST constructor 'object'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class object extends Expression {
    protected AbstractSymbol name;
    /** Creates "object" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for name
      */
    public object(int lineNumber, AbstractSymbol a1) {
        super(lineNumber);
        name = a1;
    }
    public TreeNode copy() {
        return new object(lineNumber, copy_AbstractSymbol(name));
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "object\n");
        dump_AbstractSymbol(out, n+2, name);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_object");
	dump_AbstractSymbol(out, n + 2, name);
	dump_type(out, n);
    }

    protected void assignTypes(ClassTable classTable, SymbolTable symbolTable) {
        if (name == TreeConstants.self) {
            set_type(TreeConstants.SELF_TYPE);
        } else {
			AbstractSymbol tableLookup = (AbstractSymbol) symbolTable.lookup(name);

			if (tableLookup != null) {
				set_type(tableLookup);
				return;
			}

			AbstractSymbol selfType = (AbstractSymbol) symbolTable.lookup(TreeConstants.self);
			class_c selfClass = classTable.getClassByName(selfType);
            attr attrLookup = selfClass.getAttr(name, classTable);

            if (attrLookup != null) {
                set_type(attrLookup.type_decl);
            } else {
                classTable.semantError().println("No variable " + name + " found in scope.");
            }
        }
    }    
    
    public void cgen(CGenUtil util) {
    }
}