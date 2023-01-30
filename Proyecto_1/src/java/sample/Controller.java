package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.scene.image.ImageView;
import sample.model.EjeNube;
import sample.model.Nube;

import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class Controller implements Initializable,Observer  {

    @FXML
    private AnchorPane scene;

    @FXML
    private Circle circle;

    @FXML
    private Rectangle paddle;

    @FXML
    private Rectangle bottomZone;

    @FXML
    private Button startButton;
    private ImageView fondo;
    private Nube nubesita1;


    @FXML
    void startGameButtonAction(ActionEvent event){
        // fondo = new ImageView(new Image(getClass().getResourceAsStream("/image/img.png")));
        //scene.getChildren().add(fondo);
        nubesita1 = new Nube();
        nubesita1.setEjeNube(new EjeNube(600,100));
        nubesita1.addObserver( this);
        new Thread(nubesita1).start();
        System.out.printf(""+nubesita1);

        createBricks();
        timeline.play();
        startButton.setVisible(true);
        Thread thread = new Thread(){
            public void run(){
                System.out.println("Thread Running");
                startButton.setVisible(false);

            }
        }; thread.start();
    }

    private int paddleStartSize = 600;

    Robot robot = new Robot();

    private ArrayList<Rectangle> bricks = new ArrayList<>();

    double deltaX = -1;
    double deltaY = -3;

    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {

            movePaddle();

            checkCollisionPaddle(paddle);
            circle.setLayoutX(circle.getLayoutX() + deltaX);
            circle.setLayoutY(circle.getLayoutY() + deltaY);

            if(!bricks.isEmpty()){
                bricks.removeIf(brick -> checkCollisionBrick(brick));
            } else {
                timeline.stop();
            }

            checkCollisionScene(scene);
            checkCollisionBottomZone();


        }
    }));



    public void initialize(URL url, ResourceBundle resourceBundle) {

        paddle.setWidth(paddleStartSize);
        timeline.setCycleCount(Animation.INDEFINITE);
        fondo = new ImageView(new Image(getClass().getResourceAsStream("/image/img.png")));
        fondo.setFitWidth(50);
        fondo.setFitHeight(50);
        fondo.setLayoutY(100);
        fondo.setLayoutX(600);
        scene.getChildren().add(fondo);

    }





    public void checkCollisionScene(Node node){

        Bounds bounds = node.getBoundsInLocal();
        boolean rightBorder = circle.getLayoutX() >= (bounds.getMaxX() - circle.getRadius());
        boolean leftBorder = circle.getLayoutX() <= (bounds.getMinX() + circle.getRadius());
        boolean bottomBorder = circle.getLayoutY() >= (bounds.getMaxY() - circle.getRadius());
        boolean topBorder = circle.getLayoutY() <= (bounds.getMinY() + circle.getRadius());

        if (rightBorder || leftBorder) {
            deltaX *= -1;
        }
        if (bottomBorder || topBorder) {
            deltaY *= -1;
        }
    }


    public boolean checkCollisionBrick(Rectangle brick){

        if(circle.getBoundsInParent().intersects(brick.getBoundsInParent())){
            boolean rightBorder = circle.getLayoutX() >= ((brick.getX() + brick.getWidth()) - circle.getRadius());
            boolean leftBorder = circle.getLayoutX() <= (brick.getX() + circle.getRadius());
            boolean bottomBorder = circle.getLayoutY() >= ((brick.getY() + brick.getHeight()) - circle.getRadius());
            boolean topBorder = circle.getLayoutY() <= (brick.getY() + circle.getRadius());

            if (rightBorder || leftBorder) {
                deltaX *= -1;
            }
            if (bottomBorder || topBorder) {
                deltaY *= -1;
            }

            paddle.setWidth(paddle.getWidth() - (0.10 * paddle.getWidth()));
            scene.getChildren().remove(brick);

            return true;
        }
        return false;
    }


    public void createBricks(){
        double width = 560;
        double height = 200;

        int spaceCheck = 1;

        for (double i = height; i > 0 ; i = i - 50) {
            for (double j = width; j > 0 ; j = j - 25) {
                if(spaceCheck % 2 == 0){
                    Rectangle rectangle = new Rectangle(j,i,30,30);
                    rectangle.setFill(Color.DARKGREEN);
                    scene.getChildren().add(rectangle);
                    bricks.add(rectangle);
                }
                spaceCheck++;
            }
        }
    }

    public void movePaddle(){
        Bounds bounds = scene.localToScreen(scene.getBoundsInLocal());
        double sceneXPos = bounds.getMinX();

        double xPos = robot.getMouseX();
        double paddleWidth = paddle.getWidth();

        if(xPos >= sceneXPos + (paddleWidth/2) && xPos <= (sceneXPos + scene.getWidth()) - (paddleWidth/2)){
            paddle.setLayoutX(xPos - sceneXPos - (paddleWidth/2));
        } else if (xPos < sceneXPos + (paddleWidth/2)){
            paddle.setLayoutX(0);
        } else if (xPos > (sceneXPos + scene.getWidth()) - (paddleWidth/2)){
            paddle.setLayoutX(scene.getWidth() - paddleWidth);
        }
    }

    public void checkCollisionPaddle(Rectangle paddle){

        if(circle.getBoundsInParent().intersects(paddle.getBoundsInParent())){

            boolean rightBorder = circle.getLayoutX() >= ((paddle.getLayoutX() + paddle.getWidth()) - circle.getRadius());
            boolean leftBorder = circle.getLayoutX() <= (paddle.getLayoutX() + circle.getRadius());
            boolean bottomBorder = circle.getLayoutY() >= ((paddle.getLayoutY() + paddle.getHeight()) - circle.getRadius());
            boolean topBorder = circle.getLayoutY() <= (paddle.getLayoutY() + circle.getRadius());

            if (rightBorder || leftBorder) {
                deltaX *= -1;
            }
            if (bottomBorder || topBorder) {
                deltaY *= -1;
            }
        }
    }

    public void checkCollisionBottomZone(){

        if(circle.getBoundsInParent().intersects(bottomZone.getBoundsInParent())){
            timeline.stop();
            bricks.forEach(brick -> scene.getChildren().remove(brick));
            bricks.clear();
            paddle.setWidth(paddleStartSize);

            deltaX = -1;
            deltaY = -3;

            circle.setLayoutX(300);
            circle.setLayoutY(300);
            startButton.setVisible(true);
            System.out.println("Game over!");
            nubesita1.setEstatus(false);
            fondo = new ImageView(new Image(getClass().getResourceAsStream("/image/fondo-final.jpg")));
            fondo.setFitWidth(50);
            fondo.setFitHeight(50);
            fondo.setLayoutY(100);
            fondo.setLayoutX(600);
            scene.getChildren().add(fondo);

        }
    }


    @Override
    public void update(Observable o, Object arg) {
        EjeNube poscion = (EjeNube)arg;
        Platform.runLater(()-> fondo.setLayoutX(poscion.getX()));
        if (poscion.getX() - 10 <  -100) {
            nubesita1.setEstatus(false);
            nubesita1 = new Nube();
            nubesita1.setEjeNube(new EjeNube(600,100));
            nubesita1.addObserver( this);
            new Thread(nubesita1).start();

        }

    }
}