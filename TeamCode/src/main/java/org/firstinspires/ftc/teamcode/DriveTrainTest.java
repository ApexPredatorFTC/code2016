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
 * Created by Admin on 9/21/2016.
 */

@TeleOp(name = "DriveTrainTest", group = "Sensor")

public class DriveTrainTest  extends LinearOpMode{

    DcMotor r1;
    DcMotor r2;
    DcMotor l1;
    DcMotor l2;

    public void runOpMode() throws InterruptedException {

        r2 = hardwareMap.dcMotor.get("r2");
        l1 = hardwareMap.dcMotor.get("l1");
        r1 = hardwareMap.dcMotor.get("r1");
        l2 = hardwareMap.dcMotor.get("l2");
        r1.setDirection(DcMotor.Direction.REVERSE);
        r2.setDirection(DcMotor.Direction.FORWARD);
        l1.setDirection(DcMotor.Direction.FORWARD);
        l2.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();

        while (opModeIsActive()) {

            r1.setPower(gamepad1.right_stick_y);
            r2.setPower(gamepad1.right_stick_y);
            l1.setPower(gamepad1.left_stick_y);
            l2.setPower(gamepad1.left_stick_y);
        }
    }

    }

