
package simplegame;

import java.util.ArrayList;
import java.util.Iterator;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;


public class ZolotykhGame extends Application {

    @Override
    public void start(Stage primaryStage) {
        Button Start = new Button();
        Button How = new Button();
        Button Exit = new Button();
        Start.setText("Start");
        Start.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                StackPane secondaryLayout = new StackPane();
                Canvas screen = new Canvas(1000, 780);
                secondaryLayout.getChildren().add(screen);
                Scene newScene = new Scene(secondaryLayout);
                Stage newWindow = new Stage();
                newWindow.setTitle("Princess Game");
                newWindow.setScene(newScene);
                newWindow.setX(primaryStage.getX() - 80);
                newWindow.setY(primaryStage.getY() - 10);
               
                
                GraphicsContext gc1 = screen.getGraphicsContext2D();
                Image fon = new Image("fon.jpeg");
                newWindow.show();
                ArrayList<String> input = new ArrayList<String>();

                newScene.setOnKeyPressed(
                        new EventHandler<KeyEvent>() {
                    public void handle(KeyEvent e) {
                        String code = e.getCode().toString();
                        if (!input.contains(code)) {
                            input.add(code);
                        }
                    }
                });
                newScene.setOnKeyReleased(
                        new EventHandler<KeyEvent>() {
                    public void handle(KeyEvent e) {
                        String code = e.getCode().toString();
                        input.remove(code);
                    }
                });
                //Создание игрока
                Sprite player = new Sprite();
                player.setImage("belosnejka.png");
                player.setPosition(560, 600);

                ArrayList<Sprite> appleList = new ArrayList<Sprite>();
                //Создание яблок
                for (int i = 0; i < 15; i++) {
                    Sprite apple = new Sprite();
                    apple.setImage("apple.png");
                    double px = 800 * Math.random() + 50;
                    double py = 700 * Math.random() + 50;
                    apple.setPosition(px, py);
                    appleList.add(apple);
                }
                IntValue lastNanoTime = new IntValue(System.nanoTime());

                IntValue appleCount = new IntValue(0);

                new AnimationTimer() {
                    public void handle(long currentNanoTime) {
                        // calculate time since last update.
                        double elapsedTime = (currentNanoTime - lastNanoTime.value) / 1000000000.0;
                        lastNanoTime.value = currentNanoTime;

                        // game logic
                        int speed = 100;
                        player.setVelocity(0, 0);
                        if (input.contains("SHIFT")) {
                            speed = speed * 4;
                        }
                        if (input.contains("LEFT")) {
                            player.addVelocity(-speed, 0);
                        }
                        if (input.contains("RIGHT")) {
                            player.addVelocity(speed, 0);
                        }
                        if (input.contains("UP")) {
                            player.addVelocity(0, -speed);
                        }
                        if (input.contains("DOWN")) {
                            player.addVelocity(0, speed);
                        }

                        player.update(elapsedTime);

                        // collision detection
                        Iterator<Sprite> appleIter = appleList.iterator();
                        while (appleIter.hasNext()) {
                            Sprite apple = appleIter.next();
                            if (player.intersects(apple)) {
                                appleIter.remove();
                                appleCount.value++;
                            }
                        }

                        // render
                        gc1.clearRect(0, 0, 1000, 1000);
                        gc1.drawImage(fon, 0, 0);
                        player.render(gc1);

                        for (Sprite apple : appleList) {
                            apple.render(gc1);
                        }
                        String pointsText = "Apples: " + (1 * appleCount.value);
                        gc1.setFill(Color.PINK);
                        gc1.setStroke(Color.DARKMAGENTA);
                        gc1.setLineWidth(2);
                        Font theFont = Font.font("Times New Roman", FontWeight.BOLD, 40);
                        gc1.setFont(theFont);
                        gc1.fillText(pointsText, 800, 36);
                        gc1.strokeText(pointsText, 800, 36);
                        if (appleCount.value == 15) {
                            Button close = new Button();
                            close.setText("Claim reward");
                            close.setOnAction(new EventHandler<ActionEvent>() {

                                @Override
                                public void handle(ActionEvent event) {
                                    System.out.println("Finish");
                                 
                                    Stage stage = (Stage) close.getScene().getWindow();
                                   
                                    stage.close();
                                }
                            });
                            String winText = "  Great!\n"
                                    + "You Win!";
                            gc1.setFill(Color.PINK);
                            gc1.setStroke(Color.DARKMAGENTA);
                            gc1.setLineWidth(2);
                            Font winFont = Font.font("Times New Roman", FontWeight.BOLD, 50);
                            gc1.setFont(winFont);
                            gc1.fillText(winText, 400, 400);
                            gc1.strokeText(winText, 400, 400);

                        }
                    }

                }.start();

            }

        });
        //Кнопка справочной информации
        How.setText("Help");
        How.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                 Label secondLabel = new Label("Справочная информация по игре:\n"
                     + "Движение осуществляется по кнопкам: left; right; up; down\n"
                     + "Цель игры: добыть Белоснежкой максимальное количество яблок, равное 15\n"
                     + "Яблоки расположены в разных частях поля\n"
                     + "Собрав нужное количество яблок, игра окончена\n");
                 StackPane secondaryLayout = new StackPane();
                secondaryLayout.getChildren().add(secondLabel);
 
                Scene secondScene = new Scene(secondaryLayout, 800, 800);
 
                // Окно справочной информации
                Stage newWindow = new Stage();
                newWindow.setTitle("Second Stage");
                newWindow.setScene(secondScene);
 
                
                newWindow.setX(primaryStage.getX() + 0);
                newWindow.setY(primaryStage.getY() + 10);
                secondLabel.getStylesheets().add("css/label.css");
            
                newWindow.show();
            }
        });
        //Кнопка выхода
        Exit.setText("Exit");
        Exit.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                
                Stage stage = (Stage) Exit.getScene().getWindow();
                
                stage.close();
            }
            
        });

        primaryStage.setTitle("Simple game");
        Pane root = new Pane();
        Scene theScene = new Scene(root);
        primaryStage.setScene(theScene);

        Canvas canvas = new Canvas(800,700);
        root.getChildren().add(canvas);
     GraphicsContext gc = canvas.getGraphicsContext2D();
     //Оформление титульного листа
    gc.setFill( Color.PINK );
    gc.setStroke( Color.PURPLE );
    gc.setLineWidth(2);
    Font theFont = Font.font( "Times New Roman", FontWeight.BOLD, 48 );
    gc.setFont( theFont );
        Image fon = new Image("fon.jpeg");
        gc.drawImage(fon, 0, 0);
         gc.fillText( "Disney princess", 200, 50 );
    gc.strokeText( "Disney princess", 200, 50 );
    Image pic = new Image( "avrora.png" );
    Image pic1 = new Image( "snejka.png" );
    Image pic3 = new Image( "rusalochka.png" );
    
    gc.drawImage( pic, 30, 370 );
    gc.drawImage( pic1, 500, 250 );
    gc.drawImage( pic3, 250, 200 );
    
    gc.setFill(Color.GREY );
    gc.setLineWidth(2);
    root.getStylesheets().add("css/buttonStyle.css");
    Font in = Font.font("Times New Roman", FontWeight.BOLD, 18);
    gc.setFont(in);
    gc.fillText("©Zolotykh E.V. FMFI-b18PIo", 450, 670);
    
    //Координаты кнопок
        root.getChildren().add(Start);
        Start.relocate(600,600);
        root.getChildren().add(How);
        How.relocate(500,600);
        root.getChildren().add(Exit);
        Exit.relocate(700,600);
       
        primaryStage.show();
        
       
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
