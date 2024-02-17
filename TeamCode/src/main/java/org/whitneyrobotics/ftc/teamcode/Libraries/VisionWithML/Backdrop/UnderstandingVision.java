package org.whitneyrobotics.ftc.teamcode.Libraries.VisionWithML.Backdrop;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

import android.graphics.Camera;

import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.stream.CameraStreamServer;
import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource;

public class UnderstandingVision {
    public Camera webcam;
    public CameraStreamServer cameraStreamServer;
    public CameraStreamSource cameraStreamSource;
    public Continuation continuation;

    public UnderstandingVision(){
        //continuation = new Continuation(T target);
        //cameraStreamSource.getFrameBitmap(Continuation);
        //cameraStreamServer.setSource()
    }
}
