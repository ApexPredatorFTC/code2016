
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;



@TeleOp(name="Motor Test", group="Linear Opmode")  // @Autonomous(...) is the other common choice
//@Disabled
public class MotorTest extends LinearOpMode {
    DcMotor robotMotor;


    @Override
    public void runOpMode() {
        robotMotor  = hardwareMap.dcMotor.get("robotMotor");
        robotMotor.setDirection(DcMotor.Direction.FORWARD);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive())
        {
            float position=gamepad1.right_stick_y;
            robotMotor.setPower(position);
            //telemetry.addData("Status", "Run Time: " + runtime.toString());
            //telemetry.update();

            // eg: Run wheels in tank mode (note: The joystick goes negative when pushed forwards)
            // leftMotor.setPower(-gamepad1.left_stick_y);
            // rightMotor.setPower(-gamepad1.right_stick_y);
        }
    }
}
