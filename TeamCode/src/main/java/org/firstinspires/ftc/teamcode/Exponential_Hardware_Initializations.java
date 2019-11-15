package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.Servo;

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
    protected Servo intakeServoLeft;
    protected Servo intakeServoRight;
    protected Servo hookServo;
    protected DcMotor slideUp;
    protected DcMotor slideDown;
    protected DcMotor[] driveMotors = new DcMotor[4];

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
        intakeServoLeft = hardwareMap.servo.get("intakeServoLeft");
        intakeServoRight = hardwareMap.servo.get("intakeServoRight");

//        hookServo = hardwareMap.servo.get("hookServo");
//        intakeLeft = hardwareMap.dcMotor.get("intakeLeft");
//        intakeRight = hardwareMap.dcMotor.get("intakeRight");
//        slideUp = hardwareMap.dcMotor.get("slideLeft");
//        slideDown = hardwareMap.dcMotor.get("slideRight");



        imu = hardwareMap.get(BNO055IMU.class, "imu");

        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);
        //Note that one of the intake motors has to be set to reverse but we don't know yet
        //Note that one of the slides motors MIGHT have to be set reverse
        //Note that one of the intake servos has to be set to reverse Servo.Direction.REVERSE


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
//          intakeRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        slideUp.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        slideDown.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }
}