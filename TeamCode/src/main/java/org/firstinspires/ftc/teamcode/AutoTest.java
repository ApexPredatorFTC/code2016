package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by Admin on 10/31/2016.
 */
@Autonomous(name = "AutoTest", group = "Sensor")
public class AutoTest extends LinearOpMode{
        DcMotor frontLeft;
        DcMotor backLeft;
        DcMotor frontRight;
        DcMotor backRight;
        double frontLeftEncoder;
        double frontRightEncoder;
        double backRightEncoder;
        double backLeftEncoder;
        double encoderCounts;
        int x;


    public void runOpMode() throws InterruptedException {
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backRight = hardwareMap.dcMotor.get("backRight");

        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        x=1;

        frontRight.setMaxSpeed(2500);
        frontLeft.setMaxSpeed(2500);
        backLeft.setMaxSpeed(2500);
        backRight.setMaxSpeed(2500);

        waitForStart();
        while (opModeIsActive()) {


            frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            frontLeftEncoder = frontLeft.getCurrentPosition();
            frontRightEncoder = frontRight.getCurrentPosition();
            backLeftEncoder = backLeft.getCurrentPosition();
            backRightEncoder = backRight.getCurrentPosition();

            Forward(0.3, 0.5);
            /*if (x==1) {
                frontRight.setPower(.5);
                sleep(500);
                frontRight.setPower(0);
                frontLeft.setPower(.5);
                sleep(500);
                frontLeft.setPower(0);
                backLeft.setPower(.5);
                sleep(500);
                backLeft.setPower(0);
                backRight.setPower(.5);
                sleep(500);
                backRight.setPower(0);
                x=2;
            }*/


            telemetry.addData("Target Encoder", encoderCounts);
            telemetry.addData("frontRight Encoder", frontRightEncoder);
            telemetry.addData("backLeft Encoder", backLeftEncoder);
            telemetry.addData("backRight Encoder", backRightEncoder);
            telemetry.addData("frontLeft Encoder", frontLeftEncoder);

            telemetry.update();
            idle(); //Always call idle at bottom of while(opMode is active)
        }
    }


    public void Forward(double distanceFeet, double speed){
                encoderCounts = -distanceFeet*87.5*12;

            if(frontRightEncoder >= encoderCounts && backLeftEncoder >= encoderCounts){
                frontRight.setPower(0);
                backLeft.setPower(0);
            }
            else if (frontRightEncoder < encoderCounts && backLeftEncoder >= encoderCounts)
            {
                frontRight.setPower(-speed);
                backLeft.setPower(0);
            }
            else if (frontRightEncoder >= encoderCounts && backLeftEncoder < encoderCounts){
                frontRight.setPower(0);
                backLeft.setPower(-speed);
            }
            else{
                frontRight.setPower(-speed);
                backLeft.setPower(-speed);
            }
        }
    public void turnClockwise(double distanceFeet, double speed){
        encoderCounts = distanceFeet*87.5*12;


    }
}



