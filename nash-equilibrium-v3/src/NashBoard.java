import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class NashBoard extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    
    private GridPane gridPane;
    private BorderPane borderPane;
    private List<Integer> row_changes = new ArrayList<>();
    private List<Integer> col_changes = new ArrayList<>();
    private int[][][] b = {
            {{4, 2}, {0, 0}, {5, 0}, {0, 0}},
            {{1, 4}, {1, 4}, {0, 5}, {-1, 0}},
            {{0, 0}, {2, 4}, {1, 2}, {0, 0}},
            {{0, 0}, {0, 0}, {0, -1}, {0, 0}}
    };
    
    /** Constructor: Initialize NashBoard with GUI */
    public NashBoard() {
        initializeGridPane();
    }
    
    /**
     * Gets the borderpane which holds the gridpane in its center and
     * buttons in its lower. See initializeGridPane()
     */
    public BorderPane getBorderPane() {
        return borderPane;
    }
    
    /** Solves nashboard for the nash equilibria and prints the results */
    public void findNashEquilibrium() {
        printBoard();
        int[] b_array = new int[4], a_array = new int[4];
        int b_max = Integer.MIN_VALUE;
        int a_max = Integer.MIN_VALUE;
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                // operate on B
                if (b[i][j][1] > b_max) {
                    b_array[i] = b[i][j][1]; // each row is another b_array index
                    b_max = b[i][j][1];
                }
                // now operate on A
                if (b[j][i][0] > a_max) {
                    a_array[i] = b[j][i][0]; // each col is another a_array index
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
                            b[i][j][1] + "]"
                    );
                    ++found;
                    specialTextFieldStyling((TextField) getNodeFromGridPane(this.gridPane, j, i));
                }
                else {
                    originalTextFieldStyling((TextField) getNodeFromGridPane(this.gridPane, j, i));
                }
            }
        }
        row_changes.clear();
        col_changes.clear();
        System.out.println("Found " + found + " Nash equilibria\n");
    }   // end findNashEquilibrium
    
    /**
     * Updates the nashboard under-the-hood by applying the row and column changes
     * detected by the textfield key release event. These changes are located in
     * member variables row_changes and col_changes.
     */
    private void textFieldParserUpdatesBoardBeforeSolving() {
        for (int i = 0; i < row_changes.size(); ++i) {
            TextField tf = (TextField) getNodeFromGridPane(this.gridPane, col_changes.get(i), row_changes.get(i));
            String str = tf.getText();
            int[] arr = Arrays.stream(str.substring(1, str.length() - 1)
                    .split(","))
                    .map(String::trim)
                    .mapToInt(Integer::parseInt)
                    .toArray();
            b[row_changes.get(i)][col_changes.get(i)] = arr;
        }
    }   // end textFieldParserUpdatesBoardBeforeSolving
    
    /** Prints the nashboard */
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
    
    /**
     * Retrieves the Node element in the gridpane by passing in indices. This
     * relatively simple function coupled with its extreme usefulness with no
     * alternatives leaves me ABSOLUTELY BAFFLED about why isn't implemented
     * in a standard Java library. If it is somewhere please let me know.
     *
     * @param gridPane gridpane object
     * @param col      column index
     * @param row      row index
     * @return Node object or null if no node is found. Careful.
     */
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
    
    /**
     * Helper function for the constructor to initialize gridpane,
     * borderpane including any buttons, textfields, and event listeners.
     */
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
                text.setPrefSize(700, 700);
                originalTextFieldStyling(text);
                text.setOnKeyReleased(event -> {
                    if (event.getCode().isDigitKey()) {
                        int row = GridPane.getRowIndex((TextField) event.getSource());
                        int col = GridPane.getColumnIndex((TextField) event.getSource());
                        row_changes.add(row);
                        col_changes.add(col);
                        System.out.println("Change detected at location (" +
                                row + ", " + col + ") Value now => " +
                                ((TextField) event.getSource()).getText()
                        );
                    }
                });
                gridPane.add(text, j, i);
            }
        }
        // Place our gridpane into borderpane center
        borderPane = new BorderPane(gridPane);
        Button bSolve = new Button("SOLVE!");
        bSolve.setOnAction(event -> {
            textFieldParserUpdatesBoardBeforeSolving();
            findNashEquilibrium();
        });
        Button bScramble = new Button("SCRAMBLE!");
        bScramble.setOnAction(event -> {
            System.out.println("Scrambled!");
            Random r = new Random();
            for (int i = 0; i < 4; ++i) {
                for (int j = 0; j < 4; ++j) {
                    for (int k = 0; k < 2; ++k) {
                        b[i][j][k] = r.nextInt(10) + 1; // [1-10]
                    }
                }
            }
            // update gridpane textfields for user
            for (int i = 0; i < 4; ++i) {
                for (int j = 0; j < 4; ++j) {
                    TextField tf = (TextField) getNodeFromGridPane(this.gridPane, j, i);
                    tf.setText(Arrays.toString(b[i][j]));
                    originalTextFieldStyling(tf);
                }
            }
        }); // end lambda bSolve
        
        // Make buttons take up equal amount of space side-by-side
        HBox buttonLayout = new HBox();
        buttonLayout.getChildren().add(bSolve);
        buttonLayout.getChildren().add(bScramble);
        HBox.setHgrow(bSolve, Priority.ALWAYS);
        HBox.setHgrow(bScramble, Priority.ALWAYS);
        
        int btnCount = buttonLayout.getChildren().size();
        bSolve.prefWidthProperty().bind(buttonLayout.widthProperty().divide(btnCount));
        bScramble.prefWidthProperty().bind(buttonLayout.widthProperty().divide(btnCount));
        borderPane.setBottom(buttonLayout);
    }   // end initializeGridpane
    
    /** set textfield's background color. Use for the primary
     * textfield background color.
     * @param tf the textfield to style.
     */
    private void originalTextFieldStyling(TextField tf) {
        tf.setStyle("-fx-font-size: 40px; -fx-alignment: center; -fx-text-fill: black;");
    }
    
    /** sets textfield's background a specific color. Use to
     * distinguish the location of nash equilibria in the GUI
     * @param tf the textfield to style.
     */
    private void specialTextFieldStyling(TextField tf) {
        tf.setStyle("-fx-font-size: 40px; -fx-alignment: center; -fx-text-fill: black; -fx-background-color: #8c8c8c");
    }
    
    @Override
    public void start(Stage primaryStage) {
        NashBoard nashBoard = new NashBoard();
        primaryStage.setScene(new Scene(nashBoard.getBorderPane(), 900, 500));
        primaryStage.setTitle("NASH-EQ");
        primaryStage.show();
    }
}
