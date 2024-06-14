import java.awt.event.KeyEvent;

/**
 * Program implements a simple game where the player launches projectiles
 * to hit targets while avoiding obstacles.
 * @author Ömer Faruk Koramaz, Student ID: 2021400048
 * @since Date: 20.03.2024
 */

public class AngryBullet {


    // Constants
    private static final int canvasWidth = 1600;
    private static final int canvasHeight = 800;
    private static final double Gravity = 9.80665;
    private static final int pauseDuration = 30;
    private static final int ballSize = 3;


    // Array of target positions and sizes [x, y, width, height]
    private static final double[][] targetArray = {
            {1160, 0, 30, 30},
            {730, 0, 30, 30},
            {150, 0, 20, 20},
            {1480, 0, 60, 60},
            {340, 80, 60, 30},
            {1500, 600, 60, 60}
    };


    // Array of obstacle positions and sizes [x, y, width, height]
    private static final double[][] obstacleArray = {
            {1200, 0, 60, 220},
            {1000, 0, 60, 160},
            {600, 0, 60, 80},
            {600, 180, 60, 160},
            {220, 0, 120, 180}
    };

    // Variables
    private static double bulletAngle = 45.0;
    private static double bulletVelocity = 180;
    private static boolean gameRunning = true;
    private static boolean canControl = true;

    /**
     * The main method to start the game.
     */
    public static void main(String[] args) {
        setupCanvas();
        drawObstacles();
        drawTargets();
        shootingPlatform();

        updateDisplay();

        handleUserInput();
    }

    /**
     * Set up the canvas for drawing.
     */
    private static void setupCanvas() {
        StdDraw.setCanvasSize(canvasWidth, canvasHeight);
        StdDraw.setXscale(0, canvasWidth);
        StdDraw.setYscale(0, canvasHeight);
        StdDraw.enableDoubleBuffering();
        StdDraw.clear(StdDraw.WHITE);
    }

