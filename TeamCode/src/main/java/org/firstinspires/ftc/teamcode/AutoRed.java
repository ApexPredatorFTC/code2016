package org.firstinspires.ftc.teamcode;

import android.graphics.Color;
import android.text.method.Touch;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

/**
 * Created by Admin on 11/19/2016.
 */
@Autonomous(name = "AutoRed", group = "Sensor")
public class AutoRed extends LinearOpMode {
    DcMotor frontLeft;
    DcMotor backLeft;
    DcMotor frontRight;
    DcMotor backRight;
    double frontLeftEncoder;
    double frontRightEncoder;
    double backRightEncoder;
    double backLeftEncoder;
    double encoderCounts;
    Servo flipperRight;
    Servo flipperLeft;
    ColorSensor cSensor1;
    ColorSensor cSensor2;
    TouchSensor touchRight;
    TouchSensor touchLeft;


    public void runOpMode() throws InterruptedException {
        //Motors
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

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        frontRight.setMaxSpeed(2700);
        frontLeft.setMaxSpeed(2700);
        backLeft.setMaxSpeed(2700);
        backRight.setMaxSpeed(2700);

        //Servos
        flipperRight = hardwareMap.servo.get("flipperRight");
        flipperLeft = hardwareMap.servo.get("flipperLeft");
        //flipperRight.setPosition(0.728399999999);
        //flipperLeft.setPosition(0.27999999999999);

        //Color Sensor
        float hsvValues[] = {0F, 0F, 0F};
        float hsvValues1[] = {0F, 0F, 0F};
        final float values[] = hsvValues;
        final float values1[] = hsvValues1;

        cSensor1 = hardwareMap.colorSensor.get("cSensor1");
        cSensor2 = hardwareMap.colorSensor.get("cSensor2");

        cSensor1.setI2cAddress(I2cAddr.create7bit(0x1E));
        cSensor2.setI2cAddress(I2cAddr.create7bit(0x26));

        Color.RGBToHSV(cSensor1.red() * 8, cSensor1.green() * 8, cSensor1.blue() * 8, hsvValues);
        Color.RGBToHSV(cSensor2.red() * 8, cSensor2.green() * 8, cSensor2.blue() * 8, hsvValues1);

        waitForStart();
        Turn(false, 1000);
        idle();




       /* while (opModeIsActive()) {

            frontLeftEncoder = frontLeft.getCurrentPosition();
            frontRightEncoder = frontRight.getCurrentPosition();
            backLeftEncoder = backLeft.getCurrentPosition();
            backRightEncoder = backRight.getCurrentPosition();


            telemetry.update();
            idle();
        }*/
    }

    public void Forward(double distanceFeet, double speed) {
        encoderCounts = distanceFeet * 87.5 * 12;
        boolean Forward = true;

        frontRight.setMaxSpeed(2700);
        frontLeft.setMaxSpeed(2700);
        backLeft.setMaxSpeed(2700);
        backRight.setMaxSpeed(2700);

        while (Forward) {
            frontLeftEncoder = frontLeft.getCurrentPosition();
            backRightEncoder = backRight.getCurrentPosition();

            if (-frontLeftEncoder >= encoderCounts && -backRightEncoder >= encoderCounts) {
                frontLeft.setPower(0);
                backRight.setPower(0);
                Forward = false;
            } else if (-frontLeftEncoder < encoderCounts && -backRightEncoder >= encoderCounts) {
                frontLeft.setPower(-speed);
                backRight.setPower(0);
            } else if (-frontLeftEncoder >= encoderCounts && -backRightEncoder < encoderCounts) {
                frontLeft.setPower(0);
                backRight.setPower(-speed);
            } else if (-frontLeftEncoder < encoderCounts && -backRightEncoder < encoderCounts) {
                frontLeft.setPower(-speed);
                backRight.setPower(-speed);
            }
            telemetry.addData("frontLeftEncoder", frontLeftEncoder);
            telemetry.addData("backRightEncoder", backRightEncoder);
            telemetry.update();
        }
    }

    public void scan() {

    }

