package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable  {

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



        @FXML
    void startGameButtonAction(ActionEvent event){
           // fondo = new ImageView(new Image(getClass().getResourceAsStream("/image/img.png")));
            //scene.getChildren().add(fondo);

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
            circle.setLayoutX(circle.getLayoutX() + deltaX);
            circle.setLayoutY(circle.getLayoutY() + deltaY);


            checkCollisionScene(scene);



        }
    }));



    public void initialize(URL url, ResourceBundle resourceBundle) {

        paddle.setWidth(paddleStartSize);
        timeline.setCycleCount(Animation.INDEFINITE);


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


}