package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Admin on 11/21/2016.
 */

@TeleOp(name = "ServoPosition", group = "Sensor")
public class ServoPosition extends LinearOpMode{
    Servo flipperRight;
    Servo flipperLeft;
    Servo ballDoor;
    double positionLeft;
    double positionRight;
    double positionBoot;


    @Override
    public void runOpMode() throws InterruptedException {
        flipperLeft = hardwareMap.servo.get("flipperLeft");
        flipperRight = hardwareMap.servo.get("flipperRight");
        ballDoor = hardwareMap.servo.get("ballDoor");

        flipperRight.setPosition(0);
        flipperLeft.setPosition(0);
        ballDoor.setPosition(0);

        waitForStart();

        while (opModeIsActive()){
            if(gamepad1.dpad_up){
                positionLeft = positionLeft +.01;
                if (positionLeft>1){
                    positionLeft = 1;
                }
            }
            else if (gamepad1.dpad_down){
                positionLeft = positionLeft -.01;
                if (positionLeft<0){
                    positionLeft =0;
                }
            }

            if(gamepad1.dpad_right){
                positionLeft = positionLeft +.0001;
                if (positionLeft>1){
                    positionLeft = 1;
                }
            }
            else if (gamepad1.dpad_left){
                positionLeft = positionLeft -.0001;
                if (positionLeft<0){
                    positionLeft =0;
                }
            }

            if(gamepad1.y){
                positionRight = positionRight + .01;
                if(positionRight>1){
                    positionRight = 1;
                }
            }
            else if(gamepad1.a){
                positionRight = positionRight - .01;
                if (positionRight<0){
                    positionRight=0;
                }
                }

            if (gamepad1.x) {
                positionRight = positionRight + .0001;
                if (positionRight > 1) {
                    positionRight = 1;
                }
            }
            else if(gamepad1.b) {
                positionRight = positionRight - .0001;
                if (positionRight < 0) {
                    positionRight = 0;
                }

            }

            flipperLeft.setPosition(positionLeft);
            flipperRight.setPosition(positionRight);
            ballDoor.setPosition(positionLeft);

            telemetry.addData("Right Servo", positionRight);
            telemetry.addData("Left Servo", positionLeft);
            telemetry.addData("Boot", positionBoot);
            telemetry.update();

            idle();
        }
    }
}

/* Servo Notes
        Perpedicular Position:

        Right: 0.6239
        Left: 0.37529999999

        Upright Position:

        Right: 0.0
        Left: 1

        Downward Position
        Right: 1.0
        Left: 0.0
 */