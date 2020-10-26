module mod.app {
    requires static lombok;
    requires mod.b;
    requires static mod.bimpl;
    requires mod.a;
    requires mod.common;
    exports app;
}