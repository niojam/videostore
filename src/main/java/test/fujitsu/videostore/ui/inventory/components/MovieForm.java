package test.fujitsu.videostore.ui.inventory.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.value.ValueChangeMode;
import test.fujitsu.videostore.backend.domain.Movie;
import test.fujitsu.videostore.backend.domain.MovieType;
import test.fujitsu.videostore.ui.inventory.VideoStoreInventoryLogic;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

/**
 * Movie form
 */
public class MovieForm extends Div {

    private VerticalLayout content;

    private TextField name;
    private TextField stockCount;
    private ComboBox<MovieType> type;
    private Button save;
    private Button cancel;
    private Button delete;

    private VideoStoreInventoryLogic viewLogic;
    private Binder<Movie> binder;
    private Movie currentMovie;

    public MovieForm(VideoStoreInventoryLogic videoStoreInventoryLogic) {
        setId("edit-form");

        content = new VerticalLayout();
        content.setSizeUndefined();
        add(content);

        viewLogic = videoStoreInventoryLogic;

        name = new TextField("Movie name");
        name.setId("movie-name");
        name.setWidth("100%");
        name.setRequired(true);
        name.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(name);

        type = new ComboBox<>("Movie type");
        type.setId("movie-type");
        type.setWidth("100%");
        type.setRequired(true);
        type.setItems(MovieType.values());
        type.setItemLabelGenerator(MovieType::getTextualRepresentation);
        content.add(type);

        stockCount = new TextField("In stock");
        stockCount.setId("stock-count");
        stockCount.setWidth("100%");
        stockCount.setRequired(true);
        stockCount.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        stockCount.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(stockCount);

        // Binding field to domain
        binder = new Binder<>(Movie.class);
        binder.forField(name)
                .bind("name");
        binder.forField(type)
                .withValidator(Objects::nonNull, "Please Select Movie Type")
                .bind("type");
        binder.forField(stockCount).withConverter(new StockCountConverter())
                .withValidator(count -> count >= 0, "Movie stock count cannot be negative")
                .bind("stockCount");

        // enable/disable save button while editing
        binder.addStatusChangeListener(event -> {
            boolean isValid = !event.hasValidationErrors();
            boolean hasChanges = binder.hasChanges();
            save.setEnabled(hasChanges && isValid);
        });

        save = new Button("Save");
        save.setId("save");
        save.setWidth("100%");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(event -> {
            if (currentMovie != null) {
                binder.writeBeanIfValid(currentMovie);
                if (currentMovie.getName() == null || currentMovie.getName().equals("")) {
                    Notification.show("Please insert Movie name", 3000, Notification.Position.MIDDLE);
                    return;
                }
                viewLogic.saveMovie(currentMovie);
            }
        });

        cancel = new Button("Cancel");
        cancel.setId("cancel");
        cancel.setWidth("100%");
        cancel.addClickListener(event -> viewLogic.cancelMovie());
        getElement()
                .addEventListener("keydown", event -> viewLogic.cancelMovie())
                .setFilter("event.key == 'Escape'");
        delete = new Button("Delete");
        delete.setId("delete");
        delete.setWidth("100%");
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
        delete.addClickListener(event -> {
            if (currentMovie != null) {
                viewLogic.deleteMovie(currentMovie);
            }
        });

        content.add(save, delete, cancel);
    }

    public void editMovie(Movie movie) {
        if (movie == null) {
            movie = new Movie();
        }
        currentMovie = movie;
        binder.readBean(movie);
        delete.setEnabled(!viewLogic.canBeDeleted(movie));
    }

    private static class StockCountConverter extends StringToIntegerConverter {

        public StockCountConverter() {
            super(0, "Could not convert value to " + Integer.class.getName()
                    + ".");
        }

        @Override
        protected NumberFormat getFormat(Locale locale) {
            DecimalFormat format = new DecimalFormat();
            format.setMaximumFractionDigits(0);
            format.setDecimalSeparatorAlwaysShown(false);
            format.setParseIntegerOnly(true);
            format.setGroupingUsed(false);
            return format;
        }
    }
}
