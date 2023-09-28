package com.bankcomm.demobankcomm.utils;

import com.bankcomm.demobankcomm.dto.NewSignDTO;
import com.bankcomm.demobankcomm.entity.NewSign;

/**
 * Created with IntelliJ IDEA.
 * User: Phantom Sean
 * Date: 2023/9/28
 * Time: 14:18
 */
public class NewSignConvert {
    // entity -> dto
    public static NewSignDTO toFrontEnd(NewSign newSign) {
        return new NewSignDTO(newSign.getId(), ByteConvertUtil.byteArray2BinaryString(newSign.getSigned()));
    }

    // dto -> entity
    public static NewSign toBackEnd(NewSignDTO newSignDTO) {
        return new NewSign(newSignDTO.getId(), ByteConvertUtil.string2ByteArray(newSignDTO.getSigned()));
    }

}

