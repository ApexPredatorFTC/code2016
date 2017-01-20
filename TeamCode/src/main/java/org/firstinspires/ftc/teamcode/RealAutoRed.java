package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Admin on 11/27/2016.
 */

@Autonomous(name = "RealAutoRed", group = "Sensor")
public class RealAutoRed extends LinearOpMode {
    DcMotor frontLeft;
    DcMotor backLeft;
    DcMotor frontRight;
    DcMotor backRight;
    DcMotor shooter;

    Servo flipperRight;
    Servo flipperLeft;
    Servo ballDoor;


    /*ColorSensor cSensor1;
    ColorSensor cSensor2;*/

    TouchSensor touchRight;
    TouchSensor touchLeft;
    ModernRoboticsI2cGyro gyro = null;

    static final double COUNTS_PER_MOTOR_REV = 1100;    // eg: TETRIX Motor Encoder
    static final double DRIVE_GEAR_REDUCTION = 1.0;     // This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES = 4.0;     // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);

    static final double DRIVE_SPEED = 0.7;     // Nominal speed for better accuracy.
    static final double TURN_SPEED = 0.5;     // Nominal half speed for better accuracy.

    static final double HEADING_THRESHOLD = 1;      // As tight as we can make it with an integer gyro
    static final double P_TURN_COEFF = 0.017;     // Larger is more responsive, but also less stable
    static final double P_DRIVE_COEFF = 0.10;     // Larger is more responsive, but also less stable
    //Color Sensor

    public static final I2cAddr c1Addr = I2cAddr.create7bit(0x1E);
    public static final I2cAddr c2Addr = I2cAddr.create7bit(0x26);
    public static final I2cAddr c3Addr = I2cAddr.create8bit(0x50);

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
    public I2cDevice c3;
    public I2cDeviceSynch c3Reader;


    //I2C Result arrays
    byte[] c1Cache;
    byte[] c2Cache;
    byte[] c3Cache;


    @Override

    public void runOpMode() {
        //Motors
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backRight = hardwareMap.dcMotor.get("backRight");
        shooter = hardwareMap.dcMotor.get("shooter");

        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.REVERSE);
        shooter.setDirection(DcMotorSimple.Direction.FORWARD);

        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        frontRight.setMaxSpeed(4000);
        frontLeft.setMaxSpeed(4000);
        backLeft.setMaxSpeed(4000);
        backRight.setMaxSpeed(4000);

        //Touch
        touchRight = hardwareMap.touchSensor.get("touchRight");
        touchLeft = hardwareMap.touchSensor.get("touchLeft");

        //Gyro
        gyro = (ModernRoboticsI2cGyro) hardwareMap.gyroSensor.get("gyro");

        telemetry.addData(">", "Calibrating Gyro");    //
        telemetry.update();

        gyro.calibrate();


        // make sure the gyro is calibrated before continuing
        while (gyro.isCalibrating()) {
            sleep(50);
        }
        telemetry.addData(">", "Robot Ready.");    //
        telemetry.update();

        gyro.resetZAxisIntegrator();

        //Servos
        flipperRight = hardwareMap.servo.get("flipperRight");
        flipperLeft = hardwareMap.servo.get("flipperLeft");
        ballDoor = hardwareMap.servo.get("ballDoor");

        flipperRight.setPosition(0);
        flipperLeft.setPosition(1);
        ballDoor.setPosition(1);


        //Color Sensor
        c1 = hardwareMap.i2cDevice.get("cSensor1");
        c2 = hardwareMap.i2cDevice.get("cSensor2");
        c3 = hardwareMap.i2cDevice.get("cSensor3");

        c1Reader = new I2cDeviceSynchImpl(c1, c1Addr, false);
        c2Reader = new I2cDeviceSynchImpl(c2, c2Addr, false);
        c3Reader = new I2cDeviceSynchImpl(c3, c3Addr, false);


        c1Reader.engage();
        c2Reader.engage();
        c3Reader.engage();


        telemetry.addData("Status", "Initialized");
        telemetry.update();

        //Set Color Sensor to Passive Mode
        c1Reader.write8(c1CmdReg, c1PassiveCmd);
        c2Reader.write8(c1CmdReg, c1PassiveCmd);
        c3Reader.write8(c1CmdReg, c1ActiveCmd);
        //Wait for the user to press play and reset the timer



