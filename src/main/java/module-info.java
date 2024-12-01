module com.cloudedquartz.functionaltext {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.fxmisc.richtext;


    opens com.cloudedquartz.functionaltext to javafx.fxml;
    exports com.cloudedquartz.functionaltext;
}