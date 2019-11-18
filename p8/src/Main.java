import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {

    private Image[] imagesMario = {new Image("file:mario0.jpg"), new Image("file:mario1.jpg"), new Image("file:mario2.jpg"), new Image("file:mario3.jpg"),
            new Image("file:mario4.jpg"), new Image("file:mario5.jpg"), new Image("file:mario6.jpg"), new Image("file:mario7.jpg"), new Image("file:mario8.jpg")};

    private Image[] imagesCat = {new Image("file:cat0.jpg"), new Image("file:cat1.jpg"), new Image("file:cat2.jpg"), new Image("file:cat3.jpg"),
            new Image("file:cat4.jpg"), new Image("file:cat5.jpg"), new Image("file:cat6.jpg"), new Image("file:cat7.jpg"), new Image("file:cat8.jpg")};

    private ImageView[] imageView = new ImageView[9];

    private GridPane gp = new GridPane();

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) {

        BorderPane bp = new BorderPane();
        bp.setTop(menu());
        bp.setCenter(gp);
        Label bottom = new Label("Veuillez choisir une image dans Settings");
        bp.setBottom(bottom);

        Scene scene = new Scene(bp);

        stage.setTitle("Laboratoire 8");
        stage.setHeight(603);
        stage.setWidth(603);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    private MenuBar menu() {
        Menu settings = new Menu("Settings");
        Menu image = new Menu("Choisir l'image");

        MenuItem mario = new MenuItem("Mario");
        MenuItem thanos = new MenuItem("Cat");
        MenuItem reset = new MenuItem("Reset");

        mario.setOnAction(event -> changerImage(1));

        thanos.setOnAction(event -> changerImage(2));

        reset.setOnAction(event -> setPuzzle());

        image.getItems().addAll(mario, thanos);
        settings.getItems().addAll(image, reset);

        return new MenuBar(settings);
    }

    private void changerImage(int img) {
        if (img==1) {
            for (int i=0;i<9;i++) {
                imageView[i] = new ImageView(imagesMario[i]);
            }
        }
        if (img==2) {
            for (int i=0;i<9;i++) {
                imageView[i] = new ImageView(imagesCat[i]);
            }
        }

        setPuzzle();
    }

    private void setPuzzle() {
        gp.getChildren().clear();

        boolean[] available = new boolean[9];
        for (int i=0;i<available.length;i++) {
            available[i] = true;
        }
        int x=2;
        int y=2;

        while (x >= 0) {
            int random = (int)(Math.random() * 9);

            if (available[random]) {

                rotate(imageView[random]);
                gp.add(imageView[random], x, y);
                available[random] = false;

                y--;

                if (y == -1) {
                    y = 2;
                    x--;
                }
            }
        }

        setDragAndDrop(gp);
    }

    private void setDragAndDrop(GridPane gp) {
        for (ImageView iv : imageView) {
            iv.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.SECONDARY) {
                    switch ((int) iv.getRotate()) {
                        case 0:
                            iv.setRotate(270);
                            break;
                        case 90:
                            iv.setRotate(0);
                            break;
                        case 180:
                            iv.setRotate(90);
                            break;
                        case 270:
                            iv.setRotate(180);
                            break;
                    }
                }
            });

            iv.setOnDragDetected(event -> {
                Dragboard dragboard = iv.startDragAndDrop(TransferMode.ANY);
                ClipboardContent contenu = new ClipboardContent();
                contenu.putString("");
                dragboard.setContent(contenu);
            });

            iv.setOnDragOver(event -> event.acceptTransferModes(TransferMode.ANY));

            iv.setOnDragDropped(event -> {
                ImageView image1 = (ImageView) event.getGestureSource();
                ImageView image2 = (ImageView) event.getGestureTarget();
                ImageView source = (ImageView) event.getGestureSource();
                Image temp = iv.getImage();
                iv.setImage(source.getImage());
                double rotate = image1.getRotate();
                image1.setRotate(image2.getRotate());
                image1.setRotate(image1.getRotate());
                image2.setRotate(rotate);
                source.setImage(temp);

                event.setDropCompleted(true);
            });

            iv.setOnDragDone(event -> {
                
            });
        }
    }

    private void rotate(ImageView iv) {
        int random = (int)(Math.random() * 3);
        switch (random) {
            case 0 :
                iv.setRotate(90);
                break;
            case 1:
                iv.setRotate(180);
                break;
            case 2:
                iv.setRotate(270);
                break;
        }
    }
}
