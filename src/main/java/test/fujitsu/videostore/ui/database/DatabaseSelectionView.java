package test.fujitsu.videostore.ui.database;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.commons.io.FilenameUtils;
import test.fujitsu.videostore.backend.excetion.InvalidDbPathException;

import java.io.File;

import static test.fujitsu.videostore.backend.database.connector.DBConnector.JSON_EXTENSION;
import static test.fujitsu.videostore.backend.database.connector.DBConnector.YAML_EXTENSION;

@Route("DatabaseSelection")
@PageTitle("Database Selection")
@HtmlImport("css/shared-styles.html")
public class DatabaseSelectionView extends FlexLayout {

    public static final String WRONG_INPUT = "DB File is missing or Unknown format";
    public static final String ERROR_NOTIFICATION_TEXT = "Database file path is incorrect";
    private TextField databasePath;
    private Button selectDatabaseButton;

    public DatabaseSelectionView() {
        setSizeFull();
        setClassName("database-selection-screen");

        FlexLayout centeringLayout = new FlexLayout();
        centeringLayout.setSizeFull();
        centeringLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        centeringLayout.setAlignItems(Alignment.CENTER);
        centeringLayout.add(buildLoginForm());

        add(centeringLayout);
    }

    private Component buildLoginForm() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setWidth("310px");

        databasePath = new TextField("Enter database file path");
        databasePath.setId("database-path");
        databasePath.setRequired(true);

        verticalLayout.add(databasePath);

        HorizontalLayout buttons = new HorizontalLayout();
        verticalLayout.add(buttons);

        selectDatabaseButton = new Button("Select database");
        selectDatabaseButton.setId("database-select");
        selectDatabaseButton.addClickListener(event -> selectDatabase());
        selectDatabaseButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);
        buttons.add(selectDatabaseButton);

        return verticalLayout;
    }

    private void selectDatabase() {
        selectDatabaseButton.setEnabled(false);

        try {
            String filepath = databasePath.getValue();
            validateInputDbPath(filepath);
            CurrentDatabase.set(filepath);
            getUI().get().navigate("");
        } catch (InvalidDbPathException e) {
            showNotificationError();

            e.printStackTrace();
        } finally {
            selectDatabaseButton.setEnabled(true);
        }
    }



    private void validateInputDbPath(String filepath) throws InvalidDbPathException {
        String fileExtension = FilenameUtils.getExtension(filepath);
        if (!(new File(filepath).exists() &
                (fileExtension.equals(JSON_EXTENSION) || fileExtension.equals(YAML_EXTENSION)))) {
            throw new InvalidDbPathException(WRONG_INPUT);
        }
    }


    public void showNotificationError() {
        Div content = new Div();
        content.addClassName("notification-error");
        content.setText(ERROR_NOTIFICATION_TEXT);

        Notification notification = new Notification(content);
        notification.setDuration(3000);
        notification.setPosition(Notification.Position.BOTTOM_CENTER);
        notification.open();
    }
}
