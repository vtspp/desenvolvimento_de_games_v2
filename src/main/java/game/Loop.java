package game;

import lombok.extern.log4j.Log4j2;

import static java.util.Objects.isNull;

@Log4j2
public class Loop {

    private static Loop instance;
    private boolean isRunning = true;

    private Loop() {}

    protected static Loop getInstance() {
        if (instance == null) {
            synchronized (Loop.class) {
                if (isNull(instance)) {
                    instance = new Loop();
                }
            }
        }
        return instance;
    }

    public void execute (Game game) {
        double frame = 0.0D;
        double seconds = 0;
        double numberOfFrames = 60.0D;
        double ns = 1000000000 / numberOfFrames;
        long lastTime = System.nanoTime();
        long time = System.currentTimeMillis();

        while (isRunning) {
            long now = System.nanoTime();
            seconds += (now - lastTime) / ns;
            lastTime = now;

            if (seconds >= 1) {
                game.update();
                game.render();

                frame++;
                seconds--;
            }
            if (System.currentTimeMillis() - time >= 1000) {
                System.out.println("FPS: " + frame);
                frame = 0;
                time += 1000;
            }
        }
    }
}
