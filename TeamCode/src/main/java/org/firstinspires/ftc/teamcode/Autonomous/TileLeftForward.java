package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.AutonomousPaths;

@Autonomous(group = "Autonomous", name = "Tile Left Forward")
public class TileLeftForward extends AutonomousPaths {
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        waitForStart();
        tileSidewaysForwards("left");
    }
}
