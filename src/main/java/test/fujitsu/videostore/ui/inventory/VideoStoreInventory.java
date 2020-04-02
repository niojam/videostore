package test.fujitsu.videostore.ui.inventory;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import test.fujitsu.videostore.backend.domain.Movie;
import test.fujitsu.videostore.ui.MainLayout;
import test.fujitsu.videostore.ui.ModelList;
import test.fujitsu.videostore.ui.inventory.components.MovieForm;
import test.fujitsu.videostore.ui.inventory.components.MovieGrid;

import java.util.ArrayList;
import java.util.List;

@Route(value = VideoStoreInventory.VIEW_NAME, layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class VideoStoreInventory extends ModelList<Movie>
        implements HasUrlParameter<String> {

    public static final String VIEW_NAME = "Inventory";
    public static final String NEW_MOVIE_BTN_TEXT = "New Movie";
    private MovieGrid grid;
    private MovieForm form;

    private ListDataProvider<Movie> dataProvider = new ListDataProvider<>(new ArrayList<>());

    private VideoStoreInventoryLogic viewLogic = new VideoStoreInventoryLogic(this);

    public VideoStoreInventory() {
        setId(VIEW_NAME);
        setSizeFull();
        grid = new MovieGrid();
        grid.asSingleSelect().addValueChangeListener(
                event -> viewLogic.rowSelected(event.getValue()));
        grid.setDataProvider(dataProvider);
        form = new MovieForm(viewLogic);
        add(buildVerticalLayout(grid));
        add(form);
        viewLogic.init();
    }


    @Override
    public void setNewItemTextAndLogic() {
        Button newItemBtn = getNewItemButton();
        newItemBtn.setText(NEW_MOVIE_BTN_TEXT);
        newItemBtn.addClickListener(click -> viewLogic.newMovie());
    }


    @Override
    public TextField createFilter() {
        TextField filter = new TextField();
        filter.setId("filter");
        filter.setPlaceholder("Filter by name");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(event -> {
            // TODO: Implement filtering by movie name
        });
        return filter;
    }

    public void clearSelection() {
        grid.getSelectionModel().deselectAll();
    }


    public void selectRow(Movie row) {
        grid.getSelectionModel().select(row);
    }

    public void addMovie(Movie movie) {
        dataProvider.getItems().add(movie);
        grid.getDataProvider().refreshAll();
    }

    public void updateMovie(Movie movie) {
        dataProvider.refreshItem(movie);
    }

    public void removeMovie(Movie movie) {
        dataProvider.getItems().remove(movie);
        dataProvider.refreshAll();
    }

    public void editMovie(Movie movie) {
        showForm(movie != null);
        form.editMovie(movie);
    }

    public void showForm(boolean show) {
        form.setVisible(show);
    }

    public void setMovies(List<Movie> movies) {
        dataProvider.getItems().clear();
        dataProvider.getItems().addAll(movies);
        dataProvider.refreshAll();
    }


    @Override
    public void setParameter(BeforeEvent event,
                             @OptionalParameter String parameter) {
        viewLogic.enter(parameter);
    }
}
