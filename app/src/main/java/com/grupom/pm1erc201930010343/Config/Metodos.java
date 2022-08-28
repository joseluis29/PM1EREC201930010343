package com.grupom.pm1erc201930010343.Config;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
public class Metodos {

    public byte[] setBitmaptobyte(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] ArrayFoto  = stream.toByteArray();
        return ArrayFoto;

    }
}
