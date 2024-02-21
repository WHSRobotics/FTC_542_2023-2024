package org.whitneyrobotics.ftc.teamcode.Libraries.TensorFlow;

import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tflite.java.TfLite;

import org.tensorflow.lite.TensorFlowLite;

public class TF_Util {
    public static Task<Void> retrieveInitializeTask(Context hardwareContext){
        return TfLite.initialize(hardwareContext);
    }


}
