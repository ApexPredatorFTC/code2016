package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Admin on 12/1/2016.
 */
@TeleOp(name = "MotorSet", group = "sensor")
public class MotorSet extends LinearOpMode{

    DcMotor shooter;

    @Override
    public void runOpMode() throws InterruptedException {
        shooter = hardwareMap.dcMotor.get("shooter");

        shooter.setPower(1);
        sleep(200);
        shooter.setPower(0);
    }
}
