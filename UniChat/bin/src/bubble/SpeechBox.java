package bubble;


import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.control.Label;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;

import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;


public class SpeechBox extends HBox{
    private Color DEFAULT_SENDER_COLOR = Color.ROYALBLUE;
    private Color DEFAULT_RECEIVER_COLOR = Color.AQUAMARINE;
    private Background DEFAULT_SENDER_BACKGROUND, DEFAULT_RECEIVER_BACKGROUND;

    private String message;
    private SpeechDirection direction;

    private Label displayedText;
    private SVGPath directionIndicator;

    public SpeechBox(String message, SpeechDirection direction){
        this.message = message;
        this.direction = direction;
        initialiseDefaults();
        setupElements();
    }

    private void initialiseDefaults(){
        DEFAULT_SENDER_BACKGROUND = new Background(new BackgroundFill(DEFAULT_SENDER_COLOR, new CornerRadii(5,0,5,5,false), Insets.EMPTY));
        DEFAULT_RECEIVER_BACKGROUND = new Background(new BackgroundFill(DEFAULT_RECEIVER_COLOR, new CornerRadii(0,5,5,5,false), Insets.EMPTY));
    }

    private void setupElements(){
        displayedText = new Label(message);
        displayedText.setTextFill(Color.WHITE);
        displayedText.setFont(new Font("Arial", 15));
        displayedText.setPadding(new Insets(5));
        displayedText.setWrapText(true);
        directionIndicator = new SVGPath();

        if(direction == SpeechDirection.LEFT){
            configureForReceiver();
        }
        else{
            configureForSender();
        }
    }

    private void configureForSender(){
        displayedText.setBackground(DEFAULT_SENDER_BACKGROUND);
        displayedText.setAlignment(Pos.CENTER_RIGHT);
        displayedText.setWrapText(true);
        directionIndicator.setContent("M10 0 L0 10 L0 0 Z");
        directionIndicator.setFill(DEFAULT_SENDER_COLOR);
       

        HBox container = new HBox(displayedText, directionIndicator);
        //Use at most 60% of the width provided to the SpeechBox for displaying the message
        container.maxWidthProperty().bind(widthProperty().multiply(0.6));
        getChildren().setAll(container);
        setAlignment(Pos.CENTER_RIGHT);
    }

    private void configureForReceiver(){
        displayedText.setBackground(DEFAULT_RECEIVER_BACKGROUND);
        displayedText.setAlignment(Pos.CENTER_LEFT);
        directionIndicator.setContent("M0 0 L10 0 L10 10 Z");
        directionIndicator.setFill(DEFAULT_RECEIVER_COLOR);
      

        HBox container = new HBox(directionIndicator, displayedText);
        //Use at most 60% of the width provided to the SpeechBox for displaying the message
        container.maxWidthProperty().bind(widthProperty().multiply(0.6));
        getChildren().setAll(container);
        setAlignment(Pos.CENTER_LEFT);
    }
}