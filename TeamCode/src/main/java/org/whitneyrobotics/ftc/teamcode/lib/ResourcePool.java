package org.whitneyrobotics.ftc.teamcode.lib;

import com.qualcomm.robotcore.hardware.HardwareDevice;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;

public class ResourcePool {

    public ArrayList<HardwareDevice> devices = new ArrayList<>();
    public HashSet<HardwareDevice> devicesInUse = new HashSet<>();

    public PriorityQueue e;
}