        /*float hsvValues[] = {0F, 0F, 0F};
        float hsvValues1[] = {0F, 0F, 0F};
        final float values[] = hsvValues;
        final float values1[] = hsvValues1;

        cSensor1 = hardwareMap.colorSensor.get("cSensor1");
        cSensor2 = hardwareMap.colorSensor.get("cSensor2");

        cSensor1.setI2cAddress(I2cAddr.create7bit(0x1E));
        cSensor2.setI2cAddress(I2cAddr.create7bit(0x26));

        Color.RGBToHSV(cSensor1.red() * 8, cSensor1.green() * 8, cSensor1.blue() * 8, hsvValues);
        Color.RGBToHSV(cSensor2.red() * 8, cSensor2.green() * 8, cSensor2.blue() * 8, hsvValues1);
*/
        while (!isStarted()) {
            telemetry.addData(">", "Robot Heading = %d", gyro.getIntegratedZValue());
            telemetry.update();
        }
        gyro.resetZAxisIntegrator();
        boolean on = true;
        //Do Stuff
        while (opModeIsActive() && on == true) {
            shoot(500, 1);
            ballDoorDown();
            sleep(1400);
            shoot(500 , 1);

            Move(61.5, 1, 45);
            untilButton(0.35);
            squareWall(0.35);
            Move(1.5, 0.3, 180);
            flipperDownRed();
            //Move(3, 0.4, 180);
            //flipperIn();
            //Move(3.1, 0.4, 0);
            findLine(true, 0.18);
            Move(2.5, 0.25, 0);
            chooseColor(true);
            //Move(36, 0.8, 108);
            Move(3, 0.4, 180);

            Move(40, 1, 90);
            untilButton(0.35);
            squareWall(0.35);

            //Move(3, 0.7, 180);
            Move(1.5, 0.3, 180);
            flipperDownRed();

            //Move(3.15, 0.7, 0);
            findLine(true, 0.18);
            //lMove(2, 0.3, 0);
            Move(2.5, 0.4, 0);
            chooseColor(true);
            Move(3, 0.8, 180);
            flipperIn();
            Move(60,1,218);
            GyroTurn(90);
            //Move(70, 1, 225)
            on = false;
        }
    }

    public double getError(double targetAngle) {

        double robotError;

        // calculate error in -179 to +180 range  (
        robotError = targetAngle - gyro.getIntegratedZValue();
        while (robotError > 180) robotError -= 360;
        while (robotError <= -180) robotError += 360;
        return robotError;
    }

    public double getSteer(double error, double PCoeff) {
        return Range.clip(error * PCoeff, -1, 1);
    }

    public void Move(double distance, double speed, double direction) {
        if (opModeIsActive()) {
            direction = direction * Math.PI / 180;

            telemetry.addLine("0.1");
            telemetry.update();

            gyro.resetZAxisIntegrator();

            int frontLeftPosition = (int) (distance * COUNTS_PER_INCH * Math.cos(direction) - frontLeft.getCurrentPosition());
            int frontRightPosition = (int) (distance * COUNTS_PER_INCH * Math.sin(direction) - frontRight.getCurrentPosition());
            int backLeftPosition = (int) (distance * COUNTS_PER_INCH * Math.sin(direction) - backLeft.getCurrentPosition());
            int backRightPosition = (int) (distance * COUNTS_PER_INCH * Math.cos(direction) - backRight.getCurrentPosition());

            frontLeft.setTargetPosition(-frontLeftPosition);
            frontRight.setTargetPosition(-frontRightPosition);
            backLeft.setTargetPosition(-backLeftPosition);
            backRight.setTargetPosition(-backRightPosition);

            frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            telemetry.addLine("0.2");
            telemetry.update();

            while (opModeIsActive() &&
                    ((frontLeft.isBusy() && backRight.isBusy()) || (backLeft.isBusy() && frontRight.isBusy()))) {
                telemetry.addLine("0.3");
                // adjust relative speed based on heading error.
                double error = getError(0);
                double steer = getSteer(error, P_DRIVE_COEFF);

                double frontLeftSpeed = (speed * Math.cos(direction) + steer);
                double frontRightSpeed = (speed * Math.sin(direction)) + steer;
                double backRightSpeed = (speed * Math.cos(direction)) - steer;
                double backLeftSpeed = (speed * Math.sin(direction)) - steer;

                // Normalize speeds if any one exceeds +/- 1.0;
                double max1 = Math.max(Math.abs(frontLeftSpeed), Math.abs(frontRightSpeed));
                double max2 = Math.max(Math.abs(backRightSpeed), Math.abs(backLeftSpeed));
                double max = Math.max(max1, max2);
                if (max > 1.0) {
                    frontLeftSpeed /= max;
                    frontRightSpeed /= max;
                    backRightSpeed /= max;
                    backLeftSpeed /= max;

                }

                frontLeft.setPower(-frontLeftSpeed);
                frontRight.setPower(-frontRightSpeed);
                backLeft.setPower(-backLeftSpeed);
                backRight.setPower(-backRightSpeed);
                // Display drive status for the driver.
                telemetry.addData("Err/St", "%5.1f/%5.1f", error, steer);
                telemetry.addData("Target", "%7d:%7d:%7d:%7d", frontLeftPosition, frontRightPosition, backLeftPosition, backRightPosition);
                telemetry.addData("Actual", "%7d:%7d:%7d:%7d", frontLeft.getCurrentPosition(), frontRight.getCurrentPosition(), backLeft.getCurrentPosition(), backRight.getCurrentPosition());
                telemetry.addData("Speed", "%5.2f:%5.2f:%5.2f:%5.2f", frontLeftSpeed, frontRightSpeed, backLeftSpeed, backRightSpeed);
                telemetry.update();

            }
            // Turn off RUN_TO_POSITION
            frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            // Stop all motion;
            frontRight.setPower(0);
            backLeft.setPower(0);
            frontLeft.setPower(0);
            backRight.setPower(0);

        }
    }

    public void GyroTurn(double degrees) {
        if (opModeIsActive()) {

            gyro.resetZAxisIntegrator();

            double turnError = getError(degrees);


            boolean turn = false;

            frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            while (opModeIsActive() && (turn == false)) {
                turnError = getError(degrees);

                telemetry.addData("turnerror", turnError);
                telemetry.update();

                if (Math.abs(turnError) <= HEADING_THRESHOLD) {
                    turn = true;
                } else {

                    double steer = getSteer(turnError, P_TURN_COEFF);

                    double frontLeftSpeed = (steer);
                    double frontRightSpeed = (steer);
                    double backRightSpeed = (-steer);
                    double backLeftSpeed = (-steer);

                    double max1 = Math.max(Math.abs(frontLeftSpeed), Math.abs(frontRightSpeed));
                    double max2 = Math.max(Math.abs(backRightSpeed), Math.abs(backLeftSpeed));
                    double max = Math.max(max1, max2);
                    if (max > 1.0) {
                        frontLeftSpeed /= max;
                        frontRightSpeed /= max;
                        backRightSpeed /= max;
                        backLeftSpeed /= max;

                    }

                    frontLeft.setPower(-frontLeftSpeed);
                    frontRight.setPower(-frontRightSpeed);
                    backLeft.setPower(-backLeftSpeed);
                    backRight.setPower(-backRightSpeed);
                }
            }
            frontRight.setPower(0);
            backLeft.setPower(0);
            frontLeft.setPower(0);
            backRight.setPower(0);
        }
    }

    public void untilButton(double speed) {
        if (opModeIsActive()) {
            double direction = 0;

            flipperRight.setPosition(0.624);
            flipperLeft.setPosition(.38);
            sleep(200);
            gyro.resetZAxisIntegrator();

            frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            while (opModeIsActive() &&
                    (!touchLeft.isPressed() && !touchRight.isPressed())) {

                // adjust relative speed based on heading error.
                double error = getError(0);
                double steer = getSteer(error, P_DRIVE_COEFF);

                double frontLeftSpeed = (speed * Math.cos(direction) + steer);
                double frontRightSpeed = (speed * Math.sin(direction)) + steer;
                double backRightSpeed = (speed * Math.cos(direction)) - steer;
                double backLeftSpeed = (speed * Math.sin(direction)) - steer;

                // Normalize speeds if any one exceeds +/- 1.0;
                double max1 = Math.max(Math.abs(frontLeftSpeed), Math.abs(frontRightSpeed));
                double max2 = Math.max(Math.abs(backRightSpeed), Math.abs(backLeftSpeed));
                double max = Math.max(max1, max2);
                if (max > 1.0) {
                    frontLeftSpeed /= max;
                    frontRightSpeed /= max;
                    backRightSpeed /= max;
                    backLeftSpeed /= max;

                }

                frontLeft.setPower(-frontLeftSpeed);
                frontRight.setPower(-frontRightSpeed);
                backLeft.setPower(-backLeftSpeed);
                backRight.setPower(-backRightSpeed);
                // Display drive status for the driver.
                telemetry.addData("Err/St", "%5.1f/%5.1f", error, steer);
                telemetry.addData("Speed", "%5.2f:%5.2f:%5.2f:%5.2f", frontLeftSpeed, frontRightSpeed, backLeftSpeed, backRightSpeed);
                telemetry.update();

            }
            // Stop all motion;
            frontRight.setPower(0);
            backLeft.setPower(0);
            frontLeft.setPower(0);
            backRight.setPower(0);

            // Turn off RUN_TO_POSITION
            frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    public void squareWall(double speed) {
        if (opModeIsActive()) {
            frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            boolean square = false;
            while (opModeIsActive() && (square == false)) {
                if (!touchRight.isPressed()) {
                    frontLeft.setPower(-speed);
                    frontRight.setPower(-0.15);
                }
                if (!touchLeft.isPressed()) {
                    backRight.setPower(-speed);
                    frontRight.setPower(0.15);
                }
                if (touchRight.isPressed() && touchLeft.isPressed()) {
                    square = true;
                }
            }
            frontRight.setPower(0);
            backLeft.setPower(0);
            frontLeft.setPower(0);
            backRight.setPower(0);
        }
    }

    public void flipperOut() {
        flipperRight.setPosition(0.59);
        flipperLeft.setPosition(.38);
    }

    public void flipperDownBlue() {
        flipperRight.setPosition(0);
        flipperLeft.setPosition(0);
        sleep(400);
    }

    public void flipperDownRed() {
        flipperLeft.setPosition(1);
        flipperRight.setPosition(1);
        sleep(400);
    }

    public void flipperIn() {
        flipperRight.setPosition(0);
        flipperLeft.setPosition(1);
    }

    public void findBeacon(boolean direction, double speed) {
        double cThreshold = 3500;
        double cThresholdLow = 42;
        if (opModeIsActive()) {

            double rightleft;
            if (direction) {
                rightleft = 90;
                rightleft = rightleft * Math.PI / 180;
            } else {
                rightleft = 270;
                rightleft = rightleft * Math.PI / 180;
            }

            gyro.resetZAxisIntegrator();

            frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            double rightRed = 0;
            double leftRed = 0;
            double rightBlue = 0;
            double leftBlue = 0;

            while (opModeIsActive() &&
                    /*((((rightRed * leftBlue) <= cThreshold) || (Math.abs(rightRed - leftBlue) >= cThresholdLow))
            && (((rightBlue * leftRed) <= cThreshold) || (Math.abs(rightBlue - leftRed) >= cThresholdLow))))*/
                    (((rightRed != 255) && (leftBlue != 255)) || ((rightBlue != 255 && (leftRed != 255)))))


            {

                c1Reader.write8(c1CmdReg, c1PassiveCmd);
                c2Reader.write8(c1CmdReg, c1PassiveCmd);

                c1Cache = c1Reader.read(c1StartReg, c1ReadLength);
                c2Cache = c2Reader.read(c1StartReg, c1ReadLength);

                /*rightRed = (c1Cache[10] & 0xff);
                leftRed = (c2Cache[10] & 0xff);
                rightBlue = (c1Cache[14] & 0xff);
                leftBlue = (c2Cache[14] & 0xff);
                */

                rightRed = (c1Cache[6] & 0xff);
                leftRed = (c2Cache[6] & 0xff);
                rightBlue = (c1Cache[8] & 0xff);
                leftBlue = (c2Cache[8] & 0xff);

                // adjust relative speed based on heading error.

                double error = getError(0);
                double steer = getSteer(error, P_DRIVE_COEFF);

                double frontLeftSpeed = (speed * Math.cos(rightleft) + steer);
                double frontRightSpeed = (speed * Math.sin(rightleft)) + steer;
                double backRightSpeed = (speed * Math.cos(rightleft)) - steer;
                double backLeftSpeed = (speed * Math.sin(rightleft)) - steer;

                // Normalize speeds if any one exceeds +/- 1.0;
                double max1 = Math.max(Math.abs(frontLeftSpeed), Math.abs(frontRightSpeed));
                double max2 = Math.max(Math.abs(backRightSpeed), Math.abs(backLeftSpeed));
                double max = Math.max(max1, max2);
                if (max > 1.0) {
                    frontLeftSpeed /= max;
                    frontRightSpeed /= max;
                    backRightSpeed /= max;
                    backLeftSpeed /= max;

                }

                frontLeft.setPower(-frontLeftSpeed);
                frontRight.setPower(-frontRightSpeed);
                backLeft.setPower(-backLeftSpeed);
                backRight.setPower(-backRightSpeed);
                // Display drive status for the driver.
                telemetry.addData("Err/St", "%5.1f/%5.1f", error, steer);
                telemetry.addData("Speed", "%5.2f:%5.2f:%5.2f:%5.2f", frontLeftSpeed, frontRightSpeed, backLeftSpeed, backRightSpeed);
                telemetry.addData("rightRed", rightRed);
                telemetry.addData("rightBlue", rightBlue);
                telemetry.addData("leftRed", leftRed);
                telemetry.addData("leftBlue", leftBlue);
                telemetry.update();

            }
            // Stop all motion;
            frontRight.setPower(0);
            backLeft.setPower(0);
            frontLeft.setPower(0);
            backRight.setPower(0);

            // Turn off RUN_TO_POSITION
            frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

    }

    public void shoot(long time, double power) {

        shooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooter.setPower(power);
        sleep(time);
        shooter.setPower(0);
    }

    public void chooseColor(boolean color) {
        if (opModeIsActive()) {
            boolean on = false;
            while (opModeIsActive() && (on == false)) {
                c1Reader.write8(c1CmdReg, c1PassiveCmd);
                c2Reader.write8(c1CmdReg, c1PassiveCmd);

                c1Cache = c1Reader.read(c1StartReg, c1ReadLength);
                c2Cache = c2Reader.read(c1StartReg, c1ReadLength);

                double rightRed = c1Cache[10];
                double leftRed = c2Cache[10];
                double rightBlue = c1Cache[14];
                double leftBlue = c2Cache[14];

                telemetry.addData("c1 Red/Blue", (c1Cache[6] & 0xff) + " " + (c1Cache[8] & 0xff));
                telemetry.addData("c2 Red/Blue", (c2Cache[6] & 0xff) + " " + (c2Cache[8] & 0xff));
                telemetry.update();
                if (color) {
                    if ((c1Cache[6] & 0xff) == 255 && ((c2Cache[6] & 0xff) != 255)) {
                        telemetry.addLine("a");
                        telemetry.update();
                        Move(3.25, 0.25, 90);
                        Move(0.5, 0.25, 0);
                        telemetry.addLine("b");
                        telemetry.update();
                        on = true;
                    } else if ((c2Cache[6] & 0xff) == 255 && (c1Cache[6] & 0xff) != 255) {
                        telemetry.addLine("c");
                        telemetry.update();
                        Move(3.25, 0.25, 270);
                        Move(0.5, 0.25, 0);
                        telemetry.addLine("d");
                        telemetry.update();
                        on = true;
                    }
                } else {
                    if ((c2Cache[8] & 0xff) == 255 && (c1Cache[8] & 0xff) != 255) {
                        Move(3.25, 0.42, 270);
                        Move(0.5, 0.25, 0);
                        on = true;
                    } else if ((c2Cache[8] & 0xff) != 255 && (c1Cache[8] & 0xff) == 255) {
                        Move(3.25, 0.42, 90);
                        Move(0.5, 0.25, 0);
                        on = true;
                    }

                }
            }
        }
    }

    public void findLine(boolean direction, double speed) {
        if (opModeIsActive()) {
            double colorWhite = 180;
            double rightleft;
            if (direction) {
                rightleft = 90;
                rightleft = rightleft * Math.PI / 180;
            } else {
                rightleft = 270;
                rightleft = rightleft * Math.PI / 180;
            }

            gyro.resetZAxisIntegrator();

            frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            double bottomWhite = 0;
            double highWhite = 0;

            while (opModeIsActive() && (((bottomWhite) < colorWhite))) {
                c3Reader.write8(c1CmdReg, c1ActiveCmd);
                c3Cache = c3Reader.read(c1StartReg, c1ReadLength);


                bottomWhite = ((c3Cache[16] & 0xff) + (256*(c3Cache[17] & 0xff)));

                //TEST getError(rightleft)
                //Orig: getError(0)
                double error = getError(0);
                double steer = getSteer(error, P_DRIVE_COEFF);

                double frontLeftSpeed = (speed * Math.cos(rightleft) + steer);
                double frontRightSpeed = (speed * Math.sin(rightleft)) + steer;
                double backRightSpeed = (speed * Math.cos(rightleft)) - steer;
                double backLeftSpeed = (speed * Math.sin(rightleft)) - steer;

                // Normalize speeds if any one exceeds +/- 1.0;
                double max1 = Math.max(Math.abs(frontLeftSpeed), Math.abs(frontRightSpeed));
                double max2 = Math.max(Math.abs(backRightSpeed), Math.abs(backLeftSpeed));
                double max = Math.max(max1, max2);
                if (max > 1.0) {
                    frontLeftSpeed /= max;
                    frontRightSpeed /= max;
                    backRightSpeed /= max;
                    backLeftSpeed /= max;

                }

                frontLeft.setPower(-frontLeftSpeed);
                frontRight.setPower(-frontRightSpeed);
                backLeft.setPower(-backLeftSpeed);
                backRight.setPower(-backRightSpeed);
                // Display drive status for the driver.
                telemetry.addData("Err/St", "%5.1f/%5.1f", error, steer);
                telemetry.addData("Speed", "%5.2f:%5.2f:%5.2f:%5.2f", frontLeftSpeed, frontRightSpeed, backLeftSpeed, backRightSpeed);
                telemetry.addData("Back White", bottomWhite);
                telemetry.addData("Upper White", highWhite);
                telemetry.update();

            }
            // Stop all motion;
            frontRight.setPower(0);
            backLeft.setPower(0);
            frontLeft.setPower(0);
            backRight.setPower(0);

            // Turn off RUN_TO_POSITION
            frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        }
    }
    public void ballDoorDown(){
        ballDoor.setPosition(.56);
    }
    public void ballDoorUp(){
        ballDoor.setPosition(1);
    }
}
   /* public void lineUpLine(boolean color){
        if (opModeIsActive()){
            double colorWhite = 150;

            double lowWhite = 0;
            double highWhite = 0;

            while(opModeIsActive() && ((lowWhite < colorWhite) || (highWhite < colorWhite))){
                c3Reader.write8(c1CmdReg, c1ActiveCmd);
                c3Cache = c3Reader.read(c1StartReg, c1ReadLength);
                c4Reader.write8(c1CmdReg, c1ActiveCmd);
                c4Cache = c4Reader.read(c1StartReg, c1ReadLength);
                highWhite = (c4Cache[16] &0xff);
                lowWhite = (c3Cache[16] &0xff);
                telemetry.addData("Back White", lowWhite);
                telemetry.addData("Upper White", highWhite);
                telemetry.update();

                if (color){
                    if (lowWhite < colorWhite){
                        frontRight.setPower(-0.25);
                    }
                    else if(highWhite < colorWhite){
                        backLeft.setPower(-0.25);
                    }
                }
                else {
                    if (lowWhite < colorWhite){
                        frontRight.setPower(0.25);
                    }
                    else if(highWhite < colorWhite){
                        backLeft.setPower(0.25);
                    }
                }
            }*/












