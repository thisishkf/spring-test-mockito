package com.thisishkf.springmockito.service;

import com.thisishkf.springmockito.exception.BunnyNotHealthyException;
import com.thisishkf.springmockito.model.Bunny;

public interface DoctorService {
    boolean check(Bunny bunny) throws BunnyNotHealthyException;

    Bunny heal(Bunny bunny);
}
