/*
 * Copyright (C) 2011-2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * This file is auto-generated. DO NOT MODIFY!
 * The source Renderscript file: C:\\Users\\Smidadi\\Desktop\\RenduAndroid\\ProjetTechRS-master\\app\\src\\main\\rs\\histogram.rs
 */

package com.example.sushi;

import androidx.renderscript.*;
import com.example.sushi.histogramBitCode;

/**
 * @hide
 */
public class ScriptC_histogram extends ScriptC {
    private static final String __rs_resource_name = "histogram";
    // Constructor
    public  ScriptC_histogram(RenderScript rs) {
        super(rs,
              __rs_resource_name,
              histogramBitCode.getBitCode32(),
              histogramBitCode.getBitCode64());
        __I32 = Element.I32(rs);
        __F32 = Element.F32(rs);
        __U8_4 = Element.U8_4(rs);
    }

    private Element __F32;
    private Element __I32;
    private Element __U8_4;
    private FieldPacker __rs_fp_F32;
    private FieldPacker __rs_fp_I32;
    private final static int mExportVarIdx_histo = 0;
    private int[] mExportVar_histo;
    public synchronized void set_histo(int[] v) {
        mExportVar_histo = v;
        FieldPacker fp = new FieldPacker(1024);
        for (int ct1 = 0; ct1 < 256; ct1++) {
            fp.addI32(v[ct1]);
        }

        int []__dimArr = new int[1];
        __dimArr[0] = 256;
        setVar(mExportVarIdx_histo, fp, __I32, __dimArr);
    }

    public int[] get_histo() {
        return mExportVar_histo;
    }

    public Script.FieldID getFieldID_histo() {
        return createFieldID(mExportVarIdx_histo, null);
    }

    private final static int mExportVarIdx_remapArray = 1;
    private float[] mExportVar_remapArray;
    public synchronized void set_remapArray(float[] v) {
        mExportVar_remapArray = v;
        FieldPacker fp = new FieldPacker(1024);
        for (int ct1 = 0; ct1 < 256; ct1++) {
            fp.addF32(v[ct1]);
        }

        int []__dimArr = new int[1];
        __dimArr[0] = 256;
        setVar(mExportVarIdx_remapArray, fp, __F32, __dimArr);
    }

    public float[] get_remapArray() {
        return mExportVar_remapArray;
    }

    public Script.FieldID getFieldID_remapArray() {
        return createFieldID(mExportVarIdx_remapArray, null);
    }

    private final static int mExportVarIdx_size = 2;
    private int mExportVar_size;
    public synchronized void set_size(int v) {
        setVar(mExportVarIdx_size, v);
        mExportVar_size = v;
    }

    public int get_size() {
        return mExportVar_size;
    }

    public Script.FieldID getFieldID_size() {
        return createFieldID(mExportVarIdx_size, null);
    }

    private final static int mExportForEachIdx_root = 0;
    public Script.KernelID getKernelID_root() {
        return createKernelID(mExportForEachIdx_root, 59, null, null);
    }

    public void forEach_root(Allocation ain, Allocation aout) {
        forEach_root(ain, aout, null);
    }

    public void forEach_root(Allocation ain, Allocation aout, Script.LaunchOptions sc) {
        // check ain
        if (!ain.getType().getElement().isCompatible(__U8_4)) {
            throw new RSRuntimeException("Type mismatch with U8_4!");
        }
        // check aout
        if (!aout.getType().getElement().isCompatible(__U8_4)) {
            throw new RSRuntimeException("Type mismatch with U8_4!");
        }
        Type t0, t1;        // Verify dimensions
        t0 = ain.getType();
        t1 = aout.getType();
        if ((t0.getCount() != t1.getCount()) ||
            (t0.getX() != t1.getX()) ||
            (t0.getY() != t1.getY()) ||
            (t0.getZ() != t1.getZ()) ||
            (t0.hasFaces()   != t1.hasFaces()) ||
            (t0.hasMipmaps() != t1.hasMipmaps())) {
            throw new RSRuntimeException("Dimension mismatch between parameters ain and aout!");
        }

        forEach(mExportForEachIdx_root, ain, aout, null, sc);
    }

    private final static int mExportForEachIdx_remaptoRGB = 1;
    public Script.KernelID getKernelID_remaptoRGB() {
        return createKernelID(mExportForEachIdx_remaptoRGB, 59, null, null);
    }

    public void forEach_remaptoRGB(Allocation ain, Allocation aout) {
        forEach_remaptoRGB(ain, aout, null);
    }

    public void forEach_remaptoRGB(Allocation ain, Allocation aout, Script.LaunchOptions sc) {
        // check ain
        if (!ain.getType().getElement().isCompatible(__U8_4)) {
            throw new RSRuntimeException("Type mismatch with U8_4!");
        }
        // check aout
        if (!aout.getType().getElement().isCompatible(__U8_4)) {
            throw new RSRuntimeException("Type mismatch with U8_4!");
        }
        Type t0, t1;        // Verify dimensions
        t0 = ain.getType();
        t1 = aout.getType();
        if ((t0.getCount() != t1.getCount()) ||
            (t0.getX() != t1.getX()) ||
            (t0.getY() != t1.getY()) ||
            (t0.getZ() != t1.getZ()) ||
            (t0.hasFaces()   != t1.hasFaces()) ||
            (t0.hasMipmaps() != t1.hasMipmaps())) {
            throw new RSRuntimeException("Dimension mismatch between parameters ain and aout!");
        }

        forEach(mExportForEachIdx_remaptoRGB, ain, aout, null, sc);
    }

    private final static int mExportFuncIdx_createRemapArray = 0;
    public Script.InvokeID getInvokeID_createRemapArray() {
        return createInvokeID(mExportFuncIdx_createRemapArray);
    }

    public void invoke_createRemapArray() {
        invoke(mExportFuncIdx_createRemapArray);
    }

}

