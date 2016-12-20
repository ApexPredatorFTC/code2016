package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.Servo;
/**
 * Created by Admin on 11/5/2016.
 */

@TeleOp(name = "ColorCompare", group = "Sensor")
//@Disabled
public class ColorCompare extends LinearOpMode{

    ColorSensor cSensor1;
    ColorSensor cSensor2;



    @Override
    public void runOpMode() throws InterruptedException{

        float hsvValues[] = {0F,0F,0F};
        float hsvValues1[] = {0F,0F,0F};
        final float values[] = hsvValues;
        final float values1[] = hsvValues1;
        cSensor1 = hardwareMap.colorSensor.get("cSensor1");
        cSensor2 = hardwareMap.colorSensor.get("cSensor2");

       cSensor1.setI2cAddress(I2cAddr.create7bit(0x1E));
       cSensor2.setI2cAddress(I2cAddr.create7bit(0x26));




        boolean bPrevState = false;
        boolean bCurrState = false;

        boolean bLedOn = true;

        waitForStart();

        while(opModeIsActive()){

            bCurrState = gamepad1.x;

            if ((bCurrState == true) && (bCurrState != bPrevState))  {

                // button is transitioning to a pressed state. So Toggle LED
                bLedOn = !bLedOn;
                cSensor1.enableLed(bLedOn);
                cSensor2.enableLed(bLedOn);
            }

            bPrevState = bCurrState;

            Color.RGBToHSV(cSensor1.red() * 8, cSensor1.green() * 8, cSensor1.blue() * 8, hsvValues);
            Color.RGBToHSV(cSensor2.red() * 8, cSensor2.green() * 8, cSensor2.blue() * 8, hsvValues1);
            // send the info back to driver station using telemetry function.
            telemetry.addData("LED", bLedOn ? "On" : "Off");
            telemetry.addData("Clear1", cSensor1.alpha());
            telemetry.addData("Red1  ", cSensor1.red());
            telemetry.addData("Green1", cSensor1.green());
            telemetry.addData("Blue1 ", cSensor1.blue());
            telemetry.addData("Hue", hsvValues[0]);

            telemetry.addData("LED", bLedOn ? "On" : "Off");
            telemetry.addData("Clear", cSensor2.alpha());
            telemetry.addData("Red  ", cSensor2.red());
            telemetry.addData("Green", cSensor2.green());
            telemetry.addData("Blue ", cSensor2.blue());
            telemetry.addData("Hue", hsvValues1[0]);

            telemetry.update();

            idle();
        }
    }
}
