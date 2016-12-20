package org.firstinspires.ftc.teamcodez;

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
 * Created by Admin on 10/12/2016.
 */

@TeleOp(name = "TeleOP", group = "Sensor")
public class TeleOP extends LinearOpMode{
    DcMotor frontLeft;
    DcMotor backLeft;
    DcMotor frontRight;
    DcMotor backRight;
    DcMotor shooter;

    Servo flipperRight;
    Servo flipperLeft;
    Servo boot;
    int mode;



    public void runOpMode() throws InterruptedException {
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backRight = hardwareMap.dcMotor.get("backRight");
        shooter = hardwareMap.dcMotor.get("shooter");

        flipperLeft = hardwareMap.servo.get("flipperLeft");
        flipperRight = hardwareMap.servo.get("flipperRight");
        boot = hardwareMap.servo.get("boot");

        flipperRight.setPosition(0.08);
        flipperLeft.setPosition(0.94);
        boot.setPosition(0.85);

        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        mode = 1;

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        frontRight.setMaxSpeed(3500);
        frontLeft.setMaxSpeed(3500);
        backLeft.setMaxSpeed(3500);
        backRight.setMaxSpeed(3500);

        waitForStart();


        while (opModeIsActive()) {

            float y_raw = gamepad1.left_stick_y;
            float x_raw = gamepad1.left_stick_x;
            float z_raw = gamepad1.right_stick_x;
            float xscale = (float) 0.5;
            float yscale = (float) 0.5;
            float zscale = (float) 0.5;
            float x = xscale*(float)Math.pow(x_raw, 3.0) + (1-xscale)*x_raw;
            float y = yscale*(float)Math.pow(y_raw, 3.0) + (1-yscale)*y_raw;
            float z = zscale*(float)Math.pow(z_raw, 3.0) + (1-zscale)*z_raw;

            float fr = 0;
            float bk = 0;
            float lft = 0;
            float rt = 0;

            if(mode==1) {
                fr = -y+x+z;
                bk = -y+x-z;
                lft =y+x-z;
                rt = y+x+z;
            }

            else if (mode ==2) {
                fr = y+z;
                lft =-x-z;
                rt = -x+z;
                bk = y-z;
            }

            if (gamepad1.dpad_right){
                mode =1;
            }
            if (gamepad1.dpad_left){
                mode =2;
            }
            if (gamepad2.dpad_down){
                boot.setPosition(0.85);
            }
            if (gamepad2.dpad_up){
                boot.setPosition(0.15);
            }


            float [] joystickVals = new float[] {fr, lft, rt, bk};
            float maxVal = Math.abs(fr);
            for (int i = 1; i<4; i++)
            {
                if(Math.abs(joystickVals[i])>maxVal){
                    maxVal = Math.abs(joystickVals[i]);
                }
            }

            if ((maxVal)>1){
                fr/=maxVal;
                lft/=maxVal;
                rt/=maxVal;
                bk/=maxVal;

            }

            frontLeft.setPower(fr);
            backLeft.setPower(lft);
            frontRight.setPower(rt);
            backRight.setPower(bk);

            idle();
        }

    }
}
