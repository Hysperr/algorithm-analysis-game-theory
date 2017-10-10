import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

import java.util.Arrays;

public class NashBoard extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private GridPane gridPane;
    private int[][][] b = {
            {{4, 2}, {0, 0}, {5, 0}, {0, 0}},
            {{1, 4}, {1, 4}, {0, 5}, {-1, 0}},
            {{0, 0}, {2, 4}, {1, 2}, {0, 0}},
            {{0, 0}, {0, 0}, {0, -1}, {0, 0}}
    };

    public NashBoard() { initializeGridPane(); }

    public GridPane getGridPane() { return gridPane; }

    public void findNashEquilibrium() {
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
        System.out.println("Found " + found + " Nash equilibria");
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
    }

















    @Override
    public void start(Stage primaryStage) {

        NashBoard nashBoard = new NashBoard();
        nashBoard.printBoard();
        nashBoard.findNashEquilibrium();

        primaryStage.setScene(new Scene(nashBoard.getGridPane(), 800, 800));
        primaryStage.setTitle("TITULO");
        primaryStage.show();

    }
}
