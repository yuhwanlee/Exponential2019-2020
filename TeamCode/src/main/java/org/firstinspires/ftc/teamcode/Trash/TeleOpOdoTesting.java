package org.firstinspires.ftc.teamcode.Trash;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Exponential_Methods;
import org.firstinspires.ftc.teamcode.TeleOp.TeleOpMethods;

@TeleOp(name = "Odometry Testing", group = "TeleOp")
public class TeleOpOdoTesting extends TeleOpMethods {
    private double[] rototePoint(double x, double y, double angle /*in degrees*/) {
        double[] translatedPoint = new double[2];
        double angleRad = Math.PI / 180 * angle;
        translatedPoint[0] = x * Math.cos(angleRad) - y * Math.sin(angleRad);
        translatedPoint[1] = y * Math.cos(angleRad) + x * Math.sin(angleRad);
        return translatedPoint;
    }

    double odoForwardsError = 0; // the amount of encoders the forward odometry wheel rotates per radian of robot rotation,
    // negative indicates it loses encoder counts when rotating anti-clockwise
    double odoSidewaysError = 0; // the amount of encoders the sideways odometry wheel rotates per radian of robot rotation

    double xTarget = convertInchToEncoderOdom(0); // In encoders, relative to the field, x coordinate with the origin being the start
    double yTarget = convertInchToEncoderOdom(0); // In encoders, relative to the field, y coordinate with the origin being the start
    double xRobotPos = 0; // in terms of field
    double yRobotPos = 0; // in terms of field
    double xRobotVel = 0;
    double yRobotVel = 0;

    //IMU from -180 to 180
    double initialAngle = getRotationinDimension('Z'); // -180 to 180
    double lastAngleIMU = initialAngle; // -180 to 180
    double currentAngle = initialAngle; // -inf to inf
    double lastodoWheelSidewaysPosition = odoWheelSideways.getCurrentPosition();
    double lastodoWheelForwardsPosition = odoWheelForwards.getCurrentPosition();

    double tolerance = convertInchToEncoderOdom(0); // Tolerance in encoders
    double areaXDis = 0; // perspective of field
    double areaYDis = 0; // perspective of field

    ElapsedTime time = new ElapsedTime();


