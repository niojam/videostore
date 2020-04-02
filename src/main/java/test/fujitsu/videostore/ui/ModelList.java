package test.fujitsu.videostore.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.HasUrlParameter;

public abstract class ModelList<T> extends HorizontalLayout implements HasUrlParameter<String> {


    private Button newItem;

    public HorizontalLayout createTopBar() {
        TextField filter = createFilter();
        newItem = new Button();
        setNewItemTextAndLogic();
        newItem.setId("new-item");
        newItem.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        newItem.setIcon(VaadinIcon.PLUS_CIRCLE.create());

        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth("100%");
        topLayout.add(filter);
        topLayout.add(newItem);
        topLayout.setVerticalComponentAlignment(Alignment.START, filter);
        topLayout.expand(filter);
        return topLayout;
    }


    public VerticalLayout buildVerticalLayout(Grid<?> grid) {
        HorizontalLayout topLayout = createTopBar();
        VerticalLayout barAndGridLayout = new VerticalLayout();
        barAndGridLayout.add(topLayout);
        barAndGridLayout.add(grid);
        barAndGridLayout.setFlexGrow(1, grid);
        barAndGridLayout.setFlexGrow(0, topLayout);
        barAndGridLayout.setSizeFull();
        barAndGridLayout.expand(grid);
        return barAndGridLayout;
    }

    public void showSaveNotification(String msg) {
        Notification.show(msg);
    }

    public void setNewItemEnabled(boolean enabled) {
        newItem.setEnabled(enabled);
    }

    public abstract TextField createFilter();

    public Button getNewItemButton() {
        return newItem;
    }

    public abstract void setNewItemTextAndLogic();

}
