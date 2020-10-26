module mod.aimpl {
    requires mod.a;
    provides a.A with aimpl.AImpl;
}