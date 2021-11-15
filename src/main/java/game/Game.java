package game;

import lombok.extern.log4j.Log4j2;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

@Log4j2
public class Game extends Canvas {

    private transient Loop loop;
    private static Game instance;
    private static int posicaoX;
    private static int posicaoY = 498;
    private static int alturaTela = 720;
    private static int larguraTela = 1080;
    private static int alturaSprite = 150;
    private static int larguraSprite = 150;
    private final int velocidadeDeslocamento = 4;
    private transient BufferStrategy strategy;
    private transient Graphics2D graphics2D;
    private transient Map<Integer, Position> positions = new HashMap<>(0);
    private transient Map<String, BufferedImage> images = new HashMap<>(0);
    private static final String IMAGE_PATH = "src/main/resources/images";

    private static int walkPicture = 1;

    private Game () {
        JFrame jFrame = new JFrame();
        jFrame.setTitle("Novo game");
        jFrame.setSize(new Dimension(larguraTela, alturaTela));
        jFrame.setVisible(true);
        jFrame.setResizable(false);
        jFrame.add(this);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // Criar Buffer
        this.createBufferStrategy(3);
        this.strategy = this.getBufferStrategy();
        // criar graficos
        graphics2D = (Graphics2D) strategy.getDrawGraphics();
        loadImages();
        loadControl();

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (positions.containsKey(e.getKeyCode())) {
                    positions.get(e.getKeyCode()).move();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (positions.containsKey(e.getKeyCode())) {
                    positions.get(e.getKeyCode()).move();
                }
            }
        });

        loop = Loop.getInstance();
        createGameLoop();
    }

    public static final void init() {
        if (instance == null) {
            synchronized (Game.class) {
                if (isNull(instance)) {
                    instance = new Game();
                }
            }
        }
    }

    private void loadControl () {
        log.info("Carregando controles");
        positions.put(KeyEvent.VK_D, Position.RIGHT);
        positions.put(KeyEvent.VK_A, Position.LEFT);
        positions.put(KeyEvent.VK_W, Position.DOWN);
        positions.put(KeyEvent.VK_X, Position.UP);
        positions.forEach((key, value) -> log.info("Direção: {}", value));
    }

    private void loadImages () {
        try {
            log.info("Carregando images");
            images.put("walk (1)", ImageIO.read(new File(IMAGE_PATH + "/walk/walk (1).png")));
            images.put("walk (2)", ImageIO.read(new File(IMAGE_PATH + "/walk/walk (2).png")));
            images.put("walk (3)", ImageIO.read(new File(IMAGE_PATH + "/walk/walk (3).png")));
            images.put("walk (4)", ImageIO.read(new File(IMAGE_PATH + "/walk/walk (4).png")));
            images.put("walk (5)", ImageIO.read(new File(IMAGE_PATH + "/walk/walk (5).png")));
            images.put("walk (6)", ImageIO.read(new File(IMAGE_PATH + "/walk/walk (6).png")));
            images.put("walk (7)", ImageIO.read(new File(IMAGE_PATH + "/walk/walk (7).png")));
            images.put("walk (8)", ImageIO.read(new File(IMAGE_PATH + "/walk/walk (8).png")));
            images.put("walk (9)", ImageIO.read(new File(IMAGE_PATH + "/walk/walk (9).png")));
            images.put("walk (10)", ImageIO.read(new File(IMAGE_PATH + "/walk/walk (10).png")));
            images.put("background", ImageIO.read(new File(IMAGE_PATH + "/forest/forest.jpg")));
            images.forEach((key, value) -> log.info(key));
        }
        catch (IOException e) {
            log.error("Não foi possível carregar imagens do game");
            e.printStackTrace();
        }
    }

    private void createGameLoop () {
        new Thread(() -> loop.execute(this)).start();
    }

    protected void update () {
    }

    protected void render () {
        graphics2D.drawImage(images.get("background"), 0, 0, larguraTela, alturaTela, null);
        graphics2D.drawImage(images.get("walk (" + walkPicture + ")"), posicaoX * velocidadeDeslocamento, posicaoY, alturaSprite, larguraSprite, null);
        strategy.show();
    }

    private enum Position {
        RIGHT {
            @Override
            void move() {
                updateImage();
                posicaoX++;
            }

            @Override
            void updateImage() {
                final int max = 10;
                if (walkPicture >= max) walkPicture = 1;
                walkPicture++;
            }
        },
        LEFT {
            @Override
            void move() {
                updateImage();
                posicaoX--;
            }

            @Override
            void updateImage() {

            }
        },
        UP {
            @Override
            void move() {
                updateImage();
                posicaoY++;
            }

            @Override
            void updateImage() {

            }
        },
        DOWN {
            @Override
            void move() {
                updateImage();
                posicaoY--;
            }

            @Override
            void updateImage() {

            }
        };

        abstract void updateImage ();

        abstract void move ();
    }
}