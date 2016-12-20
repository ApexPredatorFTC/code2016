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
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Admin on 11/27/2016.
 */

@Autonomous(name = "RealAutoBlue", group = "Sensor")
public class RealAutoBlue extends LinearOpMode {
    DcMotor frontLeft;
    DcMotor backLeft;
    DcMotor frontRight;
    DcMotor backRight;
    DcMotor shooter;

    double frontLeftEncoder;
    double frontRightEncoder;
    double backRightEncoder;
    double backLeftEncoder;
    double encoderCounts;
    Servo flipperRight;
    Servo flipperLeft;
    Servo boot;
    ColorSensor cSensor1;
    ColorSensor cSensor2;
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

    @Override

    public void runOpMode() throws InterruptedException {
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

        frontRight.setMaxSpeed(3500);
        frontLeft.setMaxSpeed(3500);
        backLeft.setMaxSpeed(3500);
        backRight.setMaxSpeed(3500);

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
            Thread.sleep(50);
            idle();
        }
        telemetry.addData(">", "Robot Ready.");    //
        telemetry.update();

        gyro.resetZAxisIntegrator();

        //Servos
        flipperRight = hardwareMap.servo.get("flipperRight");
        flipperLeft = hardwareMap.servo.get("flipperLeft");
        boot = hardwareMap.servo.get("boot");
        flipperRight.setPosition(0.158);
        flipperLeft.setPosition(0.932);
        boot.setPosition(0.75);

        //Color Sensor
        float hsvValues[] = {0F, 0F, 0F};
        float hsvValues1[] = {0F, 0F, 0F};
        final float values[] = hsvValues;
        final float values1[] = hsvValues1;

        cSensor1 = hardwareMap.colorSensor.get("cSensor1");
        cSensor2 = hardwareMap.colorSensor.get("cSensor2");

        cSensor1.setI2cAddress(I2cAddr.create7bit(0x1E));
        cSensor2.setI2cAddress(I2cAddr.create7bit(0x26));

        Color.RGBToHSV(cSensor1.red() * 8, cSensor1.green() * 8, cSensor1.blue() * 8, hsvValues);
        Color.RGBToHSV(cSensor2.red() * 8, cSensor2.green() * 8, cSensor2.blue() * 8, hsvValues1);

        while (!isStarted()) {
            telemetry.addData(">", "Robot Heading = %d", gyro.getIntegratedZValue());
            telemetry.update();
            idle();
        }
        gyro.resetZAxisIntegrator();

        //Do Stuff
        shoot(500, 1);
        Move(60, 1, 180);
        GyroTurn(135);
        untilButton(0.4);
        squareWall(0.4);
        flipperDownBlue();
        findBeacon(false, 0.21);
        Move(2.5, 0.4, 0);
        chooseColor(false);
        Move(2.5, 0.7, 180);
        Move(38, 1, 270);
        untilButton(0.4);
        squareWall(0.4);

        flipperDownBlue();

        findBeacon(false, 0.21);
        Move(2.5, 0.4, 0);
        chooseColor(false);
        Move(5, 0.8, 180);


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

    public void Move(double distance, double speed, double direction) throws InterruptedException {
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

                // Allow time for other processes to run.
                idle();
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

    public void GyroTurn(double degrees) throws InterruptedException {
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
                idle();
            }
            frontRight.setPower(0);
            backLeft.setPower(0);
            frontLeft.setPower(0);
            backRight.setPower(0);
        }
    }

    public void untilButton(double speed) throws InterruptedException {
        if (opModeIsActive()) {
            double direction = 0;

            flipperLeft.setPosition(0.2888);
            flipperRight.setPosition(0.699999999);
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

                // Allow time for other processes to run.
                idle();
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

    public void squareWall(double speed) throws InterruptedException {
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
                idle();
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

    public void flipperDownBlue() throws InterruptedException {
        flipperRight.setPosition(0);
        flipperLeft.setPosition(0);
        sleep(400);
    }

    public void flipperDownRed() throws InterruptedException {
        flipperLeft.setPosition(1);
        flipperRight.setPosition(1);
        sleep(400);
    }

    public void flipperIn() {
        flipperRight.setPosition(0);
        flipperLeft.setPosition(1);
    }

    public void findBeacon(boolean direction, double speed) throws InterruptedException {
        double cThreshold = 10;
        if (opModeIsActive()) {

            double rightRed = cSensor1.red();
            double leftRed = cSensor2.red();
            double rightBlue = cSensor1.blue();
            double leftBlue = cSensor2.blue();

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

            while (opModeIsActive() && ((rightRed + leftBlue <= cThreshold) && (rightBlue + leftRed <= cThreshold))) {
                rightRed = cSensor1.red();
                leftRed = cSensor2.red();
                rightBlue = cSensor1.blue();
                leftBlue = cSensor2.blue();

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

                // Allow time for other processes to run.
                idle();
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

    public void shoot(long time, double power) throws InterruptedException {

        shooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooter.setPower(power);
        sleep(time);
        shooter.setPower(0);
    }

    public void chooseColor(boolean color) throws InterruptedException {
        if (opModeIsActive()) {
            boolean on = false;
            while (opModeIsActive() && (on == false)) {
                double rightRed = cSensor1.red();
                double leftRed = cSensor2.red();
                double rightBlue = cSensor1.blue();
                double leftBlue = cSensor2.blue();
                if (color == true) {
                    if (rightRed + leftBlue > rightBlue + leftRed) {
                        telemetry.addLine("a");
                        telemetry.update();
                        Move(5, 0.42, 90);
                        telemetry.addLine("b");
                        telemetry.update();
                        on = true;
                    } else if (rightRed + leftBlue < rightBlue + leftRed) {
                        telemetry.addLine("c");
                        telemetry.update();
                        Move(5, 0.42, 270);
                        telemetry.addLine("d");
                        telemetry.update();
                        on = true;
                    }
                } else {
                    if (rightRed + leftBlue > rightBlue + leftRed) {
                        Move(5, 0.42, 270);
                        on = true;
                    } else if (rightRed + leftBlue < rightBlue + leftRed) {
                        Move(5, 0.42, 90);
                        on = true;
                    }

                }
            }
        }

    }
}







