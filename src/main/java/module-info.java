module com.cloudedquartz.functionaltext {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.cloudedquartz.functionaltext to javafx.fxml;
    exports com.cloudedquartz.functionaltext;
}