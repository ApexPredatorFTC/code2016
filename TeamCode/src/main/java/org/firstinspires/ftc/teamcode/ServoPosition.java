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
    Servo rightClaw;
    Servo leftClaw;
    //Servo liftClaw;

    double positionLeft;
    double positionRight;

    double leftSideClaw;
    double rightSideClaw;
    double upClaw;



    @Override
    public void runOpMode() throws InterruptedException {
        flipperLeft = hardwareMap.servo.get("flipperLeft");
        flipperRight = hardwareMap.servo.get("flipperRight");
        ballDoor = hardwareMap.servo.get("ballDoor");
        rightClaw = hardwareMap.servo.get("rightClaw");
        leftClaw = hardwareMap.servo.get("leftClaw");
        //liftClaw = hardwareMap.servo.get("liftClaw");

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

            if (gamepad2.dpad_down){
            leftSideClaw = leftSideClaw -0.01;
                if (leftSideClaw<0){
                    leftSideClaw = 0;
                }
            }
            else if (gamepad2.dpad_up){
                leftSideClaw = leftSideClaw +0.01;
                if (leftSideClaw>1){
                    leftSideClaw=1;
                  }

            }
            if (gamepad2.dpad_left) {
                rightSideClaw = rightSideClaw -0.01;
                if (rightSideClaw<0) {
                    rightSideClaw=0;
                }
            }
            else if (gamepad2.dpad_right) {
                rightSideClaw = rightSideClaw + 0.01;
                if (rightSideClaw > 1) {
                    rightSideClaw = 1;
                }

            }
            if (gamepad2.x) {
                upClaw = upClaw -0.01;
                if (upClaw<0){
                    upClaw=0;
                }


            }
            else if (gamepad2.a){
                upClaw = upClaw +0.01;
                if (upClaw>1){
                    upClaw=1;
                }
            }

            flipperLeft.setPosition(positionLeft);
            flipperRight.setPosition(positionRight);
            ballDoor.setPosition(positionLeft);
            leftClaw.setPosition(leftSideClaw);
            rightClaw.setPosition(rightSideClaw);
            //
            // liftClaw.setPosition(upClaw);


            telemetry.addData("Right Servo", positionRight);
            telemetry.addData("Left Servo", positionLeft);
            telemetry.addData("Right Claw", rightSideClaw);
            telemetry.addData("Left Claw", leftSideClaw);
            //0telemetry.addData("Lifter", upClaw);
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

        Claw:
         Down: .21
         Up: 1

        Right Claw:
         Out (collect): .12
         In (holding): .46
         Wider out: .73

        Left Claw:
         Out: .79
         In: .37
         Wider out: .21

 */