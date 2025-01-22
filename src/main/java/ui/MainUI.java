package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;

import pdf.PdfService;
import pdf.PdfServiceImpl;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

public class MainUI extends Application {
    private PdfService pdfService;

    File selectedFile;

    String fileName= "";
    private int numberPages = 0;
    String selectedFilePath = "";

    private int[] singleNumber;
    private int [] checkedInput;
    private Boolean inputIsNotValid = true;
    final private String WarningStringForBadInput = "Input is not valid. Please check!!!";

    public MainUI() {
        // Initialize PDF service
        this.pdfService = new PdfServiceImpl();
    }

    private String changeFileName(String name, String path){
        String changedName = name.substring(0, (name.length()-4));
        return path.replace(name, changedName) + "_changed.pdf";
    }

    private int[] checkInput (String input){
        int[] result = {};
        String copy = input;
        int[] copyArray = {};
        copy = input.replaceAll("//s+", "");
        copy = copy.replaceAll(" ", "");
        if(copy.matches("^[0-9,]*$")){
            copyArray = Arrays.stream(copy.split(","))
                    .mapToInt(Integer::parseInt)
                    .map(num -> num -1)
                    .toArray();
            System.out.println("Array saved: " + Arrays.toString(copyArray));
            if(areAllNumbersLessThanTheNumberOfPages(copyArray, numberPages -1)){
                inputIsNotValid = false;
                result = copyArray ;
            }
            else{
                inputIsNotValid = true;
            }
        }
        else
        {
            inputIsNotValid = true;
        }
        return result;
    }

    private boolean areAllNumbersLessThanTheNumberOfPages(int[] array, int limit) {
        return Arrays.stream(array).allMatch(num -> num <= limit);
    }



    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("PDF Wizard");

        // Set window icon
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/wizard.png"))));

        // FileChooser for browsing files
        Button browseButton = new Button("Browse");
        Button extractButton = new Button("Extract and reorder Pages");
        TextField numberInput = new TextField();
        numberInput.setPromptText("Enter a number or numbers separated by commas (e.g., 1,2,3)");

        // Label for invalid inputs
        Label notValidInput = new Label(WarningStringForBadInput);

        // Label to display selected file information
        Label fileInfoLabel = new Label();
        fileInfoLabel.setVisible(false);

        // Set visibility of extract button and input field to false
        extractButton.setVisible(false);
        numberInput.setVisible(false);
        notValidInput.setVisible(false);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a PDF File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

        // Browse Button Action
        browseButton.setOnAction(e -> {
            File file = fileChooser.showOpenDialog(primaryStage); // Opens the file chooser
            if (file != null) {
                selectedFile = file;
                fileName = selectedFile.getName();
                selectedFilePath = selectedFile.getPath();
                try {
                    numberPages = pdfService.getNumberOfPages(selectedFile);
                    fileInfoLabel.setText("File: " + fileName + " | Pages: " + (numberPages > 1 ? "1-" + numberPages : "1"));
                    fileInfoLabel.setVisible(true);
                } catch (Exception ex) {
                    fileInfoLabel.setText("Error reading file: " + fileName);
                    fileInfoLabel.setVisible(true);
                }
                extractButton.setVisible(true);
                numberInput.setVisible(true);
            }
        });

        // Button to extract and reorder pages
        extractButton.setOnAction(e -> {
            File inputFile = selectedFile;
            File outputFile = new File(changeFileName(fileName, selectedFilePath));

            checkedInput = checkInput(numberInput.getText());
            notValidInput.setVisible(inputIsNotValid);
            if (!inputIsNotValid) {
                singleNumber = checkedInput;
                try {
                    pdfService.extractAndReorderPages(inputFile, outputFile, singleNumber);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Apply styles and configure layout
        browseButton.getStyleClass().add("button");
        extractButton.getStyleClass().add("button");
        fileInfoLabel.getStyleClass().add("file-info-label");

        numberInput.setMaxWidth(350);

        VBox layout = new VBox(20);
        layout.getChildren().addAll(browseButton, fileInfoLabel, extractButton, numberInput, notValidInput);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Scene scene = new Scene(layout, 500, 400); // Increased window size
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

}