    /**
     * Draw the shooting platform.
     */
    private static void shootingPlatform() {
        double x0 = 120;
        double y0 = 120;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.005);
        StdDraw.filledRectangle(x0 / 2, y0 / 2, x0 / 2, y0 / 2); // Drawing shooting platform
    }

    /**
     * Draw the obstacles on the canvas.
     */
    private static void drawObstacles() {
        for (double[] obstacles : obstacleArray) {
            StdDraw.setPenColor(StdDraw.DARK_GRAY);
            StdDraw.filledRectangle(obstacles[0] + (obstacles[2] / 2), obstacles[1] + (obstacles[3] / 2), obstacles[2] / 2, obstacles[3] / 2); // Drawing obstacles
        }
    }

    /**
     * Draw the targets on the canvas.
     */
    private static void drawTargets() {
        for (double[] targets : targetArray) {
            StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
            StdDraw.filledRectangle(targets[0] + (targets[2] / 2), targets[1] + (targets[3] / 2), targets[2] / 2, targets[3] / 2); // Drawing targets
        }
    }

    /**
     * Launch a projectile.
     */
    private static void launchProjectile() {
        double x0 = 120;
        double y0 = 120;
        double dt = 0.05;

        double x = x0;
        double y = y0;
        double vx = bulletVelocity * Math.cos(Math.toRadians(bulletAngle));
        double vy = bulletVelocity * Math.sin(Math.toRadians(bulletAngle));

        boolean hitTarget = false;
        boolean hitObstacle = false;
        boolean touchedGround = false;
        boolean exceededMaxX = false;

        while (true) {

            double x1 = x, y1 = y;

            vy -= Gravity * dt * 3; // Applying gravity
            x += vx * dt; // Updating x-coordinate
            y += vy * dt; // Updating y-coordinate

            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.filledCircle(x, y, ballSize); // Drawing projectile
            StdDraw.line(x1, y1, x, y); // Drawing trajectory line


            if (checkTargetHit(x, y)) {
                hitTarget = true;
                canControl = false;
                break;
            } else if (checkObstacleHit(x, y)) {
                hitObstacle = true;
                canControl = false;
                break;
            } else if (touchedGround(y)) {
                touchedGround = true;
                canControl = false;
                break;
            } else if (exceededMaxX(x)) {
                exceededMaxX = true;
                canControl = false;
                break;
            }

            StdDraw.show();
            StdDraw.pause(pauseDuration); // Pause for animation

        }

        if (hitTarget) {
            StdDraw.textLeft(50, canvasHeight - 50, "Congratulations: You hit the target"); // Display success message
        } else if (hitObstacle) {
            StdDraw.textLeft(50, canvasHeight - 50, "Hit an obstacle. Press 'r' to shoot again."); // Display obstacle hit message
        } else if (touchedGround) {
            StdDraw.textLeft(50, canvasHeight - 50, "Hit the ground. Press 'r' to shoot again."); // Display ground hit message
        } else if (exceededMaxX) {
            StdDraw.textLeft(50, canvasHeight - 50, "Max X reached. Press 'r' to shoot again."); // Display max X reached message
        }
    }

    /**
     * Check if the projectile touched the ground.
     * @param y The y-coordinate of the projectile.
     * @return True if touched the ground, false otherwise.
     */
    private static boolean touchedGround(double y) {
        return y <= 0;
    }

    /**
     * Check if the projectile exceeded the maximum x-coordinate.
     * @param x The x-coordinate of the projectile.
     * @return True if exceeded, false otherwise.
     */
    private static boolean exceededMaxX(double x) {
        return x >= canvasWidth;
    }

    /**
     * Check if the projectile hit any target.
     * @param x The x-coordinate of the projectile.
     * @param y The y-coordinate of the projectile.
     * @return True if hit a target, false otherwise.
     */
    private static boolean checkTargetHit(double x, double y) {
        for (double[] target : targetArray) {
            if (x >= target[0] && x <= target[0] + target[2] && y >= target[1] && y <= target[1] + target[3]) {
                return true; // Projectile hits the target
            }
        }
        return false;
    }

    /**
     * Check if the projectile hit any obstacle.
     * @param x The x-coordinate of the projectile.
     * @param y The y-coordinate of the projectile.
     * @return True if hit an obstacle, false otherwise.
     */
    private static boolean checkObstacleHit(double x, double y) {
        for (double[] obstacle : obstacleArray) {
            if (x >= obstacle[0] && x <= obstacle[0] + obstacle[2] && y >= obstacle[1] && y <= obstacle[1] + obstacle[3]) {
                return true; // Projectile hits an obstacle
            }
        }
        return false;
    }

    /**
     * Reset the game to its initial state.
     */
    private static void resetGame() {
        bulletAngle = 45.0;
        bulletVelocity = 180;

        setupCanvas();
        drawObstacles();
        drawTargets();
        shootingPlatform();
        updateDisplay();

        canControl = true;
    }

    /**
     * Handle user input during the game.
     */
    private static void handleUserInput() {
        while (gameRunning) {
            if (canControl) {
                if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT)) {
                    bulletAngle += 1; // Increase angle
                    updateDisplay();
                }
                if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)) {
                    bulletAngle -= 1; // Decrease angle
                    updateDisplay();
                }
                if (StdDraw.isKeyPressed(KeyEvent.VK_UP)) {
                    bulletVelocity += 1; // Increase velocity
                    updateDisplay();
                }
                if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN)) {
                    bulletVelocity -= 1; // Decrease velocity
                    updateDisplay();
                }
            }
            if (StdDraw.isKeyPressed(KeyEvent.VK_SPACE)) {
                launchProjectile(); // Launch projectile on space press
            }
            if (StdDraw.isKeyPressed(KeyEvent.VK_R)) {
                resetGame(); // Reset game on 'r' press
            }

            StdDraw.show();
            StdDraw.pause(pauseDuration);
        }
    }

    /**
     * Update the display after any changes.
     */
    private static void updateDisplay() {
        StdDraw.clear();
        drawObstacles();
        drawTargets();
        shootingPlatform();

        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.textRight(90, 60, "a: " + bulletAngle); // Display angle
        StdDraw.textRight(90, 40, "v: " + bulletVelocity); // Display velocity

        double xLine = bulletVelocity * Math.cos(Math.toRadians(bulletAngle));
        double yLine = bulletVelocity * Math.sin(Math.toRadians(bulletAngle));
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.line(120, 120, 120 + (xLine / 2), 120 + (yLine / 2)); // Draw velocity vector
    }
}

