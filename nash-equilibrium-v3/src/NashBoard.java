import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.Random;

public class NashBoard extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private Button bSolve, bScramble;
    private GridPane gridPane;
    private BorderPane borderPane;
    private int[][][] b = {
            {{4, 2}, {0, 0}, {5, 0}, {0, 0}},
            {{1, 4}, {1, 4}, {0, 5}, {-1, 0}},
            {{0, 0}, {2, 4}, {1, 2}, {0, 0}},
            {{0, 0}, {0, 0}, {0, -1}, {0, 0}}
    };

    public NashBoard() {
        initializeGridPane();
    }

    public GridPane getGridPane() {
        return gridPane;
    }

    public BorderPane getBorderPane() {
        return borderPane;
    }

    public void findNashEquilibrium() {
        printBoard();
        int[] b_array = new int[4], a_array = new int[4];
        int b_max = Integer.MIN_VALUE;
        int a_max = Integer.MIN_VALUE;

        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                // operate on B
                if (b[i][j][1] > b_max) {
                    b_array[i] = b[i][j][1]; // insert at index. each row is another index
                    b_max = b[i][j][1];
                }
                // now operate on A
                if (b[j][i][0] > a_max) {
                    a_array[i] = b[j][i][0]; // insert at index. each col is another index
                    a_max = b[j][i][0];
                }
            }
            b_max = Integer.MIN_VALUE; // reset b_max for next row
            a_max = Integer.MIN_VALUE; // reset a_max for next col
        }

        for (int b : b_array) System.out.print(b + " ");
        System.out.println(" - B");
        for (int a : a_array) System.out.print(a + " ");
        System.out.println(" - A");

        int found = 0;
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                if (b[i][j][1] == b_array[i] && b[i][j][0] == a_array[j]) {
                    System.out.println("Nash equilibrium found at: (" +
                            i + ", " + j + ") => Value: [" +
                            b[i][j][0] + ", " +
                            b[i][j][1] + "]");
                    ++found;
                }
            }
        }
        System.out.println("Found " + found + " Nash equilibria\n");
    }

    public void printBoard() {
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                System.out.print("(");
                for (int k = 0; k < 2; ++k) {
                    if (k != 1) System.out.print(b[i][j][k] + ", ");
                    else System.out.print(b[i][j][k]);
                }
                System.out.print(") ");
            }
            System.out.println();
        }
        System.out.println("==========================");
    }

    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (node instanceof Group) continue;
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        System.out.println("RETURNING NULL!!!");
        return null;
    }

    private void initializeGridPane() {
        gridPane = new GridPane();
        gridPane.setGridLinesVisible(true);

        for (int i = 0; i < 4; i++) {
            RowConstraints rc = new RowConstraints();
            rc.setVgrow(Priority.ALWAYS);
            rc.setFillHeight(true);
            gridPane.getRowConstraints().add(rc);
        }
        for (int j = 0; j < 4; j++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setHgrow(Priority.ALWAYS);
            cc.setFillWidth(true);
            gridPane.getColumnConstraints().add(cc);
        }

        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                TextField text = new TextField(Arrays.toString(b[i][j]));
                text.setPrefSize(800, 800);
                text.setStyle("-fx-font-size: 50px");
                gridPane.add(text, j, i);
            }
        }

        // Place into borderpane
        borderPane = new BorderPane(gridPane);
        bSolve = new Button("SOLVE!");
        bSolve.setOnMouseClicked(event -> findNashEquilibrium());

        bScramble = new Button("SCRAMBLE!");
        bScramble.setOnMouseClicked(event -> {
            System.out.println("Scrambled!");
            Random r = new Random();
            for (int i = 0; i < 4; ++i) {
                for (int j = 0; j < 4; ++j) {
                    for (int k = 0; k < 2; ++k) {
                        b[i][j][k] = r.nextInt(10) + 1; // -10 to 10
                    }
                }
            }
            // update gridpane text fields
            for (int i = 0; i < 4; ++i) {
                for (int j = 0; j < 4; ++j) {
                    TextField tf = (TextField) getNodeFromGridPane(this.gridPane, j, i);
                    tf.setText(Arrays.toString(b[i][j]));
                }
            }
        }); // end lambda bSolve

        HBox buttonLayout = new HBox();
        buttonLayout.getChildren().add(bSolve);
        buttonLayout.getChildren().add(bScramble);

        HBox.setHgrow(bSolve, Priority.ALWAYS);
        HBox.setHgrow(bScramble, Priority.ALWAYS);

        int btnCount = buttonLayout.getChildren().size();
        bSolve.prefWidthProperty().bind(buttonLayout.widthProperty().divide(btnCount));
        bScramble.prefWidthProperty().bind(buttonLayout.widthProperty().divide(btnCount));


        borderPane.setBottom(buttonLayout);


    } // end initializeGridpane


    @Override
    public void start(Stage primaryStage) {

        NashBoard nashBoard = new NashBoard();
        nashBoard.printBoard();
        nashBoard.findNashEquilibrium();

        primaryStage.setScene(new Scene(nashBoard.getBorderPane(), 800, 800));
        primaryStage.setTitle("TITULO");
        primaryStage.show();

    }
}


//bSolve.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); // allows button to grow
// bScramble.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

//  if (GridPane.getRowIndex(gridPane.getChildren().get(i+1)) == i && GridPane.getColumnIndex(gridPane.getChildren().get(j+1)) == j) { // +1 because index 0 in getChildren() list is weird group object
////   System.out.println("row: " + i + " col: " + j);
//  }