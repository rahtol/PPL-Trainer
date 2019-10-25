package com.becke.quizappfx.controllers;

import com.becke.quizappfx.App;
import com.becke.quizappfx.questionList;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXSnackbar;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.jensd.fx.glyphs.emojione.EmojiOneView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import javax.swing.*;

public class JavaQuizController implements Initializable {

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private AnchorPane parent;
    
    @FXML
    private StackPane root;

    @FXML
    private AnchorPane homePane, testPane, finishedPane, resultPane;
    @FXML
    private Pane menuPane;

    public static JFXDialog aboutDialog;

    @FXML
    private EmojiOneView emoji;

    @FXML
    private ProgressIndicator progress;

    @FXML
    private Label titleLbl;

    @FXML
    private TextArea questionLbl;

    @FXML
    private ImageView questionImage;

    @FXML
    private JFXRadioButton response1, response2, response3, response4;

    @FXML
    private Label detailedprogress;

    @FXML
    private Label questionId;

    // JDOM2-stuff to access the XML
    private SAXBuilder saxBuilder;
    private Document doc;

    private JFXSnackbar toastMsg, toastMsg2;

    // Type of Test (Basic, OOP, ...)
    public static int choix;

    // This contains the lists of question
    questionList questions;

    @FXML
    private Label scoreLbl, precentageScoreLbl;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        makeStageDrageable();
        toastMsg = new JFXSnackbar(homePane);
        toastMsg2 = new JFXSnackbar(testPane);
        try {
            saxBuilder = new SAXBuilder();
            // TODO: path to xml, xml should be a resource in the project
//            doc = saxBuilder.build("D:\\work\\tmp\\QuizApp-FX\\database\\ecqb_fragenkatalog.xml");
            URL url0 = getClass().getClassLoader().getResource("database/ecqb_fragenkatalog.xml");
            doc = saxBuilder.build(url0);

            AnchorPane aboutPane = FXMLLoader.load(getClass().getResource("/views/About.fxml"));
            aboutDialog = new JFXDialog(root, aboutPane, JFXDialog.DialogTransition.TOP);
        } catch (JDOMException | IOException ex) {
            Logger.getLogger(JavaQuizController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void makeStageDrageable() {
        parent.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
            }
        });
        parent.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            App.stage.setX(event.getScreenX() - xOffset);
            App.stage.setY(event.getScreenY() - yOffset);
            App.stage.setOpacity(0.7f);
            }
        });
        parent.setOnDragDone((e) -> {
            App.stage.setOpacity(1.0f);
        });
        parent.setOnMouseReleased((e) -> {
            App.stage.setOpacity(1.0f);
        });

    }

    @FXML
    private void close() {
        System.exit(0);
    }

    @FXML
    private void minimize(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void btnALW() {
        choix = 1;
        initCategory("ALW");
    }

    @FXML
    private void btnAGK() {
        choix = 2;
        initCategory("AGK");
    }

    @FXML
    private void btnHPL() {
        choix = 3;
        initCategory("HPL");
    }

    @FXML
    private void btnMET() {
        choix = 4;
        initCategory("MET");
    }

    @FXML
    private void btnNAV() {
        choix = 5;
        initCategory("NAV");
    }

    @FXML
    private void btnOPR() {
        choix = 6;
        initCategory("OPR");
    }

    @FXML
    private void btnFPP() {
        choix = 7;
        initCategory("FPP");
    }

    @FXML
    private void btnCOM() {
        choix = 8;
        initCategory("COM");
    }

    @FXML
    private void btnPFA() {
        choix = 9;
        initCategory("PFA");
    }

    @FXML
    private void btnBZF() {
        choix = 10;
        initCategory("BZF");
    }

    private void initCategory(String category) {
        // load question from xml-database
        questions = new questionList(doc, category);

        if (!questions.openQuestionAvailable()) {
            // empty question list, back to home
            toastMsg.show("Fragenkatalog leer !", 4000);
            return;
        }

        // TODO: Avoid duplicate entry of full category names, i.e. fxml and here
        switch (choix) {
            case 1: {
                titleLbl.setText("Luftrecht");
            }
            break;
            case 2: {
                titleLbl.setText("Allgemeine Luftfahrzeugkunde");
            }
            break;
            case 3: {
                titleLbl.setText("Menschliches Leistungsvermögen");
            }
            break;
            case 4: {
                titleLbl.setText("Meteorologie");
            }
            break;
            case 5: {
                titleLbl.setText("Navigation");
            }
            break;
            case 6: {
                titleLbl.setText("Betriebliche Verfahren");
            }
            break;
            case 7: {
                titleLbl.setText("Flugleistung und Flugplanung");
            }
            break;
            case 8: {
                titleLbl.setText("Kommunikation");
            }
            break;
            case 9: {
                titleLbl.setText("Grundlagen des Fluges (Flugzeug)");
            }
            break;
            case 910: {
                titleLbl.setText("BZF (Sprechfunk)");
            }
            break;
        }

        loadQuestion();

        questions.dumpstate();
        homePane.setVisible(false);
        testPane.setVisible(true);
    }

    @FXML
    private void btnAbout() {
        if (aboutDialog.isVisible()) {
            return;
        }
        aboutDialog.show();
    }

    @FXML
    private void btnPrevious() {
        questions.registerAnswer(this.getSelectedResponse());
        questions.dumpstate();
        if (questions.prevQuestion()) {
            loadQuestion();
        }
    }

    @FXML
    private void btnNext() {
        questions.registerAnswer(this.getSelectedResponse());
        if (questions.getAnswer() != 0) {
            toastMsg2.show((questions.getCorrectAnswer()==questions.getAnswer() ? "Solved!" : "Failed!"), 2000);
        }
        questions.dumpstate();
        if (questions.nextQuestion()) {
            loadQuestion();
        }
        else {
            testPane.setVisible(false);
            finishedPane.setVisible(true);
        }
    }

    private void loadQuestion() {
        // called only if there is a current question in questions
        // TODO: implement assert(questions.openQuestionAvailable());

        questionList.score score = questions.getScore();
        progress.setProgress(1.0 - (score.open() / (float) score.total()));
        detailedprogress.setText(String.format("%d / %d / %d", score.solved(), score.total()-score.open(), score.total()));

        questionId.setText(String.format("Frage #%d (Nr. %d)", questions.getCurrent()+1, questions.getQuestionId()));
        questionLbl.setText(questions.getQuestionText());
        if (questions.hasImage()){
            try {
                String res = "images/"+questions.getImageName() + ".png";
                URL url = getClass().getClassLoader().getResource(res);
                String resfname = url.toString();
                Image img0 = new Image(resfname);
                questionImage.setImage(img0);
                questionImage.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                questionImage.setVisible(false);
            }
        }
        else {
            questionImage.setVisible(false);
        }
        // TODO: present the image associated to the current question, if any

        String [] response = questions.getResponse();
        // TODO: handle deviating number of responses, i.e. cases where there are not exactly 4 responses
        response1.setText(response[0]);
        response2.setText(response[1]);
        response3.setText(response[2]);
        response4.setText(response[3]);

        switch (questions.getAnswer()) {
            case 0: {
                response1.setSelected(false);
                response2.setSelected(false);
                response3.setSelected(false);
                response4.setSelected(false);
            }
            break;
            case 1: {
                response1.setSelected(true);
                response2.setSelected(false);
                response3.setSelected(false);
                response4.setSelected(false);
            }
            break;
            case 2: {
                response1.setSelected(false);
                response2.setSelected(true);
                response3.setSelected(false);
                response4.setSelected(false);
            }
            break;
            case 3: {
                response1.setSelected(false);
                response2.setSelected(false);
                response3.setSelected(true);
                response4.setSelected(false);
            }
            break;
            case 4: {
                response1.setSelected(false);
                response2.setSelected(false);
                response3.setSelected(false);
                response4.setSelected(true);
            }
            break;
        }
   }

    private int getSelectedResponse() {
        int selectedResponse = 0;
        if (response1.isSelected()) {
            selectedResponse = 1;
        } else if (response2.isSelected()) {
            selectedResponse =2;
        } else if (response3.isSelected()) {
            selectedResponse =  3;
        } else if (response4.isSelected()) {
            selectedResponse =  4;
        }
        return selectedResponse;
    }

    @FXML
    private void radioClicked(ActionEvent e) {
        response1.setSelected(false);
        response2.setSelected(false);
        response3.setSelected(false);
        response4.setSelected(false);
        ((JFXRadioButton) e.getSource()).setSelected(true);
    }

    @FXML
    void btnBackHome() {
        questions.dumpstate();
        testPane.setVisible(false);
        finishedPane.setVisible(false);
        resultPane.setVisible(false);
        homePane.setVisible(true);
    }

    @FXML
    void btnBackTest() {
        loadQuestion();
        finishedPane.setVisible(false);
        testPane.setVisible(true);
    }

    @FXML
    private void btnViewResult() {
        questionList.score score = questions.getScore();

        // Load result to resultPane
        scoreLbl.setText(score.solved() + "/" + score.total());
        if (score.solved() >= (score.total() * 0.75)) {
            scoreLbl.setStyle(scoreLbl.getStyle() + "; -fx-text-fill: #0B0");
            //emoji.setGlyphName("GRINNING");
        } else {
            scoreLbl.setStyle(scoreLbl.getStyle() + "; -fx-text-fill: #E00");
            //emoji.setGlyphName("SLIGHT_FROWN");
        }

        precentageScoreLbl.setText(String.valueOf(score.solved() * 100.0 / score.total()).concat("  ").substring(0, 5) + "%");

        finishedPane.setVisible(false);
        resultPane.setVisible(true);
    }
}
