package org.firstinspires.ftc.teamcode;

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
    DcMotor liftClaw;

    Servo flipperRight;
    Servo flipperLeft;
    Servo ballDoor;
    Servo rightClaw;
    Servo leftClaw;


    int mode;



    public void runOpMode() throws InterruptedException {
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backRight = hardwareMap.dcMotor.get("backRight");
        shooter = hardwareMap.dcMotor.get("shooter");


        flipperLeft = hardwareMap.servo.get("flipperLeft");
        flipperRight = hardwareMap.servo.get("flipperRight");
        ballDoor = hardwareMap.servo.get("ballDoor");
        rightClaw = hardwareMap.servo.get("rightClaw");
        leftClaw = hardwareMap.servo.get("leftClaw");
        liftClaw = hardwareMap.dcMotor.get("liftClaw");


        flipperRight.setPosition(0.08);
        flipperLeft.setPosition(0.94);

        rightClaw.setPosition(.12);
        leftClaw.setPosition(.79);
        ballDoor.setPosition(.4);


        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        mode = 2;

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftClaw.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontRight.setMaxSpeed(4700);
        frontLeft.setMaxSpeed(4700);
        backLeft.setMaxSpeed(4700);
        backRight.setMaxSpeed(4700);

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

            float y2_raw =  gamepad2.right_stick_y;
            //float liftscale = (float) 0.07;
            float lift;

            if(y2_raw < 0){
                lift = (0.3f * y2_raw);

            }
            else{
                lift = (y2_raw * 0.2f);
            }



            //float lift = liftscale*(float)Math.pow(y2_raw, 3) + ((liftscale) * y2_raw);



            float fr = 0;
            float bk = 0;
            float lft = 0;
            float rt = 0;

            if(mode==1) {
                fr = x+z;
                lft = y-z;
                rt = y+z;
                bk = x-z;
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

            if (gamepad2.y){
                shooter.setPower(1);
            }
            else if (gamepad2.x){
                shooter.setPower(0.8);
            }
            else if (gamepad2.a){
                shooter.setPower(-0.8);
            }
            else {
                shooter .setPower(0);
            }

            if (gamepad2.dpad_down){
                rightClaw.setPosition(.46);
                leftClaw.setPosition(.18);
            }
            else if (gamepad2.dpad_up){
                rightClaw.setPosition(.12);
                leftClaw.setPosition(.5);
            }

           /* if (gamepad2.right_stick_y < 0){
                liftClaw.setPower(0.15);
            }
            else if (gamepad2.right_stick_y > 0){
                liftClaw.setPower(-0.4);
            }
            else{
                liftClaw.setPower(0);
            }*/




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

            if (lift >1){
                lift = 1;
            }
            else if (lift < -1){
                lift = -1;
            }

            frontLeft.setPower(fr);
            backLeft.setPower(lft);
            frontRight.setPower(rt);
            backRight.setPower(bk);
            liftClaw.setPower(lift);




            idle();
        }

    }
}
