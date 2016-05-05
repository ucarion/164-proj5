class Main inherits IO {
    main(): Object {{
        test_attr();
        test_fn_args(1, 2);
        test_if(true);
        test_if(false);

        test_typcase("");
        test_typcase(0);
        test_typcase(false);
        test_typcase(new Box);
        test_typcase(self);

        test_loop();
        test_recursion(0);
        test_let();
        test_math();
        test_default_init_and_isvoid();

        test_inheritance();
        test_static_dispatch();
    }};

    a: Int <- 5;

    test_attr(): Object {{
        out_int(a);
        a <- 10;
        out_int(a);
    }};

    test_fn_args(a: Int, b: Int): Object {{
        out_int(a);
        out_int(b);
        out_int(a + b);
    }};

    test_if(a: Bool): Object {{
        out_int(if a then 1 else 2 fi);
    }};

    test_typcase(a: Object): Object {{
        case a of
            a: String => out_string(a);
            a: Int => out_int(a);
            a: Bool => out_string(a.type_name());
            a: Box => out_string("box");
            a: Main => out_string("main");
        esac;
    }};

    x: Int <- 0;

    test_loop(): Object {{
        while x < 10 loop {
            out_int(x);
            x <- x + 1;
        } pool;
    }};

    test_recursion(x: Int): Object {{
        if x < 10 then {
            out_int(x);
            test_recursion(x + 1);
        } else self fi;
    }};

    test_let(): Object {{
        let x: Int <- 3 in {
            out_int(x);

            let x: Int <- 5 in {
                out_int(x);
            };
        };
    }};

    test_math(): Object {{
        out_int(2 + 3);
        out_int(2 * 3);
        out_int(2 - 3);
        out_int(10 / 3);
        out_int(~3);
        out_string(bool_to_str(1 < 2));
        out_string(bool_to_str(2 < 2));
        out_string(bool_to_str(1 <= 2));
        out_string(bool_to_str(2 <= 2));
        out_string(bool_to_str(3 <= 2));
        out_string(bool_to_str(1 = 1));
        out_string(bool_to_str(1 = 2));
    }};

    bool_to_str(a: Bool): String {
        if a then "true" else "false" fi
    };

    default_int: Int;
    default_str: String;
    default_bool: Bool;
    default_obj: Object;

    test_default_init_and_isvoid(): Object {{
        out_int(default_int);
        out_string(default_str);
        out_string(bool_to_str(default_bool));
        out_string(bool_to_str(isvoid default_obj));
        out_string(bool_to_str(isvoid (new Object)));
    }};

    test_inheritance(): Object {{
        out_int((new A).getA());
        out_int((new A).getB());
        out_int((new B).getA());
        out_int((new B).getB());
        out_int((new C).getA());
        out_int((new C).getB());
    }};

    test_static_dispatch(): Object {{
        out_int((new B)@A.getA());
        out_int((new C)@B.getB());
    }};
};

class Box {
    a: Int <- 1337;

    set(x: Int): SELF_TYPE {{
        a <- x;
        self;
    }};

    get(): Int { a };
};

class A {
    a: Int <- 6;

    getA(): Int { a };
    getB(): Int { 2 };
};

class B inherits A {
    b: Int <- 8;

    getB(): Int { b };
    getA(): Int { 42 };
};

class C inherits B {
    getB(): Int { getA() };
};
