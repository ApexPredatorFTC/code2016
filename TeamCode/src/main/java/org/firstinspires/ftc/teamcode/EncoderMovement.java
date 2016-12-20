package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.TouchSensor;
/**
 * Created by Admin on 10/29/2016.
 */
@Autonomous(name = "Encoder Movement", group = "Sensor")
@Disabled

public class EncoderMovement extends LinearOpMode {
    DcMotor frontLeft;
    DcMotor backLeft;
    DcMotor frontRight;
    DcMotor backRight;
    double frontLeftEncoder;
    double frontRightEncoder;
    double backRightEncoder;
    double backLeftEncoder;
    double encoderCounts;


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

            telemetry.addData("frontLeft", frontLeftEncoder);
            telemetry.addData("frontRight", frontRightEncoder);
            telemetry.addData("backLeft", backLeftEncoder);
            telemetry.addData("backRight", backRightEncoder);
            telemetry.update();

         /*   Forward(3, .5);

            telemetry.addData("Target Encoder", encoderCounts);
            telemetry.addData("frontLeft Encoder", frontLeftEncoder);
            telemetry.addData("backRight Encoder", backRightEncoder);
            telemetry.update(); */
        }

    }

  /*  public void Forward (double distanceFeet, double speed){

        encoderCounts = distanceFeet*87.5*12;

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        if(frontLeftEncoder >= encoderCounts && backRightEncoder >= encoderCounts){
            frontLeft.setPower(0);
            backRight.setPower(0);
        }
        else if (frontLeftEncoder < encoderCounts && backRightEncoder >= encoderCounts)
        {
            frontLeft.setPower(speed);
            backRight.setPower(0);
        }
        else if (frontLeftEncoder >= encoderCounts && backRightEncoder < encoderCounts){
            frontLeft.setPower(0);
            backRight.setPower(speed);
        }
        else{
            frontLeft.setPower(speed);
            backRight.setPower(speed);
        }
*/


  //  }
}