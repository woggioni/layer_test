module test.bimpl {
    requires test.b;
    provides b.B with bimpl.BImpl;
}