    public void reset() {
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void Backward(double distanceFeet, double speed) {
        encoderCounts = distanceFeet * 87.5 * 12;
        boolean Backward = true;

        frontRight.setMaxSpeed(2700);
        frontLeft.setMaxSpeed(2700);
        backLeft.setMaxSpeed(2700);
        backRight.setMaxSpeed(2700);

        while (Backward) {
            frontLeftEncoder = frontLeft.getCurrentPosition();
            backRightEncoder = backRight.getCurrentPosition();

            if (frontLeftEncoder >= encoderCounts && backRightEncoder >= encoderCounts) {
                frontLeft.setPower(0);
                backRight.setPower(0);
                Backward = false;
            } else if (frontLeftEncoder < encoderCounts && backRightEncoder >= encoderCounts) {
                frontLeft.setPower(speed);
                backRight.setPower(0);
            } else if (frontLeftEncoder >= encoderCounts && backRightEncoder < encoderCounts) {
                frontLeft.setPower(0);
                backRight.setPower(speed);
            } else if (frontLeftEncoder < encoderCounts && backRightEncoder < encoderCounts) {
                frontLeft.setPower(speed);
                backRight.setPower(speed);
            }
            telemetry.addData("frontLeftEncoder", frontLeftEncoder);
            telemetry.addData("backRightEncoder", backRightEncoder);
            telemetry.update();
        }


    }

    public void Right(double distanceFeet, double speed) {
        encoderCounts = distanceFeet * 87.5 * 12;
        boolean Right = true;

        frontRight.setMaxSpeed(2700);
        frontLeft.setMaxSpeed(2700);
        backLeft.setMaxSpeed(2700);
        backRight.setMaxSpeed(2700);

        while (Right) {
            frontRightEncoder = frontRight.getCurrentPosition();
            backLeftEncoder = backLeft.getCurrentPosition();

            if (-frontRightEncoder >= encoderCounts && -backLeftEncoder >= encoderCounts) {
                frontRight.setPower(0);
                backLeft.setPower(0);
                Right = false;
            } else if (-frontRightEncoder < encoderCounts && -backLeftEncoder >= encoderCounts) {
                frontRight.setPower(-speed);
                backLeft.setPower(0);
            } else if (-frontRightEncoder >= encoderCounts && -backLeftEncoder < encoderCounts) {
                frontRight.setPower(0);
                backLeft.setPower(-speed);
            } else if (-frontRightEncoder < encoderCounts && -backLeftEncoder < encoderCounts) {
                frontRight.setPower(-speed);
                backLeft.setPower(-speed);
            }
            telemetry.addData("frontRightEncoder", frontRightEncoder);
            telemetry.addData("backLeftEncoder", backLeftEncoder);
            telemetry.update();
        }
    }

    public void Left(double distanceFeet, double speed) {
        encoderCounts = distanceFeet * 87.5 * 12;
        boolean Left = true;

        frontRight.setMaxSpeed(2700);
        frontLeft.setMaxSpeed(2700);
        backLeft.setMaxSpeed(2700);
        backRight.setMaxSpeed(2700);

        while (Left) {
            frontRightEncoder = frontRight.getCurrentPosition();
            backLeftEncoder = backLeft.getCurrentPosition();

            if (frontRightEncoder >= encoderCounts && -backLeftEncoder >= encoderCounts) {
                frontRight.setPower(0);
                backLeft.setPower(0);
                Left = false;
            } else if (frontRightEncoder < encoderCounts && backLeftEncoder >= encoderCounts) {
                frontRight.setPower(speed);
                backLeft.setPower(0);
            } else if (frontRightEncoder >= encoderCounts && backLeftEncoder < encoderCounts) {
                frontRight.setPower(0);
                backLeft.setPower(speed);
            } else if (frontRightEncoder < encoderCounts && backLeftEncoder < encoderCounts) {
                frontRight.setPower(speed);
                backLeft.setPower(speed);
            }
            telemetry.addData("frontRightEncoder", frontRightEncoder);
            telemetry.addData("backLeftEncoder", backLeftEncoder);
            telemetry.update();
        }
    }
    public void Turn(boolean direction, double degrees){
        boolean Turn = true;

        while(Turn){
            frontLeftEncoder = frontLeft.getCurrentPosition();
        if(direction == true) {
            if (frontLeftEncoder >= degrees) {
                frontLeft.setPower(0);
                frontRight.setPower(0);
                backLeft.setPower(0);
                backRight.setPower(0);
                Turn = false;
            } else {
                frontLeft.setPower(0.5);
                frontRight.setPower(0.5);
                backLeft.setPower(-0.5);
                backRight.setPower(-0.5);
            }
        }
            else{
            if (-frontLeftEncoder >= degrees) {
                frontLeft.setPower(0);
                frontRight.setPower(0);
                backLeft.setPower(0);
                backRight.setPower(0);
                Turn = false;
            } else {
                frontLeft.setPower(-0.5);
                frontRight.setPower(-0.5);
                backLeft.setPower(0.5);
                backRight.setPower(0.5);
            }
        }
        }





    }
}
    /*public void Backward(double distanceFeet, double speed){
        double frontLeftEncoder = 0;
        double backRightEncoder = 0;
        double encoderCounts;
        encoderCounts = distanceFeet * 87.5 * 12;

        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        while(frontLeftEncoder <= encoderCounts && backRightEncoder <= encoderCounts){
            frontLeftEncoder = frontLeft.getCurrentPosition();
            backRightEncoder = backRight.getCurrentPosition();

            frontLeft.setPower(speed);
            backRight.setPower(speed);

            telemetry.addData("frontLeftEncoder", frontLeftEncoder);
            telemetry.addData("backRightEncoder", backRightEncoder);
            telemetry.update();
        }

    }
*/


/* Motor Encoder Notes
    backRight and frontLeft encoders are negative when going towards sensors

 */