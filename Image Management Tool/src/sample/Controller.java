package sample;

        import java.awt.image.BufferedImage;//to implement the filter
        import java.io.File;
        import java.io.IOException;
        import java.net.URL;

        import java.util.List;
        import java.util.ResourceBundle;
        import java.util.function.Consumer;

        import com.drew.imaging.ImageMetadataReader;
        import com.drew.imaging.ImageProcessingException;

        import com.jhlabs.image.*;//filters
        import javafx.embed.swing.SwingFXUtils;//to convert javafx.image to java.awt.image.bufferedimage
        import javafx.event.ActionEvent;
        import javafx.event.EventHandler;
        import javafx.fxml.FXML;
        import javafx.scene.control.*;
        import javafx.scene.image.Image;
        import javafx.scene.image.ImageView;
        import javafx.scene.image.WritableImage;
        import javafx.scene.input.MouseEvent;
        import javafx.scene.layout.AnchorPane;
        import javafx.stage.FileChooser;
        import javafx.stage.Stage;
        import javax.imageio.ImageIO;


        import com.drew.metadata.Directory;
        import com.drew.metadata.Metadata;
        import com.drew.metadata.Tag;



public class Controller {

    private int number = 0;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Menu MenuFConv;

    @FXML
    private MenuItem MenuOpen;


    //@FXML
    //private MenuItem MenuDDoc;

    @FXML
    private ImageView mainImageView;

    @FXML
    private AnchorPane AnchorP;

    @FXML
    private Menu MenuFilter;

    @FXML
    private TextArea txtField;

    @FXML
    private Menu MenuFile;

    @FXML
    private MenuItem MenuExport;

    @FXML
    private ScrollPane ScrollP;

    @FXML
    private ProgressBar ProgBar;

    @FXML
    void ExportFile(ActionEvent event) {     //Export the showing image in one form

        FileChooser fileChooser = new FileChooser();
        Stage stage = new Stage();
        fileChooser.setTitle("Save Pictures");
        fileChooser.getExtensionFilters().addAll(  //Supported Forms
                new FileChooser.ExtensionFilter("JPEG", "*.jpeg"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("GIF", "*.gif"),
                new FileChooser.ExtensionFilter("BMP", "*.bmp")
        );
        File file = fileChooser.showSaveDialog(stage);  // Open a fileChooser to save the image
        if(file == null) return;
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(mainImageView.getImage(),
                    null),"png", file);  // Change the image into the selected form and output it (image conversion)
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    void OpenFile(ActionEvent event) {     // Open images and set a listener to each thumbnail
        FileChooser fileChooser = new FileChooser(); // Open a filechooser to read images
        Stage stage = new Stage();
        fileChooser.setTitle("View Pictures");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Picture", "*.jpeg", "*.jpg","*.png","*.gif","*.bmp")// Select the supported form
        );
        List<File> l = fileChooser.showOpenMultipleDialog(stage);// this kind of chooser support opening a few files
        if(l == null) return;
        l.forEach(new Consumer<File>() //Do the same configuration to each image
        {
            @Override
            public void accept(File f) {

                String name,date = "",make = "",model = "",lo = "",la = "",lod = "",lad = "";// the information to be shown at GUI
                Image i = new Image(f.toURI().toString());
                name = f.getName();
                ImageView view = new ImageView(i);//set thumbnails into a new imageview
                view.prefWidth(133);
                view.prefWidth(133);
                view.setSmooth(true);
                view.setPreserveRatio(true);
                if(i.getHeight()>i.getWidth())  //Set room
                {
                    view.setFitHeight(133);
                }
                else {view.setFitWidth(133);}
                view.setSmooth(true);
                AnchorP.getChildren().add(view);
                view.setLayoutY(9+number*(133+9));
                view.setLayoutX(5);
                number++;
                if(number>3)//Settings drop-down menu
                {
                    AnchorP.setPrefHeight(AnchorP.getPrefHeight()+150);
                }
                mainImageView.setImage(i);
                mainImageView.setSmooth(true);
                mainImageView.setPreserveRatio(true);


                try {
                    Metadata metadata = ImageMetadataReader.readMetadata(f);  // use metadata-extractor packet to get the information of the image
                    for (Directory directory : metadata.getDirectories()) {
                        for (Tag tag : directory.getTags()) {
                            System.out.println(tag);
                            if (tag.getTagName() == "Make")
                            {
                                make = tag.getDescription();// get camera
                            }
                            else if(tag.getTagName() == "Model")
                            {
                                model = tag.getDescription();//get camera type
                            }
                            else if(tag.getTagName() == "GPS Latitude")
                            {
                                la = tag.getDescription();// get gps latitude
                            }
                            else if(tag.getTagName() == "GPS Latitude Ref")
                            {
                                lad = tag.getDescription();// N/S
                            }
                            else if(tag.getTagName() == "GPS Longitude")
                            {
                                lo = tag.getDescription();//get gps longitude
                            }
                            else if(tag.getTagName() == "GPS Longitude Ref")
                            {
                                lod = tag.getDescription();// E/W
                            }
                            else if(tag.getTagName() == "Date/Time Original")
                            {
                                date = tag.getDescription(); // Picture generation time
                            }
                        }
                    }


                } catch (ImageProcessingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                String finalMake = make;
                String finalModel = model;
                String finalDate = date;
                String finalLa = la;
                String finalLad = lad;

                String finalLo = lo;
                String finalLod = lod;
                view.setOnMouseClicked(new EventHandler<MouseEvent>() {//Do the function when click the thumbnails
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        Image im;
                        mainImageView.setImage(im = view.getImage()); // Show the picture on the mainimageview
                        if(im.getHeight()>im.getWidth())
                        {
                            mainImageView.setFitHeight(440);
                        }
                        else {mainImageView.setFitWidth(514);}

                        txtField.setText("Name: "+ name +"  Height: "+ im.getHeight()+ "  Width: "+ im.getWidth()+"\n"
                                         + "Camera: "+ finalMake +" "+ finalModel+"\n"
                                         + "Date: " + finalDate + "\n"
                                         + "Location: " + finalLa + finalLad+"  "+ finalLo + finalLod);// show informations on the screen

                    }
                });
            }
        });


    }
    @FXML
    void grayscale(ActionEvent event) {
        BufferedImage BeforeImage = SwingFXUtils.fromFXImage(mainImageView.getImage(),null);//convert javafx.image to java.awt.image.BufferedImage
        GrayscaleFilter F = new GrayscaleFilter();//instantiate a filter
        BufferedImage AfterImage = F.filter(BeforeImage, null);//filter
        WritableImage wim = SwingFXUtils.toFXImage(AfterImage, null);//convert java.awt.image.BufferedImage to javafx.image
        mainImageView.setImage(wim);//set image to the mainImageView after filtering

    }

