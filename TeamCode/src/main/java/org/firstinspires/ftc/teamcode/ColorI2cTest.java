
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.util.ElapsedTime;

//Impprts for I2C
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;

@TeleOp(name="ColorI2cTest", group="Linear Opmode")
//@Disabled

public class ColorI2cTest extends LinearOpMode
{
    //Constand for I2C Address and commands
    public static final I2cAddr c1Addr = I2cAddr.create7bit(0x1E);
    public static final I2cAddr c2Addr = I2cAddr.create7bit(0x26);
    public static final int c1StartReg = 0x04;
    public static final int c1ReadLength = 26;
    public static final int c1CmdReg = 0x03;

    public static final int c1PassiveCmd = 0x01;
    public static final int c1ActiveCmd = 0x00;

    //Elapsed Timer Object
    private ElapsedTime runtime = new ElapsedTime();

    //Create I2C Objects
    public I2cDevice c1;
    public I2cDeviceSynch c1Reader;
    public I2cDevice c2;
    public I2cDeviceSynch c2Reader;

    //I2C Result arrays
    byte[] c1Cache;
    byte[] c2Cache;

    @Override
    public void runOpMode()
    {
        //Map the hardware
        c1 = hardwareMap.i2cDevice.get("cSensor1");
        c2 = hardwareMap.i2cDevice.get("cSensor2");
        c1Reader = new I2cDeviceSynchImpl(c1, c1Addr, false);
        c2Reader = new I2cDeviceSynchImpl(c2, c2Addr, false);
        c1Reader.engage();
        c2Reader.engage();

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        //Set Color Sensor to Passive Mode
        c1Reader.write8(c1CmdReg, c1ActiveCmd);
        c2Reader.write8(c1CmdReg, c1ActiveCmd);
        //Wait for the user to press play and reset the timer
        waitForStart();
        runtime.reset();

        //Run until user presses stop
        while (opModeIsActive())
        {
            //Send command to dump all registers
            c1Reader.write8(c1CmdReg, c1PassiveCmd);
            c2Reader.write8(c1CmdReg, c1PassiveCmd);

            c1Cache = c1Reader.read(c1StartReg, c1ReadLength);
            c2Cache = c2Reader.read(c1StartReg, c1ReadLength);

            //Print Registers
            telemetry.addData("Run Time", runtime.toString());
            telemetry.addData("Color #",  (c1Cache[0] & 0xff) + " " + (c2Cache[0] & 0xff));
            telemetry.addData("Red", (c1Cache[1] & 0xff) + " " + (c2Cache[1] & 0xff));
            telemetry.addData("Green", (c1Cache[2] & 0xff) + " " + (c2Cache[2] & 0xff));
            telemetry.addData("Blue", (c1Cache[3] & 0xff) + " " + (c2Cache[3] & 0xff));
            telemetry.addData("White", (c1Cache[4] & 0xff) + " " + (c2Cache[4] & 0xff));
            telemetry.addData("Color Index Number", (c1Cache[5] & 0xff) + " " + (c2Cache[5] & 0xff));
            telemetry.addData("Red Index", (c1Cache[6] & 0xff) + " " + (c2Cache[6] & 0xff));
            telemetry.addData("Green Index", (c1Cache[7] & 0xff) + " " + (c2Cache[7] & 0xff));
            telemetry.addData("Blue Index", (c1Cache[8] & 0xff) + " " + (c2Cache[8] & 0xff));
            telemetry.addData("Undefined", (c1Cache[9] & 0xff) + " " + (c2Cache[9] & 0xff));
            telemetry.addData("Red LSB", (c1Cache[10] & 0xff) + " " + (c2Cache[10] & 0xff));
            telemetry.addData("Green LSB", (c1Cache[12] & 0xff) + " " + (c2Cache[12] & 0xff));
            telemetry.addData("Blue LSB", (c1Cache[14] & 0xff) + " " + (c2Cache[14] & 0xff));
            telemetry.addData("White LSB", (c1Cache[16] & 0xff) + " " + (c2Cache[16] & 0xff));
            telemetry.addData("Norm Red LSB", (c1Cache[18] & 0xff) + " " + (c2Cache[18] & 0xff));
            telemetry.addData("Norm Green LSB", (c1Cache[20] & 0xff) + " " + (c2Cache[20] & 0xff));
            telemetry.addData("Norm Blue LSB", (c1Cache[22] & 0xff) + " " + (c2Cache[22] & 0xff));
            telemetry.addData("Norm White LSB", (c1Cache[24] & 0xff) + " " + (c2Cache[24] & 0xff));
            telemetry.update();

            // eg: Run wheels in tank mode (note: The joystick goes negative when pushed forwards)
            // leftMotor.setPower(-gamepad1.left_stick_y);
            // rightMotor.setPower(-gamepad1.right_stick_y);
        }
    }
}
