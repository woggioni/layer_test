module test.app {
    requires test.b;
    requires test.a;
    uses b.B;
    uses a.A;
    exports app;
}