package com.lonntec.sufuserservice.lang;

import com.lonntec.framework.lang.MicroServiceException;
import com.lonntec.framework.lang.StateCode;

public class SufUserSystemException extends MicroServiceException{
    public SufUserSystemException(StateCode stateCode) {
        super(stateCode);
    }
    public SufUserSystemException(StateCode stateCode, String message) {
        super(stateCode, message);
    }

    public SufUserSystemException(StateCode stateCode, Throwable cause) {
        super(stateCode, cause);
    }

    public SufUserSystemException(StateCode stateCode, String message, Throwable cause) {
        super(stateCode, message, cause);
    }
}