    @FXML
    void invert(ActionEvent event) {

        BufferedImage BeforeImage = SwingFXUtils.fromFXImage(mainImageView.getImage(),null);//convert javafx.image to java.awt.image.BufferedImage
        InvertFilter F = new InvertFilter();//instantiate a filter
        BufferedImage AfterImage = F.filter(BeforeImage, null);//filter
        WritableImage wim = SwingFXUtils.toFXImage(AfterImage, null);//convert java.awt.image.BufferedImage to javafx.image
        mainImageView.setImage(wim);//set image to the mainImageView after filtering



    }

    @FXML
    void chrome(ActionEvent event) {
        BufferedImage BeforeImage = SwingFXUtils.fromFXImage(mainImageView.getImage(),null);//convert javafx.image to java.awt.image.BufferedImage
        ChromeFilter F = new ChromeFilter();//instantiate a filter
        BufferedImage AfterImage = F.filter(BeforeImage, null);//filter
        WritableImage wim = SwingFXUtils.toFXImage(AfterImage, null);//convert java.awt.image.BufferedImage to javafx.image
        mainImageView.setImage(wim);//set image to the mainImageView after filtering

    }

    @FXML
    void lines(ActionEvent event) {
        BufferedImage BeforeImage = SwingFXUtils.fromFXImage(mainImageView.getImage(),null);//convert javafx.image to java.awt.image.BufferedImage
        DoGFilter F = new DoGFilter();//instantiate a filter
        BufferedImage AfterImage = F.filter(BeforeImage, null);//filter
        WritableImage wim = SwingFXUtils.toFXImage(AfterImage, null);//convert java.awt.image.BufferedImage to javafx.image
        mainImageView.setImage(wim);//set image to the mainImageView after filtering

    }

    @FXML
    void crystallize(ActionEvent event) {
        BufferedImage BeforeImage = SwingFXUtils.fromFXImage(mainImageView.getImage(),null);//convert javafx.image to java.awt.image.BufferedImage
        CrystallizeFilter F = new CrystallizeFilter();//instantiate a filter
        BufferedImage AfterImage = F.filter(BeforeImage, null);//filter
        WritableImage wim = SwingFXUtils.toFXImage(AfterImage, null);//convert java.awt.image.BufferedImage to javafx.image
        mainImageView.setImage(wim);//set image to the mainImageView after filtering

    }

    @FXML
    void ShowDoc(ActionEvent event) {

    }

    @FXML
    void initialize() {
        assert MenuOpen != null : "fx:id=\"MenuOpen\" was not injected: check your FXML file 'Untitled'.";
        assert AnchorP != null : "fx:id=\"AnchorP\" was not injected: check your FXML file 'Untitled'.";
        //assert MenuDDoc != null : "fx:id=\"MenuDDoc\" was not injected: check your FXML file 'Untitled'.";
        assert mainImageView != null : "fx:id=\"mainImageView\" was not injected: check your FXML file 'Untitled'.";
        assert MenuFilter != null : "fx:id=\"MenuFilter\" was not injected: check your FXML file 'Untitled'.";
        assert txtField != null : "fx:id=\"txtField\" was not injected: check your FXML file 'Untitled'.";
        assert MenuFile != null : "fx:id=\"MenuFile\" was not injected: check your FXML file 'Untitled'.";
        assert MenuExport != null : "fx:id=\"MenuExport\" was not injected: check your FXML file 'Untitled'.";
        assert ScrollP != null : "fx:id=\"ScrollP\" was not injected: check your FXML file 'Untitled'.";
        assert ProgBar != null : "fx:id=\"ProgBar\" was not injected: check your FXML file 'Untitled'.";

    }
}