    public void driveTrain() {
        double currentAngleIMU = getRotationinDimension('Z');
        double changeInAngle;

        // Allows angle to go greater than 180 and less than -180
        if (Math.abs(lastAngleIMU - currentAngleIMU) > 300) {
            if (lastAngleIMU > currentAngleIMU) {
                currentAngle += currentAngleIMU - lastAngleIMU + 360;
                changeInAngle = currentAngleIMU - lastAngleIMU + 360;
            } else {
                currentAngle += currentAngleIMU - lastAngleIMU - 360;
                changeInAngle = currentAngleIMU - lastAngleIMU - 360;
            }
        } else {
            currentAngle += currentAngleIMU - lastAngleIMU;
            changeInAngle = currentAngleIMU - lastAngleIMU;
        }
        lastAngleIMU = currentAngleIMU;
        //telemetry.addData("angle", lastAngle);
        //telemetry.update();

        double arcDistance = Math.sqrt((odoWheelSideways.getCurrentPosition() - lastodoWheelSidewaysPosition - odoSidewaysError * (changeInAngle))
                * (odoWheelSideways.getCurrentPosition() - lastodoWheelSidewaysPosition - odoSidewaysError * (changeInAngle))
                + (odoWheelForwards.getCurrentPosition() - lastodoWheelForwardsPosition - odoForwardsError * (changeInAngle))
                * (odoWheelForwards.getCurrentPosition() - lastodoWheelForwardsPosition - odoForwardsError * (changeInAngle)));

        if (changeInAngle != 0) {
            // Since the robot rotates with the arc, the distance the odometry wheels measure is going to be the distance of the arc
            double radius = Math.abs(arcDistance / (Math.PI / 180 * changeInAngle));
            // Segment of the arc is the chord that represents the total displacement of the robot as it travelled on the arc

            xRobotPos += rototePoint(radius * (1 - Math.cos(changeInAngle * Math.PI / 180)), radius * (Math.sin(changeInAngle * Math.PI / 180)), currentAngle - initialAngle)[0];
            yRobotPos += rototePoint(radius * (1 - Math.cos(changeInAngle * Math.PI / 180)), radius * (Math.sin(changeInAngle * Math.PI / 180)), currentAngle - initialAngle)[1];
            xRobotVel = rototePoint(radius * (1 - Math.cos(changeInAngle * Math.PI / 180)), radius * (Math.sin(changeInAngle * Math.PI / 180)), currentAngle - initialAngle)[0] / time.seconds();
            yRobotVel = rototePoint(radius * (1 - Math.cos(changeInAngle * Math.PI / 180)), radius * (Math.sin(changeInAngle * Math.PI / 180)), currentAngle - initialAngle)[1] / time.seconds();
        } else {
            xRobotPos += rototePoint(odoWheelSideways.getCurrentPosition() - lastodoWheelSidewaysPosition, odoWheelForwards.getCurrentPosition() - lastodoWheelForwardsPosition, currentAngle - initialAngle)[0];
            yRobotPos += rototePoint(odoWheelSideways.getCurrentPosition() - lastodoWheelSidewaysPosition, odoWheelForwards.getCurrentPosition() - lastodoWheelForwardsPosition, currentAngle - initialAngle)[1];
            xRobotVel = rototePoint(odoWheelSideways.getCurrentPosition() - lastodoWheelSidewaysPosition, odoWheelForwards.getCurrentPosition() - lastodoWheelForwardsPosition, currentAngle - initialAngle)[0] / time.seconds();
            yRobotVel = rototePoint(odoWheelSideways.getCurrentPosition() - lastodoWheelSidewaysPosition, odoWheelForwards.getCurrentPosition() - lastodoWheelForwardsPosition, currentAngle - initialAngle)[1] / time.seconds();
        }
        // Segment of the arc is the chord that represents the total displacement of the robot as it travelled on the arc

        // Rotation of the displacement to get the displacement relative to the robot
        double xDisplacement = (xTarget - xRobotPos) * Math.cos(-(currentAngle - initialAngle) * Math.PI / 180) - (yTarget - yRobotPos) * Math.sin(-(currentAngle + initialAngle) * Math.PI / 180); // Displacement is relative to robot
        double yDisplacement = (xTarget - xRobotPos) * Math.sin(-(currentAngle - initialAngle) * Math.PI / 180) + (yTarget - yRobotPos) * Math.cos(-(currentAngle + initialAngle) * Math.PI / 180); // Displacement is relative to robot

        areaXDis += (xTarget - xRobotPos) * time.seconds(); // in terms of the field
        areaYDis += (yTarget - yRobotPos) * time.seconds(); // in terms of the field

        // TODO: 2/19/2020 figure out if dLin is supposed to be negative or positive
            /*frontLeft.setPower(
                    Range.clip(pLin * (yDisplacement - xDisplacement)
                            + iLin * (rototePoint(areaXDis, areaYDis, -currentAngle + initialAngle)[1] - rototePoint(areaXDis, areaYDis, -currentAngle + initialAngle)[0])
                            + dLin * (rototePoint(xRobotVel, yRobotVel, -currentAngle + initialAngle)[1] - rototePoint(xRobotVel, yRobotVel, -currentAngle + initialAngle)[0])
                            - pRot * (-currentAngle + initialAngle), -maxPower, maxPower));
            frontRight.setPower(
                    Range.clip(pLin * (yDisplacement + xDisplacement)
                            + iLin * (rototePoint(areaXDis, areaYDis, -currentAngle + initialAngle)[1] + rototePoint(areaXDis, areaYDis, -currentAngle + initialAngle)[0])
                            + dLin * (rototePoint(xRobotVel, yRobotVel, -currentAngle + initialAngle)[1] + rototePoint(xRobotVel, yRobotVel, -currentAngle + initialAngle)[0])
                            + pRot * (-currentAngle + initialAngle), -maxPower, maxPower));
            backLeft.setPower(
                    Range.clip(pLin * (yDisplacement + xDisplacement)
                            + iLin * (rototePoint(areaXDis, areaYDis, -currentAngle + initialAngle)[1] + rototePoint(areaXDis, areaYDis, -currentAngle + initialAngle)[0])
                            + dLin * (rototePoint(xRobotVel, yRobotVel, -currentAngle + initialAngle)[1] + rototePoint(xRobotVel, yRobotVel, -currentAngle + initialAngle)[0])
                            - pRot * (-currentAngle + initialAngle), -maxPower, maxPower));
            backRight.setPower(
                    Range.clip(pLin * (yDisplacement - xDisplacement)
                            + iLin * (rototePoint(areaXDis, areaYDis, -currentAngle + initialAngle)[1] - rototePoint(areaXDis, areaYDis, -currentAngle + initialAngle)[0])
                            + dLin * (rototePoint(xRobotVel, yRobotVel, -currentAngle + initialAngle)[1] - rototePoint(xRobotVel, yRobotVel, -currentAngle + initialAngle)[0])
                            + pRot * (-currentAngle + initialAngle), -maxPower, maxPower));
            */
        double magnitude = Math.sqrt(xDisplacement * xDisplacement + yDisplacement * yDisplacement);
        double[] answer = circle_to_taxicab(gamepad1.left_stick_x, gamepad1.left_stick_y, ROTATE_TO_MOVE_RATIO * gamepad1.right_stick_x);
        double speed = 1;
        frontRight.setPower(speed * answer[0]);
        backRight.setPower(speed * answer[1]);
        backLeft.setPower(speed * answer[2]);
        frontLeft.setPower(speed * answer[3]);
        lastodoWheelSidewaysPosition = odoWheelSideways.getCurrentPosition();
        lastodoWheelForwardsPosition = odoWheelForwards.getCurrentPosition();
        telemetry.addData("robotX", convertEncoderToInchOdom(xRobotPos));
        telemetry.addData("robotY", convertEncoderToInchOdom(yRobotPos));
        telemetry.addData("Angle", convertEncoderToInchOdom(currentAngle));
        telemetry.addData("Time of Loop", time.seconds());
        telemetry.update();
        time.reset();
    }
}

