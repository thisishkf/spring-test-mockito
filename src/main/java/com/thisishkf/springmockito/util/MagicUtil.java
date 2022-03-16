package com.thisishkf.springmockito.util;

import com.thisishkf.springmockito.model.Bunny;

public class MagicUtil {
    public static Bunny clone(Bunny bunny) {
        return new Bunny(bunny.getName(), bunny.isHealthy());
    }
}
