package org.whitneyrobotics.ftc.teamcode.Libraries.VisionWithML.Backdrop;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

public class BackdropConvertOpenCVPipeline extends OpenCvPipeline {
    @Override
    public Mat processFrame(Mat input) {
        List<Mat> channels = new ArrayList<>();
        Core.flip(input.t(), input, 2);
        Core.split(input, channels);

        int resolution = 10;

        Mat redChannel = channels.get(2);
        Mat greenChannel = channels.get(1);
        Mat blueChannel = channels.get(0);

        boolean leftReqOne, leftReqTwo, leftReqThree = false;
        boolean rightReqOne, rightReqTwo, rightReqThree = false;

        //double

        for (int i = 0; i < resolution; i++){
            Rect edge = new Rect(0 + input.cols() / resolution,input.rows() / 2, input.cols(), input.rows() / resolution);
            if (true){//leftReqOne && leftReqTwo && leftReqThree){
                break;
            }

            Mat currentRegion = new Mat(redChannel, edge);


            //if (Mat )

        }



        return null;
    }
}
