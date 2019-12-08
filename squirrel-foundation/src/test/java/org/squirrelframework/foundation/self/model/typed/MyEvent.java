package org.squirrelframework.foundation.self.model.typed;

public enum MyEvent {
    // - external -
    ab,
    ac,
    ad,

    ba,
    bc,

    cd,

    _b, // any -> b
    _c, // any -> c

    // - internal -
    it,

    // - local -
    lc,
}