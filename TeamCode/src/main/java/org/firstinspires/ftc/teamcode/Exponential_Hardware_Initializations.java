package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.hardware.bosch.BNO055IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;


public abstract class Exponential_Hardware_Initializations extends LinearOpMode {
    protected DcMotor frontLeft;
    protected DcMotor frontRight;
    protected DcMotor backLeft;
    protected DcMotor backRight;
    protected DcMotor intakeLeft;
    protected DcMotor intakeRight;
    protected DcMotor slideLeft;
    protected DcMotor slideRight;
    protected DcMotor[] driveMotors = new DcMotor[4];
    protected CRServo rightIntakeServo;
    protected CRServo leftIntakeServo;

    protected Orientation orientation = new Orientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES,0,0,0,0);
    protected BNO055IMU imu;
    double initialHeading;
    double initialPitch;
    double initialRoll;

    @Override
    public void runOpMode() throws InterruptedException {
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        backRight = hardwareMap.dcMotor.get("backRight");
        rightIntakeServo = hardwareMap.crservo.get("servoIntakeRight");
        leftIntakeServo = hardwareMap.crservo.get("servoIntakeLeft");

//        intakeLeft = hardwareMap.dcMotor.get("intakeLeft");
//        intakeRight = hardwareMap.dcMotor.get("intakeRight");
//        slideLeft = hardwareMap.dcMotor.get("slideLeft");
//        slideRight = hardwareMap.dcMotor.get("slideRight");

        imu = hardwareMap.get(BNO055IMU.class, "imu");

        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);
        //Note that one of the intake motors has to be set to reverse but we don't know yet

        driveMotors[0] = frontLeft;
        driveMotors[1] = frontRight;
        driveMotors[2] = backLeft;
        driveMotors[3] = backRight;

        for(DcMotor motor : driveMotors){
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

//        intakeLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        intakeRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        slideLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        slideRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }
}