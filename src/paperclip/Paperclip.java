package paperclip;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author zainul
 */
public class Paperclip extends Application {
    private final String[] button_content = new String[]{"Add image", "Remove image","Previous", "Play", "Next", };
    private FileChooser fileChooser;
    private final ObservableList<Image> image_list = FXCollections.observableArrayList(new ArrayList<>());
    private int index = 0;
    private final ImageView img = new ImageView();
    private final Button[] buttons = new Button[button_content.length];
    
    @Override
    public void start(Stage primaryStage) {
        Scene scene = setLayout(primaryStage);
        primaryStage.setTitle("Paperclip");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private Scene setLayout(Stage primaryStage) {
        Scene scene;
        MenuBar menuBar = new MenuBar();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(0, 20, 0, 20));
        gridPane.add(img, 0, 0);
        
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(menuBar);
        Menu menu = new Menu("File");
        MenuItem export_item = new MenuItem("Export sprite");
        MenuItem exit_Item = new MenuItem("Exit");
        export_item.setOnAction((event) -> {
            ImageHandler.exportImage();
        });
        exit_Item.setOnAction((event) -> {
            Platform.exit();
        });
        menu.getItems().addAll(export_item,exit_Item);
        menuBar.getMenus().addAll(menu);
        
        img.setFitWidth(350);
        img.setFitHeight(350);
    
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(gridPane);
        scrollPane.setPrefSize(500, 500);
        HBox hBox = new HBox(20);
        hBox.setPadding(new Insets(50, 75, 75, 50));
        for(int i = 0;i<buttons.length;i++) {
            buttons[i] = new Button();
            buttons[i].setPrefSize(120, 50);
            buttons[i].setText(button_content[i]);
            hBox.getChildren().add(buttons[i]);
        }
        
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Image files","*.png","*.jpg","*.jpeg");
        fileChooser = new FileChooser();
        fileChooser.setTitle("Select your files");
        fileChooser.getExtensionFilters().add(filter);
        
        image_list.addListener((ListChangeListener.Change<? extends Image> c) -> {
            if (index > 0) {
                img.setImage(image_list.get(index));
                if (index == 0) {
                    buttons[2].setDisable(true);
                } else if (index == image_list.size() - 1) {
                    buttons[3].setDisable(true);
                } else {
                    buttons[2].setDisable(false);
                    buttons[3].setDisable(false);
                }
            }
        });

        // Add image button
        buttons[0].setOnAction((ActionEvent event) -> {
            try {
                String image_path = showFileChooser(primaryStage).toPath().toString();
                Image image = ImageHandler.loadImages(image_path);
                if(image!=null && image_path != null) {
                index = image_list.size();
                image_list.add(image);
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Paperclip.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        buttons[1].setOnAction((ActionEvent event) -> {
            removeImage(index);
        });
        // Previous button
        buttons[2].setDisable(true);
        buttons[2].setOnAction((ActionEvent event) -> {
            previous();
        });
        // Next button
        buttons[4].setDisable(true);
        buttons[4].setOnAction((ActionEvent event) -> {
            next();
        });
        buttons[3].setOnAction((ActionEvent event) -> {
            if (!image_list.isEmpty()) {
                play_anim(1.25f);
            }
        });
        borderPane.setCenter(gridPane);
        borderPane.setBottom(hBox);
        
        Group root = new Group();
        root.getChildren().add(borderPane);
        scene = new Scene(root, 768, 500);
        return scene;
    }
    
    private void play_anim(float speed) {
        Thread anim_thread;
        anim_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                index = 0;
                for(int i = 0;i<=index;i++) {
                    try {
                        image_list.set(0, image_list.get(0));
                        Thread.sleep((long)speed * 1000);
                        next();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Paperclip.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        anim_thread.start();
    }
    
    private void previous() {
        if (image_list!=null && index > 0) {
            index--;
            image_list.set(index, image_list.get(index));
        }
    }
    private void next() {
        if (image_list!=null && index < image_list.size() - 1) {
            index++;
            image_list.set(index, image_list.get(index));
        }
    }
    
    private void removeImage(Integer pos) {
        if (pos != null && pos >= 0 && pos < image_list.size()) {
            image_list.remove(image_list.get(pos));
        }
    }
    
    private File showFileChooser(Stage parent) {
        return fileChooser.showOpenDialog(parent);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
