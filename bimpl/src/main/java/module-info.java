module mod.bimpl {
    requires mod.b;
    provides b.B with bimpl.BImpl;
    exports bimpl.api;
}