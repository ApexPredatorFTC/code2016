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
    public void runOpMode()
    {
        shooter = hardwareMap.dcMotor.get("shooter");
        shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        waitForStart();

        while (opModeIsActive())
        {
            if(gamepad1.x)
            {
                shooter.setPower(1.0);
                telemetry.addData("Button Pressed", 1.0);
                telemetry.update();
            }
            else if (gamepad1.a){
                shooter.setPower(.8);
            }
            else
            {
                shooter.setPower(0);
            }
        }
    }
}
