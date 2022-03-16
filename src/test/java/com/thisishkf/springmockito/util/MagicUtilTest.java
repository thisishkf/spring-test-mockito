package com.thisishkf.springmockito.util;

import com.thisishkf.springmockito.model.Bunny;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MagicUtilTest {

    @Test
    public void clone_shouldReturnNewBunny() {
        Bunny bunny = new Bunny("parent", true);
        Bunny newBunny = MagicUtil.clone(bunny);
        assertTrue(bunny != newBunny);
        assertTrue(bunny.getName().equals(newBunny.getName()));
        assertTrue(bunny.isHealthy() == newBunny.isHealthy());
    }
}
