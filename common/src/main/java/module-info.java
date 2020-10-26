module mod.common {
    requires static lombok;
    exports common;
    requires mod.a;
    requires mod.b;
    uses a.A;
    uses b.B;
}